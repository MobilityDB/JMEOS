package types.collections.number;
import types.collections.base.Base;
import types.collections.base.SpanSet;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.time.Period;

import java.util.List;


/**
 * Class for representing lists of disjoint intspans.
 * <p>
 *     ``IntSpanSet`` objects can be created with a single argument of type string
 *     as in MobilityDB.
 * <p>
 *         >>> IntSpanSet(string='{[8, 10], [11, 1]}')
 * <p>
 *     Another possibility is to give a list specifying the composing
 *     spans, which can be instances  of ``str`` or ``IntSpan``. The composing
 *     spans must be given in increasing order.
 * <p>
 *         >>> IntSpanSet(span_list=['[8, 10]', '[11, 12]'])
 *         >>> IntSpanSet(span_list=[IntSpan('[8, 10]'), IntSpan('[11, 12]')])
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class IntSpanSet extends SpanSet<Integer> implements Number{
    private final Pointer _inner;


    /** ------------------------- Constructors ---------------------------------- */

    public IntSpanSet(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public IntSpanSet(String str){
        super(str);
        this._inner = functions.intspanset_in(str);
    }


    @Override
    public Pointer createStringInner(String str){
        return functions.intspanset_in(str);
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
     *             <li>intspanset_out</li>
     *
     *
     * @return A new {@link String} instance
     */
    public String toString(){
        return functions.intspanset_out(this._inner);
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
     * @return A new {@link IntSpan} instance
     */
    public IntSpan to_span(){
        return new IntSpan(functions.spanset_span(this._inner));
    }

    /**
     * Returns an intspanset that encompasses "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>intspanset_floatspanset</li>
     *
     * @return A new {@link FloatSpanSet} instance
     */

    public FloatSpanSet to_floatspanset(){
        return new FloatSpanSet(functions.intspanset_floatspanset(this._inner));
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
        return (float) functions.spanset_width(this._inner,ignore_gaps);
    }

    /*
      Returns the first span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_start_span</li>

      @return A {@link IntSpan} instance
     */
    /*
    public IntSpan start_span(){
        return new IntSpan(super.start_span().get_inner());
    }


     */

    /*
      Returns the last span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_end_span</li>

      @return A {@link IntSpan} instance
     */
    /*
    public IntSpan end_span(){
        return new IntSpan(super.end_span().get_inner());
    }


     */

    /*
      Returns the n-th span in "this".
      <p>
              MEOS Functions:
                  <li>spanset_span_n</li>

      @return A {@link IntSpan} instance
     */
    /*
    public IntSpan span_n(int n){
        return new IntSpan(super.span_n(n).get_inner());
    }

     */



    /* ------------------------- Transformations ------------------------------- */


    /**
     * Return a new "IntSpanSet" with the lower and upper bounds shifted by
     *         "delta".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     *
     * @param delta The value to shift by
     * @return A new {@link IntSpanSet} instance
     */

    public IntSpanSet shift(int delta){
        return this.shift_scale(delta,0);
    }



    /**
     * Return a new "IntSpanSet" with the lower and upper bounds scaled so
     *         that the width is "width".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     * @param width The new width
     * @return a new {@link IntSpanSet} instance
     */

    public IntSpanSet scale(int width){
        return this.shift_scale(0,width);
    }



    /**
     * Return a new "IntSpanSet" with the lower and upper bounds shifted by
     *         "delta" and scaled so that the width is "width".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>floatspanset_shift_scale</li>
     *
     * @param delta The value to shift by
     * @param width v
     * @return a new {@link IntSpanSet} instance
     */

    public IntSpanSet shift_scale(int delta, int width){
        return new IntSpanSet(functions.floatspanset_shift_scale(this._inner,delta,width,delta != 0,width!=0));
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
     *             <li>adjacent_intspanset_int</li>
     *
     * @param other object to compare with
     * @return True if adjacent, False otherwise
     * @throws Exception
     */
    public boolean is_adjacent(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.adjacent_intspanset_int(this._inner, (int) other);
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
     *             <li>contains_intspanset_float</li>
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.contains_intspanset_int(this._inner, (int) other);
        }
        else{
            answer = super.contains((Base)other);
        }
        return answer;
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
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.spanset_eq(this._inner,functions.int_to_intspanset((int) other));
        }
        else{
            answer = super.is_same((Base)other);
        }
        return answer;
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
     *             <li>left_intspanset_int</li>
     *
     * @param other object to compare with
     * @return True if left, False otherwise
     * @throws Exception
     */
    public boolean is_left(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.left_intspanset_int(this._inner,(int) other);
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
     *             <li>overleft_intspanset_int</li>
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.overleft_intspanset_int(this._inner,(int) other);
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
     *             <li>right_intspan_int</li>
     *
     * @param other object to compare with
     * @return True if right, False otherwise
     * @throws Exception
     */
    public boolean is_right(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.right_intspanset_int(this._inner,(int) other);
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
     *             <li>overright_intspanset_int</li>
     *
     * @param other object to compare with
     * @return True if overlapping or after, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Object other) throws Exception {
        boolean answer = false;
        if (other instanceof Integer){
            answer = functions.overright_intspanset_int(this._inner,(int) other);
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
     *             <li>distance_intspanset_int</li>
     *
     * @param other object to compare with
     * @return A float value
     * @throws Exception
     */
    public float distance(Object other) throws Exception {
        float answer = 0;
        if (other instanceof Integer){
            answer = (float) functions.distance_intspanset_int(this._inner,(int) other);
        }
        else{
            answer = super.distance((Base)other);
        }
        return answer;
    }







    /** ------------------------- Set Operations -------------------------------- */


    public boolean intersection(int other){
        Pointer result = null;
        return false;
        //return functions.intersection_intspanset_int(this._inner,other,result);
    }



    /**
     * Returns the difference of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_spanset_span</li>
     *             <li>minus_spanset_spanset</li>
     *             <li>minus_intspanset_int</li>
     *
     * @param other object to diff with
     * @return  A {@link IntSpanSet} instance.
     * @throws Exception
     */
    public IntSpanSet minus(Object other) throws Exception {
        Pointer result = null;
        if ((other instanceof Integer) || (other instanceof Float)){
            result = functions.minus_intspanset_int(this._inner, (int) other);
        }
        else{
            IntSpanSet tmp = (IntSpanSet) super.minus((Base) other);
            result = tmp.get_inner();
        }
        return new IntSpanSet(result);
    }


    /**
     * Returns the union of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>union_intspanset_int</li>
     *             <li>union_spanset_spanset</li>
     *             <li>union_spanset_span</li>
     *
     * @param other object to merge with
     * @return A {@link IntSpanSet} instance.
     */

    public IntSpanSet union(Object other) throws Exception {
        Pointer result = null;
        if ((other instanceof Integer) || (other instanceof Float)){
            result = functions.union_intspanset_int(this._inner, (int) other);
        }
        else{
            IntSpanSet tmp = (IntSpanSet) super.minus((Base) other);
            result = tmp.get_inner();
        }
        return new IntSpanSet(result);

    }
























}
