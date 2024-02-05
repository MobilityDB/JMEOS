package types.basic.tbool;

import jnr.ffi.Pointer;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.*;
import functions.functions;


/**
 * Class that represents the MobilityDB type TBool used for {@link TBoolInst}, {@link TBoolSeq} and {@link TBoolSeqSet}
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public interface TBool {
    String customType = "Boolean";
    Pointer getBoolInner();
    String getCustomType();
    TemporalType getTemporalType();


    /* ------------------------- Constructors ---------------------------------- */


    /**
     * Create a temporal Boolean from a Boolean value and the time frame of
     *         another temporal object.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tbool_from_base_temp</li>
     *
     * @param value Boolean value.
     * @param base Temporal object to use as time frame.
     * @return A new :class:`TBool` object.
     */
    default TBool from_base_temporal(boolean value, Temporal base){
        return (TBool) Factory.create_temporal(functions.tbool_from_base_temp(value, base.getInner()),customType,base.getTemporalType());
    }

    /**
     * Create a temporal boolean from a boolean value and a time object.
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>tboolinst_make</li>
     *             <li>tboolseq_from_base_timestampset</li>
     *             <li>tboolseq_from_base_period</li>
     *             <li>tboolseqset_from_base_periodset</li>
     *         </ul>
     *
     * @param value Boolean value.
     * @param base Time object to use as temporal dimension.
     * @return A new temporal boolean.
     */
    static Temporal from_base_time(boolean value, Time base){
        if (base instanceof TimestampSet){
            return new TBoolSeq(functions.tboolseq_from_base_timestampset(value,((TimestampSet) base).get_inner()));

        } else if (base instanceof Period) {
            return new TBoolSeq(functions.tboolseq_from_base_period(value,((Period) base).get_inner()));

        } else if (base instanceof PeriodSet) {
            return new TBoolSeqSet(functions.tboolseqset_from_base_periodset(value,((PeriodSet) base).get_inner()));
        }

        return null;
    }


    /* ------------------------- Output ---------------------------------- */


    /**
     * Returns the string representation of "this".
     * <p>
     *         MEOS Function:
     *             <li>tbool_out</li>
     * @return Returns the string representation of "this"
     */
    default String to_string(){
        return functions.tbool_out(getBoolInner());
    }


    /* ------------------------- Accessors ---------------------------------- */

    /**
     * Returns the starting value of "this".
     *
     *         MEOS Function:
     *            <li>tbool_start_value</li>
     * @return Returns the starting value of "this".
     */
    default boolean start_value(){
        return functions.tbool_start_value(getBoolInner());
    }

    /**
     * Returns the ending value of "this".
     *
     *         MEOS Function:
     *             <li>tbool_end_value</li>
     * @return Returns the ending value of "this".
     */
    default boolean end_value(){
        return functions.tbool_end_value(getBoolInner());
    }


    /* ------------------------- Ever and Always Comparisons ------------------- */


    /**
     * Returns whether "this" is always equal to "value".
     *
     * <p>
     *
     *         MEOS Function:
     *             <li>tbool_always_eq</li>
     * @param value Value to check for.
     * @return True if "this" is always equal to "value", False otherwise.
     */
    default boolean always_eq(boolean value){
        return functions.tbool_always_eq(getBoolInner(), value);
    }


    /**
     * Returns whether "this" is ever equal to "value".
     *
     * <p>
     *
     *         MEOS Function:
     *             <li>tbool_ever_eq</li>
     * @param value Value to check for.
     * @return True if "this" is ever equal to "value", False otherwise.
     */
    default boolean ever_eq(boolean value){
        return functions.tbool_ever_eq(getBoolInner(), value);
    }

    /**
     * Returns whether "this" is never equal to "value".
     *
     * <p>
     *
     *         MEOS Function:
     *             <li>tbool_ever_eq</li>
     * @param value Value to check for.
     * @return True if "this" is never equal to "value", False otherwise.
     */
    default boolean never_eq(boolean value){
        return !(functions.tbool_ever_eq(getBoolInner(), value));
    }




    /* ------------------------- Temporal Comparisons -------------------------- */

    /**
     * Returns the temporal equality relation between "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>teq_tbool_tbool</li>
     *             <li>teq_temporal_temporal</li>
     *         </ul>
     *
     * @param other A temporal or boolean object to compare to "this".
     * @return A {@link TBool} with the result of the temporal equality relation.
     */
    default TBool temporal_equal_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.teq_tbool_bool(getBoolInner(),other), getCustomType(),getTemporalType());
    }




    /**
     * Returns the temporal inequality relation between "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>teq_tbool_tbool</li>
     *             <li>teq_temporal_temporal</li>
     *         </ul>
     *
     * @param other A temporal or boolean object to compare to "this".
     * @return A {@link TBool} with the result of the temporal inequality relation.
     */
    default TBool temporal_not_equal_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.tne_tbool_bool(getBoolInner(),other), getCustomType(),getTemporalType());
    }


    /* ------------------------- Restrictions ---------------------------------- */


    /**
     * Returns a new temporal boolean with the values of "this" restricted to
     *         the time or value "other".
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>tbool_at_value</li>
     *             <li>temporal_at_timestamp</li>
     *             <li>temporal_at_timestampset</li>
     *             <li>temporal_at_period</li>
     *             <li>temporal_at_periodset</li>
     *         </ul>
     * @param other Time or value to restrict to.
     * @return A new temporal boolean.
     */
    default TBool at_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.tbool_at_value(getBoolInner(),other), getCustomType(),getTemporalType());
    }


    /**
     * Returns a new temporal boolean with the values of "this" restricted to
     *         the complement of the time or value
     *          "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *         <ul>
     *             <li>tbool_minus_value</li>
     *             <li>temporal_minus_timestamp</li>
     *             <li>temporal_minus_timestampset</li>
     *             <li>temporal_minus_period</li>
     *             <li>temporal_minus_periodset</li>
     *        </ul>
     * @param other Time or value to restrict to the complement of.
     * @return A new temporal boolean.
     */
    default TBool minus_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.tbool_minus_value(getBoolInner(),other), getCustomType(),getTemporalType());
    }

    /* ------------------------- Boolean Operations ---------------------------- */


    /**
     * Returns the temporal conjunction of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tand_tbool_bool</li>
     *             <li>tand_tbool_tbool</li>
     *
     * @param other A temporal or boolean object to combine with "this".
     * @return A {@link TBool} with the temporal conjunction of "this" and
     *      *             "other".
     */
    default TBool temporal_and(TBool other){
        return (TBool) Factory.create_temporal(functions.tand_tbool_tbool(getBoolInner(),other.getBoolInner()), getCustomType(),getTemporalType());
    }


    /**
     * Returns the temporal conjunction of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tand_tbool_bool</li>
     *             <li>tand_tbool_tbool</li>
     *
     * @param other A temporal or boolean object to combine with "this".
     * @return A {@link TBool} with the temporal conjunction of "this" and
     *      *             "other".
     */
    default TBool temporal_and_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.tand_tbool_bool(getBoolInner(),other), getCustomType(),getTemporalType());
    }




    /**
     * Returns the temporal disjunction of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tor_tbool_bool</li>
     *             <li>tor_tbool_tbool</li>
     *
     * @param other A temporal or boolean object to combine with "this".
     * @return A {@link TBool} with the temporal disjunction of "this" and
     *      *             "other".
     */
    default TBool temporal_or(TBool other){
        return (TBool) Factory.create_temporal(functions.tor_tbool_tbool(getBoolInner(),other.getBoolInner()), getCustomType(),getTemporalType());
    }


    /**
     * Returns the temporal disjunction of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tor_tbool_bool</li>
     *             <li>tor_tbool_tbool</li>
     *
     * @param other A temporal or boolean object to combine with "this".
     * @return A {@link TBool} with the temporal disjunction of "this" and
     *      *             "other".
     */
    default TBool temporal_or_bool(boolean other){
        return (TBool) Factory.create_temporal(functions.tor_tbool_bool(getBoolInner(),other), getCustomType(),getTemporalType());
    }


    /**
     * Returns the temporal negation of "this".
     *
     * <p>
     *         MEOS Function:
     *             <li>tnot_tbool</li>
     * @return A {@link TBool} with the temporal negation of "this".
     */
    default TBool temporal_not(){
        return (TBool) Factory.create_temporal(functions.tnot_tbool(getBoolInner()),getCustomType(),getTemporalType());
    }


    /**
     * Returns a period set with the periods where "this" is True.
     *
     * <p>
     *         MEOS Function:
     *             <li>tbool_when_true</li>
     * @return A {@link PeriodSet} with the periods where "this" is True.
     */
    default PeriodSet when_true(){
        return new PeriodSet(functions.tbool_when_true(getBoolInner()));
    }


    /**
     * Returns a period set with the periods where "this" is False.
     *
     * <p>
     *         MEOS Function:
     *             <li>tbool_when_true</li>
     * @return A {@link PeriodSet} with the periods where "this" is False.
     */
    default PeriodSet when_false(){
        return new PeriodSet(functions.tbool_when_true(functions.tnot_tbool(getBoolInner())));
    }
	
}