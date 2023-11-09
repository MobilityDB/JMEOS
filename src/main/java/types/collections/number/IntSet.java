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
 */
public class IntSet extends Set<Integer> implements Number{
    private Pointer _inner;

    public IntSet(Pointer inner){
        this._inner = inner;
    }

    public IntSet(String str){
        this._inner = functions.intset_in(str);
    }

    /** ------------------------- Constructors ---------------------------------- */


    /** ------------------------- Output ---------------------------------------- */

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

    /** ------------------------- Conversions ----------------------------------- */


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
        return new IntSpanSet(super.to_spanset().get_inner());
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
        return new IntSpan(super.to_span().get_inner());
    }

    /*
    public FloatSet to_floatset(){
        return new FloatSet();
    }
     */


    /** ------------------------- Accessors ------------------------------------- */



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

    /**
     * Returns the "n"-th element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>intset_value_n</li>
     *
     * @param n The 0-based index of the element to return.
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



    /** ------------------------- Transformations ------------------------------------ */

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





    /** ------------------------- Position Operations -------------------------------- */



    /** ------------------------- Distance Operations --------------------------- */

    public float distance(Object other) throws Exception {
        if (other instanceof Integer){
            return (float) functions.distance_intset_int(this._inner, (int) other);
        }
        else {
            return super.distance((Base) other);
        }
    }

    /** ------------------------- Set Operations -------------------------------- */











    /** ------------------------- Accessors ------------------------------------- */









}
