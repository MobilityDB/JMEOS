package jmeos.types.basic.ttext;

import jmeos.types.temporal.TInstantSet;

import java.sql.SQLException;

public class TTextInstSet extends TInstantSet<String> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TTextInstSet value
	 * @throws SQLException
	 */
	public TTextInstSet(String value) throws SQLException {
		super(value, TTextInst::new, TText::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TTextInstSet(String[] values) throws SQLException {
		super(values, TTextInst::new, TText::compareValue);
	}
	
	/**
	 * The TTextInst array constructor
	 *
	 * @param values - an array of TTextInst
	 * @throws SQLException
	 */
	public TTextInstSet(TTextInst[] values) throws SQLException {
		super(values, TText::compareValue);
	}
}
