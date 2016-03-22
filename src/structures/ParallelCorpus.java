package structures;

import language.LanguageExpression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Define a Parallel Corpus as two sets of aligned sentences in different languages.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class ParallelCorpus {
    protected final Class<LanguageExpression> source;
    protected final Class<LanguageExpression> target;
    protected int maxTargetLength;
    public List<AlignedSent> corpus;

    /**
     * Build ParallelCorpus from file paths to source and target corpuses.
     *
     * @param sourcePath Path to source half of weakly aligned parallel corpus
     * @param targetPath Path to target half of weakly aligned parallel corpus
     */
    public ParallelCorpus(Class<LanguageExpression> source, Class<LanguageExpression> target, String sourcePath, String targetPath) {
        this.source = source;
        this.target = target;
        this.maxTargetLength = 0;
        this.corpus = new ArrayList<>();
        try(BufferedReader brs = new BufferedReader(new FileReader(sourcePath));
            BufferedReader brt = new BufferedReader(new FileReader(targetPath))){
            String sourceLine;
            String targetLine;
            while((sourceLine = brs.readLine()) != null && (targetLine = brt.readLine()) != null){
                LanguageExpression sourceExpr = this.source.getConstructor(List.class).newInstance(Arrays.asList(sourceLine.split(" ")));
                LanguageExpression targetExpr = this.target.getConstructor(List.class).newInstance(Arrays.asList(targetLine.split(" ")));
                AlignedSent alignedSent = new AlignedSent(sourceExpr, targetExpr);
                this.corpus.add(alignedSent);
                this.maxTargetLength += Math.max(maxTargetLength, targetLine.split(" ").length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
    public AlignedSent get(int index) {
        return this.corpus.get(index);
    }

    /**
     * Get the maximum length of target sentence in the corpus
     * @return The maximum length of target sentence in the corpus
     */
    public int getMaxTargetLength(){
        return this.maxTargetLength;
    }

    /**
     * Compute the probability of a target language sentence of length l given
     * source language sentences of length m
     */
    public double computeLengthEstimates(int l, int m){
        List<AlignedSent> filtered = this.corpus.stream().filter(s -> s.getSourceWords().size() == m).collect(Collectors.toList());
        long total = filtered.size();
        long lCount = filtered.stream().filter(s -> s.getTargetWords().size() == l).count();
        return (double) (lCount + 1) / total;
    }

    /**
     * Returns the list of AlignedSentences in this corpus
     *
     * @return List of AlignedSentences in the corpus
     */
    public List<AlignedSent> getSentences(){
        return this.corpus;
    }

}
