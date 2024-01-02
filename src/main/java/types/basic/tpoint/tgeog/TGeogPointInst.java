package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointInst;
import functions.functions;
import types.temporal.TemporalType;

public class TGeogPointInst extends TPointInst implements TGeogPoint {
	private Pointer inner;
	private String customType = "Geog";
	private TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;


	public TGeogPointInst(){}

	public TGeogPointInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}

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
