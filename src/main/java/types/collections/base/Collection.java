package types.collections.base;


public abstract class Collection<T extends Object> {


    /** ------------------------- Topological Operations ------------------------ */

    public abstract boolean is_contained_in(Base other);
    public abstract boolean contains(Base other);
    public abstract boolean overlaps(Base other);


    /** ------------------------- Position Operations --------------------------- */

    public abstract boolean is_left(Base other);
    public abstract boolean is_over_or_left(Base other);
    public abstract boolean is_over_or_right(Base other);
    public abstract boolean is_right(Base other);


    /** ------------------------- Database Operations --------------------------- */


}
