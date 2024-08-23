package types.basic.tfloat;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;
import types.basic.tint.TInt;
import types.basic.tnumber.TNumber;
import types.collections.number.FloatSet;
import types.collections.number.FloatSpan;
import types.collections.number.FloatSpanSet;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.Time;
import types.collections.time.tstzspanset;
import types.temporal.*;
import functions.functions;
import utils.ConversionUtils;

import java.time.LocalDateTime;


/**
 * Class that represents the MobilityDB type TFloat used for {@link TFloatInst}, {@link TFloatSeq} and {@link TFloatSeqSet}
 *
 * @author ARIJIT SAMAL
 */
public interface TFloat extends TNumber {
	String customType = "Float";
	Pointer getNumberInner();
	String getCustomType();
	TemporalType getTemporalType();

	
	/* ------------------------- Constructors ---------------------------------- */


	/**
	 * Returns a new temporal float with the value "value" and the temporal
	 *         frame of "base".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_from_base_temp</li>
	 *
	 * @param value Value of the temporal float.
	 * @param base Temporal object to get the temporal frame from.
	 * @param interp Interpolation of the temporal float.
	 * @return A new {@link Float} object.
	 */
	default TFloat from_base_temporal(float value, Temporal base, TInterpolation interp){
		return (TFloat) Factory.create_temporal(functions.tfloat_from_base_temp(value,base.getInner()),getCustomType(),getTemporalType());
	}

	/**
	 * Returns a new temporal float with the value "value" and the temporal
	 *         frame of "base".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>tfloatinst_make</li>
	 *             <li>tfloatseq_from_base_timestampset</li>
	 *             <li>tfloatseq_from_base_time</li>
	 *             <li>tfloatseqset_from_base_time</li>
	 *         </ul>
	 * @param value Value of the temporal float.
	 * @param base Time object to get the temporal frame from.
	 * @param interpolation Interpolation of the temporal float.
	 * @return A new temporal float.
	 */
	static TFloat from_base_time(float value, Time base, TInterpolation interpolation){
		if (base instanceof tstzspanset) {
			return new TFloatSeq(functions.tfloatseqset_from_base_tstzspanset((double) value, ((tstzspanset) base).get_inner(), interpolation.getValue()));
		} else if (base instanceof tstzset) {
			return new TFloatSeq(functions.tfloatseq_from_base_tstzset(value, ((tstzset) base).get_inner()));
		} else if (base instanceof tstzspan) {
			return new TFloatSeqSet(functions.tfloatseq_from_base_tstzspan(value, ((tstzspan) base).get_inner(), interpolation.getValue()));
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
            tfloat_from_mfjson
*/
	default TFloat from_mfjson(String mfjson) {
			Pointer resPointer= functions.tfloat_from_mfjson(mfjson);
			return (TFloat) Factory.create_temporal(resPointer, getCustomType(), getTemporalType());
	}
		


    /* ------------------------- Output ---------------------------------------- */


	/**
	 * Returns a string representation of "this".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tfloat_out</li>
	 * @param max_decimals
	 * @return A string representation of "this".
	 */
	default String to_string(int max_decimals){
		return functions.tfloat_out(getNumberInner(), max_decimals);
	}

	/**
	 * Returns a string representation of "this".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tfloat_out</li>
	 * @param max_decimals
	 * @return A string representation of "this".
	 */
	default String as_wkt(int max_decimals){
		return functions.tfloat_out(getNumberInner(),max_decimals);
	}

	/* ------------------------- Conversions ---------------------------------- */


	/**
	  Returns a new temporal integer with the values of "this" floored.
	          This operation can only be performed when the interpolation is stepwise
	          or discrete.
	  <p>

	          MEOS Functions:
	              <li>tfloat_to_tint</li>

	          Raises:
	              ValueError: If the interpolation is linear.
	  @return A new temporal integer.
	 */
	default TInt to_tint(){
		return (TInt) Factory.create_temporal(functions.tfloat_to_tint(getNumberInner()),"Integer",getTemporalType());
	}




	/**
	 * Returns value span of "this".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_to_span</li>
	 *
	 * @return An {@link FloatSpan} with the value span of "this".
	 */
	default FloatSpan to_floatrange(){
		return new FloatSpan(functions.tnumber_to_span(getNumberInner()));
	}


    /* ------------------------- Accessors ------------------------------------- */


	/**
	 * Returns the value span of "this".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_to_span</li>
	 * @return An {@link FloatSpan} with the value span of `self`.
	 */
	default FloatSpan value_span(){
		return to_floatrange();
	}

	/**
	 * Returns the value spans of `self` taking into account gaps.
	 *
	 *         Returns:
	 *             A :class:`FloatSpanSet` with the value spans of `self`.
	 *
	 *         MEOS Functions:
	 *             tnumber_valuespans
	 * @return
	 */
	default FloatSpanSet value_spans(){
		return new FloatSpanSet(functions.tnumber_valuespans(getNumberInner()));
	}

	/**
	 * Returns the start value of "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_start_value</li>
	 * @return A {@link Float} with the start value.
	 */
	default float start_value(){
		return (float) functions.tfloat_start_value(getNumberInner());
	}

	/**
	 * Returns the end value of "this".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_end_value</li>
	 * @return A {@link Float} with the end value.
	 */
	default float end_value(){
		return (float) functions.tfloat_end_value(getNumberInner());
	}
/**
        Returns the set of values of `self`.
        Note that when the interpolation is linear, the set will contain only
        the waypoints.

        Returns:
            A :class:`set` with the values of `self`.

        MEOS Functions:
            tint_values
*/
	default FloatSet value_set(){
		// Create a JNR-FFI runtime instance
		Runtime runtime = Runtime.getSystemRuntime();
		// Allocate memory for an integer (4 bytes) but do not set a value
		Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
		Pointer resPointer = functions.tfloat_values(this.getNumberInner(), intPointer);
		StringBuilder sb = null;
		sb.append("{");
		int count= intPointer.getInt(Integer.BYTES);
		for (int i=0;i<count;i++){
			double res= resPointer.getDouble((long) i *Double.BYTES);
			sb.append(res);
			if(i<count-1){
				sb.append(", ");
			}
		}
		sb.append("}");
		System.out.println(sb.toString());
		return new FloatSet(sb.toString());
	}

	/**
	 * Returns the minimum value of the "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_min_value</li>
	 * @return A {@link Float} with the minimum value.
	 */
	default float min_value(){
		return (float) functions.tfloat_min_value(getNumberInner());
	}

	/**
	 * Returns the maximum value of the "this".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_max_value</li>
	 * @return A {@link Float} with the maximum value.
	 */
	default float max_value(){
		return (float) functions.tfloat_max_value(getNumberInner());
	}


    /* ------------------------- Ever and Always Comparisons ------------------- */


	/**
	 * Returns whether the values of "this" are always equal to "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_equal(float value){
		return functions.always_eq_tfloat_float(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are always not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_not_equal(float value){
		return (functions.always_ne_tfloat_float(getNumberInner(),value)) > 0;
	}


	/**
	 * Returns whether the values of "this" are always less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_lt</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean always_less(float value){
		return functions.always_lt_tfloat_float(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are always less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean always_less_or_equal(float value){
		return functions.always_le_tfloat_float(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are always greater than or equal
	 *         to "value".
	 *
	 *   <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_lt</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean always_greater_or_equal(float value){
		return (functions.always_ge_tfloat_float(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are always greater than "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are always greater than "value",
	 * 	 *            " `False`" otherwise.
	 */
	default boolean always_greater(float value){
		return (functions.always_gt_tfloat_float(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of `self` are ever less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_lt</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_less(float value){
		return functions.ever_lt_tfloat_float(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are ever less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean ever_less_or_equal(float value){
		return functions.ever_le_tfloat_float(getNumberInner(),value) > 0;
	}


	/**
	 * Returns whether the values of "this" are ever equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever equal to "value", "False"
	 * 	 *             otherwise.
	 */
	default boolean ever_equal(float value){
		return functions.ever_eq_tfloat_float(getNumberInner(),value) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_not_equal(float value){
		return (functions.ever_ne_tfloat_float(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever greater than or equal to
	 *         "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_lt</li>
	 *
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean ever_greater_or_equal(float value){
		return (functions.ever_ge_tfloat_float(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are ever greater than "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are ever greater than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean ever_greater(float value){
		return  (functions.ever_gt_tfloat_float(getNumberInner(),value)) > 0;
	}

	/**
	 * Returns whether the values of "this" are never equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are never equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_equal(float value){
		return ! (this.ever_equal(value));
	}


	/**
	 * Returns whether the values of "this" are never not equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_eq</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are never not equal to "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_not_equal(float value){
		return !(this.ever_not_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never less than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_lt</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are never less than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_less(float value){
		return ! (this.ever_less(value));
	}


	/**
	 * Returns whether the values of "this" are never less than or equal to
	 *         "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_ever_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are never less than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean never_less_or_equal(float value){
		return ! (ever_less_or_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never greater than or equal to
	 *         "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_lt</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this "are never greater than or equal to
	 * 	 *             "value", "False" otherwise.
	 */
	default boolean never_greater_or_equal(float value){
		return ! (this.ever_greater_or_equal(value));
	}

	/**
	 * Returns whether the values of "this" are never greater than "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_always_le</li>
	 * @param value {@link Float} to compare.
	 * @return "True" if the values of "this" are never greater than "value",
	 * 	 *             "False" otherwise.
	 */
	default boolean never_greater(float value){
		return ! (this.ever_greater(value));
	}


    /* ------------------------- Temporal Comparisons -------------------------- */


	/**
	 * Returns the temporal equality relation between "this" and "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>teq_tfloat_float</li>
	 *             <li>teq_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_equal(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.teq_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
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
	 *             <li>tne_tfloat_float</li>
	 *             <li>tne_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_not_equal(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tne_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
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
	 *             <li>tlt_tfloat_float</li>
	 *             <li>tlt_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_less(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tlt_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
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
	 *             <li>tle_tfloat_float</li>
	 *             <li>tle_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_less_or_equal(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tle_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
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
	 *             <li>tge_tfloat_float</li>
	 *             <li>tge_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_greater_or_equal(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tge_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
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
	 *             <li>tgt_tfloat_float</li>
	 *             <li>tgt_temporal_temporal</li>
	 * @param other An {@link Integer}, {@link Float} or temporal object to
	 * 	 *             compare to `self`.
	 * @return A {@link Temporal} with the result of the temporal equality relation.
	 */
	default Temporal temporal_greater(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tgt_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
		}
		else{
			throw new UnsupportedOperationException("Parameter not supported");
		}
	}


    /* ------------------------- Restrictions ---------------------------------- */

/**
        Returns the value that `self` takes at a certain moment.

        Args:
            timestamp: The moment to get the value.

        Returns:
            A class:`float` with the value of `self` at `timestamp`.

        MEOS Functions:
            tfloat_value_at_timestamp
*/
	default float value_at_timestamp(LocalDateTime ts){
		// Create a JNR-FFI runtime instance
		Runtime runtime = Runtime.getSystemRuntime();
		// Allocate memory for an integer (4 bytes) but do not set a value
		Pointer floatPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
		boolean res= functions.tfloat_value_at_timestamptz(this.getNumberInner(), ConversionUtils.datetimeToTimestampTz(ts), true, floatPointer);
		float value= floatPointer.getFloat(Float.BYTES);
		return value;
	}

/**
        Returns the derivative of `self`.

        Returns:
            A new :class:`TFloat` instance.

        MEOS Functions:
            tfloat_derivative
*/
	default TFloat derivative(){
		return (TFloat) Factory.create_temporal(functions.tfloat_derivative(this.getNumberInner()), getCustomType(), getTemporalType());
	}



	/* ------------------------- Transformations ---------------------------------- */


	/**
	 * Returns a copy of "this" converted from radians to degrees.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tfloat_degrees</li>
	 * @param normalize normalize: If True, the result will be normalized to the range [0, 360).
	 * @return A {@link TFloat} instance.
	 */
	default Temporal to_degrees(boolean normalize){
		return Factory.create_temporal(functions.tfloat_degrees(getNumberInner(),normalize), getCustomType(),getTemporalType());

	}


	/**
	 * Returns a copy of this converted from degrees to radians.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tfloat_radians</li>
	 * @return A new {@link TFloat} instance.
	 */
	default Temporal to_radians(){
		return Factory.create_temporal(functions.tfloat_radians(getNumberInner()), getCustomType(),getTemporalType());

	}


	/**
	 * Returns "this" rounded to the given number of decimal digits.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tfloat_round</li>
	 * @param max_decimals Maximum number of decimal digits.
	 * @return A new {@link TFloat} instance.
	 */
	default Temporal round(int max_decimals){
		return Factory.create_temporal(functions.tfloat_round(getNumberInner(),max_decimals), getCustomType(),getTemporalType());
	}
}
