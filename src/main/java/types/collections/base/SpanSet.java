package types.collections.base;

import jnr.ffi.Pointer;
import functions.functions;
import types.collections.time.Period;

import java.util.List;


/**
 * Abstract class that represents a set of span of temporal object
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class SpanSet<T extends Object> implements Collection, Base {
    private Pointer _inner = null;

    /** ------------------------- Constructor ----------------------------------- */

    public SpanSet(){}

    public SpanSet(Pointer inner){
        this._inner = createInner(inner);
    }
    public SpanSet(String str){
        this._inner = createStringInner(str);
    }
    public SpanSet(List<Period> periods){this._inner = createListInner(periods);}


    public abstract Pointer get_inner();
    public abstract Pointer createInner(Pointer inner);
    public abstract Pointer createStringInner(String str);
    public abstract Pointer createListInner(List<Period> periods);


    /* ------------------------- Conversions ----------------------------------- */



    /* ------------------------- Accessors ------------------------------------- */


    /**
     * Returns the number of spans in "this".
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_num_spans</li>
     * @return An {@link Integer}
     */
    public int num_spans(){
        return functions.spanset_num_spans(this._inner);
    }

    /*
      Returns the first span in "this".
      <p>

              MEOS Functions:
                  <li>spanset_start_span</li>

      @return A {@link Span} instance
     */
    /*
    public Span start_span(){
        return new Span(functions.spanset_start_span(this._inner));
    }

     */

    /*
      Returns the last span in "this".
      <p>

              MEOS Functions:
                  <li>spanset_end_span</li>

      @return A {@link Span} instance
     */
    /*
    public Span end_span(){
        return new Span(functions.spanset_end_span(this._inner));
    }

     */


    /*
      Returns the n-th span in "this".
       <p>
              MEOS Functions:
                  <li>spanset_span_n</li>

      @param n number of Span
     * @return A {@link Span} instance
     */
    /*
    public Span span_n(int n){
        return new Span(functions.spanset_span_n(this._inner,n));
    }

     */


    /**
     * Return the hash representation of "this".
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_hash</li>
     *
     * @return A new :class:`int` instance
     */
    public long hash(){
        return functions.spanset_hash(this._inner);
    }

    /* ------------------------- Transformations ------------------------------- */


    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether "this" is adjacent to "other". That is, they share a bound but only one of them
     *         contains it.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>adjacent_spanset_span</li>
     *             <li>adjacent_spanset_spanset</li>
     *
     * @param other object to compare with
     * @return True if adjacent, False otherwise
     * @throws Exception
     */
    public boolean is_adjacent(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.adjacent_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.adjacent_spanset_spanset(this._inner,((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }

    /**
     * Returns whether "this" is contained in "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *           <ul>
     *             <li>contained_spanset_span</li>
     *             <li>contained_spanset_spanset</li>
     *           </ul>
     *
     * @param other temporal object to compare with
     * @return True if contained, False otherwise
     * @throws Exception
     */
    public boolean is_contained_in(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.contained_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.contained_spanset_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" contains "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *              <li>contains_spanset_span</li>
     *              <li>contains_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.contains_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.contains_spanset_spanset(this._inner, ((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" overlaps "other". That is, both share at least an instant.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overlaps_spanset_span</li>
     *             <li>overlaps_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if overlaps, False otherwise
     * @throws Exception
     */
    public boolean overlaps(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overlaps_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.overlaps_spanset_spanset(this._inner,((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether the bounding span of "this" is the same as the bounding span of "other".
     *
     *  <p>
     *
     *         See Also:
     *              {@link types.collections.time.Period#is_same(Base)}
     *
     *             
     * @param other A time or temporal object to compare to "this".
     * @return True if same, False otherwise.
     * @throws Exception
     */
    public boolean is_same(Base other) throws Exception {
        return true;
        //return this.to_span().is_same(other);
    }


    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is strictly to the left of "other". That is, "this" ends before "other" starts.
     *
     * <p>
     *
     *         MEOS Functions:
     *              <li>before_periodset_timestamp</li>
     *              <li>left_spanset_span</li>
     *              <li>left_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_left(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.left_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.left_spanset_spanset(this._inner, ((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is to the left of "other" allowing overlap. That is, "this" ends before "other" ends (or
     *         at the same time).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overleft_spanset_span</li>
     *             <li>overleft_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overleft_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.overleft_spanset_spanset(this._inner, ((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is strictly after "other". That is, "this"
     *         starts after "other" ends.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>right_spanset_span</li>
     *             <li>right_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if after, False otherwise
     * @throws Exception
     */
    public boolean is_right(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.right_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.right_spanset_spanset(this._inner, ((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is to the right of "other" allowing overlap. That is, "this" starts after "other" starts
     *         (or at the same time).
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>overright_spanset_span</li>
     *             <li>overright_spanset_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if overlapping or after, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overright_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return functions.overright_spanset_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }



    /* ------------------------- Distance Operations --------------------------- */


    /**
     * Returns the distance between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>distance_spanset_span</li>
     *             <li>distance_spanset_spanset</li>
     *
     * @param other object to compare with
     * @return A {@link Float} instance
     * @throws Exception
     */
    public float distance(Base other) throws Exception {
        if (other instanceof Span<?>){
            return (float) functions.distance_spanset_span(this._inner, ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return (float) functions.distance_spanset_spanset(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }



    /* ------------------------- Set Operations -------------------------------- */

    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *              <li>intersection_spanset_spanset</li>
     *              <li>intersection_spanset_span</li>
     *
     * @param other object to intersect with
     * @return A collection instance. The actual class depends on "other".
     * @throws Exception
     */
    public Base intersection(Base other) throws Exception {
        if (other instanceof Span<?>){
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.intersection_spanset_span(this._inner, ((Span<?>) other).get_inner()));
        } else if (other instanceof SpanSet<?>) {
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.intersection_spanset_spanset(this._inner,((SpanSet<?>) other).get_inner()));
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }




    /**
     * Returns the temporal difference of ``self`` and ``other``.
     *
     *  <p>
     *
     *         MEOS Functions:
     *              <li>minus_spanset_span</li>
     *              <li>minus_spanset_spanset</li>
     *
     * @param other temporal object to diff with
     * @return A {@link types.collections.time.PeriodSet} instance.
     * @throws Exception
     */
    public Base minus(Base other) throws Exception {
        if (other instanceof Span<?>){
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.minus_spanset_span(this._inner, ((Span<?>) other).get_inner()));
        } else if (other instanceof SpanSet<?>) {
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.minus_spanset_spanset(this._inner,((SpanSet<?>) other).get_inner()));
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }




    /**
     * Returns the temporal union of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *         <li>union_periodset_timestamp</li>
     *         <li>union_spanset_spanset</li>
     *         <li>union_spanset_span</li>
     *
     * @param other temporal object to merge with
     * @return A {@link types.collections.time.PeriodSet} instance.
     * @throws Exception
     */
    public Base union(Base other) throws Exception {
        if (other instanceof Span<?>){
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.union_spanset_span(this._inner, ((Span<?>) other).get_inner()));
        } else if (other instanceof SpanSet<?>) {
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.union_spanset_spanset(((SpanSet<?>) other).get_inner(),this._inner));
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }



    /* ------------------------- Comparisons ----------------------------------- */


    /**
     * Return whether "this" and "other" are equal.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>spanset_eq</li>
     *
     * @param other temporal object to compare with
     * @return True if equal, False otherwise
     */
    public boolean eq(Base other){
        if (other instanceof SpanSet<?>){
            return functions.spanset_eq(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            return false;
        }
    }


    /**
     * Return whether "this" and "other" are not equal.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>spanset_neq</li>
     *
     * @param other temporal object to compare with
     * @return True if not equal, False otherwise
     */
    public boolean notEquals(Base other){
        if (other instanceof SpanSet<?>){
            return functions.spanset_ne(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            return true;
        }
    }


    /**
     * Return whether "this" is less than "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_lt</li>
     *
     * @param other temporal object to compare with
     * @return True if less than, False otherwise
     * @throws Exception
     */
    public boolean lessThan(Base other) throws Exception {
        if (other instanceof SpanSet<?>){
            return functions.spanset_lt(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Return whether "this" is less than or equal to "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_le</li>
     *
     * @param other temporal object to compare with
     * @return True if less than or equal, False otherwise
     * @throws Exception
     */
    public boolean lessThanOrEqual(Base other) throws Exception {
        if (other instanceof SpanSet<?>){
            return functions.spanset_le(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Return whether "this" is greater than "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>spanset_gt</li>
     *
     * @param other temporal object to compare with
     * @return True if greater than, False otherwise
     * @throws Exception
     */
    public boolean greaterThan(Base other) throws Exception {
        if (other instanceof SpanSet<?>){
            return functions.spanset_gt(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }

    /**
     * Return whether "this" is greater than or equal to "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_ge</li>
     *
     * @param other temporal object to compare with
     * @return True if greater than or equal, False otherwise
     * @throws Exception
     */
    public boolean greaterThanOrEqual(Base other) throws Exception {
        if (other instanceof SpanSet<?>){
            return functions.spanset_ge(this._inner,((SpanSet<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


















}
