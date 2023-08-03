package types.basic.tint;

import types.temporal.TInstant;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TIntInst extends TInstant<Integer> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntInst value
	 * @throws SQLException
	 */
	public TIntInst(String value) throws SQLException {
		super(value, TInt::getSingleTemporalValue);
	}
	
	/**
	 * The value and timestamp constructor
	 *
	 * @param value - an int
	 * @param time  - a timestamp
	 * @throws SQLException
	 */
	public TIntInst(int value, OffsetDateTime time) throws SQLException {
		super(value, time);
	}
}
