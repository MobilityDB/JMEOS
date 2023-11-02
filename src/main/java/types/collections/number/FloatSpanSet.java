package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.SpanSet;

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
 */
public class FloatSpanSet extends SpanSet<Float> implements Number{

    private Pointer _inner;



    public FloatSpanSet(Pointer inner){
        this._inner = inner;
    }

    public FloatSpanSet(String str){
        this._inner = functions.floatspanset_in(str);
    }



    /** ------------------------- Constructors ---------------------------------- */


    /** ------------------------- Output ---------------------------------------- */


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


    /** ------------------------- Conversions ----------------------------------- */


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
        return new FloatSpan(super.to_span().get_inner());
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
    /*
    public IntSpanSet to_intspanset(){
        return new IntSpanSet(functions.floatspanset_intspanset(this._inner));
    }
     */


    /** ------------------------- Accessors ------------------------------------- */

    









}
