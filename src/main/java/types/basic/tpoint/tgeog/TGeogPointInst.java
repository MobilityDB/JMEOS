package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.temporal.TInstant;
import types.basic.tpoint.helpers.TPointConstants;
import types.temporal.TemporalValue;
import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TGeogPointInst extends TInstant<Point> {


	public TGeogPointInst(Pointer inner){
		super(inner);
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeogPointInst value
	 * @throws SQLException
	 */
	public TGeogPointInst(String value) throws SQLException {
		super(value, TGeogPoint::getSingleTemporalValue);
	}
	
	/**
	 * The value and timestamp constructor
	 *
	 * @param value - a Point
	 * @param time  - a timestamp
	 * @throws SQLException
	 */
	public TGeogPointInst(Point value, OffsetDateTime time) throws SQLException {
		super(value, time);
	}
	
	@Override
	protected TemporalValue<Point> buildTemporalValue(Point value, OffsetDateTime time) {
		if (value.getSrid() == TPointConstants.EMPTY_SRID) {
			value.setSrid(TPointConstants.DEFAULT_SRID);
		}
		
		return super.buildTemporalValue(value, time);
	}
}
