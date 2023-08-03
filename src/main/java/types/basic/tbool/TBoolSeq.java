package types.basic.tbool;

import types.temporal.TSequence;

import java.sql.SQLException;

/**
 * By Default Interpolation is stepwise
 */
public class TBoolSeq extends TSequence<Boolean> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolSeq value
	 * @throws SQLException
	 */
	public TBoolSeq(String value) throws SQLException {
		super(true, value, TBoolInst::new, TBool::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TBoolSeq(String[] values) throws SQLException {
		super(true, values, TBoolInst::new, TBool::compareValue);
	}
	
	/**
	 * The string array and bounds constructor
	 *
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TBoolSeq(String[] values, boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(true, values, lowerInclusive, upperInclusive, TBoolInst::new, TBool::compareValue);
	}
	
	/**
	 * The TBoolInst array constructor
	 *
	 * @param values - an array of TBoolInst
	 * @throws SQLException
	 */
	public TBoolSeq(TBoolInst[] values) throws SQLException {
		super(true, values, TBool::compareValue);
	}
	
	/**
	 * The TBoolInst array and bounds constructor
	 *
	 * @param values         - an array of TBoolInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TBoolSeq(TBoolInst[] values, boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(true, values, lowerInclusive, upperInclusive, TBool::compareValue);
	}
	
	@Override
	protected boolean explicitInterpolation() {
		return false;
	}
}
