package mt;

import structures.AlignedSent;
import structures.DefaultDict;
import structures.ParallelCorpus;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    protected final DefaultDict<Integer, DefaultDict<Integer, Double>> lengthPrior;
    protected final double targetPrior;
    protected final Set<String> outputSet;
    protected static final String NULL = "**N**";
    protected static final double MIN_PROB = 1.0e-12;
    protected static final double TAU_MIN_PROB = 1.0e-12;
    protected static final double DELTA_MIN_PROB = 1.0e-15;

    /**
     * Instantiate an IBMModel object with the specified parallel corpus.
     *
     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public IBMModel(ParallelCorpus corpus) {
        this.corpus = corpus;
        this.tau = new DefaultDict<>(o -> new DefaultDict<>(TAU_MIN_PROB));
        this.delta = new DefaultDict<>(a -> new DefaultDict<>(b -> new DefaultDict<>(c -> new
                DefaultDict<>(DELTA_MIN_PROB))));
        this.sourceVocabulary = new HashSet<>();
        this.targetVocabulary = new HashSet<>();
        this.outputSet = new HashSet<>();
        this.lengthPrior = new DefaultDict<>(o -> new DefaultDict<>(MIN_PROB));
        this.updateVocabulary(corpus);
        this.computeLengthPrior();
        this.targetPrior = 1.0 / this.targetVocabulary.size();
    }

    /**
     * Given a parallel corpus, add all words from each of the source and target pairs to the
     * respective vocabulary sets.

     * @param corpus Parallel corpus object consisting of weakly aligned source-target pairs.
     */
    public void updateVocabulary(ParallelCorpus corpus) {
        corpus.getSentences().stream().forEach(alignedSent -> {
            StringBuilder sb = new StringBuilder();
            for(String w : alignedSent.getTargetWords()){
                sb.append(w + " ");
            }
            this.outputSet.add(sb.toString().trim());
            this.sourceVocabulary.addAll(alignedSent.getSourceWords());
            this.targetVocabulary.addAll(alignedSent.getTargetWords());
        });
    }

    /**
     * Computes the prior distribution over aligned sentence lengths from the parallel corpus
     */
    public void computeLengthPrior(){
        DefaultDict<Integer, DefaultDict<Integer, Double>> nLM = new DefaultDict<>(o -> new DefaultDict<>(0.0));
        DefaultDict<Integer, Double> nLO = new DefaultDict<>(0.0);

        for(AlignedSent sent : this.corpus.getSentences()){
            List<String> sourceSent = sent.getSourceWords();
            List<String> targetSent = sent.getTargetWords();
            int l = targetSent.size();
            int m = sourceSent.size();

            double updateLM = nLM.get(l).get(m);
            double updateLO = nLO.get(l);

            nLM.get(l).put(m, updateLM + 1);
            nLO.put(l, updateLO + 1);
        }

        for(int l : nLM.keySet()){
            for(int m : nLM.get(l).keySet()){
                double estimate = nLM.get(l).get(m) / nLO.get(l);
                this.lengthPrior.get(l).put(m, estimate);
            }
        }
    }
}
