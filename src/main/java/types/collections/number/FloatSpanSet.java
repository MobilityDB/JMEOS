package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.SpanSet;


public class FloatSpanSet extends SpanSet<Float> implements Number{

    private Pointer _inner;



    public FloatSpanSet(Pointer inner){
        this._inner = inner;
    }



}
