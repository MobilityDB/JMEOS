package types.collections.base;


public interface Collection<T extends Object> {


    /** ------------------------- Topological Operations ------------------------ */

    public abstract boolean is_contained_in(Base other) throws Exception;
    public abstract boolean contains(Base other) throws Exception;
    public abstract boolean overlaps(Base other) throws Exception;


    /** ------------------------- Position Operations --------------------------- */

    public abstract boolean is_left(Base other) throws Exception;
    public abstract boolean is_over_or_left(Base other) throws Exception;
    public abstract boolean is_over_or_right(Base other) throws Exception;
    public abstract boolean is_right(Base other) throws Exception;


    /** ------------------------- Database Operations --------------------------- */


}
