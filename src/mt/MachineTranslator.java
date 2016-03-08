package mt;

import language.LanguageExpression;

/**
 * Created by Dilip Arumugam on 3/8/16.
 */
public interface MachineTranslator<S extends LanguageExpression,T extends LanguageExpression> {

    /**
     * Translate a single expression of the translator's source language into an expression of the target language
     * @param sourceExpression A language expression in the translator's source language
     * @return A language expression in the translator's target language
     */
    public T translate(S sourceExpression);
}
