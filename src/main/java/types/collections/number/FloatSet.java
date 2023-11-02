package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.Base;
import types.collections.base.Set;

import java.util.List;


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
 */
public class FloatSet extends Set<Float> implements Number{
    private Pointer _inner;

    public FloatSet(Pointer inner){
        this._inner = inner;
    }

    public FloatSet(String str){
        this._inner = functions.floatset_in(str);
    }


    /** ------------------------- Constructors ---------------------------------- */


    /** ------------------------- Output ---------------------------------------- */


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


    /** ------------------------- Conversions ----------------------------------- */


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
        return new FloatSpanSet(super.to_spanset().get_inner());
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
        return new FloatSpan(super.to_span().get_inner());
    }

    /*
    public IntSet to_intset(){}
     */

    /** ------------------------- Accessors ------------------------------------- */


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
    /*
    public Float element_n(int n) throws Exception {
        super.element_n(n);
        return functions.floatset_value_n(this._inner,n);
    }

     */

    /*
    public List<Float> elements(){
    }

     */

    /** ------------------------- Transformations ------------------------------------ */

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
    /*
    public FloatSet shift(float delta){
        return shift_scale(delta,0);
    }

     */


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
    /*
    public FloatSet scale(float new_width){
        return shift_scale(0, new_width);
    }

     */


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
    /*
    public FloatSet shift_scale(float delta, float new_width){
        return new FloatSet(functions.floatset_shift_scale);
    }

     */


    /** ------------------------- Topological Operations -------------------------------- */

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
    /*
    public boolean contains(Object other) throws Exception {
        if (other instanceof Float){
            return functions.contains_floatset_float(this._inner, other);
        }
        else{
            return super.contains((Base)other);
        }
    }

     */























}
