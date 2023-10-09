package types.collections.base;

import jnr.ffi.Pointer;
import functions.functions;


public abstract class Set<T extends Object> extends Collection {
    Pointer _inner = null;


    /** ------------------------- Constructors ---------------------------------- */

    public Set(Pointer inner){
        this._inner = inner;
    }

    /** ------------------------- Output ---------------------------------------- */


    /** ------------------------- Conversions ----------------------------------- */

    /**
     * Returns a SpanSet that contains a Span for each element in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_to_spanset</li>
     * @return A new {@link SpanSet} instance
     */
    public abstract SpanSet to_spanset();

    /**
     * Returns a span that encompasses "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_span</li>
     * @return A new :class:`Span` instance
     */
    public abstract SpanSet to_span();

    /** ------------------------- Accessors ------------------------------------- */


    /**
     * Returns the number of elements in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_num_values</li>
     *
     * @return An {@link Integer}
     */
    public int num_elements(){
        return functions.set_num_values(this._inner);
    }

    /**
     * Returns the number of elements in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_num_values</li>
     *
     * @return An {@link Integer}
     */
    public int length(){
        return this.num_elements();
    }


    /**
     * Returns the first element in "this".
     *
     * @return A {@link T} instance
     */
    public abstract T start_element();


    /**
     * Returns the last element in "this".
     *
     * @return A {@link T} instance
     */
    public abstract T end_element();



}
