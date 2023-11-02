package types.collections.number;
import types.collections.base.SpanSet;
import functions.functions;
import jnr.ffi.Pointer;

public class IntSpanSet extends SpanSet<Integer> implements Number{
    private Pointer _inner;

    public IntSpanSet(Pointer inner){
        this._inner = inner;
    }

    public IntSpanSet(String str){
        this._inner = functions.intspanset_in(str);
    }

}
