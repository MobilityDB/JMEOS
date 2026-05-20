package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointInst;
import types.temporal.TemporalType;


/**
 * Temporal geometric point instant class inherited from temporal point instant and implementing temporal geometric point.
 *
 * @author ARIJIT SAMAL
 */
public class TGeomPointInst extends TPointInst implements TGeomPoint {
	private Pointer inner;
	private final String customType = "Geom";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;

	public TGeomPointInst(){}

	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TGeomPointInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TGeomPointInst(String value){
		super(value);
		this.inner = functions.tgeompoint_in(value);
	}


	@Override
	public Pointer createStringInner(String str) {
		return functions.tgeompoint_in(str);
	}

	@Override
	public Pointer createInner(Pointer inner) {
		return inner;
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
