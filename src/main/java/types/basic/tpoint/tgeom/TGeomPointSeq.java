package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeq;
import types.temporal.TemporalType;

public class TGeomPointSeq extends TPointSeq  implements TGeomPoint{

	private Pointer inner;
	private String customType = "Geom";
	private TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE;

	public TGeomPointSeq(){}

	public TGeomPointSeq(Pointer inner){
		super(inner);
		this.inner = inner;
	}

	public TGeomPointSeq(String value){
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
