package types.basic.tbool;

import jnr.ffi.Runtime;
import types.temporal.TemporalDataType;
import types.temporal.TemporalType;
import types.temporal.TemporalValue;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import types.temporal.Temporal;
import jnr.ffi.Pointer;
import java.sql.SQLException;


/**
 * Class that represents the MobilityDB type TBool
 */
@TypeName(name = "tbool")
public class TBool extends TemporalDataType<Boolean> {

    /**
     * The default constructor
     */
    public TBool() {
        super();
    }

    /**
     * The string constructor
     * @param value - the string with the TBool value
     * @throws SQLException
     */
    public TBool(final String value) throws SQLException {
        super(value);
    }

    /**
     * The constructor for temporal types
     * @param temporal - a TBoolInst, TBoolInstSet,TBoolSeq or a TBoolSeqSet
     */
    public TBool(Temporal<Boolean> temporal) {
        super();
        this.temporal = temporal;
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(final String value) throws SQLException {
        TemporalType temporalType = TemporalType.getTemporalType(value, this.getClass().getSimpleName());
        switch (temporalType) {
            case TEMPORAL_INSTANT:
                temporal = new TBoolInst(value);
                break;
            case TEMPORAL_INSTANT_SET:
                temporal = new TBoolInstSet(value);
                break;
            case TEMPORAL_SEQUENCE:
                temporal = new TBoolSeq(value);
                break;
            case TEMPORAL_SEQUENCE_SET:
                temporal = new TBoolSeqSet(value);
                break;
        }
    }

    /**
     * Method with compatible signature for delegate
     * {@link com.mobilitydb.jdbc.temporal.delegates.GetSingleTemporalValueFunction}
     * @param value string representation of the value
     * @return Temporal value wrapper with the value parsed
     */
    public static TemporalValue<Boolean> getSingleTemporalValue(String value) {
        boolean b;
        String[] values = value.trim().split("@");
        if(values[0].length() == 1) {
            b = values[0].equals("t");
        } else {
            b = Boolean.parseBoolean(values[0]);
        }
        return new TemporalValue<>(b, DateTimeFormatHelper.getDateTimeFormat(values[1]));
    }

    /**
     * Compares two booleans
     * @param first - the first boolean to compare
     * @param second - the second boolean to compare
     * @return 0 is both booleans are equals, a positive value in case first is greater than second or a negative value
     * if first is less than second
     */
    public static int compareValue(Boolean first, Boolean second) {
        return first.compareTo(second);
    }






}