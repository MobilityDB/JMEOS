package types.basic.tbool;

import jnr.ffi.Pointer;
import types.temporal.TSequenceSet;

import java.sql.SQLException;

public class TBoolSeqSet extends TSequenceSet<Boolean> {

	public TBoolSeqSet(Pointer inner){
		super(inner);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBoolSeqSet value
	 * @throws SQLException
	 */
	public TBoolSeqSet(String value) throws SQLException {
		super(true, value, TBoolSeq::new, TBool::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TBoolSeqSet(String[] values) throws SQLException {
		super(true, values, TBoolSeq::new, TBool::compareValue);
	}
	
	/**
	 * The TBoolSeq array constructor
	 *
	 * @param values - an array of TBoolSeq
	 * @throws SQLException
	 */
	public TBoolSeqSet(TBoolSeq[] values) throws SQLException {
		super(true, values, TBool::compareValue);
	}
	
	@Override
	protected boolean explicitInterpolation() {
		return false;
	}
}
