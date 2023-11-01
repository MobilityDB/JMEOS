package types.collections.number;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.Span;


public class FloatSpan extends Span<Float> implements Number{
    private Pointer _inner;

    public FloatSpan(Pointer inner){
        this._inner = inner;
    }
}
