package mt;

import language.LanguageExpression;
import language.MachineLanguage;
import language.NaturalLanguage;
import structures.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Core class for the IBM Model 2 Translation system. Learns translation probabilities and
 * alignment probabilities from a corpus of weakly aligned sentences. In this model, an alignment
 * probability is introduced, a(i | j,l,m), which predicts a source word position, given its
 * aligned target word's position.
 *
 * EM Breakdown:
 *      E - Step: In training collect following counts weighted by training data:
 *                  a) Number of times a source word is translated into a target word
 *                  b) Number of times a particular position in source is aligned to a particular
 *                     position in target.
 *
 *      M - Step: Estimate new probabilities from E - Step counts.
 *
 * Variables:
 *      i - Position in the source sentence { 0 (NULL), 1, 2, ... Length }
 *      j - Position in the target sentence { 1, 2, ... Length }
 *      l - Number of words in the source sentence (excluding NULL)
 *      m - Number of words in the target sentence
 *      s - Word in the source language
 *      t - Word in the target language
 *
 * References:
 *      NLTK Translate Library
 *      Philipp Koehn. 2010. Statistical Machine Translation.
 *
 * Created by Sidd Karamcheti on 3/8/16.
 */
public class IBM2 extends IBMModel implements MachineTranslator{
    /**
     * Instantiate an IBM Model 2 instance with a given Parallel Corpus, and a set number
     * of EM iterations.
     *
     * @param corpus Weakly aligned parallel corpus.
     * @param em_iterations Number of EM iterations for training.
     */
    public IBM2(ParallelCorpus corpus, int em_iterations) {
        super(corpus);

        // Initialize tau translation probabilities by running a few iterations of Model 1 training
        IBM1 ibm1 = new IBM1(corpus, 2 * em_iterations);
        this.tau = ibm1.tau;

        // Initialize all delta probabilities
        this.setUniformProbabilities();

        // Run EM
        for (int i = 0; i < em_iterations; i++) {

            if(i == 3){
                //System.exit(0);
            }
            this.train();
//            System.out.println("Tau: ");
//            this.tau.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//            System.out.println("\n");
//            System.out.println("Delta: ");
//            this.delta.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//            System.out.println("***********");
        }

    }

    /**
     * Set all alignment (Delta) probabilities to be uniform.
     */
    public void setUniformProbabilities() {
        // a(i | j, l, m) = 1 / (l + 1) for all i, j, l, m
        HashSet<Pair<Integer, Integer>> lmCombinations = new HashSet<>();

        for (int index = 0; index < this.corpus.size(); index++) {
            AlignedSent alignedSent = this.corpus.get(index);
            int l = alignedSent.getTargetWords().size();
            int m = alignedSent.getSourceWords().size();

            Pair<Integer, Integer> lm = new Pair<>(l, m);
            if (!lmCombinations.contains(lm)) {
                lmCombinations.add(lm);
                double initialProb = 1.0 / (l + 1.0);

                for (int i = 0; i < l + 1; i++) {
                    for (int j = 1; j < m + 1; j++) {
                        DefaultDict<Integer, Double> mProb = new DefaultDict<>(initialProb);
                        DefaultDict<Integer, DefaultDict<Integer, Double>> lmProb = new DefaultDict<>(o -> mProb);
                        DefaultDict<Integer, DefaultDict<Integer, DefaultDict<Integer, Double>>> jlmProb = new DefaultDict<>(o -> lmProb);
                        this.delta.put(i, jlmProb);
                    }
                }
            }
        }
    }

    /**
     * Run one iteration of EM, using the given tau and delta values as prior probabilities.
     */
    public void train() {
        AlignmentCounts counts = new AlignmentCounts();

        // E - Step
        for (int index = 0; index < this.corpus.size(); index++) {
            AlignedSent alignedSent = this.corpus.get(index);
            List<String> targetSent = alignedSent.getSourceWords();
            List<String> sourceSent = alignedSent.getTargetWords();
            List<String> nulled = new ArrayList<>();
            nulled.add(NULL);
            nulled.addAll(sourceSent);
            sourceSent = nulled;
            //sourceSent.add(0, NULL); // Prepend NULL Token
            List<String> modtarget = new ArrayList<>();
            modtarget.add("UNUSED");
            modtarget.addAll(targetSent);
            targetSent = modtarget;
            //targetSent.add(0, "UNUSED"); // 1 - Indexed

            int l = sourceSent.size() - 1;
            int m = targetSent.size() - 1;

            // E - Step (a) - Compute normalization factors
            DefaultDict<String, Double> totalCount = new DefaultDict<>(0.0);
            for (int j = 1; j < targetSent.size(); j++) {
                String t = targetSent.get(j);
                for (int i = 0; i < sourceSent.size(); i++) {
                    String s = sourceSent.get(i);
                    totalCount.put(t, this.tau.get(t).get(s) * this.delta.get(i).get(j).get(l).get(m));
//                    this.delta.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
                }
            }

            // E - Step (b) - Compute counts
            for (int j = 1; j < targetSent.size(); j++) {
                String t = targetSent.get(j);
                for (int i = 0; i < sourceSent.size(); i++) {
                    String s = sourceSent.get(i);
                    double count = this.tau.get(t).get(s) * this.delta.get(i).get(j).get(l).get(m);
                    double normalized_count = count / totalCount.get(t);
                    counts.updateTau(normalized_count, s, t);
                    counts.updateDelta(normalized_count, i, j, l, m);
                }
            }

            for(int j =1;j < targetSent.size();j++){
                String t = targetSent.get(j);
                for (int i = 0; i < sourceSent.size(); i++) {
                    String s = sourceSent.get(i);
                }
            }

            //this.delta.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//            counts.nIJLM.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//            System.out.println("******");
//            counts.nIO.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
        }

        // M - Step
        // Reset Tau - Values
        for (String t : counts.nTS.keySet()) {
            for (String s : counts.nTS.get(t).keySet()) {
                double estimate = counts.nTS.get(t).get(s) / counts.nTO.get(s);
                this.tau.get(t).put(s, Math.max(estimate, MIN_PROB));
            }
        }

        // Reset Delta - Values
        for (int i : counts.nIJLM.keySet()) {
            for (int j : counts.nIJLM.get(i).keySet()) {
                for (int l : counts.nIJLM.get(i).get(j).keySet()) {
                    for (int m : counts.nIJLM.get(i).get(j).get(l).keySet()) {
                        double estimate = counts.nIJLM.get(i).get(j).get(l).get(m) /
                                counts.nIO.get(j).get(l).get(m);
                        this.delta.get(i).get(j).get(l).put(m, Math.max(estimate, MIN_PROB));
                    }
                }
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
        Collection<String> enumerated = MachineLanguage.enumerate(6);
        enumerated.remove("agentInRoom blockInRoom");
        int m = sourceExpression.getWords().size(); // m is length of source and comes from natural language
        double maxLikelihood = Double.NEGATIVE_INFINITY;
        String likelyExpr = "";
        this.delta.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue()));
//        System.exit(0);
        for(String expr : this.outputSet){
            String[] exprSplit = expr.split(" ");
            int l = exprSplit.length; //l is length of target and comes from machine language
            double likelihood = 1.0; //Math.pow(this.targetPrior, l);// * this.lengthPrior.get(l).get(m);
            double sum = 0.0;
            //for(int a : this.delta.values().stream().map(v -> v.keySet()).flatMap(x -> x.stream()).collect(Collectors.toSet())) {
            Set<Integer> aVals = this.delta.keySet();
            for(int a : aVals){
                a = Math.min(l-1, a);
                double product = 1.0;
                for (int k = 0; k < m; k++) {
                    product *= this.delta.get(a).get(k).get(l).get(m);
                    product *= this.tau.get(exprSplit[a]).get(sourceSplit.get(k));
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

//        System.out.println(this.tau);
//        System.out.println(this.delta);
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
            IBM2 ibm2 = new IBM2(corpus, 15);
            NaturalLanguage input = new NaturalLanguage(inputWords);
            List<String> output = ibm2.translate(input).getWords();
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
        String english = "/home/dilip/CSCI2951K/amdpCommands/data/corpus/english.txt";
        String machine = "/home/dilip/CSCI2951K/amdpCommands/data/corpus/machine.txt";
        ParallelCorpus corpus = new ParallelCorpus(english, machine);
        //IBM2 ibm2 = new IBM2(corpus, 15);
        //String command = "take the star to the green room";
        //List<String> test = new ArrayList<>(Arrays.asList(command.split(" ")));
        //LanguageExpression translated = ibm2.translate(new NaturalLanguage(test));
        //System.out.println(translated.getWords());



        double accuracy = runLOOTest(corpus);
        System.out.println("LOO accuracy: " + accuracy);
    }
}
