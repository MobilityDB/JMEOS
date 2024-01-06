package types.boxes;

import functions.functions;
import jnr.ffi.Pointer;
import types.basic.tnumber.TNumber;
import types.collections.base.Span;
import types.collections.number.FloatSpan;
import types.collections.number.IntSpan;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import java.time.LocalDateTime;


/**
 * Class for representing numeric temporal boxes. Both numeric and temporal bounds may be inclusive or not.
 *<pre>
 *     ``TBox`` objects can be created with a single argument of type string as in MobilityDB.
 *
 *         >>> TBox('TBOXINT XT([0, 10),[2020-06-01, 2020-06-05])')
 *         >>> TBox('TBOXFLOAT XT([0, 10),[2020-06-01, 2020-06-05])')
 *         >>> TBox('TBOX T([2020-06-01, 2020-06-05])')
 *
 *     Another possibility is to provide the ``xmin``/``xmax`` (of type str or
 *     int/float) and ``tmin``/``tmax`` (of type str or datetime) named parameters,
 *     and optionally indicate whether the bounds are inclusive or exclusive (by
 *     default, lower bounds are inclusive and upper bounds are exclusive):
 *
 *         >>> TBox(xmin=0, xmax=10, tmin='2020-06-01', tmax='2020-06-0')
 *         >>> TBox(xmin=0, xmax=10, tmin='2020-06-01', tmax='2020-06-0', xmax_inc=True, tmax_inc=True)
 *         >>> TBox(xmin='0', xmax='10', tmin=parse('2020-06-01'), tmax=parse('2020-06-0'))
 *</pre>
 *     Note that you can create a TBox with only the numerical or the temporal dimension. In these cases, it will be
 *     equivalent to a {@link Period} (if it only has temporal dimension) or to a
 *     floatrange (if it only has the numeric dimension).
 */
public class TBox implements Box {
	private boolean xmin_inc = true;
	private boolean xmax_inc = false;
	private boolean tmin_inc = true;
	private boolean tmax_inc = false;
	private Pointer _inner = null;
	
	
	/** ------------------------- Constructors ---------------------------------- */
	public TBox() {

	}
	
	
	public TBox(Pointer inner)  {
		this(inner, true, false, true, false);
	}

	public TBox(Pointer inner, boolean xmin_inc, boolean xmax_inc, boolean tmax_inc, boolean
			tmin_inc) {
		this._inner = inner;
		this.xmin_inc = xmin_inc;
		this.xmax_inc = xmax_inc;
		this.tmin_inc = tmin_inc;
		this.tmax_inc = tmax_inc;
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBox value
	 */
	public TBox(String value){
		this._inner = functions.tbox_in(value);
	}


	/**
	 * Constructor with number parameters
	 * @param xmin X minimal coordinates
	 * @param xmax X maximal coordinates
	 */
	public TBox(Number xmin, Number xmax){
		this(xmin,xmax,null,null,true,false,true,false);
	}

	/**
	 * Constructor with number and temporal parameters
	 * @param xmin X minimal coordinates
	 * @param xmax X maximal coordinates
	 * @param tmin temporal minimal value
	 * @param tmax temporal maximal value
	 */
	public TBox(Number xmin, Number xmax, LocalDateTime tmin, LocalDateTime tmax){
		this(xmin,xmax,tmin,tmax,true,false,true,false);
	}



	/**
	 * Constructor with number, temporal and inclusions parameters
	 * @param xmin X minimal coordinates
	 * @param xmax X maximal coordinates
	 * @param tmin temporal minimal value
	 * @param tmax temporal maximal value
	 * @param xmin_inc x minimal inclusion
	 * @param xmax_inc x maximal inclusion
	 * @param tmin_inc temporal minimal inclusion
	 * @param tmax_inc temporal maxmimal inclusion
	 */
	public TBox(Number xmin, Number xmax, LocalDateTime tmin, LocalDateTime tmax, boolean xmin_inc, boolean xmax_inc, boolean tmin_inc, boolean tmax_inc){
		Period p = null;
		Span span = null;
		if(xmin instanceof Integer && xmax instanceof Integer){
			 span = new IntSpan(xmin.intValue(),xmax.intValue(),xmin_inc,xmax_inc);
		} else if (xmin instanceof Float && xmax instanceof Float) {
			 span = new FloatSpan(xmin.floatValue(),xmax.floatValue(),xmin_inc,xmax_inc);
		}
		if(tmin != null && tmax != null){
			p = new Period(tmin,tmax,tmin_inc,tmax_inc);
		}
        assert span != null;
        assert p != null;
        this._inner = functions.tbox_make(span.get_inner(),p.get_inner());
	}


	/**
	 * Returns a copy of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>tbox_copy</li>
	 * @return a new TBox instance
	 */
	public TBox copy(){
		return new TBox(functions.tbox_copy(this._inner));
	}


	/**
	 *  Returns a "TBox" from its WKB representation in hex-encoded ASCII.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_from_hexwkb</li>
	 * @param hexwkb WKB representation in hex-encoded ASCII
	 * @return a new TBox instance
	 */
	public static TBox from_hexwkb(String hexwkb) {
		return new TBox(functions.tbox_from_hexwkb(hexwkb));
	}



	public static TBox from_value_number(Number value)  {
		TBox tbox = null;
		if(value instanceof Integer){
			tbox = new TBox(functions.int_to_tbox((int)value));
		}
		else if (value instanceof Float){
			tbox = new TBox(functions.float_to_tbox((float)value));
		}
		return tbox;
	}


	/**
	 * Returns a "TBox" from a numeric value or span. The created "TBox" will
	 *         only have a numerical dimension.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>int_to_tbox</li>
	 *             <li>float_to_tbox</li>
	 *             <li>span_to_tbox</li>
	 *             <li>span_to_tbox</li>
	 * @param span value to be canverted into a TBox
	 * @return A new {@link TBox} instance
	 */
	public static TBox from_value_span(Span span) {
		TBox tbox = null;
		if(span instanceof IntSpan){
			tbox = new TBox(functions.numspan_to_tbox(span.get_inner()));
		}
		else if (span instanceof FloatSpan){
			tbox = new TBox(functions.numspan_to_tbox(span.get_inner()));
		}
		return tbox;
	}




	/**
	 * Returns a "TBox" from a {@link Time} class. The created "TBox"
	 *         will only have a temporal dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *         	<ul>
	 *             <li>timestamp_to_tbox</li>
	 *             <li>timestampset_to_tbox</li>
	 *             <li>period_to_tbox</li>
	 *             <li>periodset_to_tbox</li>
	 * 			</ul>
	 *
	 * @param time value to be canverted into a TBox
	 * @return a {@link TBox} instance
	 * @throws Exception
	 */
	public static TBox from_time(Time time) throws Exception {
		TBox tbox = null;
		if (time instanceof Period){
			tbox = new TBox(functions.period_to_tbox(((Period) time).get_inner()));
		}
		else if (time instanceof PeriodSet){
			tbox = new TBox(functions.periodset_to_tbox(((PeriodSet) time).get_inner()));
		}
		else if (time instanceof TimestampSet){
			tbox = new TBox(functions.timestampset_to_tbox(((TimestampSet) time).get_inner()));
		}
		else {
			throw new Exception("Operation not supported with this type.");
		}
		return tbox;
	}


	/**
	 * Returns a "TBox" from a numerical and a temporal object.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>int_timestamp_to_tbox</li>
	 *             <li>int_period_to_tbox</li>
	 *             <li>float_timestamp_to_tbox</li>
	 *             <li>float_period_to_tbox</li>
	 *             <li>span_timestamp_to_tbox</li>
	 *             <li>span_period_to_tbox</li>
	 * @param value numerical span of the TBox
	 * @param time temporal span of the TBox
	 * @return A new {@link TBox} instance
	 */
	public static TBox from_value_time(Object value, Period time){
		TBox tbox = null;
		if (value instanceof Integer) {
			if (time instanceof Period) {
				tbox = new TBox(functions.int_period_to_tbox(((Integer) value).intValue(), time.get_inner()));
			}
		} else if (value instanceof Float) {
			if (time instanceof Period) {
				tbox = new TBox(functions.float_period_to_tbox(((Float) value).floatValue(), time.get_inner()));
			}
		} else if (value instanceof IntSpan) {
			if (time instanceof Period) {
				tbox = new TBox(functions.span_period_to_tbox(((IntSpan) value).get_inner(), time.get_inner()));
			}
		} else if (value instanceof FloatSpan) {
			if (time instanceof Period) {
				tbox = new TBox(functions.span_period_to_tbox(((FloatSpan) value).get_inner(), time.get_inner()));
			}
		}
		return tbox;
	}


	/**
	 * Returns a "TBox" from a {@link TNumber} object.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tnumber_to_tbox</li>
	 * @param temporal temporal number to be canverted into a TBox
	 * @return A new {@link TBox} instance
	 */
	public static TBox from_tnumber(TNumber temporal){
		return new TBox(functions.tnumber_to_tbox(temporal.getNumberInner()));
	}


    /* ------------------------- Output ---------------------------------------- */


	/**
	 * Returns a string representation of "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_out</li>
	 * @return a String instance
	 */
	public String toString(){
		return this.toString(15);
	}

	/**
	 * Returns a string representation of "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_out</li>
	 * @param max_decimals number of maximal decimals
	 * @return a String instance
	 */
	public String toString(int max_decimals){
		return functions.tbox_out(this._inner,max_decimals);
	}


    /* ------------------------- Conversions ---------------------------------- */


	/**
	 * Returns the numeric span of "this".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_to_floatspan</li>
	 * @return A new {@link FloatSpan} instance
	 */
	public FloatSpan to_floatspan(){
		return new FloatSpan(functions.tbox_to_floatspan(this._inner));
	}


	/**
	 * Returns the temporal span of "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_to_period</li>
	 * @return a {@link Period} instance
	 */
	public Period to_period(){
		functions.meos_initialize("UTC");
		return new Period(functions.tbox_to_period(this._inner));
	}

    /* ------------------------- Accessors ------------------------------------- */

	/**
	 * Returns whether "this" has a numeric dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_hasx</li>
	 *
	 * @return True if "this" has a numeric dimension, False otherwise
	 */
	public boolean has_x(){
		return functions.tbox_hasx(this._inner);
	}

	/**
	 * Returns whether "this" has a temporal dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_hast</li>
	 * @return True if "this" has a temporal dimension, False otherwise
	 */
	public boolean has_t(){
		return functions.tbox_hast(this._inner);
	}


    /* ------------------------- Transformation -------------------------------- */


	/**
	 * Returns the result of expanding "this" with the "other". Depending on the type of "other", the expansion
	 *         will be of the numeric dimension ({@link Float}) or both
	 *         ({@link TBox}).
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_copy</li>
	 *             <li>tbox_expand</li>
	 *             <li>tbox_expand_value</li>
	 *             <li>tbox_expand_time</li>
	 *
	 * @param obj object used to expand "this"
	 * @return a {@link TBox} instance
	 */
	public TBox expand(Object obj)   {
		Pointer result = null;
		if(obj instanceof TBox){
			result = functions.tbox_copy(this._inner);
			functions.tbox_expand(((TBox) obj).get_inner(),result);
		}
		else if(obj instanceof Integer || obj instanceof Float){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		return new TBox(result);
	}


	/**
	 * Returns "this" rounded to the given number of decimal digits.
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_round</li>
	 *
	 * @return a {@link TBox instance}
	 */
	public TBox round()   {
		return this.round(0);
	}

	/**
	 * Returns "this" rounded to the given number of decimal digits.
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_round</li>
	 *
	 * @param maxdd maximum number of decimal digits
	 * @return a {@link TBox instance}
	 */
	public TBox round(int maxdd)  {
		Pointer new_inner = functions.tbox_copy(this._inner);
		functions.tbox_round(new_inner,maxdd);
		return new TBox(new_inner);
	}



    /* ------------------------- Topological Operations ------------------------ */

	/**
	 * Returns whether "this" is adjacent to "other". That is, they share only the temporal or numerical bound
	 *         and only one of them contains it.
	 *	<pre>
	 *         Examples:
	 *             >>> TBox('TBoxInt XT([0, 1], [2012-01-01, 2012-01-02))').is_adjacent(TBox('TBoxInt XT([0, 1], [2012-01-02, 2012-01-03])'))
	 *             >>> True
	 *             >>> TBox('TBoxInt XT([0, 1], [2012-01-01, 2012-01-02])').is_adjacent(TBox('TBoxInt XT([0, 1], [2012-01-02, 2012-01-03])'))
	 *             >>> False  # Both contain bound
	 *             >>> TBox('TBoxInt XT([0, 1), [2012-01-01, 2012-01-02))').is_adjacent(TBox('TBoxInt XT([1, 2], [2012-01-02, 2012-01-03])')
	 *             >>> False  # Adjacent in both bounds
	 *
	 * </pre>
	 * <p>
	 *         MEOS Functions:
	 *             <li>adjacent_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other object to compare with
	 * @return true if adjacent, false otherwise
	 */
	public boolean is_adjacent_tbox(Object other) {
		boolean result = false;
		if (other instanceof TBox) {
			result = functions.adjacent_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if (other instanceof TNumber){
			result = functions.adjacent_tbox_tbox(this._inner,functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		} else if (other instanceof FloatSpan) {
			result = functions.adjacent_span_span(this._inner,((FloatSpan) other).get_inner());
		}


		return result;
	}


	/**
	 * Returns whether "this" is contained in "other".
	 * <pre>
	 *         Examples:
	 *             >>> TBox('TBoxInt XT([1, 2], [2012-01-02, 2012-01-03])').is_contained_in(TBox('TBoxInt XT([1, 4], [2012-01-01, 2012-01-04])'))
	 *             >>> True
	 *             >>> TBox('TBoxFloat XT((1, 2), (2012-01-01, 2012-01-02))').is_contained_in(TBox('TBoxFloat XT([1, 4], [2012-01-01, 2012-01-02])'))
	 *             >>> True
	 *             >>> TBox('TBoxFloat XT([1, 2], [2012-01-01, 2012-01-02])').is_contained_in(TBox('TBoxFloat XT((1, 2), (2012-01-01, 2012-01-02))'))
	 *             >>> False
	 * </pre>
	 * <p>
	 *         MEOS Functions:
	 *             <li>contained_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if contained, false otherwise
	 */
	public boolean is_contained_in(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.contained_tbox_tbox(this._inner, ((TBox) other).get_inner());
		} else if (other instanceof TNumber){
			result = functions.contained_tbox_tbox(this._inner,functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}
		return result;
	}


	/**
	 * Returns whether "this" temporally contains "other".
	 * <pre>
	 *         Examples:
	 *             >>> TBox('TBoxInt XT([1, 4], [2012-01-01, 2012-01-04]').contains(TBox('TBoxInt XT([2, 3], [2012-01-02, 2012-01-03]'))
	 *             >>> True
	 *             >>> TBox('TBoxFloat XT([1, 2], [2012-01-01, 2012-01-02]').contains(TBox('TBoxFloat XT((1, 2), (2012-01-01, 2012-01-02)'))
	 *             >>> True
	 *             >>> TBox('TBoxFloat XT((1, 2), (2012-01-01, 2012-01-02)').contains(TBox('TBoxFloat XT([1, 2], [2012-01-01, 2012-01-02]'))
	 *             >>> False
	 * </pre>
	 * <p>
	 *         MEOS Functions:
	 *             <li>contains_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if contains, false otherwise
	 */
	public boolean contains(Object other) {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.contains_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.contains_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}
		return result;
	}

	/**
	 * Returns whether "this" overlaps "other". That is, both share at least an instant or a value.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overlaps_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if overlaps, false otherwise
	 */
	public boolean overlaps(Object other) {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overlaps_tbox_tbox(this._inner, ((TBox) other).get_inner());
		} else if(other instanceof TNumber){
			result = functions.overlaps_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

	/**
	 * Returns whether "this" is the same as "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>same_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if same, false otherwise
	 */
	public boolean is_same(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.same_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.same_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}


    /* ------------------------- Position Operations --------------------------- */

	/**
	 * Returns whether "this" is strictly to the left of "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>left_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return True if left, False otherwise
	 */
	public boolean is_left(Object other) {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.left_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.left_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

	/**
	 * Returns whether "this" is to the left of "other" allowing overlap. That is, "this" does not extend to the
	 *         right of "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overleft_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if overleft, false otherwise
	 */
	public boolean is_over_or_left(Object other) {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overleft_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.overleft_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}


	/**
	 * Returns whether "this" is strictly to the right of "other".
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>right_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if right, false otherwise
	 */
	public boolean is_right(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.right_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.right_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}


	/**
	 * Returns whether "this" is to the right of "other" allowing overlap. That is, "this" does not extend to the
	 *         left of "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>overright_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 * @param other temporal object to compare with
	 * @return True if overright, False otherwise
	 */
	public boolean is_over_or_right(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overright_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.overright_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

	/**
	 * Returns whether "this" is strictly before "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>before_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if before, false otherwise
	 */
	public boolean is_before(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.before_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.before_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

	/**
	 * Returns whether "this" is before "other" allowing overlap. That is, "this" does not extend after
	 *         "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>overbefore_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if overbefore, false otherwise
	 */
	public boolean is_over_or_before(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overbefore_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.overbefore_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

	/**
	 * Returns whether "this" is strictly after "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>after_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if after, false otherwise
	 */
	public boolean is_after(Object other)  {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.after_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = functions.after_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}


	/**
	 * Returns whether "this" is after "other" allowing overlap. That is, "this" does not extend before
	 *         "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>overafter_tbox_tbox</li>
	 *             <li>tnumber_to_tbox</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if overafter, false otherwise
	 */
	public boolean is_over_or_after(Object other) {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overafter_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}

		else if(other instanceof TNumber){
			result = functions.overafter_tbox_tbox(this._inner, functions.tnumber_to_tbox(((TNumber) other).getNumberInner()));
		}

		return result;
	}

    /* ------------------------- Set Operations -------------------------------- */

	/**
	 * Returns the union of "this" with "other". Fails if the union is not contiguous.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>union_tbox_tbox</li>
	 *
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance
	 */
	public TBox union(TBox other, boolean strict) {
		return new TBox(functions.union_tbox_tbox(this._inner, other._inner,strict));
	}


	/**
	 * Returns the union of "this" with "other". Fails if the union is not contiguous.
	 * <p>
	 *         MEOS Functions:
	 *             <li>union_tbox_tbox</li>
	 *
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance
	 */
	public TBox add(TBox other, boolean strict) {
		return this.union(other,strict);
	}

	/**
	 * Returns the intersection of "this" with "other".
	 * <p>
	 *         MEOS Functions:
	 *             <li>intersection_tbox_tbox</li>
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance if the instersection is not empty, "null" otherwise.
	 */
	public TBox intersection(TBox other) {
		return new TBox(functions.intersection_tbox_tbox(this._inner,other.get_inner()));
	}


	/**
	 * Returns the intersection of "this" with "other".
	 * <p>
	 *         MEOS Functions:
	 *             <li>intersection_tbox_tbox</li>
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance if the instersection is not empty, "null" otherwise.
	 */
	public TBox mul(TBox other) {
		return this.intersection(other);
	}

    /* ------------------------- Distance Operations --------------------------- */

	/**
	 * Returns the distance between the nearest points of "this" and "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>nad_tbox_tbox</li>
	 *
	 *
	 * @param other temporal object to compare with
	 * @return A {@link Float} with the distance between the nearest points of "this" and "other".
	 */
	public float nearest_approach_distance(Object other) {
		float result = 0.0f;
		if(other instanceof TBox){
			result = (float) functions.nad_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		else if(other instanceof TNumber){
			result = (float) functions.nad_tbox_tbox(((TNumber) other).getNumberInner(), this._inner);
		}

		return result;
	}



    /* ------------------------- Comparisons ----------------------------------- */

	/**
	 * Returns whether "this" is equal to "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_eq</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if equal, false otherwise
	 */
	public boolean eq(Box other) {
		boolean result;
		result = other instanceof TBox && functions.tbox_eq(this._inner, ((TBox) other).get_inner());
		return result;
	}

	/**
	 * Returns whether "this" is not equal to "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_ne</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if not equal, false otherwise
	 */
	public boolean notEquals(Box other) {
		boolean result;
		result = !(other instanceof TBox) || functions.stbox_ne(this._inner, ((TBox) other).get_inner());
		return result;
	}


	/**
	 * Returns whether "this" is less than "other". The time dimension is compared first, then the space dimension.
	 * <br>
	 * <br>
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_lt</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if less than, false otherwise
	 * @throws Exception
	 */
	public boolean lessThan(Box other) throws Exception {
		if (other instanceof TBox){
			return functions.tbox_lt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new Exception("Operation not supported with this type.");
		}
	}


	/**
	 * Returns whether "this" is less than or equal to "other". The time dimension is compared first, then the
	 *         space dimension.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_le</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if less than or equal to, false otherwise
	 * @throws Exception
	 */
	public boolean lessThanOrEqual(Box other) throws Exception {
		if (other instanceof TBox){
			return functions.tbox_le(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new Exception("Operation not supported with this type.");
		}
	}


	/**
	 * Returns whether "this" is greater than "other". The time dimension is compared first, then the space
	 *         dimension.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_gt</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if greater than, false otherwise
	 * @throws Exception
	 */
	public boolean greaterThan(Box other) throws Exception {
		if (other instanceof TBox){
			return functions.tbox_gt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new Exception("Operation not supported with this type.");
		}
	}

	/**
	 * Returns whether "this" is greater than or equal to "other". The time dimension is compared first, then the
	 *         space dimension.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tbox_ge</li>
	 *
	 * @param other temporal object to compare with
	 * @return true if greater than or equal to, false otherwise
	 * @throws Exception
	 */
	public boolean greaterThanOrEqual(Box other) throws Exception {
		if (other instanceof TBox){
			return functions.tbox_ge(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new Exception("Operation not supported with this type.");
		}
	}


	public boolean get_tmin_inc(){
		return tmin_inc;
	}

	public boolean get_tmax_inc(){
		return tmax_inc;
	}

	public Pointer get_inner(){
		return this._inner;
	}



}
