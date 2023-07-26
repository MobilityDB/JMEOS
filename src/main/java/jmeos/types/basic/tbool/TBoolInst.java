package jmeos.types.basic.tbool;

import jmeos.types.temporal.TInstant;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TBoolInst extends TInstant<Boolean> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInst value
	 * @throws SQLException
	 */
	public TBoolInst(String value) throws SQLException {
		super(value, TBool::getSingleTemporalValue);
	}
	
	/**
	 * The value and timestamp constructor
	 *
	 * @param value - a boolean
	 * @param time  - a timestamp
	 * @throws SQLException
	 */
	public TBoolInst(boolean value, OffsetDateTime time) throws SQLException {
		super(value, time);
	}
}