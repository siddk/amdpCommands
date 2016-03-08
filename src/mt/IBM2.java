package mt;

import structures.Pair;
import structures.ParallelCorpus;

import java.util.HashSet;

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
public class IBM2 extends IBMModel {
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
        IBM1 ibm1 = new IBM1(corpus, em_iterations);
        this.tau = ibm1.tau;

        // Initialize all delta probabilities
        this.setUniformProbabilities();


    }

    /**
     * Set all alignment (Delta) probabilities to be uniform.
     */
    public void setUniformProbabilities() {
        // a(i | j, l, m) = 1 / (l + 1) for all i, j, l, m
        HashSet<Pair<Integer, Integer>> lmCombinations = new HashSet<>();
    }
}