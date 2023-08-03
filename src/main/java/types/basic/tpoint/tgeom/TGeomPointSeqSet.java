package types.basic.tpoint.tgeom;

import types.basic.tpoint.TPointSeqSet;
import types.basic.tpoint.helpers.TPointConstants;

import java.sql.SQLException;

public class TGeomPointSeqSet extends TPointSeqSet {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeomPointSeqSet value
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(String value) throws SQLException {
		super(value, TGeomPointSeq::new);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(String[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, false, values, TGeomPointSeq::new);
	}
	
	/**
	 * The string array and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of strings
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(boolean stepwise, String[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, stepwise, values, TGeomPointSeq::new);
	}
	
	/**
	 * The TGeomPointSeq array constructor
	 *
	 * @param values - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(TGeomPointSeq[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, false, values);
	}
	
	/**
	 * The TGeomPointSeq array  and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(boolean stepwise, TGeomPointSeq[] values) throws SQLException {
		super(TPointConstants.EMPTY_SRID, stepwise, values);
	}
	
	/**
	 * The TGeomPointSeq array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(int srid, String[] values) throws SQLException {
		super(srid, false, values, TGeomPointSeq::new);
	}
	
	/**
	 * The string array, SRID and stepwise constructor
	 *
	 * @param srid     - spatial reference identifier
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(int srid, boolean stepwise, String[] values) throws SQLException {
		super(srid, stepwise, values, TGeomPointSeq::new);
	}
	
	/**
	 * The TGeomPointSeq array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(int srid, TGeomPointSeq[] values) throws SQLException {
		super(srid, false, values);
	}
	
	/**
	 * The TGeomPointSeq array, SRID and stepwise constructor
	 *
	 * @param srid     - spatial reference identifier
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeomPointSeq
	 * @throws SQLException
	 */
	public TGeomPointSeqSet(int srid, boolean stepwise, TGeomPointSeq[] values) throws SQLException {
		super(srid, stepwise, values);
	}
}
