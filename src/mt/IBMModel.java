package mt;

import structures.DefaultDict;
import structures.ParallelCorpus;

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
public abstract class IBMModel {
    /* tau[str][str]: double ==> Probability(target word | source word)
     * Indexed as tau.get(target_word).get(source_word) */
    protected DefaultDict<String, DefaultDict<String, Double>> tau;

    /* delta[int][int][int][int]: double ==> Probability(i | j, l, m)
     * Indexed as delta.get(i).get(j).get(l).get(m) */
    protected DefaultDict<Integer,
            DefaultDict<Integer, DefaultDict<Integer, DefaultDict<Integer, Double>>>> delta;

    protected final ParallelCorpus corpus;
    protected final Set<String> sourceVocabulary;
    protected final Set<String> targetVocabulary;
    protected static final String NULL = "**N**";
    protected static final double MIN_PROB = 1.0e-12;

    /**
     * Instantiate an IBMModel object with the specified parallel corpus.
     *
     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public IBMModel(ParallelCorpus corpus) {
        this.corpus = corpus;
        this.tau = new DefaultDict<>(new DefaultDict<>(MIN_PROB));
        this.delta = new DefaultDict<>(new DefaultDict<>(new DefaultDict<>(new DefaultDict<>(MIN_PROB))));
        this.sourceVocabulary = new HashSet<>();
        this.targetVocabulary = new HashSet<>();
        this.updateVocabulary(corpus);
    }

    /**
     * Given a parallel corpus, add all words from each of the source and target pairs to the
     * respective vocabulary sets.

     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public void updateVocabulary(ParallelCorpus corpus) {
        corpus.getSentences().stream().forEach(alignedSent -> {
            this.sourceVocabulary.addAll(alignedSent.getSourceWords());
            this.targetVocabulary.addAll(alignedSent.getTargetWords());
        });
    }

}
