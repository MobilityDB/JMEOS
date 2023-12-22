package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.temporal.TInstant;
import types.basic.tpoint.helpers.TPointConstants;
import types.temporal.TemporalType;
import types.temporal.TemporalValue;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TGeogPointInst extends TInstant<Point> {


	public TGeogPointInst(Pointer inner){
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


	@Override
	protected TemporalValue<Point> buildTemporalValue(Point value, OffsetDateTime time) {
		if (value.getSrid() == TPointConstants.EMPTY_SRID) {
			value.setSrid(TPointConstants.DEFAULT_SRID);
		}
		
		return super.buildTemporalValue(value, time);
	}
}
