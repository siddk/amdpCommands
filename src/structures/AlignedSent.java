package structures;

import language.LanguageExpression;

import java.util.List;

/**
 * Structure that defines an AlignedSent object, consisting of two language expressions and an alignment
 * between them.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class AlignedSent<S extends LanguageExpression,T extends LanguageExpression> {
    protected final S source;
    protected final T target;
    protected final Alignment align;

    /**
     * Creates an Aligned Sentence with words, target words (mots).
     *
     * @param source Expression of the source language
     * @param target Expression of the target language
     */
    public AlignedSent(S source, T target) {
        this.source = source;
        this.target = target;
        this.align = new Alignment();
    }

    /**
     * Creates an Aligned Sentence with two language expressions and a specified alignment.
     *
     * @param source Expression of the source language
     * @param target Expression of the target language
     * @param align Predefined alignment.
     */
    public AlignedSent(S source, T target, Alignment align) {
        this.source = source;
        this.target = target;
        this.align = align;
    }

    public List<String> getSourceWords() {
        return this.source.getWords();
    }

    public List<String> getTargetWords() {
        return this.target.getWords();
    }

    public Alignment getAlign() {
        return this.align;
    }

}

