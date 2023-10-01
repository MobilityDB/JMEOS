package types.basic.tpoint.tgeom;

import jnr.ffi.Pointer;
import types.basic.tpoint.TPointSeq;
import types.basic.tpoint.helpers.TPointConstants;

import java.sql.SQLException;

public class TGeomPointSeq extends TPointSeq {

	public TGeomPointSeq(Pointer inner){
		super(inner);
	}

	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeomPointSeq value
	 * @throws SQLException
	 */
	public TGeomPointSeq(String value) throws SQLException {
		super(value, TGeomPointInst::new);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeq(String[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, false, values, TGeomPointInst::new);
	}
	
	/**
	 * The string array and stepwise constructor
	 *
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeq(boolean isStepwise, String[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, isStepwise, values, TGeomPointInst::new);
	}
	
	/**
	 * The string array and bounds constructor
	 *
	 * @param values         - an array of strings
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeomPointSeq(String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.EMPTY_SRID,
				false,
				values,
				lowerInclusive,
				upperInclusive,
				TGeomPointInst::new);
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
	public TGeomPointSeq(boolean isStepwise, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.EMPTY_SRID,
				isStepwise,
				values,
				lowerInclusive,
				upperInclusive,
				TGeomPointInst::new);
	}
	
	/**
	 * The TGeomPointInst array constructor
	 *
	 * @param values - an array of TGeomPointInst
	 * @throws SQLException
	 */
	public TGeomPointSeq(TGeomPointInst[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, false, values);
	}
	
	/**
	 * The TGeomPointInst array and stepwise constructor
	 *
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of TGeomPointInst
	 * @throws SQLException
	 */
	public TGeomPointSeq(boolean isStepwise, TGeomPointInst[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, isStepwise, values);
	}
	
	/**
	 * The TGeomPointInst array and bounds constructor
	 *
	 * @param values         - an array of TGeomPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeomPointSeq(TGeomPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.EMPTY_SRID, false, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The TGeomPointInst array, stepwise and bounds constructor
	 *
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of TGeomPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeomPointSeq(boolean isStepwise, TGeomPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(TPointConstants.EMPTY_SRID, isStepwise, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The string array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, String[] values) throws SQLException {
		super(srid, false, values, TGeomPointInst::new);
	}
	
	/**
	 * The string array, stepwise and SRID constructor
	 *
	 * @param srid       - spatial reference identifier
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, boolean isStepwise, String[] values) throws SQLException {
		super(srid, isStepwise, values, TGeomPointInst::new);
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
	public TGeomPointSeq(int srid, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, false, values, lowerInclusive, upperInclusive, TGeomPointInst::new);
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
	public TGeomPointSeq(int srid, boolean isStepwise, String[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, isStepwise, values, lowerInclusive, upperInclusive, TGeomPointInst::new);
	}
	
	/**
	 * The TGeomPointInst array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeomPointInst
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, TGeomPointInst[] values) throws SQLException {
		super(srid, false, values);
	}
	
	/**
	 * The TGeomPointInst array, SRID and stepwise constructor
	 *
	 * @param srid       - spatial reference identifier
	 * @param isStepwise - if it is stepwise
	 * @param values     - an array of TGeomPointInst
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, boolean isStepwise, TGeomPointInst[] values) throws SQLException {
		super(srid, isStepwise, values);
	}
	
	/**
	 * The TGeomPointInst array, SRID and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param values         - an array of TGeomPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, TGeomPointInst[] values, boolean lowerInclusive, boolean upperInclusive)
			throws SQLException {
		super(srid, false, values, lowerInclusive, upperInclusive);
	}
	
	/**
	 * The TGeomPointInst array, SRID, stepwise and bounds constructor
	 *
	 * @param srid           - spatial reference identifier
	 * @param isStepwise     - if it is stepwise
	 * @param values         - an array of TGeomPointInst
	 * @param lowerInclusive - if the lower bound is inclusive
	 * @param upperInclusive - if the upper bound is inclusive
	 * @throws SQLException
	 */
	public TGeomPointSeq(int srid, boolean isStepwise, TGeomPointInst[] values,
						 boolean lowerInclusive, boolean upperInclusive) throws SQLException {
		super(srid, isStepwise, values, lowerInclusive, upperInclusive);
	}
}
