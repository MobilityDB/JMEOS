package types.collections.number;
import types.collections.base.Base;
import types.collections.base.Set;
import functions.functions;
import jnr.ffi.Pointer;

/**
 * Class for representing a set of text values.
 * <p>
 *     ``TextSet`` objects can be created with a single argument of type string as
 *     in MobilityDB.
 * <p>
 *         >>> IntSet(string='{1, 3, 56}')
 * <p>
 *     Another possibility is to create a ``TextSet`` object from a list of
 *     strings or integers.
 * <p>
 *         >>> IntSet(elements=[1, '2', 3, '56'])
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class IntSet extends Set<Integer> implements Number{
    private final Pointer _inner;

    public IntSet(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public IntSet(String str){
        super(str);
        _inner = functions.intset_in(str);
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.intset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return inner;
    }

    /* ------------------------- Constructors ---------------------------------- */


    /* ------------------------- Output ---------------------------------------- */

    /**
     * Return the string representation of the content of "this".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>intset_out</li>
     *
     *
     * @return A new {@link String} instance
     */
    public String toString(){
        return functions.intset_out(this._inner);
    }

    /* ------------------------- Conversions ----------------------------------- */


    /**
     * Returns a SpanSet that contains a Span for each element in "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>set_to_spanset</li>
     *
     * @return A new {@link IntSpanSet} instance
     */
    public IntSpanSet to_spanset(){
        return new IntSpanSet(functions.set_to_spanset(this._inner));
    }


    /**
     * Returns a span that encompasses "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>set_span</li>
     *
     * @return A {@link IntSpan} instance
     */
    public IntSpan to_span(){
        return new IntSpan(functions.set_span(this._inner));
    }



    /*
    public FloatSet to_floatset(){
        return new FloatSet();
    }
     */


    /** ------------------------- Accessors ------------------------------------- */

    @Override
    public Pointer get_inner(){
        return this._inner;
    }

    /**
     * Returns the first element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_start_value</li>
     *
     * @return A {@link Integer} instance
     */
    public Integer start_element(){
        return functions.intset_start_value(this._inner);
    }


    /**
     * Returns the last element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_end_value</li>
     *
     * @return A {@link Integer} instance
     */
    public Integer end_element(){
        return functions.intset_end_value(this._inner);
    }

    /*
      Returns the "n"-th element in "this".

       <p>

              MEOS Functions:
                  <li>intset_value_n</li>

      @param n The 0-based index of the element to return.
     * @return A {@link Integer} instance
     * @throws Exception
     */
    /*
    public Integer element_n(int n) throws Exception {
        super.element_n(n);
        return functions.intset_value_n(this._inner,n);
    }
     */



    /*
    public List<Float> elements(){
    }

     */



    /* ------------------------- Transformations ------------------------------------ */

    /**
     * Returns a new "IntSet" instance with all elements shifted by "delta".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_shift_scale</li>
     *
     * @param delta The value to shift by.
     * @return A new {@link IntSet} instance
     */
    public IntSet shift(int delta){
        return this.shift_scale(delta,0);
    }

    /**
     * Returns a new "IntSet" instance with all elements scaled to so that the encompassing
     *         span has width "width".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_shift_scale</li>
     *
     * @param width The new width.
     * @return A new {@link IntSet} instance
     */
    public IntSet scale(int width){
        return this.shift_scale(0,width);
    }


    /**
     * Returns a new "IntSet" instance with all elements shifted by
     *         "delta" and scaled to so that the encompassing span has width
     *         "width".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_shift_scale</li>
     *
     * @param delta The value to shift by.
     * @param width The new width.
     * @return A new {@link IntSet} instance
     */
    public IntSet shift_scale(int delta, int width){
        return new IntSet(functions.intset_shift_scale(this._inner, delta,width,delta != 0, width != 0));
    }



    /** ------------------------- Topological Operations -------------------------------- */



    public boolean contains(Object other) throws Exception {
        if ((other instanceof Integer) || (other instanceof Float)){
            return functions.contains_intset_int(this._inner, (int) other);
        }
        else {
            return super.contains((Base) other);
        }
    }








    /* ------------------------- Position Operations -------------------------------- */


    /**
     * Returns whether "this" is strictly to the left of "other". That is,
     *         "this" ends before "other" starts.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>left_set_set</li>
     *             <li>left_intset_int</li>
     *
     * @param other object to compare with
     * @return True if left, False otherwise
     * @throws Exception
     */
    public boolean is_left(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.left_intset_int(this._inner, (int) other);
        }
        else{
            return super.is_left((Base) other);
        }
    }

    /**
     * Returns whether "this" is to the left of "other" allowing overlap.
     *         That is, "this" ends before "other" ends (or at the same value).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overleft_set_set</li>
     *             <li>overleft_intset_int</li>
     *
     * @param other object to compare with
     * @return True if is over or left, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.overleft_intset_int(this._inner, (int) other);
        }
        else{
            return super.is_over_or_left((Base) other);
        }
    }


    /**
     * Returns whether "this" is strictly to the right of "other". That is,
     *         "this" ends after "other" starts.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>right_set_set</li>
     *             <li>right_intset_int</li>
     *
     * @param other object to compare with
     * @return True if right, False otherwise
     * @throws Exception
     */
    public boolean is_right(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.right_intset_int(this._inner, (int) other);
        }
        else{
            return super.is_right((Base) other);
        }
    }


    /**
     * Returns whether "this" is to the right of "other" allowing overlap.
     *         That is, "this" starts before "other" ends (or at the same value).
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>overright_set_set</li>
     *             <li>overright_intset_int</li>
     *
     * @param other object to compare with
     * @return True if is over or right, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Object other) throws Exception {
        if (other instanceof Integer){
            return functions.overright_intset_int(this._inner, (int) other);
        }
        else{
            return super.is_over_or_right((Base) other);
        }
    }



    /* ------------------------- Set Operations -------------------------------- */

    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intersection_set_set</li>
     *             <li>intersection_intset_int</li>
     *
     * @param other A {@link IntSet} or {@link Integer} instance
     * @return An object of the same type as "other" or "None" if the
     *      *             intersection is empty.
     * @throws Exception
     */
    public boolean intersection(Integer other) throws Exception{
        Pointer result = null;
        return false;
        //return functions.intersection_intset_int(this._inner, (int) other, result);
    }


    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intersection_set_set</li>
     *             <li>intersection_intset_int</li>
     *
     * @param other A {@link IntSet} or {@link Integer} instance
     * @return An object of the same type as "other" or "None" if the
     *      *             intersection is empty.
     * @throws Exception
     */
    public IntSet intersection(IntSet other) throws Exception {
        Pointer result = null;
        if(other instanceof IntSet){
            result = functions.intersection_set_set(this._inner, other._inner);
        }

        return new IntSet(result);
    }


    /**
     * Returns the difference of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_set_set</li>
     *             <li>minus_intset_int</li>
     *
     * @param other A {@link IntSet} or {@link Integer} instance
     * @return A {@link IntSet} instance or "None" if the difference is empty.
     * @throws Exception
     */
    public IntSet minus(Object other) throws Exception {
        Pointer result = null;
        if (other instanceof Integer){
            result = functions.minus_intset_int(this._inner, (int) other);
        }
        else if(other instanceof IntSet){
            result = functions.minus_set_set(this._inner, ((IntSet) other)._inner);
        }

        return new IntSet(result);
    }


    /**
     * Returns the difference of "other" and "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_int_intset</li>
     *
     *
     * @param other A {@link Integer} instance
     * @return A {@link Integer} instance or "None" if the difference is empty.
     */
    public boolean subtract_from(int other){
        Pointer result = null;
        return false;
        //return functions.minus_int_intset(other,this._inner,result);

    }


    /**
     * Returns the union of "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>union_set_set</li>
     *             <li>union_intset_int</li>
     *
     * @param other A {@link IntSet} or {@link Integer} instance
     * @return A {@link IntSet} instance.
     * @throws Exception
     */
    public IntSet union(Object other) throws Exception {
        Pointer result = null;
        if (other instanceof Integer){
            result = functions.union_intset_int(this._inner, (int) other);
        }
        else if(other instanceof IntSet){
            result = functions.union_set_set(this._inner, ((IntSet) other)._inner);
        }

        return new IntSet(result);
    }





    /* ------------------------- Distance Operations --------------------------- */


    /**
     * /**
     * Returns the distance between "this" and "other".
     * <p>
     *
     *      MEOS functions:
     *          <li>distance_intset_int</li>
     *
     *
     * @param other object to compare with
     * @return A float value
     * @throws Exception
     */
    public float distance(Object other) throws Exception {
        if (other instanceof Integer){
            return (float) functions.distance_intset_int(this._inner, (int) other);
        }
        else {
            return super.distance((Base) other);
        }
    }




}
