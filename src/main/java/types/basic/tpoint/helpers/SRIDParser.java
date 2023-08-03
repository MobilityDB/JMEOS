package types.basic.tpoint.helpers;

import net.postgis.jdbc.geometry.Point;

import java.sql.SQLException;
import java.util.List;

/**
 * Helper class for parsing SRID from string representation
 */
public class SRIDParser {
	private SRIDParser() {
	}
	
	/**
	 * Retrieves the initial SRID from the value
	 *
	 * @param value - a Temporal value in string representation
	 * @return Temporal value without initial SRID and the SRID value
	 * @throws SQLException - When the SRID is invalid
	 */
	public static SRIDParseResponse parseSRID(String value) throws SQLException {
		String newString = value;
		int srid = TPointConstants.EMPTY_SRID;
		
		if (newString.startsWith(TPointConstants.SRID_PREFIX)) {
			int idx = newString.indexOf(";");
			
			if (idx < 0) {
				throw new SQLException("Incorrect format for SRID");
			}
			
			String sridString = newString.substring(TPointConstants.SRID_PREFIX.length(), idx);
			newString = newString.substring(idx + 1);
			
			try {
				srid = Integer.parseInt(sridString);
			} catch (NumberFormatException ex) {
				throw new SQLException("Invalid SRID", ex);
			}
			
			if (srid < 0) {
				throw new SQLException("Invalid SRID");
			}
		}
		
		return new SRIDParseResponse(srid, newString);
	}
	
	/**
	 * Applies the SRID to the given temporal values
	 * If it is not defined it will use the first defined SRID
	 *
	 * @param srid           - the current SRID
	 * @param temporalValues - Temporal values
	 * @return the modified SRID
	 * @throws SQLException - If any value has a different SRID defined
	 */
	public static int applySRID(int srid, List<Point> temporalValues) throws SQLException {
		if (srid == TPointConstants.EMPTY_SRID) {
			srid = getFirstSRID(temporalValues);
			
			if (srid == TPointConstants.EMPTY_SRID) {
				return srid;
			}
		}
		
		for (Point temporalValue : temporalValues) {
			int currentSRID = temporalValue.getSrid();
			
			if (currentSRID == TPointConstants.EMPTY_SRID) {
				currentSRID = srid;
				temporalValue.setSrid(currentSRID);
			}
			
			if (currentSRID != srid) {
				throw new SQLException(String.format(
						"Geometry SRID (%d) does not match temporal type SRID (%d)", currentSRID, srid
				));
			}
		}
		
		return srid;
	}
	
	/**
	 * Gets the first SRID
	 *
	 * @param temporalValues - the list of temporal values
	 * @return the SRID
	 */
	private static int getFirstSRID(List<Point> temporalValues) {
		for (Point temporalValue : temporalValues) {
			int currentSRID = temporalValue.getSrid();
			
			if (currentSRID != TPointConstants.EMPTY_SRID) {
				return currentSRID;
			}
		}
		
		return TPointConstants.EMPTY_SRID;
	}
}
