package structures;

import language.LanguageExpression;

import java.util.List;

/**
 * Structure that defines an AlignedSent object, consisting of two language expressions and an alignment
 * between them.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class AlignedSent{
    protected final LanguageExpression source;
    protected final LanguageExpression target;
    protected final Alignment align;

    /**
     * Creates an Aligned Sentence with words, target words (mots).
     *
     * @param source Expression of the source language
     * @param target Expression of the target language
     */
    public AlignedSent(LanguageExpression source, LanguageExpression target) {
        this.source = source;
        this.target = target;
        this.align = new Alignment();
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

