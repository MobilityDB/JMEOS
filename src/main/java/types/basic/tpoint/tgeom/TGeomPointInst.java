package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import types.temporal.TInstant;
import net.postgis.jdbc.geometry.Point;
import types.temporal.TemporalType;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TGeomPointInst extends TInstant<Point> {


	public TGeomPointInst(Pointer inner){
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
