package types.temporal;

import java.sql.SQLException;

/**
 * Classification of temporal types
 */
public enum TemporalType {
	TEMPORAL_INSTANT,
	TEMPORAL_INSTANT_SET,
	TEMPORAL_SEQUENCE,
	TEMPORAL_SEQUENCE_SET;
	
	/**
	 * Gets the temporal type based on the string received.
	 *
	 * @param value                - the string to be analyzed
	 * @param temporalDataTypeName - the name of the calling class
	 * @return a TemporalType
	 * @throws SQLException if the value does not have a valid format
	 */
	public static TemporalType getTemporalType(String value, String temporalDataTypeName) throws SQLException {
		if (value.startsWith("Interp=Stepwise;")) {
			if (value.startsWith("{", 16)) {
				return TemporalType.TEMPORAL_SEQUENCE_SET;
			} else {
				return TemporalType.TEMPORAL_SEQUENCE;
			}
		} else if (!value.startsWith("{") && !value.startsWith("[") && !value.startsWith("(")) {
			return TemporalType.TEMPORAL_INSTANT;
		} else if (value.startsWith("[") || value.startsWith("(")) {
			return TemporalType.TEMPORAL_SEQUENCE;
		} else if (value.startsWith("{")) {
			if (value.startsWith("[", 1) || value.startsWith("(", 1)) {
				return TemporalType.TEMPORAL_SEQUENCE_SET;
			} else {
				return TemporalType.TEMPORAL_INSTANT_SET;
			}
		} else {
			throw new SQLException(String.format("Could not parse %s value.", temporalDataTypeName));
		}
	}
}
