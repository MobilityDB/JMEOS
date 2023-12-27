package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.temporal.TSequence;

/**
 * Base abstract class for TGeomPointSeq and TGeogPointSeq
 * Contains logic for handling SRID
 */
public abstract class TPointSeq extends TSequence<TPoint> implements TPoint {

    public Pointer inner;

    public TPointSeq(){
        super();

    }
    public TPointSeq(Pointer inner){
        super(inner);
        this.inner = createInner(inner);
    }

    public TPointSeq(String value){
        super(value);
        this.inner = createStringInner(value);
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
}
