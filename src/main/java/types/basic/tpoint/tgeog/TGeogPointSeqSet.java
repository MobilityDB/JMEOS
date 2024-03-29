package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeqSet;
import types.temporal.TemporalType;


/**
 * Temporal geographic point sequence set class inherited from temporal point sequence set and implementing temporal geographic point.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TGeogPointSeqSet extends TPointSeqSet implements TGeogPoint {

	private Pointer inner;
	private final String customType = "Geog";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;


	public TGeogPointSeqSet(){}


	/**
	 * The Pointer constructor
	 * @param inner Pointer
	 */
	public TGeogPointSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 */
	public TGeogPointSeqSet(String value){
		super(value);
		this.inner = functions.tgeogpoint_in(value);
	}

	@Override
	public Pointer createStringInner(String str) {
		return functions.tgeogpoint_in(str);
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
