package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import functions.functions;
import types.basic.tpoint.TPointSeqSet;
import types.temporal.TemporalType;

public class TGeomPointSeqSet extends TPointSeqSet implements TGeomPoint{

	private Pointer inner;
	private final String customType = "Geom";
	private final TemporalType temporalType = TemporalType.TEMPORAL_SEQUENCE_SET;

	public TGeomPointSeqSet(){}

	public TGeomPointSeqSet(Pointer inner){
		super(inner);
		this.inner = inner;
	}

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
