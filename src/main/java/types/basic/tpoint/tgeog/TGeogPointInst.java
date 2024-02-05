package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointInst;
import functions.functions;
import types.temporal.TemporalType;



/**
 * Temporal geographic point instant class inherited from temporal point instant and implementing temporal geographic point.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TGeogPointInst extends TPointInst implements TGeogPoint {
	private Pointer inner;
	private final String customType = "Geog";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;


	public TGeogPointInst(){}

	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TGeogPointInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TGeogPointInst(String value){
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
