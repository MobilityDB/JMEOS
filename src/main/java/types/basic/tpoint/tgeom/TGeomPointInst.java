package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointInst;
import types.temporal.TemporalType;

public class TGeomPointInst extends TPointInst implements TGeomPoint {
	private Pointer inner;
	private final String customType = "Geom";
	private final TemporalType temporalType = TemporalType.TEMPORAL_INSTANT;

	public TGeomPointInst(){}

	public TGeomPointInst(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	public TGeomPointInst(String value){
		super(value);
		this.inner = functions.tgeompointinst_in(value);
	}


	@Override
	public Pointer createStringInner(String str) {
		return functions.tgeompointinst_in(str);
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
