package types.basic.ttext;

import functions.functions;
import jnr.ffi.Pointer;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.*;


/**
 * Class that represents the MobilityDB type TText used for {@link TTextInst}, {@link TTextSeq} and {@link TTextSeqSet}
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public interface TText {
    String customType = "String";
    Pointer getTextInner();
    String getCustomType();
    TemporalType getTemporalType();


    /* ------------------------- Constructors ---------------------------------- */


    /**
     * Create a temporal string from a string value and the time frame of
     *         another temporal object.
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_from_base_temp</li>
     *
     * @param value String value.
     * @param base Temporal object to use as time frame.
     * @return A new {@link TText} object.
     */
    static TText from_base_temporal(String value, Temporal base){
        return (TText) Factory.create_temporal(functions.ttext_from_base_temp(functions.cstring2text(value), base.getInner()),customType,base.getTemporalType());
    }

    /**
     * Create a temporal string from a string value and a time object.
     *
     *  <p>
     *         MEOS Functions:
     *         <ul>
     *             <li>ttextinst_make</li>
     *             <li>ttextseq_from_base_timestampset</li>
     *             <li>ttextseq_from_base_period</li>
     *             <li>ttextseqset_from_base_periodset</li>
     *         </ul>
     *
     * @param value String value.
     * @param base Time object to use as temporal dimension.
     * @return A new temporal boolean.
     */
    static Temporal from_base_time(String value, Time base){
        if (base instanceof TimestampSet){
            return new TTextSeq(functions.ttextseq_from_base_timestampset(functions.cstring2text(value),((TimestampSet) base).get_inner()));

        } else if (base instanceof Period) {
            return new TTextSeq(functions.ttextseq_from_base_period(functions.cstring2text(value),((Period) base).get_inner()));

        } else if (base instanceof PeriodSet) {
            return new TTextSeqSet(functions.ttextseqset_from_base_periodset(functions.cstring2text(value),((PeriodSet) base).get_inner()));
        }

        return null;
    }


    /* ------------------------- Output ---------------------------------- */


    /**
     * Returns the string representation of "this".
     * <p>
     *         MEOS Function:
     *             <li>ttext_out</li>
     * @return Returns the string representation of "this"
     */
    default String to_string(){
        return functions.ttext_out(getTextInner());
    }



    /**
     * Returns the Well-Known Text representation of "this".
     * <p>
     *         MEOS Function:
     *             <li>ttext_out</li>
     * @return A string with the Well-Known Text representation of "this".
     */
    default String as_wkt(){
        return functions.ttext_out(getTextInner());
    }


    /* ------------------------- Accessors ---------------------------------- */


    /**
     * Returns the minimum value of the "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_min_value</li>
     * @return A {@link String} with the minimum value.
     */
    default String min_value(){
        return functions.text2cstring(functions.ttext_min_value(getTextInner()));
    }

    /**
     * Returns the maximum value of the "this".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_max_value</li>
     * @return A {@link String} with the maximum value.
     */
    default String max_value(){
        return functions.text2cstring(functions.ttext_max_value(getTextInner()));
    }


    /**
     * Returns the starting value of "this".
     *
     *         MEOS Function:
     *            <li>ttext_start_value</li>
     * @return Returns the starting value of "this".
     */
    default String start_value(){
        return functions.text2cstring(functions.ttext_start_value(getTextInner()));
    }

    /**
     * Returns the ending value of "this".
     *
     *         MEOS Function:
     *             <li>ttext_end_value</li>
     * @return Returns the ending value of "this".
     */
    default String end_value(){
        return functions.text2cstring(functions.ttext_end_value(getTextInner()));
    }


    /* ------------------------- Ever and Always Comparisons ------------------- */


    /**
     * Returns whether the values of "this" are always equal to "value".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always equal to "value",
     * 	 *             "False" otherwise.
     */
    default boolean always_equal(String value){
        return functions.ttext_always_eq(getTextInner(),functions.cstring2text(value));
    }

    /**
     * Returns whether the values of "this" are always not equal to "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always not equal to "value",
     * 	 *             "False" otherwise.
     */
    default boolean always_not_equal(String value){
        return ! (functions.ttext_ever_eq(getTextInner(),functions.cstring2text(value)));
    }


    /**
     * Returns whether the values of "this" are always less than "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_lt</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always less than "value",
     * 	 *             "False" otherwise.
     */
    default boolean always_less(String value){
        return functions.ttext_always_lt(getTextInner(),functions.cstring2text(value));
    }


    /**
     * Returns whether the values of "this" are always less than or equal to
     *         "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always less than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean always_less_or_equal(String value){
        return functions.ttext_always_le(getTextInner(),functions.cstring2text(value));
    }

    /**
     * Returns whether the values of "this" are always greater than or equal
     *         to "value".
     *
     *   <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_lt</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always greater than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean always_greater_or_equal(String value){
        return ! (functions.ttext_ever_lt(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of "this" are always greater than "value".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are always greater than "value",
     * 	 *            " `False`" otherwise.
     */
    default boolean always_greater(String value){
        return ! (functions.ttext_ever_le(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of `self` are ever less than "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_lt</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever less than "value",
     * 	 *             "False" otherwise.
     */
    default boolean ever_less(String value){
        return functions.ttext_ever_lt(getTextInner(),functions.cstring2text(value));
    }


    /**
     * Returns whether the values of "this" are ever less than or equal to
     *         "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever less than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean ever_less_or_equal(String value){
        return functions.ttext_ever_le(getTextInner(),functions.cstring2text(value));
    }


    /**
     * Returns whether the values of "this" are ever equal to "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever equal to "value", "False"
     * 	 *             otherwise.
     */
    default boolean ever_equal(String value){
        return functions.ttext_ever_eq(getTextInner(),functions.cstring2text(value));
    }

    /**
     * Returns whether the values of "this" are ever not equal to "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever not equal to "value",
     * 	 *             "False" otherwise.
     */
    default boolean ever_not_equal(String value){
        return ! (functions.ttext_always_eq(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of "this" are ever greater than or equal to
     *         "value".
     *
     * <p>
     *         MEOS Functions:
     *             <li>ttext_always_lt</li>
     *
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever greater than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean ever_greater_or_equal(String value){
        return ! (functions.ttext_always_lt(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of "this" are ever greater than "value".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are ever greater than "value",
     * 	 *             "False" otherwise.
     */
    default boolean ever_greater(String value){
        return ! (functions.ttext_always_le(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of "this" are never equal to "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are never equal to "value",
     * 	 *             "False" otherwise.
     */
    default boolean never_equal(String value){
        return ! (functions.ttext_ever_eq(getTextInner(),functions.cstring2text(value)));
    }


    /**
     * Returns whether the values of "this" are never not equal to "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_eq</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are never not equal to "value",
     * 	 *             "False" otherwise.
     */
    default boolean never_not_equal(String value){
        return functions.ttext_always_eq(getTextInner(),functions.cstring2text(value));
    }

    /**
     * Returns whether the values of "this" are never less than "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_lt</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are never less than "value",
     * 	 *             "False" otherwise.
     */
    default boolean never_less(String value){
        return ! (functions.ttext_ever_lt(getTextInner(),functions.cstring2text(value)));
    }


    /**
     * Returns whether the values of "this" are never less than or equal to
     *         "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_ever_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are never less than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean never_less_or_equal(String value){
        return ! (functions.ttext_ever_le(getTextInner(),functions.cstring2text(value)));
    }

    /**
     * Returns whether the values of "this" are never greater than or equal to
     *         "value".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_lt</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this "are never greater than or equal to
     * 	 *             "value", "False" otherwise.
     */
    default boolean never_greater_or_equal(String value){
        return functions.ttext_always_lt(getTextInner(),functions.cstring2text(value));
    }

    /**
     * Returns whether the values of "this" are never greater than "value".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>ttext_always_le</li>
     * @param value {@link String} to compare.
     * @return "True" if the values of "this" are never greater than "value",
     * 	 *             "False" otherwise.
     */
    default boolean never_greater(String value){
        return functions.ttext_always_le(getTextInner(),functions.cstring2text(value));
    }




    /* ------------------------- Temporal Comparisons -------------------------- */

    /**
     * Returns the temporal equality relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>teq_ttext_text</li>
     *             <li>teq_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_equal_string(String other){
        if ((other instanceof String)){
            return Factory.create_temporal(functions.teq_ttext_text(getTextInner(),functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }




    /**
     * Returns the temporal not equal relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tne_ttext_text</li>
     *             <li>tne_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_not_equal_string(String other){
        if ((other instanceof String)){
            return Factory.create_temporal(functions.tne_ttext_text(getTextInner(), functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }



    /**
     * Returns the temporal less than relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tlt_ttext_text</li>
     *             <li>tlt_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_less_string(String other){
        if ((other instanceof String)){
            return Factory.create_temporal(functions.tlt_ttext_text(getTextInner(),functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }




    /**
     *  Returns the temporal less or equal relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tle_ttext_text</li>
     *             <li>tle_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_less_or_equal_string(String other){
        if ((other instanceof String)){
            return Factory.create_temporal(functions.tle_ttext_text(getTextInner(),functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }




    /**
     *  Returns the temporal greater or equal relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tge_ttext_text</li>
     *             <li>tge_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_greater_or_equal_string(String other){
        if ((other instanceof String)){
            return Factory.create_temporal(functions.tge_ttext_text(getTextInner(),functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }



    /**
     *  Returns the temporal greater than relation between "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>tgt_ttext_text</li>
     *             <li>tgt_temporal_temporal</li>
     * @param other An {@link String} or temporal object to
     * 	 *             compare to `self`.
     * @return A {@link Temporal} with the result of the temporal equality relation.
     */
    default Temporal temporal_greater_string(String other){
        if ((other instanceof String) ){
            return Factory.create_temporal(functions.tgt_ttext_text(getTextInner(),functions.cstring2text(other)), getCustomType(),getTemporalType());
        }
        else{
            throw new UnsupportedOperationException("Parameter not supported");
        }
    }

    /* ------------------------- Restrictions ---------------------------------- */



    /* ------------------------- Text Operations ------------------------------ */

}
