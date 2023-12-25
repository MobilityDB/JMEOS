package types.boxes;

import functions.functions;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import jnr.ffi.Pointer;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;

import java.sql.SQLException;
import java.time.OffsetDateTime;


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
@TypeName(name = "tbox")
public class TBox implements Box {
	private double xmin = 0.0f;
	private double xmax = 0.0f;
	private OffsetDateTime tmin;
	private OffsetDateTime tmax;
	private boolean xmin_inc = true;
	private boolean xmax_inc = true;
	private boolean tmin_inc = true;
	private boolean tmax_inc = true;
	private Pointer _inner = null;
	
	
	/** ------------------------- Constructors ---------------------------------- */
	public TBox() {
		super();
	}
	
	
	public TBox(Pointer inner) throws SQLException {
		this(inner, true, true, true, true);
	}
	
	public TBox(Pointer inner, boolean xmin_inc, boolean xmax_inc, boolean tmax_inc, boolean
			tmin_inc) throws SQLException {
		super();
		this._inner = inner;
		this.xmin_inc = xmin_inc;
		this.xmax_inc = xmax_inc;
		this.tmin_inc = tmin_inc;
		this.tmax_inc = tmax_inc;
		String str = functions.tbox_out(this._inner,2);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBox value
	 * @throws SQLException
	 */
	public TBox(final String value) throws SQLException {
		super();
		this._inner = functions.tbox_in(value);
	}
	
	/**
	 * The constructor for only value dimension x
	 *
	 * @param xmin - minimum x value
	 * @param xmax - maximum x value
	 */
	public TBox(Double xmin, Double xmax) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
	}
	
	/**
	 * The constructor for only time dimension
	 *
	 * @param tmin - minimum time dimension
	 * @param tmax - maximum time dimension
	 */
	public TBox(OffsetDateTime tmin, OffsetDateTime tmax) {
		super();
		this.tmin = tmin;
		this.tmax = tmax;
	}
	
	/**
	 * The constructor for value dimension x and time dimension
	 *
	 * @param xmin - minimum x value
	 * @param tmin - minimum time dimension
	 * @param xmax - maximum x value
	 * @param tmax - maximum time dimension
	 */
	public TBox(Double xmin, OffsetDateTime tmin, Double xmax, OffsetDateTime tmax) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.tmin = tmin;
		this.tmax = tmax;
	}


	/**
	 * Returns a copy of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>tbox_copy</li>
	 * @return a new TBox instance
	 * @throws SQLException
	 */
	public TBox copy() throws SQLException {
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
	 * @throws SQLException
	 */
	public static TBox from_hexwkb(String hexwkb) throws SQLException {
		return new TBox(functions.tbox_from_hexwkb(hexwkb));
	}

	public TBox from_value_number(Number value) throws SQLException {
		TBox tbox = null;
		if(Integer.class.isInstance(value)){
			tbox = new TBox(functions.int_to_tbox((int)value));
		}
		else if (Float.class.isInstance(value)){
			tbox = new TBox(functions.float_to_tbox((int)value));
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
	 * @throws SQLException
	 */
	public TBox from_time(Time time) throws SQLException {
		TBox tbox = null;
		if (time instanceof Period){
			//tbox = new TBox(((Period) time).getLower(), ((Period) time).getUpper(), ((Period) time).lower_inc(), ((Period) time).upper_inc());
		}
		else if (time instanceof PeriodSet){
			//tbox = new TBox(((PeriodSet) time).start_period().getLower(), ((PeriodSet) time).end_period().getUpper(), ((PeriodSet) time).start_period().lower_inc(), ((PeriodSet) time).end_period().upper_inc());
		}
		else if (time instanceof TimestampSet){
			tbox = new TBox(((TimestampSet) time).startTimestamp(), ((TimestampSet) time).endTimestamp());
		}
		else {
			throw new SQLException("Operation not supported with this type.");
		}
		return tbox;
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
	 * Returns the temporal span of "this".
	 * <p>
	 *         MEOS Functions:
	 *             <li>tbox_to_period</li>
	 * @return a {@link Period} instance
	 */
	@Override
	public Period to_period(){
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
	 * @throws SQLException
	 */
	public TBox expand(Object obj) throws SQLException {
		Pointer result = null;
		if(obj instanceof TBox){
			result = functions.tbox_copy(this._inner);
			functions.tbox_expand(((TBox) obj).get_inner(),result);
		}
		else if(Integer.class.isInstance(obj) || Float.class.isInstance(obj)){
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
	 * @throws SQLException
	 */
	public TBox round() throws SQLException {
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
	 * @throws SQLException
	 */
	public TBox round(int maxdd) throws SQLException {
		Pointer new_inner = functions.tbox_copy(this._inner);
		functions.tbox_round(new_inner,maxdd);
		return new TBox(new_inner);
	}


    /*
    //Add the timedelta function
    public STBox expand_timedelta(STBox stbox, Duration duration){
        Pointer result = tbox_expand_temporal(this._inner, timedelta_to_interval(duration));
        return new TBox(result);
    }

     */

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
		/*
		else if (other instanceof TNumber){
			result = functions.adjacent_tbox_tbox(this._inner,functions.tnumber_to_tbox(other.get_inner()))
		}

		 */
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
	 * @throws SQLException
	 */
	public boolean is_contained_in(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.contained_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean contains(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.contains_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean overlaps(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overlaps_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_same(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.same_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_left(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.left_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_over_or_left(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overleft_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_right(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.right_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_over_or_right(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overright_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_before(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.before_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_over_or_before(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overbefore_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_after(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.after_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public boolean is_over_or_after(Object other) throws SQLException {
		boolean result = false;
		if(other instanceof TBox){
			result = functions.overafter_tbox_tbox(this._inner, ((TBox) other).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)other);
		}
		 */
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
	 * @throws SQLException
	 */
	public TBox union(TBox other, boolean strict) throws SQLException {
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
	 * @throws SQLException
	 */
	public TBox add(TBox other, boolean strict) throws SQLException {
		return this.union(other,strict);
	}

	/**
	 * Returns the intersection of "this" with "other".
	 * <p>
	 *         MEOS Functions:
	 *             <li>intersection_tbox_tbox</li>
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance if the instersection is not empty, "null" otherwise.
	 * @throws SQLException
	 */
	public TBox intersection(TBox other) throws SQLException {
		return new TBox(functions.intersection_tbox_tbox(this._inner,other.get_inner()));
	}


	/**
	 * Returns the intersection of "this" with "other".
	 * <p>
	 *         MEOS Functions:
	 *             <li>intersection_tbox_tbox</li>
	 * @param other temporal object to merge with
	 * @return a {@link TBox} instance if the instersection is not empty, "null" otherwise.
	 * @throws SQLException
	 */
	public TBox mul(TBox other) throws SQLException {
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
	public float nearest_approach_distance(TBox other) {
		return (float) functions.nad_tbox_tbox(this._inner, other._inner);
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
	 * @throws SQLException
	 */
	public boolean equals(Box other) throws SQLException{
		boolean result;
		result = other instanceof TBox ? functions.tbox_eq(this._inner,((TBox) other).get_inner()) : false;
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
	 * @throws SQLException
	 */
	public boolean notEquals(Box other) throws SQLException{
		boolean result;
		result = other instanceof TBox ? functions.stbox_ne(this._inner,((TBox) other).get_inner()) : true;
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
	 * @throws SQLException
	 */
	public boolean lessThan(Box other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_lt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean lessThanOrEqual(Box other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_le(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean greaterThan(Box other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_gt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
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
	 * @throws SQLException
	 */
	public boolean greaterThanOrEqual(Box other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_ge(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}









	public String getValue() {
		if (xmin != 0.0f && tmin != null) {
			return String.format("TBOX((%f, %s), (%f, %s))",
					xmin,
					DateTimeFormatHelper.getStringFormat(tmin),
					xmax,
					DateTimeFormatHelper.getStringFormat(tmax));
		} else if (xmin != 0.0f) {
			return String.format("TBOX((%f, ), (%f, ))", xmin, xmax);
		} else if (tmin != null) {
			return String.format("TBOX((, %s), (, %s))", DateTimeFormatHelper.getStringFormat(tmin),
					DateTimeFormatHelper.getStringFormat(tmax));
		} else {
			return null;
		}
		
	}

	public void setValue(String value) throws SQLException {
		value = value.replace("TBOX", "")
				.replace("(", "")
				.replace(")", "");
		String[] values = value.split(",", -1);
		
		if (values.length != 4) {
			throw new SQLException("Could not parse TBox value, invalid number of arguments.");
		}
		if (values[0].trim().length() > 0) {
			if (values[2].trim().length() > 0) {
				this.xmin = Double.parseDouble(values[0]);
				this.xmax = Double.parseDouble(values[2]);
			} else {
				throw new SQLException("Xmax should have a value.");
			}
		} else if (values[2].trim().length() > 0) {
			throw new SQLException("Xmin should have a value.");
		}
		if (values[1].trim().length() > 0) {
			if (values[3].trim().length() > 0) {
				this.tmin = DateTimeFormatHelper.getDateTimeFormat(values[1].trim());
				this.tmax = DateTimeFormatHelper.getDateTimeFormat(values[3].trim());
			} else {
				throw new SQLException("Tmax should have a value.");
			}
		} else if (values[3].trim().length() > 0) {
			throw new SQLException("Tmin should have a value.");
		}
	}

	
	@Override
	public int hashCode() {
		String value = getValue();
		return value != null ? value.hashCode() : 0;
	}
	
	public double getXmin() {
		return xmin;
	}
	
	public double getXmax() {
		return xmax;
	}
	
	public OffsetDateTime getTmin() {
		return tmin;
	}
	
	public OffsetDateTime getTmax() {
		return tmax;
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
