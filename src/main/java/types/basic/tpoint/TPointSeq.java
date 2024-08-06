package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.temporal.TSequence;

/**
 * Base abstract class for TGeomPointSeq and TGeogPointSeq
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class TPointSeq extends TSequence<TPoint> implements TPoint {

    public Pointer inner;

    public TPointSeq(){
        super();

    }

    /**
     * The Pointer constructor
     * @param inner Pointer
     */
    public TPointSeq(Pointer inner){
        super(inner);
        this.inner = createInner(inner);
    }

    /**
     * The string constructor
     *
     * @param value - the string with the TIntInst value
     */
    public TPointSeq(String value){
        super(value);
        this.inner = createStringInner(value);
    }

    public abstract Pointer createStringInner(String str);
    public abstract Pointer createInner(Pointer inner);
}
