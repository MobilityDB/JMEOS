package jmeos.types.basic.ttext;

import jmeos.types.temporal.TSequenceSet;

import java.sql.SQLException;

public class TTextSeqSet extends TSequenceSet<String> {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TTextSeqSet value
	 * @throws SQLException
	 */
	public TTextSeqSet(String value) throws SQLException {
		super(true, value, TTextSeq::new, TText::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TTextSeqSet(String[] values) throws SQLException {
		super(true, values, TTextSeq::new, TText::compareValue);
	}
	
	/**
	 * The TTextSeq array constructor
	 *
	 * @param values - an array of TTextSeq
	 * @throws SQLException
	 */
	public TTextSeqSet(TTextSeq[] values) throws SQLException {
		super(true, values, TText::compareValue);
	}
	
	@Override
	protected boolean explicitInterpolation() {
		return false;
	}
}
