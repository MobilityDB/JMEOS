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
    public Float element_n(int n) throws Exception {
        super.element_n(n);
        return functions.intset_value_n(this._inner,n);
    }

     */

    /*
    public List<Float> elements(){
    }

     */



    /** ------------------------- Transformations ------------------------------------ */




    /** ------------------------- Topological Operations -------------------------------- */


    /*
    public boolean contains(Object other) throws Exception {
        if ((other instanceof Integer) || (other instanceof Float)){
            return functions.contains_intset_int(this._inner, (float) other);
        }
        else {
            return super.contains((Base) other);
        }
    }

     */



    /** ------------------------- Position Operations -------------------------------- */



    /** ------------------------- Distance Operations --------------------------- */



    /** ------------------------- Set Operations -------------------------------- */











    /** ------------------------- Accessors ------------------------------------- */









}
