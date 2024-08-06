package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeqSet;
import types.temporal.TemporalType;


/**
 * Temporal geometric point sequence set class inherited from temporal point sequence set and implementing temporal geometric point.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TGeomPointSeqSet extends TPointSeqSet implements TGeomPoint{

	private Pointer inner;
	private final String customType = "Geom";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;

	public TGeomPointSeqSet(){}

	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TGeomPointSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TGeomPointSeqSet(String value){
		super(value);
		this.inner = functions.tgeompoint_in(value);
	}


	@Override
	public Pointer createStringInner(String str) {
		return functions.tgeompoint_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner) {
		return null;
	}

	@Override
	public String getCustomType() {
		return this.customType;
	}

	@Override
	public TemporalType getTemporalType() {
		return this.temporalType;
	}

	@Override
	public Pointer getPointInner(){
		return this.inner;
	}


}
