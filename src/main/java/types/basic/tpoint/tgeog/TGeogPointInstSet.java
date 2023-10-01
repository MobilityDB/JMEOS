package types.basic.tpoint.tgeog;

import types.basic.tpoint.TPointInstSet;
import types.basic.tpoint.helpers.TPointConstants;

import java.sql.SQLException;

public class TGeogPointInstSet extends TPointInstSet {
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TGeogPointInstSet value
	 * @throws SQLException
	 */
	public TGeogPointInstSet(String value) throws SQLException {
		super(value, TGeogPointInst::new);
	}
	
	/**
	 * The string array constructor
	 *
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointInstSet(String[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, values, TGeogPointInst::new);
	}
	
	/**
	 * The string array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of strings
	 * @throws SQLException
	 */
	public TGeogPointInstSet(int srid, String[] values) throws SQLException {
		super(srid, values, TGeogPointInst::new);
	}
	
	/**
	 * The TGeogPointInst array constructor
	 *
	 * @param values - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointInstSet(TGeogPointInst[] values) throws SQLException {
		super(TPointConstants.DEFAULT_SRID, values);
	}
	
	/**
	 * The TGeogPointInst array and SRID constructor
	 *
	 * @param srid   - spatial reference identifier
	 * @param values - an array of TGeogPointInst
	 * @throws SQLException
	 */
	public TGeogPointInstSet(int srid, TGeogPointInst[] values) throws SQLException {
		super(srid, values);
	}
}
