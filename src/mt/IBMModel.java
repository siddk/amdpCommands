package mt;

import language.LanguageExpression;
import structures.ParallelCorpus;
import structures.DefaultDict;

import java.util.HashSet;
import java.util.Set;

/**
 * General Abstract Class for the IBM Model Series of Machine Translation Systems. Implements
 * all shared methods for IBM Models. Implemented by the IBM Model 1, Model 2, etc.
 *
 * References:
 *      NLTK Translate Library
 *      Philipp Koehn. 2010. Statistical Machine Translation.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public abstract class IBMModel<S extends LanguageExpression,T extends LanguageExpression> {
    /* tau[str][str]: double ==> Probability(target word | source word)
     * Indexed as tau.get(target_word).get(source_word) */
    DefaultDict<String, DefaultDict<String, Double>> tau;

    /* delta[int][int][int][int]: double ==> Probability(i | j, l, m)
     * Indexed as delta.get(i).get(j).get(l).get(m) */
    DefaultDict<Integer,
            DefaultDict<Integer, DefaultDict<Integer, DefaultDict<Integer, Double>>>> delta;

    ParallelCorpus<S,T> corpus;
    Set<String> sourceVocabulary;
    Set<String> targetVocabulary;
    static final String NULL = "**N**";
    static final double MIN_PROB = 1.0e-12;

    /**
     * Instantiate an IBMModel object with the specified parallel corpus.
     *
     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public IBMModel(ParallelCorpus corpus) {
        this.corpus = corpus;
        this.updateVocabulary(corpus);
        this.initProbabilities();
    }

    /**
     * Given a parallel corpus, add all words from each of the source and target pairs to the
     * respective vocabulary sets.

     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public void updateVocabulary(ParallelCorpus corpus) {
        // TODO - Update sourceVocabulary, targetVocabulary
    }

    /**
     * Initialize tables of parameters. For IBM Model 2, these are the translation and the
     * alignment parameters.
     */
    public void initProbabilities() {
        // TODO - Initialize tau, delta maps properly! Default values should be MIN_PROB
    }


}
