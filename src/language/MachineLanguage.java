package language;

import java.util.List;

/**
 * Created by Dilip Arumugam on 3/8/16.
 */
public class MachineLanguage implements LanguageExpression {
    protected final List<String> words;

    public MachineLanguage(List<String> words){
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
