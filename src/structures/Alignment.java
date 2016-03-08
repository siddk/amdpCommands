package structures;

import java.util.ArrayList;

/**
 * Simple Alignment class, represented of a list of Integer-Integer Pairs.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class Alignment {
    ArrayList<Pair<Integer, Integer>> alignment;

    /**
     * Default Alignment Constructor.
     */
    public Alignment() {
        this.alignment = new ArrayList<Pair<Integer, Integer>>();
    }

    /**
     * Constructs Alignment from List of Pairs.
     *
     * @param alignment List of Integer-Integer Pairs.
     */
    public Alignment(ArrayList<Pair<Integer, Integer>> alignment) {
        this.alignment = alignment;
    }

}
