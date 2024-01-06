package types.basic.tfloat;

import jnr.ffi.Pointer;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;
import types.basic.tint.TInt;
import types.basic.tnumber.TNumber;
import types.collections.number.FloatSpan;
import types.collections.number.FloatSpanSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.*;
import functions.functions;


/**
 * Class that represents the MobilityDB type TFloat used for {@link TFloatInst}, {@link TFloatSeq} and {@link TFloatSeqSet}
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
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
		if (base instanceof TimestampSet) {
			return new TFloatSeq(functions.tfloatseq_from_base_timestampset(value, ((TimestampSet) base).get_inner()));
		} else if (base instanceof Period) {
			return new TFloatSeq(functions.tfloatseq_from_base_period(value, ((Period) base).get_inner(), interpolation.getValue()));
		} else if (base instanceof PeriodSet) {
			return new TFloatSeqSet(functions.tfloatseqset_from_base_periodset(value, ((PeriodSet) base).get_inner(), interpolation.getValue()));
		}
		throw new UnsupportedOperationException("Operation not supported with type " + base.getClass());
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
		return functions.tfloat_out(getNumberInner(),15);
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
		return functions.tfloat_always_eq(getNumberInner(),value);
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
		return ! (functions.tfloat_ever_eq(getNumberInner(),value));
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
		return functions.tfloat_always_lt(getNumberInner(),value);
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
		return functions.tfloat_always_le(getNumberInner(),value);
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
		return ! (functions.tfloat_ever_lt(getNumberInner(),value));
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
		return ! (functions.tfloat_ever_le(getNumberInner(),value));
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
		return functions.tfloat_ever_lt(getNumberInner(),value);
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
		return functions.tfloat_ever_le(getNumberInner(),value);
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
		return functions.tfloat_ever_eq(getNumberInner(),value);
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
		return ! (functions.tfloat_always_eq(getNumberInner(),value));
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
		return ! (functions.tfloat_always_lt(getNumberInner(),value));
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
		return ! (functions.tfloat_always_le(getNumberInner(),value));
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
		return ! (functions.tfloat_ever_eq(getNumberInner(),value));
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
		return functions.tfloat_always_eq(getNumberInner(),value);
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
		return ! (functions.tfloat_ever_lt(getNumberInner(),value));
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
		return ! (functions.tfloat_ever_le(getNumberInner(),value));
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
		return functions.tfloat_always_lt(getNumberInner(),value);
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
		return functions.tfloat_always_le(getNumberInner(),value);
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
	default Temporal temporal_equal_number(Number other){
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
	default Temporal temporal_not_equal_number(Number other){
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
	default Temporal temporal_less_number(Number other){
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
	default Temporal temporal_less_or_equal_number(Number other){
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
	default Temporal temporal_greater_or_equal_number(Number other){
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
	default Temporal temporal_greater_number(Number other){
		if ((other instanceof Float) || (other instanceof Integer)){
			return Factory.create_temporal(functions.tgt_tfloat_float(getNumberInner(),(float) other), getCustomType(),getTemporalType());
		}
		else{
			throw new UnsupportedOperationException("Parameter not supported");
		}
	}


    /* ------------------------- Restrictions ---------------------------------- */



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
