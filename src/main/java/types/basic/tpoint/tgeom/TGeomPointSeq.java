package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointSeq;
import types.basic.tpoint.helpers.TPointConstants;
import types.temporal.TemporalType;

import java.sql.SQLException;

public class TGeomPointSeq extends TPointSeq {

	public TGeomPointSeq(Pointer inner){
		super(inner);
	}

	@Override
	public Pointer createStringInner(String str) {
		return null;
	}

	@Override
	public Pointer createInner(Pointer inner) {
		return null;
	}

	@Override
	public String getCustomType() {
		return null;
	}

	@Override
	public TemporalType getTemporalType() {
		return null;
	}


}
