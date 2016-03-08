package structures;

/**
 * Pair (Tuple) Class, for convenience.
 *
 * Created by Sidd Karamcheti on 3/7/16.
 */
public class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair p0 = (Pair) o;
        return this.left.equals(p0.getLeft()) &&
                this.right.equals(p0.getRight());
    }
}
