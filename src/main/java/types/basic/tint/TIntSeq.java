package types.basic.tint;

import types.temporal.TSequence;

import java.sql.SQLException;

/**
 * By Default Interpolation is stepwise
 */
public class TIntSeq extends TSequence<Integer> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeq value
	 * @throws SQLException
	 */
	public TIntSeq(String value) throws SQLException {
		super(true, value, TIntInst::new, TInt::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TIntSeq(String[] values) throws SQLException {
		super(true, values, TIntInst::new, TInt::compareValue);
	}
	
	/**
	 * The string array and bounds constructor
	 *
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TIntSeq(String[] values, boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(true, values, lowerInclusive, upperInclusive, TIntInst::new, TInt::compareValue);
	}
	
	/**
	 * The TIntInst array constructor
	 *
	 * @param values - an array of TIntInst
	 * @throws SQLException
	 */
	public TIntSeq(TIntInst[] values) throws SQLException {
		super(true, values, TInt::compareValue);
	}
	
	/**
	 * The TIntInst array and bounds constructor
	 *
	 * @param values         - an array of TIntInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TIntSeq(TIntInst[] values, boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(true, values, lowerInclusive, upperInclusive, TInt::compareValue);
	}
	
	@Override
	protected boolean explicitInterpolation() {
		return false;
	}
}
