package types.basic.tbool;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.annotations.In;
import org.locationtech.jts.io.ParseException;
import types.collections.base.Set;
import types.collections.number.FloatSet;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.Time;
import types.collections.time.tstzspanset;
import types.temporal.*;
import functions.functions;
import utils.ConversionUtils;

import java.time.LocalDateTime;


/**
 * Class that represents the MobilityDB type TBool used for {@link TBoolInst}, {@link TBoolSeq} and {@link TBoolSeqSet}
 *
 * @author ARIJIT SAMAL
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
        if (base instanceof tstzspanset){
            return new TBoolSeq(functions.tboolseqset_from_base_tstzspanset(value,((tstzspanset) base).get_inner()));

        } else if (base instanceof tstzset) {
            return new TBoolSeq(functions.tboolseq_from_base_tstzset(value,((tstzset) base).get_inner()));

        } else if (base instanceof tstzspan) {
            return new TBoolSeqSet(functions.tboolseq_from_base_tstzspan(value,((tstzspan) base).get_inner()));
        }

        return null;
    }
/**
        Returns a temporal object from a MF-JSON string.

        Args:
            mfjson: The MF-JSON string.

        Returns:
            A temporal object from a MF-JSON string.

        MEOS Functions:
            tbool_from_mfjson
*/
    default TBool from_mfjson(String mfjson){
        Pointer result= functions.tbool_from_mfjson(mfjson);
        return (TBool) Factory.create_temporal(result, getCustomType(), getTemporalType());
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

    /**
     * Returns the string representation of "this" in WKT format.
     * <p>
     *         MEOS Function:
     *             <li>tbool_out</li>
     * @return Returns the string representation of "this"
     */
    default String as_wkt(){
        return functions.tbool_out(getBoolInner());
    }


    /* ------------------------- Accessors ---------------------------------- */
/**
        Returns the unique values in `self`.

        MEOS Function:
            tbool_values
 */
    default Set<Boolean> value_set(){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer resPointer = functions.tbool_values(this.getBoolInner(), intPointer);
        StringBuilder sb = null;
        sb.append("{");
        int count= intPointer.getInt(Integer.BYTES);
        for (int i=0;i<count;i++){
            boolean boolRes= false;
            int res= resPointer.getInt((long) i *Integer.BYTES);
            if(res>0){
                boolRes= true;
            }
            sb.append(boolRes);
            if(i<count-1){
                sb.append(", ");
            }
        }
        sb.append("}");
        System.out.println(sb.toString());
        return new Set<Boolean>(sb.toString()) {
            @Override
            public Pointer get_inner() {
                return resPointer;
            }

            @Override
            public Pointer createInner(Pointer inner) {
                return inner;
            }

            @Override
            public Pointer createStringInner(String str) {
                return functions.tbool_in(str);
            }

            @Override
            public Boolean start_element() throws ParseException {
                return functions.tbool_start_value(this.get_inner());
            }

            @Override
            public Boolean end_element() throws ParseException {
                return functions.tbool_end_value(this.get_inner());
            }
        };
    }

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

/**
        Returns the value that `self` takes at a certain moment.

        Args:
            timestamp: Timestamp to get the value at.

        Returns:
            The value at the given timestamp.

        MEOS Function:
            tbool_value_at_timestamptz
*/

default boolean value_at_timestamp(LocalDateTime ts){
    // Create a JNR-FFI runtime instance
    Runtime runtime = Runtime.getSystemRuntime();
    // Allocate memory for an integer (4 bytes) but do not set a value
    Pointer boolPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
    boolean res= functions.tbool_value_at_timestamptz(this.getBoolInner(), ConversionUtils.datetimeToTimestampTz(ts), true, boolPointer);
    int value= boolPointer.getInt(Integer.BYTES);
    return value > 0;
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
        int result= functions.always_eq_tbool_bool(getBoolInner(), value);
        return result > 0;
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
        int result= functions.ever_eq_tbool_bool(getBoolInner(), value);
        return result > 0;
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
        return !(this.ever_eq(value));
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
    default TBool temporal_equal(boolean other){
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
    default TBool temporal_not_equal(boolean other){
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
    default TBool at(boolean other){
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
    default TBool minus(boolean other){
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
        Returns the temporal conjunction of `this` and `other`.

        Args:
            other: A temporal or boolean object to combine with `this`.

        Returns:
            A :class:`TBool` with the temporal conjunction of `this` and `other`.

        MEOS Functions:
            tand_tbool_bool, tand_tbool_tbool
*/
    default TBool and(Object other){
        return this.temporal_and((TBool) other);
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
        Returns the temporal disjunction of `this` and `other`.

        Args:
            other: A temporal or boolean object to combine with `this`.

        Returns:
            A :class:`TBool` with the temporal disjunction of `this` and `other`.

        MEOS Functions:
            tor_tbool_bool, tor_tbool_tbool
*/
    default TBool or(Object other){
        return this.temporal_or((TBool) other);
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
     * @return A {@link tstzspan} with the periods where "this" is True.
     */
    default tstzspanset when_true(){
        return new tstzspanset(functions.tbool_when_true(getBoolInner()));
    }


    /**
     * Returns a period set with the periods where "this" is False.
     *
     * <p>
     *         MEOS Function:
     *             <li>tbool_when_true</li>
     * @return A {@link tstzspan} with the periods where "this" is False.
     */
    default tstzspanset when_false(){
        return new tstzspanset(functions.tbool_when_true(functions.tnot_tbool(getBoolInner())));
    }
/**
        Returns the temporal negation of `this`.

        Returns:
            A :class:`TBool` with the temporal negation of `this`.

        MEOS Function:
            tnot_tbool
*/
    default TBool neg(){
        return this.temporal_not();
    }

/**
        Returns the temporal negation of `self`.

        Returns:
            A :class:`TBool` with the temporal negation of `self`.

        MEOS Function:
            tnot_tbool
*/
    default TBool invert(){
       return this.temporal_not();
    }
	
}