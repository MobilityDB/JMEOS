package jmeos.types.basic.ttext;

import jmeos.types.core.DateTimeFormatHelper;
import jmeos.types.core.TypeName;
import jmeos.types.temporal.Temporal;
import jmeos.types.temporal.TemporalDataType;
import jmeos.types.temporal.TemporalType;
import jmeos.types.temporal.TemporalValue;

import java.sql.SQLException;

/**
 * Class that represents the MobilityDB type TText
 */
@TypeName(name = "ttext")
public class TText extends TemporalDataType<String> {
	
	/**
	 * The default constructor
	 */
	public TText() {
		super();
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TText value
	 * @throws SQLException
	 */
	public TText(final String value) throws SQLException {
		super();
		setValue(value);
	}
	
	/**
	 * The constructor for temporal types
	 *
	 * @param temporal - a TTextInst, TTextInstSet, TTextSeq or a TTextSeqSet
	 */
	public TText(Temporal<String> temporal) {
		super();
		this.temporal = temporal;
	}
	
	/**
	 * Method with compatible signature for delegate
	 * {@link com.mobilitydb.jdbc.temporal.delegates.GetSingleTemporalValueFunction}
	 *
	 * @param value string representation of the value
	 * @return Temporal value wrapper with the value parsed
	 */
	public static TemporalValue<String> getSingleTemporalValue(String value) {
		String[] values = value.trim().split("@");
		if (values[0].startsWith("\"") && values[0].endsWith("\"")) {
			values[0] = values[0].replace("\"", "");
		}
		return new TemporalValue<>(String.format("%s", values[0]), DateTimeFormatHelper.getDateTimeFormat(values[1]));
	}
	
	/**
	 * Compares two strings
	 *
	 * @param first  - the first string to compare
	 * @param second - the second string to compare
	 * @return 0 is both strings are equals, a positive value in case first is greater than second or a negative value
	 * if first is less than second
	 */
	public static int compareValue(String first, String second) {
		return first.compareTo(second);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(final String value) throws SQLException {
		TemporalType temporalType = TemporalType.getTemporalType(value, this.getClass().getSimpleName());
		switch (temporalType) {
			case TEMPORAL_INSTANT:
				temporal = new TTextInst(value);
				break;
			case TEMPORAL_INSTANT_SET:
				temporal = new TTextInstSet(value);
				break;
			case TEMPORAL_SEQUENCE:
				temporal = new TTextSeq(value);
				break;
			case TEMPORAL_SEQUENCE_SET:
				temporal = new TTextSeqSet(value);
				break;
		}
	}
}
