package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.Base;
import types.collections.base.SpanSet;
import types.collections.time.Period;

import java.util.List;

/**
 * Class for representing lists of disjoint floatspans.
 * <p>
 *     ``FloatSpanSet`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 * <p>
 *         >>> FloatSpanSet(string='{[8, 10], [11, 1]}')
 * <p>
 *     Another possibility is to give a list specifying the composing
 *     spans, which can be instances  of ``str`` or ``FloatSpan``. The composing
 *     spans must be given in increasing order.
 * <p>
 *         >>> FloatSpanSet(span_list=['[8, 10]', '[11, 12]'])
 *         >>> FloatSpanSet(span_list=[FloatSpan('[8, 10]'), FloatSpan('[11, 12]')])
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class FloatSpanSet extends SpanSet<Float> implements Number{

    private final Pointer _inner;

    /** ------------------------- Constructors ---------------------------------- */

    public FloatSpanSet(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public FloatSpanSet(String str){
        super(str);
        _inner = functions.floatspanset_in(str);
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.floatspanset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return inner;
    }


    @Override
    public Pointer createListInner(List<Period> periods){
        return null;
    }

    /* ------------------------- Output ---------------------------------------- */


    /**
     * Return the string representation of the content of "this".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_out</li>
     *
     *
     * @param max_decimals number of maximum decimals
     * @return A new {@link String} instance
     */
    public String toString(int max_decimals){
        return functions.floatspanset_out(this._inner, max_decimals);
    }


    /* ------------------------- Conversions ----------------------------------- */


    /**
     * Returns a span that encompasses "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>spanset_span</li>
     *
     * @return A new {@link FloatSpan} instance
     */
    public FloatSpan to_span(){
        return new FloatSpan(functions.spanset_span(this._inner));
    }


    /**
     * Returns an intspanset that encompasses "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>floatspanset_intspanset</li>
     *
     * @return A new {@link IntSpanSet} instance
     */
    public IntSpanSet to_intspanset(){
        return new IntSpanSet(functions.floatspanset_intspanset(this._inner));
    }



    /** ------------------------- Accessors ------------------------------------- */


    public Pointer get_inner(){
        return _inner;
    }

    /**
     * Returns the width of the spanset. By default, i.e., when the second
     *         argument is False, the function takes into account the gaps within,
     *         i.e., returns the sum of the widths of the spans within.
     *         Otherwise, the function returns the width of the spanset ignoring
     *         any gap, i.e., the width from the lower bound of the first span to
     *         the upper bound of the last span.
     * <p>
     *         MEOS Functions:
     *             <li>spanset_width</li>
     *
     * @param ignore_gaps Whether to take into account potential gaps in
     *      *             the spanset.
     * @return A `float` representing the duration of the spanset
     */
    public float width(boolean ignore_gaps){
        return (float) functions.spanset_width(this._inner, ignore_gaps);
    }

    /*
      Returns the first span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_start_span</li>

      @return A {@link FloatSpan} instance
     */
    /*
    public FloatSpan start_span(){
        return new FloatSpan(super.start_span().get_inner());
    }


     */

    /*
      Returns the last span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_end_span</li>

      @return A {@link FloatSpan} instance
     */
    /*
    public FloatSpan end_span(){
        return new FloatSpan(super.end_span().get_inner());
    }


     */

    /*
      Returns the n-th span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_span_n</li>

      @return A {@link FloatSpan} instance
     */
    /*
    public FloatSpan span_n(int n){
        return new FloatSpan(super.span_n(n).get_inner());
    }

     */

    /* ------------------------- Transformations ------------------------------- */


    /**
     * Return a new "FloatSpanSet" with the lower and upper bounds shifted by
     *         "delta".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     *
     * @param delta The value to shift by
     * @return A new {@link FloatSpanSet} instance
     */
    public FloatSpanSet shif(int delta){
        return this.shift_scale(delta,0);
    }



    /**
     * Return a new "FloatSpanSet" with the lower and upper bounds scaled so
     *         that the width is "width".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     * @param width The new width
     * @return a new {@link FloatSpanSet} instance
     */

    public FloatSpanSet scale(int width){
        return this.shift_scale(0,width);
    }


    /**
     * Return a new "FloatSpanSet" with the lower and upper bounds shifted by
     *         "delta" and scaled so that the width is "width".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     * @param delta The value to shift by
     * @param width v
     * @return a new {@link FloatSpanSet} instance
     */
    public FloatSpanSet shift_scale(int delta, int width){
        return new FloatSpanSet(functions.floatspanset_shift_scale(this._inner,delta,width,delta != 0, width != 0));
    }




    /* ------------------------- Topological Operations -------------------------------- */


    /**
     * Returns whether "this" is adjacent to "other". That is, they share
     *         a bound but only one of them contains it.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>adjacent_spanset_span</li>
     *             <li>adjacent_spanset_spanset</li>
     *             <li>adjacent_floatspanset_float</li>
     *
     * @param other object to compare with
     * @return True if adjacent, False otherwise
     * @throws Exception
     */
    public boolean is_adjacent(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.adjacent_floatspanset_float(this._inner, (float) other);
        }
        else{
            answer = super.is_adjacent((Base)other);
        }
        return answer;
    }

    /**
     * Returns whether "this" contains "content".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>contains_spanset_span</li>
     *             <li>contains_spanset_spanset</li>
     *             <li>contains_floatspanset_float</li>
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.contains_floatspanset_float(this._inner, (float) other);
        }
        else{
            return super.contains((Base)other);
        }
    }


    /**
     * Returns whether "this" and the bounding period of "other" is the
     *         same.
     *
     * <p>
     *         MEOS Functions:
     *             <li>same_period_temporal</li>
     *
     * @param other object to compare with
     * @return True if equal, False otherwise
     * @throws Exception
     */
    public boolean is_same(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.spanset_eq(this._inner,functions.float_to_floatspanset((float) other));
        }
        else{
            return super.is_same((Base)other);
        }
    }



    /* ------------------------- Position Operations --------------------------- */

    /**
     * Returns whether "this" is strictly left of "other". That is,
     *         "this" ends before "other" starts.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>left_span_span</li>
     *             <li>left_span_spanset</li>
     *             <li>left_floatspan_float</li>
     *
     * @param other object to compare with
     * @return True if left, False otherwise
     * @throws Exception
     */
    public boolean is_left(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.left_floatspanset_float(this._inner,(float) other);
        }
        else{
            answer = super.is_left((Base)other);
        }
        return answer;
    }


    /**
     * Returns whether "this" is left "other" allowing overlap. That is,
     *         "this" ends before "other" ends (or at the same value).
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>overleft_span_span</li>
     *             <li>overleft_span_spanset</li>
     *             <li>overleft_floatspan_float</li>
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.overleft_floatspanset_float(this._inner,(float) other);
        }
        else{
            answer = super.is_over_or_left((Base)other);
        }
        return answer;
    }


    /**
     * Returns whether "this" is strictly right "other". That is, "this"
     *         starts after "other" ends.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>right_span_span</li>
     *             <li>right_span_spanset</li>
     *             <li>right_floatspan_float</li>
     *
     * @param other object to compare with
     * @return True if right, False otherwise
     * @throws Exception
     */
    public boolean is_right(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.right_floatspanset_float(this._inner,(float) other);
        }
        else{
            answer = super.is_right((Base)other);
        }
        return answer;
    }



    /**
     *  Returns whether "this" is right "other" allowing overlap. That is,
     *         "this" starts after "other" starts (or at the same value).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overright_spanset_span</li>
     *             <li>overright_spanset_spanset</li>
     *             <li>overright_floatspanset_float</li>
     *
     * @param other object to compare with
     * @return True if overlapping or after, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.overright_floatspanset_float(this._inner,(float) other);
        }
        else{
            answer = super.is_over_or_right((Base)other);
        }
        return answer;
    }



    /* ------------------------- Distance Operations --------------------------- */

    /**
     * Returns the distance between "this" and "other".
     *
     *   <p>
     *         MEOS Functions:
     *             <li>distance_spanset_span</li>
     *             <li>distance_spanset_spanset</li>
     *             <li>distance_floatspanset_float</li>
     *
     * @param other object to compare with
     * @return A float value
     * @throws Exception
     */
    public float distance(Object other) throws Exception {
        float answer = 0;
        if (other instanceof Integer){
            answer = (float) functions.distance_floatspanset_float(this._inner,(float) other);
        }
        else{
            answer= super.distance((Base)other);
        }
        return answer;
    }


    /* ------------------------- Set Operations -------------------------------- */

    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intersection_floatspanset_float</li>
     *
     * @param other object to intersect with
     * @return An boolean value
     * @throws Exception
     */
    public boolean intersection(java.lang.Number other){
        Pointer result = null;
        boolean answer = false;
        if ((other instanceof Integer)||(other instanceof Float)){
            //answer = functions.intersection_floatspanset_float(this._inner,(double) other, result);
        }
        return answer;
    }

    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intersection_floatspanset_float</li>
     *             <li>intersection_spanset_spanset</li>
     *             <li>intersection_spanset_span</li>
     *
     * @param other object to intersect with
     * @return An int or a {@link FloatSpanSet} instance. The actual class depends on "other".
     * @throws Exception
     */
    public FloatSpanSet intersection(FloatSpanSet other) throws Exception {
        Pointer result = null;
        FloatSpanSet tmp = (FloatSpanSet) super.intersection(other);
        result = tmp.get_inner();
        return new FloatSpanSet(result);
    }


    /**
     * Returns the difference of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_spanset_span</li>
     *             <li>minus_spanset_spanset</li>
     *             <li>minus_floatspanset_float</li>
     *
     * @param other object to diff with
     * @return  A {@link FloatSpanSet} instance.
     * @throws Exception
     */
    public FloatSpanSet minus(Object other) throws Exception {
        Pointer result = null;
        if ((other instanceof Integer) || (other instanceof Float)){
            result = functions.minus_floatspanset_float(this._inner, (float) other);
        }
        else{
            FloatSpanSet tmp = (FloatSpanSet) super.minus((Base) other);
            result = tmp.get_inner();
        }
        return new FloatSpanSet(result);
    }


    /**
     * Returns the union of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>union_floatspanset_float</li>
     *             <li>union_spanset_spanset</li>
     *             <li>union_spanset_span</li>
     *
     * @param other object to merge with
     * @return A {@link FloatSpanSet} instance.
     */
    public FloatSpanSet union(Object other) throws Exception {
        Pointer result = null;
        if ((other instanceof Integer) || (other instanceof Float)){
            result = functions.union_floatspanset_float(this._inner, (float) other);
        }
        else{
            FloatSpanSet tmp = (FloatSpanSet) super.union((Base) other);
            result = tmp.get_inner();
        }
        return new FloatSpanSet(result);

    }







}
