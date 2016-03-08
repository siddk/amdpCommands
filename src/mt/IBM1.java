package mt;

import structures.AlignedSent;
import structures.Counts;
import structures.DefaultDict;
import structures.ParallelCorpus;

import java.util.ArrayList;

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
public class IBM1 extends IBMModel {
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
        double initialProb = 1 / this.targetVocabulary.size();

        for (String t : this.targetVocabulary) {
            // Set each tau value to the initial Probability
            this.tau.put(t, new DefaultDict<String, Double>(initialProb));
        }
    }

    /**
     * Run one iteration of EM, using the given tau values as prior probabilities.
     */
    public void train() {
        Counts counts = new Counts();

        // E - Step
        for (int i = 0; i < this.corpus.size(); i++) {
            AlignedSent alignedSent = this.corpus.get(i);
            ArrayList<String> targetSent = alignedSent.getWords();
            ArrayList<String> sourceSent = alignedSent.getMots();
            sourceSent.add(0, NULL); // Prepend NULL Token

            // E - Step (a) - Compute normalization factors
            DefaultDict<String, Double> total_count = new DefaultDict<String, Double>(0.0);
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
        for (String t : counts.nTS.keySet()) {
            for (String s : counts.nTS.get(t).keySet()) {
                double estimate = counts.nTS.get(t).get(s) / counts.nTO.get(s);
                this.tau.get(t).put(s, Math.max(estimate, MIN_PROB));
            }
        }
    }
}
