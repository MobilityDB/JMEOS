package types.basic.tfloat;

import types.temporal.TSequence;

import java.sql.SQLException;

public class TFloatSeq extends TSequence<Float> {

    /**
     * The string constructor
     * @param value - the string with the TFloatSeq value
     * @throws SQLException
     */
    public TFloatSeq(String value) throws SQLException {
        super(value, TFloatInst::new, TFloat::compareValue);
    }

    /**
     * The string array constructor
     * @param values - an array of strings
     * @throws SQLException
     */
    public TFloatSeq(String[] values) throws SQLException {
        super(false, values, TFloatInst::new, TFloat::compareValue);
    }

    /**
     * The string array and stepwise constructor
     * @param isStepwise - if it is stepwise
     * @param values - an array of strings
     * @throws SQLException
     */
    public TFloatSeq(boolean isStepwise, String[] values) throws SQLException {
        super(isStepwise, values, TFloatInst::new, TFloat::compareValue);
    }

    /**
     * The string array and bounds constructor
     * @param values - an array of strings
     * @param lowerInclusive - if the lower bound is inclusive
     * @param upperInclusive  - if the upper bound is inclusive
     * @throws SQLException
     */
    public TFloatSeq(String[] values, boolean lowerInclusive, boolean upperInclusive)
            throws SQLException {
        super(false, values, lowerInclusive, upperInclusive, TFloatInst::new, TFloat::compareValue);
    }

    /**
     * The string array, stepwise and bounds constructor
     * @param isStepwise - if it is stepwise
     * @param values - an array of strings
     * @param lowerInclusive - if the lower bound is inclusive
     * @param upperInclusive  - if the upper bound is inclusive
     * @throws SQLException
     */
    public TFloatSeq(boolean isStepwise, String[] values, boolean lowerInclusive, boolean upperInclusive)
            throws SQLException {
        super(isStepwise, values, lowerInclusive, upperInclusive, TFloatInst::new, TFloat::compareValue);
    }

    /**
     * The TFloatInst array constructor
     * @param values - an array of TFloatInst
     * @throws SQLException
     */
    public TFloatSeq(TFloatInst[] values) throws SQLException {
        super(false, values, TFloat::compareValue);
    }

    /**
     * The TFloatInst array and stepwise constructor
     * @param isStepwise - if it is stepwise
     * @param values - an array of TFloatInst
     * @throws SQLException
     */
    public TFloatSeq(boolean isStepwise, TFloatInst[] values) throws SQLException {
        super(isStepwise, values, TFloat::compareValue);
    }

    /**
     * The TFloatInst array and bounds constructor
     * @param values - an array of TFloatInst
     * @param lowerInclusive - if the lower bound is inclusive
     * @param upperInclusive - if the upper bound is inclusive
     * @throws SQLException
     */
    public TFloatSeq(TFloatInst[] values, boolean lowerInclusive, boolean upperInclusive)
            throws SQLException {
        super(false, values, lowerInclusive, upperInclusive, TFloat::compareValue);
    }

    /**
     * The TFloatInst array, stepwise and bounds constructor
     * @param isStepwise - if it is stepwise
     * @param values - an array of TFloatInst
     * @param lowerInclusive - if the lower bound is inclusive
     * @param upperInclusive - if the upper bound is inclusive
     * @throws SQLException
     */
    public TFloatSeq(boolean isStepwise, TFloatInst[] values, boolean lowerInclusive, boolean upperInclusive)
            throws SQLException {
        super(isStepwise, values, lowerInclusive, upperInclusive, TFloat::compareValue);
    }

}
