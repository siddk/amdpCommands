package structures;

import java.util.ArrayList;

/**
 * Structure that defines an AlignedSent object, consisting of two sentences and an alignment
 * between them.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class AlignedSent {
    ArrayList<String> words;
    ArrayList<String> mots;
    Alignment align;

    /**
     * Creates an Aligned Sentence with words, target words (mots).
     *
     * @param words List of source words.
     * @param mots List of target words.
     */
    public AlignedSent(ArrayList<String> words, ArrayList<String> mots) {
        this.words = words;
        this.mots = mots;
        this.align = new Alignment();
    }

    /**
     * Creates an Aligned Sentence with words, target words (mots), and a specified alignment.
     *
     * @param words List of source words.
     * @param mots List of target words.
     * @param align Predefined alignment.
     */
    public AlignedSent(ArrayList<String> words, ArrayList<String> mots, Alignment align) {
        this.words = words;
        this.mots = mots;
        this.align = align;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ArrayList<String> getMots() {
        return mots;
    }

    public Alignment getAlign() {
        return align;
    }

}

