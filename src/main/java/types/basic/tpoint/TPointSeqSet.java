package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.temporal.TSequenceSet;


/**
 * Base abstract class for TGeomPointSeqSet and TGeogPointSeqSet
 * Contains logic for handling SRID
 */
public abstract class TPointSeqSet extends TSequenceSet<TPoint> implements TPoint{
    public Pointer inner;

    public TPointSeqSet(){
        super();

    }
    public TPointSeqSet(Pointer inner){
        super(inner);
        this.inner = createInner(inner);
    }

    public TPointSeqSet(String value){
        super(value);
        this.inner = createStringInner(value);
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
}
