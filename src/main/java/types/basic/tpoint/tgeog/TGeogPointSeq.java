package types.basic.tpoint.tgeog;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointSeq;
import types.basic.tpoint.helpers.TPointConstants;

import java.sql.SQLException;

public class TGeogPointSeq extends TPointSeq {

	public TGeogPointSeq(Pointer inner){
		super(inner);
	}


	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeogPointSeq value
	 * @throws SQLException
	 */
	public TGeogPointSeq(String value) throws SQLException {
		super(value, TGeogPointInst::new);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeq(String[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, false, values, TGeogPointInst::new);
	}
	
	/**
	 * The string array and stepwise constructor
	 *
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeq(boolean isStepwise, String[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, isStepwise, values, TGeogPointInst::new);
	}
	
	/**
	 * The string array and bounds constructor
	 *
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.DEFAULT_SRID,
				false,
				values,
				lowerInclusive,
				upperInclusive,
				TGeogPointInst::new);
	}
	
	/**
	 * The string array, stepwise and bounds constructor
	 *
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(boolean isStepwise, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.DEFAULT_SRID,
				isStepwise,
				values,
				lowerInclusive,
				upperInclusive,
				TGeogPointInst::new);
	}
	
	/**
	 * The TGeogPointInst array constructor
	 *
	 * @param values - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointSeq(TGeogPointInst[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, false, values);
	}
	
	/**
	 * The TGeogPointInst array and stepwise constructor
	 *
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointSeq(boolean isStepwise, TGeogPointInst[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, isStepwise, values);
	}
	
	/**
	 * The TGeogPointInst array and bounds constructor
	 *
	 * @param values         - an array of TGeogPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(TGeogPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.DEFAULT_SRID, false, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The TGeogPointInst array, stepwise and bounds constructor
	 *
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of TGeogPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(boolean isStepwise, TGeogPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.DEFAULT_SRID, isStepwise, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The string array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, String[] values) throws SQLException {
		super(srid, false, values, TGeogPointInst::new);
	}
	
	/**
	 * The string array, stepwise and SRID constructor
	 *
	 * @param srid       - spatial reference identifier
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, boolean isStepwise, String[] values) throws SQLException {
		super(srid, isStepwise, values, TGeogPointInst::new);
	}
	
	/**
	 * The string array, SRID and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, false, values, lowerInclusive, upperInclusive, TGeogPointInst::new);
	}
	
	/**
	 * The string array, SRID, stepwise and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, boolean isStepwise, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, isStepwise, values, lowerInclusive, upperInclusive, TGeogPointInst::new);
	}
	
	/**
	 * The TGeogPointInst array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, TGeogPointInst[] values) throws SQLException {
		super(srid, false, values);
	}
	
	/**
	 * The TGeogPointInst array, SRID and stepwise constructor
	 *
	 * @param srid       - spatial reference identifier
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, boolean isStepwise, TGeogPointInst[] values) throws SQLException {
		super(srid, isStepwise, values);
	}
	
	/**
	 * The TGeogPointInst array, SRID and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param values         - an array of TGeogPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, TGeogPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, false, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The TGeogPointInst array, SRID, stepwise and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of TGeogPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeogPointSeq(int srid, boolean isStepwise, TGeogPointInst[] values,
						 boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(srid, isStepwise, values, lowerInclusive, upperInclusive);
	}
}
