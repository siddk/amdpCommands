package mt;

import corpus.ParallelCorpus;

import java.util.HashSet;

/**
 * General Abstract Class for the IBM Model Series of Machine Translation Systems. Implements
 * all shared methods for IBM Models. Implemented by the IBM Model 1, Model 2, etc.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public abstract class IBMModel {
    HashSet<String> sourceWords;
    HashSet<String> targetWords;
    static final double MIN_PROB = 1.0e-12;

    /**
     * Instantiate an IBMModel object with the specified parallel corpus.
     *
     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public IBMModel(ParallelCorpus corpus) {
        this.updateVocabulary(corpus);
    }

    /**
     * Given a parallel corpus, add all words from each of the source and target pairs to the
     * respective vocabulary sets.

     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    private void updateVocabulary(ParallelCorpus corpus) {
        // TODO - Update sourceWords, targetWords
    }

}
