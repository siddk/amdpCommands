package language;

import java.util.List;

/**
 * Interface to be implemented by any class representing expressions of a particular language
 * Created by Dilip Arumugam on 3/8/16.
 */
public abstract class LanguageExpression {
    protected final List<String> words;

    public LanguageExpression(List<String> words){
        this.words = words;
    }

    public List<String> getWords(){
        return this.words;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for(String w : this.words){
            sb.append(w + " ");
        }
        return sb.toString().trim();
    }
}
