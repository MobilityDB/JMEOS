package types.collections.text;

import jnr.ffi.Pointer;
import types.collections.base.Base;
import types.collections.base.Set;
import types.collections.base.Collection;
import functions.functions;
import types.collections.base.SpanSet;
import types.collections.base.Span;

import java.util.List;

public class TextSet extends Set<String> {
    Pointer _inner;



    /** ------------------------- Constructors ---------------------------------- */

    public TextSet(String str){
        _inner = functions.textset_in(str);
    }

    public TextSet(Pointer inner){
        _inner = inner;
    }

    /** ------------------------- Output ---------------------------------------- */

    /**
     * Return the string representation of the content of ``self``.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>textset_out</li>
     *
     * @return A new {@link String} instance
     */
    public String toString(){
        return functions.textset_out(this._inner);
    }


    /** ------------------------- Conversions ----------------------------------- */
    @Override
    public Span to_spanset(){
        Span sp = new Span(functions.set_to_spanset(_inner));
        return sp;
    }

    @Override
    public Set to_span() {
        return null;
    }


    /** ------------------------- Accessors ------------------------------------- */

    @Override
    public String start_element() {
        return functions.textset_start_value(this._inner);
    }

    @Override
    public String end_element() {
        return null;
    }

    @Override
    public String element_n(int n) {
        return null;
    }

    @Override
    public List<String> element() {
        return null;
    }

}
