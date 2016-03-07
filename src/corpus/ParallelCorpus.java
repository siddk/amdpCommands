package corpus;

import java.util.ArrayList;

/**
 * Define a Parallel Corpus as two sets of aligned sentences in different languages.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class ParallelCorpus {
    public ArrayList<String> source;
    public ArrayList<String> target;

    /**
     * Build ParallelCorpus from file paths to source and target corpuses.
     *
     * @param sourcePath Path to source half of weakly aligned parallel corpus
     * @param targetPath Path to target half of weakly aligned parallel corpus
     */
    public ParallelCorpus(String sourcePath, String targetPath) {
        // TODO
    }

}
