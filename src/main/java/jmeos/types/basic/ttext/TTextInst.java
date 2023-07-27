package jmeos.types.basic.ttext;

import jmeos.types.temporal.TInstant;

import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TTextInst extends TInstant<String> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TTextInst value
	 * @throws SQLException
	 */
	public TTextInst(String value) throws SQLException {
		super(value, TText::getSingleTemporalValue);
	}
	
	/**
	 * The value and timestamp constructor
	 *
	 * @param value - a string
	 * @param time  - a timestamp
	 * @throws SQLException
	 */
	public TTextInst(String value, OffsetDateTime time) throws SQLException {
		super(value, time);
	}
}
