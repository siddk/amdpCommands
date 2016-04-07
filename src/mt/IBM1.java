package mt;

import language.LanguageExpression;
import language.MachineLanguage;
import language.NaturalLanguage;
import structures.AlignedSent;
import structures.Counts;
import structures.DefaultDict;
import structures.ParallelCorpus;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Core class for the IBM Model 1 Translation System. Used to learn simple (naive) translation
 * probabilities, ignoring all alignments.
 *
 * EM Breakdown:
 *      E - Step: In training, count how many times a source word is translated into a target word,
 *                weighted by prior probability of translation.
 *
 *      M - Step: Estimate new translation probabilities using counts from E - Step.
 *
 * Variables:
 *      i - Position in the source sentence { 0 (NULL), 1, 2, ... Length }
 *      j - Position in the target sentence { 1, 2, ... Length }
 *      s - Word in the source language
 *      t - Word in the target language
 *
 * References:
 *      NLTK Translate Library
 *      Philipp Koehn. 2010. Statistical Machine Translation.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class IBM1 extends IBMModel implements MachineTranslator{
    /**
     * Instantiate an IBM Model 1 instance with a given Parallel Corpus, and a set number
     * of EM iterations.
     *
     * @param corpus Weakly aligned parallel corpus.
     * @param em_iterations Number of EM Iterations for training.
     */
    public IBM1(ParallelCorpus corpus, int em_iterations) {
        super(corpus);
        this.setUniformProbabilities();

        for (int i = 0; i < em_iterations; i++) {
            this.train();
        }
    }

    /**
     * Set all Translation (Tau) Probabilities to be uniform.
     */
    public void setUniformProbabilities() {
        double initialProb = 1.0 / this.sourceVocabulary.size();

        // Set each tau value to the initial Probability
        this.sourceVocabulary.stream().forEach(t -> this.tau.put(t, new DefaultDict<>(initialProb)));
        this.tau.put(NULL, new DefaultDict<>(initialProb));
    }

    /**
     * Run one iteration of EM, using the given tau values as prior probabilities.
     */
    public void train() {
        Counts counts = new Counts();

        // E - Step
        for (int i = 0; i < this.corpus.size(); i++) {

            AlignedSent alignedSent = this.corpus.get(i);
            List<String> sourceSent = alignedSent.getTargetWords();
            List<String> targetSent = alignedSent.getSourceWords();
            List<String> nulled = new ArrayList<>();
            nulled.add(NULL);
            nulled.addAll(targetSent);
            //sourceSent.add(0, NULL); // Prepend NULL Token
            targetSent = nulled;

            // E - Step (a) - Compute normalization factors
            DefaultDict<String, Double> total_count = new DefaultDict<>(0.0);
            for (String t : targetSent) {
                for (String s : sourceSent) {
                    total_count.put(t, total_count.get(t) + this.tau.get(t).get(s));
                }
            }

            // E - Step (b) - Compute counts
            for (String t : targetSent) {
                for (String s : sourceSent) {
                    double count = this.tau.get(t).get(s);
                    double normalized_count = count / total_count.get(t);
                    counts.nTS.get(t).put(s, counts.nTS.get(t).get(s) + normalized_count);
                    counts.nTO.put(s, counts.nTO.get(s) + normalized_count);
                }
            }
        }

        // M - Step
        for (String t : this.tau.keySet()) {
            for (String s : counts.nTS.get(t).keySet()) {
                double estimate = counts.nTS.get(t).get(s) / counts.nTO.get(s);
                this.tau.get(t).put(s, Math.max(estimate, MIN_PROB));
            }
        }
    }

    /**
     * Translate a single expression of this IBM model's source language into an expression of the target language
     * @param sourceExpression A language expression in the model's source language
     * @return A language expression in the model's target language
     */
    @Override
    public LanguageExpression translate(LanguageExpression sourceExpression) {
        List<String> sourceSplit = sourceExpression.getWords();
        Map<String, Double> exprProbs = new HashMap<>();

        int m = sourceExpression.getWords().size(); // m is length of source and comes from natural language
        double maxLikelihood = Double.NEGATIVE_INFINITY;
        String likelyExpr = "";

        for(String expr : this.outputSet){
            String[] exprSplit = expr.split(" ");
            int l = exprSplit.length; //l is length of target and comes from machine language
            double likelihood = 1.0; //Math.pow(this.targetPrior, l);// * this.lengthPrior.get(l).get(m);
            double sum = 0.0;

            for (String anExprSplit : exprSplit) {
                double product = 1.0;
                for (int k = 0; k < m; k++) {
                    product *= this.tau.get(sourceSplit.get(k)).get(anExprSplit);
                }
                sum += product;
            }
            likelihood *= sum;
            if(likelihood > maxLikelihood){
                maxLikelihood = likelihood;
                likelyExpr = expr;
            }
            exprProbs.put(expr, likelihood);
        }
        List<String> translated = Arrays.asList(likelyExpr.split(" "));
        exprProbs.entrySet().stream().sorted((e1,e2) -> e1.getValue().compareTo(e2.getValue())).forEachOrdered(System.out::println);

        return new MachineLanguage(translated);
    }

    public static boolean goodTranslation(List<String> actual, List<String> translated){
        AtomicBoolean ret = new AtomicBoolean(true);
        actual.stream().forEach(w -> ret.compareAndSet(!translated.contains(w), false));
        return ret.get();
    }

    public static double runLOOTest(ParallelCorpus corpus){
        AtomicInteger numCorrect = new AtomicInteger(0);
        IntStream.range(0, corpus.size()).forEachOrdered(i -> {
            AlignedSent test = corpus.remove(i);
            List<String> inputWords = test.getSourceWords();
            List<String> outputWords = test.getTargetWords();
            IBM1 ibm1 = new IBM1(corpus, 30);
            NaturalLanguage input = new NaturalLanguage(inputWords);
            List<String> output = ibm1.translate(input).getWords();
            if(goodTranslation(outputWords, output)){
                numCorrect.getAndIncrement();
                System.out.println("Performed correct translation");
                System.out.println("Correctly translated: " + output.toString());
            }
            else{
                System.out.println("Performed incorrect translation");
                System.out.println("Input: " + inputWords.toString());
                System.out.println("Expected: " + outputWords.toString());
                System.out.println("Found: " + output.toString());
            }
            System.out.println("");
            corpus.insert(test, i);
        });
        return (double) numCorrect.get() / (double) corpus.size();
    }

    public static void main(String[] args){
        String english = "data/corpus/full_english.txt";
        String machine = "data/corpus/full_machine.txt";
        ParallelCorpus corpus = new ParallelCorpus(english, machine);

        double accuracy = runLOOTest(corpus);
        System.out.println("LOO accuracy: " + accuracy);
    }
}
