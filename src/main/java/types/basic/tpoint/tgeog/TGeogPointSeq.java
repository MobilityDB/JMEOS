package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeq;
import types.temporal.TemporalType;

public class TGeogPointSeq extends TPointSeq implements TGeogPoint {

	private Pointer inner;
	private String customType = "Geog";
	private TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;


	public TGeogPointSeq(){}

	public TGeogPointSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	public TGeogPointSeq(String value){
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
