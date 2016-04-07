package structures;

/**
 * Alignment Counts data structure for keeping track of alignment values during
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
        this.nIJLM = new DefaultDict<>(a -> new DefaultDict<>(b -> new DefaultDict<>(c -> new DefaultDict<>(0.0))));
        this.nIO = new DefaultDict<>(a -> new DefaultDict<>(b -> new DefaultDict<>(0.0)));
    }

    public void updateTau(double count, String s, String t) {
        this.nTS.get(t).put(s, this.nTS.get(t).get(s) + count);
        this.nTO.put(s, this.nTO.get(s) + count);
    }

    public void updateDelta(double count, int i, int j, int l, int m) {
        this.nIJLM.get(i).get(j).get(l).put(m, this.nIJLM.get(i).get(j).get(l).get(m) + count);
        this.nIO.get(j).get(l).put(m, this.nIO.get(j).get(l).get(m) + count);
    }

}
