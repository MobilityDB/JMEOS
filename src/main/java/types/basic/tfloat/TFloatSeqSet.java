package types.basic.tfloat;

import types.temporal.TSequenceSet;

import java.sql.SQLException;

public class TFloatSeqSet extends TSequenceSet<Float> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloatSeqSet value
	 * @throws SQLException
	 */
	public TFloatSeqSet(String value) throws SQLException {
		super(value, TFloatSeq::new, TFloat::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TFloatSeqSet(String[] values) throws SQLException {
		super(false, values, TFloatSeq::new, TFloat::compareValue);
	}
	
	/**
	 * The string array and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of strings
	 * @throws SQLException
	 */
	public TFloatSeqSet(boolean stepwise, String[] values) throws SQLException {
		super(stepwise, values, TFloatSeq::new, TFloat::compareValue);
	}
	
	/**
	 * The TFloatSeq array constructor
	 *
	 * @param values - an array of TFloatSeq
	 * @throws SQLException
	 */
	public TFloatSeqSet(TFloatSeq[] values) throws SQLException {
		super(false, values, TFloat::compareValue);
	}
	
	/**
	 * The TFloatSeq array and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TFloatSeq
	 * @throws SQLException
	 */
	public TFloatSeqSet(boolean stepwise, TFloatSeq[] values) throws SQLException {
		super(stepwise, values, TFloat::compareValue);
	}
}
