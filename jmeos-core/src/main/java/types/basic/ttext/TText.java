package types.basic.ttext;

import functions.functions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.locationtech.jts.io.ParseException;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tpoint.TPoint;
import types.collections.base.Set;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.Time;
import types.collections.time.tstzspanset;
import types.temporal.*;
import utils.ConversionUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Class that represents the MobilityDB type TText used for {@link TTextInst}, {@link TTextSeq} and {@link TTextSeqSet}
 *
 * @author ARIJIT SAMAL
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
     *             <li>ttextseq_from_base_tstzspanset</li>
     *             <li>ttextseq_from_base_tstzset</li>
     *             <li>ttextseqset_from_base_tstzspan</li>
     *         </ul>
     *
     * @param value String value.
     * @param base Time object to use as temporal dimension.
     * @return A new temporal boolean.
     */
    static Temporal from_base_time(String value, Time base){
        if (base instanceof tstzspanset){
            return new TTextSeq(functions.ttextseqset_from_base_tstzspanset(functions.cstring2text(value),((tstzspanset) base).get_inner()));

        } else if (base instanceof tstzset) {
            return new TTextSeq(functions.ttextseq_from_base_tstzset(functions.cstring2text(value),((tstzset) base).get_inner()));

        } else if (base instanceof tstzspan) {
            return new TTextSeqSet(functions.ttextseq_from_base_tstzspan(functions.cstring2text(value),((tstzspan) base).get_inner()));
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
            ttext_from_mfjson
*/
    default TText from_mfjson(String mfjson){
        Pointer result= functions.ttext_from_mfjson(mfjson);
        return (TText) Factory.create_temporal(result, getCustomType(), getTemporalType());
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
     Returns the set of unique values of the temporal string.

     Returns:
     A set of unique values.

     MEOS Functions:
     ttext_values
     */

    default Set<String> value_set(){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
        Pointer resPointer= functions.ttext_values(this.getTextInner(), intPointer);
        StringBuilder sb= null;
        sb.append("{");
        int count= intPointer.getInt(Integer.BYTES);
        for(int i=0; i<count; i++){
            Pointer res= resPointer.getPointer((long) i *Long.BYTES);
            String resString= functions.text2cstring(res);
            sb.append(resString);
            if(i<count-1){
                sb.append(", ");
            }
        }
        sb.append("}");
        return new Set<String>() {
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
                return functions.ttext_in(str);
            }

            @Override
            public String start_element() throws ParseException {
                return functions.text2cstring(functions.ttext_min_value(getTextInner()));
            }

            @Override
            public String end_element() throws ParseException {
                return functions.text2cstring(functions.ttext_max_value(getTextInner()));
            }
        };
    }

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


 /**
        Returns a new temporal string with the values of `self` converted to
        upper case.

        Returns:
            A new temporal string.

        MEOS Functions:
            ttext_upper
*/
    default TText upper(){
        Pointer res= functions.ttext_upper(this.getTextInner());
        return (TText) Factory.create_temporal(res, getCustomType(), getTemporalType());
    }

    /**
            Returns a new temporal string with the values of `self` converted to
            lower case.

            Returns:
                A new temporal string.

            MEOS Functions:
                ttext_lower
    */
    default TText lower(){
        Pointer res= functions.ttext_lower(this.getTextInner());
        return (TText) Factory.create_temporal(res, getCustomType(), getTemporalType());
    }

    /**
        Returns the value that `self` takes at a certain moment.

        Args:
            timestamp: The moment to get the value.

        Returns:
            A string with the value of `self` at `timestamp`.

        MEOS Functions:
            ttext_value_at_timestamp
    */

    default String value_at_timestamp(LocalDateTime ts){
        // Create a JNR-FFI runtime instance
        Runtime runtime = Runtime.getSystemRuntime();
        // Allocate memory for an integer (4 bytes) but do not set a value
        Pointer textPointer = Memory.allocate(Runtime.getRuntime(runtime), 8);
        boolean result= functions.ttext_value_at_timestamptz(this.getTextInner(), ConversionUtils.datetimeToTimestampTz(ts), true, textPointer);
        return functions.text2cstring(textPointer.getPointer(Long.BYTES));
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
        return functions.always_eq_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return (functions.always_ne_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return functions.always_lt_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return functions.always_le_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return (functions.always_ge_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return (functions.always_gt_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return functions.ever_lt_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return functions.ever_le_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return functions.ever_eq_ttext_text(getTextInner(),functions.cstring2text(value)) > 0;
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
        return (functions.ever_ne_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return (functions.ever_ge_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return (functions.ever_gt_ttext_text(getTextInner(),functions.cstring2text(value))) > 0;
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
        return ! (this.ever_equal(value));
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
        return ! (this.never_not_equal(value));
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
        return ! (this.ever_less(value));
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
        return ! (this.ever_less_or_equal(value));
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
        return ! (this.ever_greater_or_equal(value));
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
        return ! (this.ever_greater(value));
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

    /**
        Returns a new temporal string with the values of `self` restricted to
        the time or value `other`.

        Args:
            other: Time or value to restrict to.

        Returns:
            A new temporal string.

        MEOS Functions:
            ttext_at_value, temporal_at_timestamp, temporal_at_tstzset,
            temporal_at_tstzspan, temporal_at_tstzspanset
      */

      private Pointer createPointerFromString(String str) {
            // Get the runtime associated with the current MeosLibrary
            Runtime runtime = Runtime.getSystemRuntime();

            // Allocate memory for the string in native space and copy the string contents
            Pointer pointer = Memory.allocate(Runtime.getRuntime(runtime), str.length()+1);
            pointer.putString(0, str, str.length(), StandardCharsets.UTF_8);

            return pointer;
        }

      default TText at(Object other) throws Exception {
          Pointer res= null;
          if(other instanceof String){
              res= functions.ttext_at_value(this.getTextInner(), createPointerFromString((String) other));
          }
          else if(other instanceof List && ((List<?>) other).getFirst() instanceof String){
              int count=0;
              res=  functions.temporal_at_values(this.getTextInner(), functions.textset_make((Pointer) other, count));
          }
          else{
              throw new Exception("type not supported");
          }
          return (TText) Factory.create_temporal(res, getCustomType(), getTemporalType());
      }

      /**
        Returns a new temporal string with the values of `self` restricted to
        the complement of the time or value `other`.

        Args:
            other: Time or value to restrict to the complement of.

        Returns:
            A new temporal string.

        MEOS Functions:
            ttext_minus_value, temporal_minus_timestamp,
            temporal_minus_tstzset, temporal_minus_tstzspan,
            temporal_minus_tstzspanset
       */

      default TText minus(Object other) throws Exception {
          Pointer res= null;
          if(other instanceof String){
              res= functions.ttext_minus_value(this.getTextInner(), createPointerFromString((String) other));
          }
          else if(other instanceof List && ((List<?>) other).getFirst() instanceof String){
              int count=0;
              res=  functions.temporal_minus_values(this.getTextInner(), functions.textset_make((Pointer) other, count));
          }
          else{
              throw new Exception("type not supported");
          }
          return (TText) Factory.create_temporal(res, getCustomType(), getTemporalType());
      }

    /* ------------------------- Text Operations ------------------------------ */

    /**
        Returns a new temporal string with the values of `self` concatenated
        with the values of `other`.

        Args:
            other: Temporal string or string to concatenate.
            other_before: If `True` the values of `other` are prepended to the
            values of `self`.

        Returns:
            A new temporal string.

        MEOS Functions:
            textcat_ttext_text, textcat_text_ttext, textcat_ttext_ttext

     */
    
    default TText concatenate(Object other, boolean other_before) throws Exception {
        Pointer res= null;
        if(other instanceof String){
            if(!other_before){
                res= functions.textcat_ttext_text(this.getTextInner(), createPointerFromString((String) other));
            }
            else{
                res= functions.textcat_text_ttext(createPointerFromString((String) other), this.getTextInner());
            }
        }
        else if (other instanceof TText){
            if(!other_before){
                res= functions.textcat_ttext_ttext(this.getTextInner(), ((TText) other).getTextInner());
            }
            else{
                res= functions.textcat_ttext_ttext(((TText) other).getTextInner(), this.getTextInner());
            }
        }
        else{
            throw new Exception("format not supported");
        }
        return (TText) Factory.create_temporal(res, getCustomType(), getTemporalType());
    }


/**
        Returns a new temporal string with the values of `self` concatenated
        with the values of `other`.

        Args:
            other: Temporal string or string to concatenate.

        Returns:
            A new temporal string.

        MEOS Functions:
            textcat_ttext_text, textcat_text_ttext, textcat_ttext_ttext
*/
    default TText _add(Object other) throws Exception {
        return this.concatenate(other, false);
    }


/**
        Returns a new temporal string with the values of `other` concatenated
        with the values of `self`.

        Args:
            other: Temporal string or string to concatenate.

        Returns:
            A new temporal string.

        MEOS Functions:
            textcat_ttext_text, textcat_text_ttext, textcat_ttext_ttext
*/
    default TText _radd(Object other) throws Exception {
        return this.concatenate(other, true);
    }
}
