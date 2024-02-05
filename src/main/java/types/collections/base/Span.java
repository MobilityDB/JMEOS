package types.collections.base;

import jnr.ffi.Pointer;
import functions.functions;


/**
 * Abstract class that represents a span of temporal object
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class Span<T extends Object> implements Collection, Base{
    private Pointer _inner;


    /** ------------------------- Constructors ---------------------------------- */
    public Span(){}

    public Span(Pointer inner){
        this._inner = createInner(inner);
    }
    public Span(String str){
        this._inner = createStringInner(str);
    }

    public Span(java.lang.Number lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
        this._inner = createIntInt(lower, upper, lower_inc, upper_inc);
    }
    public Span(java.lang.Number lower, String upper, boolean lower_inc, boolean upper_inc){
        this._inner = createIntStr(lower, upper, lower_inc, upper_inc);
    }
    public Span(String lower, String upper, boolean lower_inc, boolean upper_inc){
        this._inner = createStrStr(lower, upper, lower_inc, upper_inc);
    }
    public Span(String lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
        this._inner = createStrInt(lower,upper, lower_inc, upper_inc);
    }
    public Span(java.lang.Number lower, java.lang.Number upper){
        this._inner = createIntIntNb(lower, upper);
    }

    public abstract Pointer get_inner();
    public abstract Pointer createInner(Pointer inner);
    public abstract Pointer createStringInner(String str);
    public abstract Pointer createIntInt(java.lang.Number lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc);
    public abstract Pointer createIntStr(java.lang.Number lower, String upper, boolean lower_inc, boolean upper_inc);
    public abstract Pointer createStrStr(String lower, String upper, boolean lower_inc, boolean upper_inc);
    public abstract Pointer createStrInt(String lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc);
    public abstract Pointer createIntIntNb(java.lang.Number lower, java.lang.Number upper);



    /* ------------------------- Conversions ----------------------------------- */



    /* ------------------------- Accessors ------------------------------------- */


    /**
     * Returns the lower bound of a period
     * @return T type
     */
    public abstract T lower();


    /**
     * Returns the upper bound of a period
     * @return T type
     */
    public abstract T upper();

    /**
     * Returns whether the lower bound of the period is inclusive or not
     *  <p>
     *         MEOS Functions:
     *             <li>span_lower_inc</li>
     * @return True if the lower bound of the period is inclusive and False otherwise
     */
    public boolean lower_inc(){
        return functions.span_lower_inc(this._inner);
    }


    /**
     * Returns whether the upper bound of the period is inclusive or not
     *  <p>
     *         MEOS Functions:
     *             <li>span_upper_inc</li>
     * @return True if the upper bound of the period is inclusive and False otherwise
     */
    public boolean upper_inc(){
        return functions.span_upper_inc(this._inner);
    }


    /**
     * Returns the duration of the period.
     * <p>
     *         MEOS Functions:
     *             <li>span_width</li>
     * @return Returns a {@link Float} representing the duration of the period in seconds
     */
    public float width(){
        return (float) functions.span_width(this._inner);
    }

    /**
     * Return the hash representation of "this".
     * <p>
     *         MEOS Functions:
     *             <li>span_hash</li>
     * @return A new {@link Integer} instance
     */
    public long hash(){
        return functions.span_hash(this._inner);
    }


    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether "this" is adjacent to "other". That is, they share
     *         a bound but only one of them contains it.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>adjacent_span_span</li>
     *             <li>adjacent_span_spanset</li>
     *
     * @param other object to compare with
     * @return True if adjacent, False otherwise
     * @throws Exception
     */
    public boolean is_adjacent(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.adjacent_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.adjacent_spanset_span(((SpanSet<?>) other).get_inner(),this._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is contained in "container".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>contained_span_span</li>
     *             <li>contained_span_spanset</li>
     *             <li>contained_period_temporal</li>
     *
     * @param other temporal object to compare with
     * @return True if contained, False otherwise
     * @throws Exception
     */
    public boolean is_contained_in(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.contained_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.contained_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }

    /**
     * Returns whether "this" contains "content".
     *
     *  <p>
     *
     *         MEOS Functions:
     *           <ul>
     *             <li>contains_span_span</li>
     *             <li>contains_span_spanset</li>
     *             <li>contains_period_timestamp</li>
     *             <li>contains_period_timestampset</li>
     *             <li>contains_period_temporal</li>
     *           </ul>
     *
     * @param other temporal object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.contains_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.contains_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" overlaps "other". That is, both share at
     *         least an element.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overlaps_span_span</li>
     *             <li>overlaps_span_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if overlaps, False otherwise
     * @throws Exception
     */
    public boolean overlaps(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overlaps_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.overlaps_spanset_span(((SpanSet<?>) other).get_inner(),this._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    public boolean is_same(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.span_eq(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.span_eq(this._inner,functions.spanset_span(((SpanSet<?>) other).get_inner()));
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /* ------------------------- Position Operations --------------------------- */

    /**
     * Returns whether "this" is strictly before "other". That is,
     *         "this" ends before "other" starts.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>left_span_span</li>
     *             <li>left_span_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_left(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.left_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.left_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is before "other" allowing overlap. That is,
     *         "this" ends before "other" ends (or at the same time).
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>overleft_span_span</li>
     *             <li>overleft_span_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overleft_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.overleft_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
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
     *             <li>right_span_span</li>
     *             <li>right_span_spanset</li>
     *
     * @param other temporal object to compare with
     * @return True if after, False otherwise
     * @throws Exception
     */
    public boolean is_right(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.right_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.right_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is after "other" allowing overlap. That is,
     *         "this" starts after "other" starts (or at the same time).
     *
     * <p>
     *
     *         MEOS Functions:
     *           <ul>
     *             <li>overright_span_span</li>
     *             <li>overright_span_spanset</li>
     *             <li>overafter_period_timestamp</li>
     *             <li>overafter_period_timestampset</li>
     *             <li>overafter_period_temporal</li>
     *           </ul>
     *
     * @param other temporal object to compare with
     * @return True if overlapping or after, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.overright_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return functions.overright_span_spanset(this._inner, ((SpanSet<?>) other).get_inner());
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
     *         <ul>
     *             <li>distance_span_span</li>
     *             <li>distance_spanset_span</li>
     *         </ul>
     * </p>
     *
     * @param other object to compare with
     * @return A {@link Float} instance
     * @throws Exception
     */
    public float distance(Base other) throws Exception {
        if (other instanceof Span<?>){
            return (float) functions.distance_span_span(this._inner, ((Span<?>) other)._inner);
        } else if (other instanceof SpanSet<?>) {
            return (float) functions.distance_spanset_span(((SpanSet<?>) other).get_inner(),this._inner);
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
     *             <li>intersection_span_span</li>
     *             <li>intersection_spanset_span</li>
     *             <li>intersection_period_timestamp</li>
     *
     * @param other object to intersect with
     * @return A collection instance. The actual class depends on ``other``.
     * @throws Exception
     */

    public Base intersection(Base other) throws Exception {
        if (other instanceof Span<?>){
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.intersection_span_span(this._inner, ((Span<?>) other)._inner));
        } else if (other instanceof SpanSet<?>) {
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.intersection_spanset_span(((SpanSet<?>) other).get_inner(),this._inner));
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
     *         <li>union_period_timestamp</li>
     *         <li>union_spanset_span</li>
     *         <li>union_span_span</li>
     *
     * @param other temporal object to merge with
     * @return A {@link types.collections.time.PeriodSet} instance.
     * @throws Exception
     */
    public Base union(Base other) throws Exception {
        if (other instanceof Span<?>){
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.union_span_span(this._inner, ((Span<?>) other)._inner));
        } else if (other instanceof SpanSet<?>) {
            return this.getClass().getConstructor(Pointer.class).newInstance(functions.union_spanset_span(((SpanSet<?>) other).get_inner(),this._inner));
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
     *             <li>span_eq</li>
     *
     * @param other temporal object to compare with
     * @return True if equal, False otherwise
     */
    public boolean eq(Base other){
        if (other instanceof Span<?>){
            return functions.span_eq(this._inner,((Span<?>) other)._inner);
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
     *             <li>span_neq</li>
     * @param other temporal object to compare with
     * @return True if not equal, False otherwise
     */
    public boolean notEquals(Base other){
        if (other instanceof Span<?>){
            return functions.span_ne(this._inner,((Span<?>) other)._inner);
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
     *             <li>span_lt</li>
     *
     * @param other temporal object to compare with
     * @return True if less than, False otherwise
     * @throws Exception
     */
    public boolean lessThan(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.span_lt(this._inner,((Span<?>) other)._inner);
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
     *             <li>span_le</li>
     *
     * @param other temporal object to compare with
     * @return True if less than or equal, False otherwise
     * @throws Exception
     */
    public boolean lessThanOrEqual(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.span_le(this._inner,((Span<?>) other)._inner);
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
     *             <li>span_gt</li>
     *
     * @param other temporal object to compare with
     * @return True if greater than, False otherwise
     * @throws Exception
     */
    public boolean greaterThan(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.span_gt(this._inner,((Span<?>) other)._inner);
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
     *             <li>span_ge</li>
     *
     * @param other temporal object to compare with
     * @return True if greater than or equal, False otherwise
     * @throws Exception
     */
    public boolean greaterThanOrEqual(Base other) throws Exception {
        if (other instanceof Span<?>){
            return functions.span_ge(this._inner,((Span<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }

























}
