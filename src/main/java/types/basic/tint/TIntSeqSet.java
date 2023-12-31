package types.basic.tint;

import types.temporal.TSequenceSet;
import jnr.ffi.Pointer;
import java.sql.SQLException;

public class TIntSeqSet extends TSequenceSet<Integer> {


	public TIntSeqSet(Pointer inner){
		super(inner);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TIntSeqSet value
	 * @throws SQLException
	 */
	public TIntSeqSet(String value) throws SQLException {
		super(true, value, TIntSeq::new, TInt::compareValue);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TIntSeqSet(String[] values) throws SQLException {
		super(true, values, TIntSeq::new, TInt::compareValue);
	}
	
	/**
	 * The TIntSeq array constructor
	 *
	 * @param values - an array of TIntSeq
	 * @throws SQLException
	 */
	public TIntSeqSet(TIntSeq[] values) throws SQLException {
		super(true, values, TInt::compareValue);
	}
	
	@Override
	protected boolean explicitInterpolation() {
		return false;
	}
}
