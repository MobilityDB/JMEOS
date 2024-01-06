package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.temporal.TSequenceSet;


/**
 * Base abstract class for TGeomPointSeqSet and TGeogPointSeqSet
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class TPointSeqSet extends TSequenceSet<TPoint> implements TPoint{
    public Pointer inner;

    public TPointSeqSet(){
        super();

    }

    /**
     * The Pointer constructor
     * @param inner Pointer
     */
    public TPointSeqSet(Pointer inner){
        super(inner);
        this.inner = createInner(inner);
    }

    /**
     * The string constructor
     *
     * @param value - the string with the TIntInst value
     */
    public TPointSeqSet(String value){
        super(value);
        this.inner = createStringInner(value);
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
}
