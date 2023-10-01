package types.basic.tfloat;

import types.temporal.TInstantSet;

import java.sql.SQLException;

public class TFloatInstSet extends TInstantSet<Float> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatInstSet value
	 * @throws SQLException
	 */
	public TFloatInstSet(String value) throws SQLException {
		super(value, TFloatInst::new, TFloat::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TFloatInstSet(String[] values) throws SQLException {
		super(values, TFloatInst::new, TFloat::compareValue);
	}
	
	/**
	 * The TFloatInst array constructor
	 *
	 * @param values - an array of TFloatInst
	 * @throws SQLException
	 */
	public TFloatInstSet(TFloatInst[] values) throws SQLException {
		super(values, TFloat::compareValue);
	}
}
