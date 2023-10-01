package types.basic.tbool;

import types.temporal.TInstantSet;

import java.sql.SQLException;

public class TBoolInstSet extends TInstantSet<Boolean> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolInstSet value
	 * @throws SQLException
	 */
	public TBoolInstSet(String value) throws SQLException {
		super(value, TBoolInst::new, TBool::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TBoolInstSet(String[] values) throws SQLException {
		super(values, TBoolInst::new, TBool::compareValue);
	}
	
	/**
	 * The TBoolInst array constructor
	 *
	 * @param values - an array of TBoolInst
	 * @throws SQLException
	 */
	public TBoolInstSet(TBoolInst[] values) throws SQLException {
		super(values, TBool::compareValue);
	}
}
