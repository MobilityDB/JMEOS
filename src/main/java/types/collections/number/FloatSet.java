package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.Base;
import types.collections.base.Set;


/**
 * Class for representing a set of text values.
 *  <p>
 *     "TextSet" objects can be created with a single argument of type string as
 *     in MobilityDB.
 *<p>
 *         >>> FloatSet(string='{1, 3, 56}')
 *<p>
 *     Another possibility is to create a "TextSet" object from a list of
 *     strings or floats.
 *<p>
 *         >>> FloatSet(elements=[1, '2', 3, '56'])
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class FloatSet extends Set<Float> implements Number{
    private final Pointer _inner;

    public FloatSet(Pointer inner){
        super(inner);
        _inner = inner;
    }

    public FloatSet(String str){
        super(str);
        _inner = functions.floatset_in(str);
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.floatset_in(str);
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
     *             <li>floatset_out</li>
     *
     *
     * @param max_decimals number of maximum decimals
     * @return A new {@link String} instance
     */
    public String toString(int max_decimals){
        return functions.floatset_out(this._inner, max_decimals);
    }


    /* ------------------------- Conversions ----------------------------------- */


    /**
     * Returns a SpanSet that contains a Span for each element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>set_to_spanset</li>
     *
     * @return A new {@link FloatSpanSet} instance
     */
    public FloatSpanSet to_spanset(){
        return new FloatSpanSet(functions.set_to_spanset(this._inner));
    }



    /**
     * Returns a span that encompasses "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>set_span</li>
     *
     * @return A new {@link FloatSpan} instance
     */
    public FloatSpan to_span(){
        return new FloatSpan(functions.set_span(this._inner));
    }


    /*
    public IntSet to_intset(){}
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
     *             <li>floatset_start_value</li>
     *
     * @return A {@link Float} instance
     */
    public Float start_element(){
        return (float) functions.floatset_start_value(this._inner);
    }


    /**
     * Returns the last element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>floatset_end_value</li>
     *
     * @return A {@link Float} instance
     */
    public Float end_element(){
        return (float) functions.floatset_end_value(this._inner);
    }

    /**
     * Returns the "n"-th element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>floatset_value_n</li>
     *
     * @param n The 0-based index of the element to return.
     * @return A {@link Float} instance
     * @throws Exception
     */
    public Float element_n(int n) throws Exception {
        super.element_n(n);
        return 2.0f;
    }



    /*
    public List<Float> elements(){
    }

     */

    /* ------------------------- Transformations ------------------------------------ */

    /**
     * Returns a new "FloatSet" instance with all elements shifted by "delta".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>floatset_shift_scale</li>
     *
     * @param delta The value to shift by.
     * @return A new {@link FloatSet} instance
     */

    public FloatSet shift(float delta){
        return shift_scale(delta,0);
    }




    /**
     * Returns a new "FloatSet" instance with all elements scaled to so that the encompassing
     *         span has width "new_width".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>floatset_shift_scale</li>
     *
     * @param new_width The new width.
     * @return A new {@link FloatSet} instance
     */

    public FloatSet scale(float new_width){
        return shift_scale(0, new_width);
    }




    /**
     * Returns a new "FloatSet" instance with all elements shifted by "delta" and scaled to so that the
     *          encompassing span has width  "new_width".
     *
     * <p>
     *         MEOS Functions:
     *             floatset_shift_scale
     *
     * @param delta The value to shift by.
     * @param new_width The new width.
     * @return A new {@link FloatSet} instance
     */

    public FloatSet shift_scale(float delta, float new_width){
        return new FloatSet(functions.floatset_shift_scale(this._inner,delta,new_width,delta != 0, new_width != 0));
    }




    /* ------------------------- Topological Operations -------------------------------- */

    /**
     * Returns whether "this" contains "content".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>contains_set_set</li>
     *             <li>contains_floatset_float</li>
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     */

    public boolean contains(Object other) throws Exception {
        if (other instanceof Float){
            return functions.contains_floatset_float(this._inner, (float) other);
        }
        else{
            return super.contains((Base)other);
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
     *             <li>left_floatset_float</li>
     *
     * @param other object to compare with
     * @return True if left, False otherwise
     * @throws Exception
     */
    public boolean is_left(Object other) throws Exception {
        if (other instanceof Float){
            return functions.left_floatset_float(this._inner, (float) other);
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
     *             <li>overleft_floatset_float</li>
     *
     * @param other object to compare with
     * @return True if is over or left, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Object other) throws Exception {
        if (other instanceof Float){
            return functions.overleft_floatset_float(this._inner, (float) other);
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
     *             <li>right_floatset_float</li>
     *
     * @param other object to compare with
     * @return True if right, False otherwise
     * @throws Exception
     */
    public boolean is_right(Object other) throws Exception {
        if (other instanceof Float){
            return functions.right_floatset_float(this._inner, (float) other);
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
     *             <li>overright_floatset_float</li>
     *
     * @param other object to compare with
     * @return True if is over or right, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Object other) throws Exception {
        if (other instanceof Float){
            return functions.overright_floatset_float(this._inner, (float) other);
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
     *             <li>intersection_floatset_float</li>
     *
     * @param other A {@link FloatSet} or {@link Float} instance
     * @return An object of the same type as "other" or "None" if the
     *      *             intersection is empty.
     * @throws Exception
     */
    public boolean intersection(Float other) throws Exception{
        Pointer result = null;
        return false;
        //return functions.intersection_floatset_float(this._inner, (float) other, result);
    }


    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intersection_set_set</li>
     *             <li>intersection_floatset_float</li>
     *
     * @param other A {@link FloatSet} or {@link Float} instance
     * @return An object of the same type as "other" or "None" if the
     *      *             intersection is empty.
     * @throws Exception
     */
    public FloatSet intersection(FloatSet other) throws Exception {
        Pointer result = null;
        if(other instanceof FloatSet){
            result = functions.intersection_set_set(this._inner, other._inner);
        }
        return new FloatSet(result);
    }



    /**
     * Returns the difference of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_set_set</li>
     *             <li>minus_floatset_float</li>
     *
     * @param other A {@link FloatSet} or {@link Float}instance
     * @return A {@link FloatSet} instance or "None" if the difference is empty.
     * @throws Exception
     */
    public FloatSet minus(Object other) throws Exception {
        Pointer result = null;
        if (other instanceof Float){
            result = functions.minus_floatset_float(this._inner, (float) other);
        }
        else if(other instanceof FloatSet){
            result = functions.minus_set_set(this._inner, ((FloatSet) other)._inner);
        }

        return new FloatSet(result);
    }



    /**
     * Returns the difference of "other" and "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>minus_float_floatset</li>
     *
     *
     * @param other A {@link Float} instance
     * @return A {@link Float} instance or "None" if the difference is empty.
     */
    public boolean subtract_from(float other){
        Pointer result = null;
        return false;
        //return functions.minus_float_floatset(other,this._inner,result);

    }


    /**
     * Returns the union of "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>union_set_set</li>
     *             <li>union_floatset_float</li>
     *
     * @param other A {@link FloatSet} or {@link Float} instance
     * @return A {@link FloatSet} instance.
     * @throws Exception
     */
    public FloatSet union(Object other) throws Exception {
        Pointer result = null;
        if (other instanceof Float){
            result = functions.union_floatset_float(this._inner, (float) other);
        }
        else if(other instanceof FloatSet){
            result = functions.union_set_set(this._inner, ((FloatSet) other)._inner);
        }

        return new FloatSet(result);
    }



    /* ------------------------- Distance Operations --------------------------- */


    /**
     * /**
     * Returns the distance between "this" and "other".
     * <p>
     *
     *      MEOS functions:
     *          <li>distance_floatset_float</li>
     *
     *
     * @param other object to compare with
     * @return A float value
     * @throws Exception
     */
    public float distance(Object other) throws Exception {
        if (other instanceof Float){
            return (float) functions.distance_floatset_float(this._inner, (float) other);
        }
        else {
            return super.distance((Base) other);
        }
    }














}
