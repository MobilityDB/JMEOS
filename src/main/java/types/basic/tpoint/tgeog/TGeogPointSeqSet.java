package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeqSet;
import types.temporal.TemporalType;

public class TGeogPointSeqSet extends TPointSeqSet implements TGeogPoint {

	private Pointer inner;
	private final String customType = "Geog";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;


	public TGeogPointSeqSet(){}

	public TGeogPointSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

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
