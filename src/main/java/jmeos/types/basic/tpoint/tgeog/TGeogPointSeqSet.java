package jmeos.types.basic.tpoint.tgeog;

import jmeos.types.basic.tpoint.TPointSeqSet;
import jmeos.types.basic.tpoint.helpers.TPointConstants;

import java.sql.SQLException;

public class TGeogPointSeqSet extends TPointSeqSet {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeogPointSeqSet value
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(String value) throws SQLException {
		super(value, TGeogPointSeq::new);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(String[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, false, values, TGeogPointSeq::new);
	}
	
	/**
	 * The string array and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(boolean stepwise, String[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, stepwise, values, TGeogPointSeq::new);
	}
	
	/**
	 * The TGeogPointSeq array constructor
	 *
	 * @param values - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(TGeogPointSeq[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, false, values);
	}
	
	/**
	 * The TGeogPointSeq array and stepwise constructor
	 *
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(boolean stepwise, TGeogPointSeq[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, stepwise, values);
	}
	
	/**
	 * The TGeogPointSeq array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(int srid, String[] values) throws SQLException {
		super(srid, false, values, TGeogPointSeq::new);
	}
	
	/**
	 * The string array, SRID and stepwise constructor
	 *
	 * @param srid     - spatial reference identifier
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(int srid, boolean stepwise, String[] values) throws SQLException {
		super(srid, stepwise, values, TGeogPointSeq::new);
	}
	
	/**
	 * The TGeogPointSeq array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(int srid, TGeogPointSeq[] values) throws SQLException {
		super(srid, false, values);
	}
	
	/**
	 * The TGeogPointSeq array, SRID and stepwise constructor
	 *
	 * @param srid     - spatial reference identifier
	 * @param stepwise - if it is stepwise
	 * @param values   - an array of TGeogPointSeq
	 * @throws SQLException
	 */
	public TGeogPointSeqSet(int srid, boolean stepwise, TGeogPointSeq[] values) throws SQLException {
		super(srid, stepwise, values);
	}
}
