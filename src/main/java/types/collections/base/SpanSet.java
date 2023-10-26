package types.collections.base;

import jnr.ffi.Pointer;

public abstract class SpanSet<T extends Object> extends Collection implements Base {
    Pointer _inner = null;

    public SpanSet(Pointer inner){
        _inner = inner;
    }
}
