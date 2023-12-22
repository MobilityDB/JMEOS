package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointSeq;
import types.basic.tpoint.helpers.TPointConstants;
import types.temporal.TemporalType;

import java.sql.SQLException;

public class TGeogPointSeq extends TPointSeq {

	public TGeogPointSeq(Pointer inner){
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
