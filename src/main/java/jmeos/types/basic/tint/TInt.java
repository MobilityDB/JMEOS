package jmeos.types.basic.tint;

import jmeos.types.core.DateTimeFormatHelper;
import jmeos.types.core.TypeName;
import jmeos.types.temporal.Temporal;
import jmeos.types.temporal.TemporalDataType;
import jmeos.types.temporal.TemporalType;
import jmeos.types.temporal.TemporalValue;

import java.sql.SQLException;

/**
 * Class that represents the MobilityDB type TInt
 */
@TypeName(name = "tint")
public class TInt extends TemporalDataType<Integer> {
	
	/**
	 * The default constructor
	 */
	public TInt() {
		super();
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TInt value
	 * @throws SQLException
	 */
	public TInt(final String value) throws SQLException {
		super(value);
	}
	
	/**
	 * The constructor for temporal types
	 *
	 * @param temporal - a TIntInst, TIntInstSet, TIntSeq or a TIntSeqSet
	 */
	public TInt(Temporal<Integer> temporal) {
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
	public static TemporalValue<Integer> getSingleTemporalValue(String value) {
		String[] values = value.trim().split("@");
		return new TemporalValue<>(Integer.parseInt(values[0]), DateTimeFormatHelper.getDateTimeFormat(values[1]));
	}
	
	/**
	 * Compares two integers
	 *
	 * @param first  - the first int to compare
	 * @param second - the second int to compare
	 * @return 0 is both ints are equals, a positive value in case first is greater than second or a negative value
	 * if first is less than second
	 */
	public static int compareValue(Integer first, Integer second) {
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
				temporal = new TIntInst(value);
				break;
			case TEMPORAL_INSTANT_SET:
				temporal = new TIntInstSet(value);
				break;
			case TEMPORAL_SEQUENCE:
				temporal = new TIntSeq(value);
				break;
			case TEMPORAL_SEQUENCE_SET:
				temporal = new TIntSeqSet(value);
				break;
		}
	}
}
