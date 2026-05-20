package functions;

public enum RTreeSearchOp {
    RTREE_OVERLAPS(0),    /**< Find stored boxes that overlap the query */
    RTREE_CONTAINS(1),    /**< Find stored boxes that contain the query */
    RTREE_CONTAINED_BY(2); /**< Find stored boxes contained by the query */

    private final int value;

    RTreeSearchOp(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
