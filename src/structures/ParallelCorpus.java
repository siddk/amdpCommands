package structures;

import language.LanguageExpression;

import java.util.List;

/**
 * Define a Parallel Corpus as two sets of aligned sentences in different languages.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class ParallelCorpus<S extends LanguageExpression,T extends LanguageExpression> {
    public List<AlignedSent<S,T>> corpus;

    /**
     * Build ParallelCorpus from file paths to source and target corpuses.
     *
     * @param sourcePath Path to source half of weakly aligned parallel corpus
     * @param targetPath Path to target half of weakly aligned parallel corpus
     */
    public ParallelCorpus(String sourcePath, String targetPath) {
        // TODO
    }

    /**
     * Get size (number of sentences) in the given Parallel Corpus.
     *
     * @return Number of sentences in corpus.
     */
    public int size() {
        return this.corpus.size();
    }

    /**
     * Get an AlignedSentence given an index in the Parallel Corpus.
     *
     * @param index Index to retrieve sentence from in Corpus.
     */
    public AlignedSent<S,T> get(int index) {
        return this.corpus.get(index);
    }

}
