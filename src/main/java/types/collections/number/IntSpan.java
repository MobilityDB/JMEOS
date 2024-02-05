package types.collections.number;
import types.collections.base.Base;
import types.collections.base.Span;
import functions.functions;
import jnr.ffi.Pointer;

/**
 * Class for representing sets of contiguous integer values between a lower and
 *     an upper bound. The bounds may be inclusive or not.
 * <p>
 *     ``IntSpan`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 * <p>
 *         >>> IntSpan('(2, 5]')
 * <p>
 *     Another possibility is to provide the ``lower`` and ``upper`` named parameters (of type str or int), and
 *     optionally indicate whether the bounds are inclusive or exclusive (by default, the lower bound is inclusive and the
 *     upper is exclusive):
 * <p>
 *         >>> IntSpan(lower=2, upper=5)
 *         >>> IntSpan(lower=2, upper=5, lower_inc=False, upper_inc=True)
 *         >>> IntSpan(lower='2', upper='5', upper_inc=True)
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class IntSpan extends Span<Integer> implements Number{
    private final Pointer _inner;

    /** ------------------------- Constructor ---------------------------------------- */

    public IntSpan(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public IntSpan(String str){
        super(str);
        _inner = functions.intspan_in(str);
    }

    public IntSpan(int lower, int upper, boolean lower_inc, boolean upper_inc){
        super(lower,upper,lower_inc,upper_inc);
        _inner = functions.intspan_make(lower,upper,lower_inc,upper_inc);
    }

    public IntSpan(int lower, String upper, boolean lower_inc, boolean upper_inc){
        super(lower,upper,lower_inc,upper_inc);
        int new_upper = Integer.parseInt(upper);
        _inner = functions.intspan_make(lower,new_upper,lower_inc,upper_inc);
    }

    public IntSpan(String lower, String upper, boolean lower_inc, boolean upper_inc){
        super(lower,upper,lower_inc,upper_inc);
        int new_upper = Integer.parseInt(upper);
        int new_lower = Integer.parseInt(lower);
        _inner = functions.intspan_make(new_lower,new_upper,lower_inc,upper_inc);
    }

    public IntSpan(String lower, int upper, boolean lower_inc, boolean upper_inc){
        super(lower,upper,lower_inc,upper_inc);
        int new_lower = Integer.parseInt(lower);
        _inner = functions.intspan_make(new_lower,upper,lower_inc,upper_inc);
    }

    public IntSpan(int lower, int upper){
        super(lower,upper,true,false);
        _inner = functions.intspan_make(lower,upper,true,false);
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.intspan_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return inner;
    }

    @Override
    public Pointer createIntInt(java.lang.Number lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
        return functions.intspan_make(lower.intValue(),upper.intValue(),lower_inc,upper_inc);
    }
    @Override
    public Pointer createIntStr(java.lang.Number lower, String upper, boolean lower_inc, boolean upper_inc){
        int new_upper = Integer.parseInt(upper);
        return functions.intspan_make(lower.intValue(),new_upper,lower_inc,upper_inc);
    }
    @Override
    public Pointer createStrStr(String lower, String upper, boolean lower_inc, boolean upper_inc){
        int new_upper = Integer.parseInt(upper);
        int new_lower = Integer.parseInt(lower);
        return functions.intspan_make(new_lower,new_upper,lower_inc,upper_inc);
    }
    @Override
    public Pointer createStrInt(String lower, java.lang.Number upper, boolean lower_inc, boolean upper_inc){
        int new_lower = Integer.parseInt(lower);
        return functions.intspan_make(new_lower,upper.intValue(),lower_inc,upper_inc);
    }
    @Override
    public Pointer createIntIntNb(java.lang.Number lower, java.lang.Number upper){
        return functions.intspan_make(lower.intValue(),upper.intValue(),true,false);
    }

    /**
     * Return a copy of "this".
     * <p>
     *         MEOS Functions:
     *             <li>span_copy</li>
     * @return a new IntSpan instance
     */
    public IntSpan copy(){
        return new IntSpan(functions.span_copy(this._inner));
    }
    /*
    public IntSpan from_wkb(Byte b){
        return new IntSpan(functions.span_from_wkb(b));
    }

     */

    /**
     * Returns a Period from its WKB representation in hex-encoded ASCII.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>span_from_hexwkb</li>
     *
     * @param str WKB representation in hex-encoded ASCII
     * @return a new {@link types.collections.time.Period} instance
     */
    public IntSpan from_hexwkb(String str){
        return new IntSpan(functions.span_from_hexwkb(str));
    }

    /* ------------------------- Output ---------------------------------------- */

    /**
     * Return the string representation of the content of "this".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>intspan_out</li>
     *
     *
     * @return A new {@link String} instance
     */
    public String toString(){
        return functions.intspan_out(this._inner);
    }



    /* ------------------------- Conversions ----------------------------------- */


    /**
     * Returns a SpanSet that contains a Span for each element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>span_to_spanset</li>
     *
     * @return A new {@link IntSpanSet} instance
     */
    public IntSpanSet to_spanset(){
        return new IntSpanSet(functions.span_to_spanset(this._inner));
    }


    /**
     * Converts "this" to a {@link FloatSpan} instance.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>intspan_floatspan</li>
     *
     * @return A new :class:`FloatSpan` instance
     */

    public FloatSpan tofloatspan(){
        return new FloatSpan(functions.intspan_floatspan(this._inner));
    }




    /** ------------------------- Accessors ------------------------------------- */

    @Override
    public Pointer get_inner(){
        return _inner;
    }


    /**
     * Returns the lower bound of "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>period_lower</li>
     *
     * @return The lower bound of the span as a {@link Integer}
     */
    public Integer lower(){
        return functions.intspan_lower(this._inner);
    }



    /**
     * Returns the upper bound of "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>period_upper</li>
     *
     * @return The lower bound of the span as a {@link Integer}
     */
    public Integer upper(){
        return functions.intspan_upper(this._inner);
    }


    /**
     * Returns the width of "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>span_width</li>
     *
     * @return Returns a "float" representing the width of the span
     */
    public float width(){
        return (float) functions.span_width(this._inner);
    }



    /* ------------------------- Topological Operations -------------------------------- */

    /**
     * Returns whether "this" is adjacent to "other". That is, they share
     *         a bound but only one of them contains it.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>adjacent_span_span</li>
     *             <li>adjacent_span_spanset</li>
     *             <li>adjacent_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if adjacent, False otherwise
     * @throws Exception
     */
    public boolean is_adjacent(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.adjacent_intspan_int(this._inner, (int) other);
        }
        else {
            return super.is_adjacent((Base) other);
        }
    }


    /**
     * Returns whether "this" contains "content".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>contains_set_set</li>
     *             <li>contains_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.contains_intspan_int(this._inner, (int) other);
        }
        else {
            return super.contains((Base) other);
        }
    }


    /**
     * Returns whether "this" and the bounding period of "other is the
     *         same.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>same_period_temporal</li>
     *
     * @param other object to compare with
     * @return True if equal, False otherwise
     * @throws Exception
     */
    public boolean is_same(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.span_eq(this._inner, functions.int_to_intspan((int)other));
        }
        else {
            return super.is_same((Base) other);
        }
    }



    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is strictly before "other". That is,
     *         "this" ends before "other" starts.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             left_span_span
     *             left_span_spanset
     *             left_intspan_int
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_left(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.left_intspan_int(this._inner, (int) other);
        }
        else {
            return super.is_left((Base) other);
        }
    }


    /**
     * Returns whether "this" is before "other" allowing overlap. That is,
     *         "this ends before "other" ends (or at the same value).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overleft_span_span</li>
     *             <li>overleft_span_spanset</li>
     *             <li>overleft_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.overleft_intspan_int(this._inner, (int) other);
        }
        else {
            return super.is_over_or_left((Base) other);
        }
    }

    /**
     * Returns whether "this" is strictly after "other". That is, "this"
     *         starts after "other" ends.
     *
     *   <p>
     *
     *         MEOS Functions:
     *             <li>right_span_span</li>
     *             <li>right_span_spanset</li>
     *             <li>right_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if after, False otherwise
     * @throws Exception
     */
    public boolean is_right(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.right_intspan_int(this._inner, (int) other);
        }
        else {
            return super.is_right((Base) other);
        }
    }


    /**
     * Returns whether "this" is after "other" allowing overlap. That is,
     *         "this" starts after "other" starts (or at the same value).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overright_span_span</li>
     *             <li>overright_span_spanset</li>
     *             <li>overright_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if overlapping or after, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.overright_intspan_int(this._inner, (int) other);
        }
        else {
            return super.is_over_or_right((Base) other);
        }
    }



    /* ------------------------- Distance Operations --------------------------- */


    /**
     * Returns the distance between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>distance_span_span</li>
     *             <li>distance_span_spanset</li>
     *             <li>distance_intspan_int</li>
     *
     * @param other object to compare with
     * @return A float value
     * @throws Exception
     */
    public Float distance(Object other) throws Exception {
        if (other instanceof Integer){
            return (float) functions.distance_intspan_int(this._inner, (int) other);
        }
        else {
            return super.distance((Base) other);
        }
    }


    /* ------------------------- Set Operations -------------------------------- */


    /**
     * Returns the difference of "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>minus_span_span</li>
     *             <li>minus_spanset_span</li>
     *             <li>minus_intspan_int</li>
     *
     * @param other object to diff with
     * @return A {@link IntSpanSet} instance.
     */
    public IntSpanSet minus(Object other){
        Pointer result = null;
        if (other instanceof Integer){
            result = functions.minus_intspan_int(this._inner,(int)other);
        }
        else if (other instanceof IntSpan) {
            result = functions.minus_span_span(this._inner,((IntSpan) other).get_inner());
        }
        else if (other instanceof IntSpanSet) {
            result = functions.minus_spanset_span(((IntSpanSet) other).get_inner(), this._inner);
        }
        return new IntSpanSet(result);
    }


    /**
     * Returns the union of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>union_spanset_span</li>
     *             <li>union_span_span</li>
     *             <li>union_intspan_int</li>
     *
     * @param other object to merge with
     * @return A {@link IntSpanSet} instance.
     */
    public IntSpanSet union(Object other) throws Exception {
        Pointer result = null;
        if (other instanceof Integer){
            result = functions.union_intspan_int(this._inner,(int)other);
        }
        else if (other instanceof IntSpan) {
            result = functions.union_span_span(this._inner,((IntSpan) other).get_inner());
        }
        else if (other instanceof IntSpanSet) {
            result = functions.union_spanset_span(((IntSpanSet) other).get_inner(), this._inner);
        }
        else {
            IntSpanSet tmp = (IntSpanSet) super.union((Base)other);
            result = tmp.get_inner();
        }
        return new IntSpanSet(result);
    }



}