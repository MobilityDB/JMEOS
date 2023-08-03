package types.basic.tfloat;

import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import types.temporal.Temporal;
import types.temporal.TemporalDataType;
import types.temporal.TemporalType;
import types.temporal.TemporalValue;

import java.sql.SQLException;


/**
 * Class that represents the MobilityDB type TFloat
 */
@TypeName(name = "tfloat")
public class TFloat extends TemporalDataType<Float> {
	
	/**
	 * The default constructor
	 */
	public TFloat() {
		super();
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TFloat value
	 * @throws SQLException
	 */
	public TFloat(String value) throws SQLException {
		super(value);
	}
	
	/**
	 * The constructor for temporal types
	 *
	 * @param temporal - a TFloatInst, TFloatInstSet, TFloatSeq or a TFloatSeqSet
	 */
	public TFloat(Temporal<Float> temporal) {
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
	public static TemporalValue<Float> getSingleTemporalValue(String value) {
		String[] values = value.trim().split("@");
		return new TemporalValue<>(Float.parseFloat(values[0]), DateTimeFormatHelper.getDateTimeFormat(values[1]));
	}
	
	/**
	 * Compares two floats
	 *
	 * @param first  - the first float to compare
	 * @param second - the second float to compare
	 * @return 0 is both floats are equals, a positive value in case first is greater than second or a negative value
	 * if first is less than second
	 */
	public static int compareValue(Float first, Float second) {
		return first.compareTo(second);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String value) throws SQLException {
		TemporalType temporalType = TemporalType.getTemporalType(value, this.getClass().getSimpleName());
		switch (temporalType) {
			case TEMPORAL_INSTANT:
				temporal = new TFloatInst(value);
				break;
			case TEMPORAL_INSTANT_SET:
				temporal = new TFloatInstSet(value);
				break;
			case TEMPORAL_SEQUENCE:
				temporal = new TFloatSeq(value);
				break;
			case TEMPORAL_SEQUENCE_SET:
				temporal = new TFloatSeqSet(value);
				break;
		}
	}
}
