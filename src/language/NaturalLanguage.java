package language;

import java.util.List;

/**
 * Created by Dilip Arumugam on 3/8/16.
 */
public class NaturalLanguage implements LanguageExpression {
    protected final List<String> words;

    public NaturalLanguage(List<String> words){
        this.words = words;
    }

    @Override
    public List<String> getWords() {
        return this.words;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (String w : this.words) {
            sb.append(w + " ");
        }
        return sb.toString().trim();
    }
}
