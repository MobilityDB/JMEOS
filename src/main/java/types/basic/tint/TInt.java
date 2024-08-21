package types.basic.tint;
import functions.functions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.annotations.In;
import types.basic.tfloat.TFloat;
import types.basic.tnumber.TNumber;
import types.collections.number.IntSpan;
import types.collections.number.IntSpanSet;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.Time;
import types.collections.time.tstzspanset;
import types.temporal.*;
import utils.ConversionUtils;
import types.collections.number.IntSet;
import java.time.LocalDateTime;


/**
 * Class that represents the MobilityDB type TInt used for {@link TIntInst}, {@link TIntSeq} and {@link TIntSeqSet}
 *
 * @author ARIJIT SAMAL
 */
public interface TInt extends TNumber {
	String customType = "Integer";
	Pointer getNumberInner();
	String getCustomType();
	TemporalType getTemporalType();



	/* ------------------------- Constructors ---------------------------------- */


	/**
	 * Returns a new temporal int with the value "value" and the temporal
	 *         frame of "base".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_from_base_temp</li>
	 *
	 * @param value Value of the temporal float.
	 * @param base Temporal object to get the temporal frame from.
	 * @param interp Interpolation of the temporal int.
	 * @return A new {@link Float} object.
	 */
	default TInt from_base_temporal(int value, Temporal base, TInterpolation interp){
		return (TInt) Factory.create_temporal(functions.tint_from_base_temp(value,base.getInner()),getCustomType(),getTemporalType());
	}

	/**
	 * Returns a new temporal int with the value "value" and the temporal
	 *         frame of "base".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>tintinst_make</li>
	 *             <li>tintseq_from_base_tstzspanset</li>
	 *             <li>tintseq_from_base_time</li>
	 *             <li>tintseqset_from_base_time</li>
	 *         </ul>
	 * @param value Value of the temporal float.
	 * @param base Time object to get the temporal frame from.
	 * @param interpolation Interpolation of the temporal int.
	 * @return A new temporal float.
	 */
	static TInt from_base_time(int value, Object base, TInterpolation interpolation){
		if (base instanceof LocalDateTime){
			return new TIntInst(functions.tintinst_make(value, ConversionUtils.datetimeToTimestampTz((LocalDateTime) base)));
		}
		if (base instanceof tstzspanset) {
			return new TIntSeq(functions.tintseqset_from_base_tstzspanset(value, ((tstzspanset) base).get_inner()));
		} else if (base instanceof tstzset) {
			return new TIntSeq(functions.tintseq_from_base_tstzset(value, ((tstzset) base).get_inner()));
		} else if (base instanceof tstzspan) {
			return new TIntSeqSet(functions.tintseq_from_base_tstzspan(value, ((tstzspan) base).get_inner()));
		}
		throw new UnsupportedOperationException("Operation not supported with type " + base.getClass());
	}

/*
        Returns a temporal object from a MF-JSON string.

        Args:
            mfjson: The MF-JSON string.

        Returns:
            A temporal object from a MF-JSON string.

        MEOS Functions:
            tint_from_mfjson
*/

	default TInt from_mfjson(String mfjson){
		Pointer result= functions.tint_from_mfjson(mfjson);
		return (TInt) Factory.create_temporal(result, getCustomType(), getTemporalType());
	}


    /* ------------------------- Output ---------------------------------------- */


	/**
	 * Returns a string representation of "this".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tint_out</li>
	 * @return A string representation of "this".
	 */
	default String to_string(){
		return functions.tint_out(getNumberInner());
	}

	/**
	 * Returns a string representation of "this".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tint_out</li>
	 * @return A string representation of "this".
	 */
	default String as_wkt(){
		return functions.tint_out(getNumberInner());
	}


	/* ------------------------- Conversions ---------------------------------- */


	/**
	 * Returns a new temporal float with the values of "this" floored.
	 *         This operation can only be performed when the interpolation is stepwise
	 *         or discrete.
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_to_tfloat</li>
	 *
	 *         Raises:
	 *             ValueError: If the interpolation is linear.
	 * @return A new temporal float.
	 */
	default TFloat to_tfloat(){
		return (TFloat) Factory.create_temporal(functions.tint_to_tfloat(getNumberInner()),"Float",getTemporalType());
	}




	/**
	 * Returns value span of "this".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_to_span</li>
	 *
	 * @return An {@link IntSpan} with the value span of "this".
	 */
	default IntSpan to_intspan(){
		return new IntSpan(functions.tnumber_to_span(getNumberInner()));
	}


    /* ------------------------- Accessors ------------------------------------- */


	/**
	 * Returns the value span of "this".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_to_span</li>
	 * @return An {@link IntSpan} with the value span of `self`.
	 */
	default IntSpan value_span(){
		return to_intspan();
	}

	/**
	 * Returns the value spans of "this" taking into account gaps.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_valuespans</li>
	 * @return A {@link IntSpanSet} with the value spans of "this".
	 */
	default IntSpanSet value_spans(){
		return new IntSpanSet(functions.tnumber_valuespans(getNumberInner()));
	}

	/**
	 * Returns the start value of "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_start_value</li>
	 * @return A {@link Integer} with the start value.
	 */
	default int start_value(){
		return functions.tint_start_value(getNumberInner());
	}

	/**
	 * Returns the end value of "this".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_end_value</li>
	 * @return A {@link Integer} with the end value.
	 */
	default int end_value(){
		return functions.tint_end_value(getNumberInner());
	}

/*
        Returns the set of values of `self`.

        Returns:
            A :class:`set` with the values of `self`.

        MEOS Functions:
            tint_values
*/
	default IntSet value_set(){
		// Create a JNR-FFI runtime instance
		Runtime runtime = Runtime.getSystemRuntime();
		// Allocate memory for an integer (4 bytes) but do not set a value
		Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
		Pointer res= functions.tint_values(this.getNumberInner(), intPointer);
		int count= intPointer.getInt(Integer.BYTES);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(int i=0;i<count;i++){
			int num = res.getInt((long) i *Integer.BYTES);
			sb.append(num);
			if(i<count-1){
				sb.append(",");
			}
		}
		sb.append("}");
        return new IntSet(sb.toString());
	}

	/**
	 * Returns the minimum value of the "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_min_value</li>
	 * @return A {@link Integer} with the minimum value.
	 */
	default int min_value(){
		return functions.tint_min_value(getNumberInner());
	}

	/**
	 * Returns the maximum value of the "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_max_value</li>
	 * @return A {@link Integer} with the maximum value.
	 */
	default int max_value(){
		return functions.tint_max_value(getNumberInner());
	}

/*
        Returns the value that `self` takes at a certain moment.

        Args:
            timestamp: The moment to get the value.

        Returns:
            An :class:`int` with the value of `self` at `timestamp`.

        MEOS Functions:
            tint_value_at_timestamp
*/

	default int value_at_timestamp(LocalDateTime timestamp){
		// Create a JNR-FFI runtime instance
		Runtime runtime = Runtime.getSystemRuntime();
		// Allocate memory for an integer (4 bytes) but do not set a value
		Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
		boolean x= functions.tint_value_at_timestamptz(this.getNumberInner(), ConversionUtils.datetimeToTimestampTz(timestamp), true, intPointer);
		int num= intPointer.getInt(Integer.BYTES);
		return num;
	}

    /* ------------------------- Ever and Always Comparisons ------------------- */


	/**
	 * Returns whether the values of "this" are always equal to "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_equal(int value){
		return functions.always_eq_tint_int(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are always not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_not_equal(int value){
		return (functions.ever_ne_tint_int(getNumberInner(),value)) > 0;
	}


	/**
	 * Returns whether the values of "this" are always less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_lt</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_less(int value){
		return functions.always_lt_tint_int(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are always less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean always_less_or_equal(int value){
		return functions.always_le_tint_int(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are always greater than or equal
	 *         to "value".
	 *
	 *   <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_lt</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean always_greater_or_equal(int value){
		return (functions.ever_lt_tint_int(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are always greater than "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are always greater than "value",
	 * 	 *            " `False`" otherwise.
	 */
	default boolean always_greater(int value){
		return (functions.always_gt_tint_int(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of `self` are ever less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_lt</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_less(int value){
		return functions.ever_lt_tint_int(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are ever less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean ever_less_or_equal(int value){
		return functions.ever_le_tint_int(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are ever equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever equal to "value", "False"
	 * 	 *             otherwise.
	 */
	default boolean ever_equal(int value){
		return functions.ever_eq_tint_int(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_not_equal(int value){
		return (functions.ever_ne_tint_int(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever greater than or equal to
	 *         "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_lt</li>
	 *
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean ever_greater_or_equal(int value){
		return (functions.ever_ge_tint_int(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever greater than "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are ever greater than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_greater(int value){
		return (functions.ever_gt_tint_int(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are never equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are never equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_equal(int value){
		return ! (this.ever_equal(value));
	}


	/**
	 * Returns whether the values of "this" are never not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_eq</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are never not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_not_equal(int value){
		return ! (this.ever_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_lt</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are never less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_less(int value){
		return ! (this.ever_less(value));
	}


	/**
	 * Returns whether the values of "this" are never less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_ever_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are never less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean never_less_or_equal(int value){
		return ! (this.ever_less_or_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never greater than or equal to
	 *         "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_lt</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this "are never greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean never_greater_or_equal(int value){
		return !(this.ever_greater_or_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never greater than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tint_always_le</li>
	 * @param value {@link Integer} to compare.
	 * @return "True" if the values of "this" are never greater than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_greater(int value){
		return !(this.ever_greater(value));
	}


    /* ------------------------- Temporal Comparisons -------------------------- */


	/**
	 * Returns the temporal equality relation between "this" and "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>teq_tint_int</li>
	 *             <li>teq_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_equal(Object other){
		if ((other instanceof Integer)){
			return Factory.create_temporal(functions.teq_tint_int(getNumberInner(), ((Integer) other)), getCustomType(),getTemporalType());
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
	 *             <li>tne_tint_int</li>
	 *             <li>tne_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_not_equal(Integer other){
		if ((other != null)){
			return Factory.create_temporal(functions.tne_tint_int(getNumberInner(), other), getCustomType(),getTemporalType());
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
	 *             <li>tlt_tint_int</li>
	 *             <li>tlt_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_less(Integer other){
		if ((other != null)){
			return Factory.create_temporal(functions.tlt_tint_int(getNumberInner(), other), getCustomType(),getTemporalType());
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
	 *             <li>tle_tint_int</li>
	 *             <li>tle_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_less_or_equal(Integer other){
		if ((other != null)){
			return Factory.create_temporal(functions.tle_tint_int(getNumberInner(), other), getCustomType(),getTemporalType());
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
	 *             <li>tge_tint_int</li>
	 *             <li>tge_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_greater_or_equal(Integer other){
		if ((other != null)){
			return Factory.create_temporal(functions.tge_tint_int(getNumberInner(), other), getCustomType(),getTemporalType());
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
	 *             <li>tgt_tint_int</li>
	 *             <li>tgt_temporal_temporal</li>
	 * @param other An {@link Integer} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_greater(Integer other){
		if ((other instanceof Integer)){
			return Factory.create_temporal(functions.tgt_tint_int(getNumberInner(), other), getCustomType(),getTemporalType());
		}
		else{
			throw new UnsupportedOperationException("Parameter not supported");
		}
	}


    /* ------------------------- Restrictions ---------------------------------- */

}
