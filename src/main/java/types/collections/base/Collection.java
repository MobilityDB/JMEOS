package types.collections.base;

/**
 * Class representing a collection of temporal object with same common behavior
 *
 * @param <T> Base type
 * @author Nidhal Mareghni
 * @since 23/09/2023
 *
 */
public interface Collection<T extends Object> {


    /** ------------------------- Topological Operations ------------------------ */

    boolean is_contained_in(Base other) throws Exception;
    boolean contains(Base other) throws Exception;
    boolean overlaps(Base other) throws Exception;


    /** ------------------------- Position Operations --------------------------- */

    boolean is_left(Base other) throws Exception;
    boolean is_over_or_left(Base other) throws Exception;
    boolean is_over_or_right(Base other) throws Exception;
    boolean is_right(Base other) throws Exception;


    /* ------------------------- Database Operations --------------------------- */


}
