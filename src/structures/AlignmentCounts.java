package structures;

/**
 * Alignment Counts data structure for keeping track of aligment values during
 * EM for IBM Model 2.
 *
 * Created by Sidd Karamcheti on 3/8/16.
 */
public class AlignmentCounts extends Counts {
    public DefaultDict<Integer, DefaultDict<Integer,
                                    DefaultDict<Integer, DefaultDict<Integer, Double>>>> nIJLM;
    public DefaultDict<Integer, DefaultDict<Integer, DefaultDict<Integer, Double>>> nIO;

    /**
     * AlignmentCounts default constructor.
     */
    public AlignmentCounts() {
        super();
    }

}
