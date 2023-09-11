package types.boxes;

import functions.functions;
import types.TemporalObject;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import jnr.ffi.Pointer;
import types.time.Period;

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
public class TBox extends Box {
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
		setValue(str);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - the string with the TBox value
	 * @throws SQLException
	 */
	public TBox(final String value) throws SQLException {
		super();
		setValue(value);
		
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




	public String toString(){
		return this.toString(15);
	}
	public String toString(int max_decimals){
		return functions.tbox_out(this._inner,max_decimals);
	}

	@Override
	public Period to_period() throws SQLException {
		return new Period(functions.tbox_to_period(this._inner));
	}

	public boolean has_x(){
		return functions.tbox_hasx(this._inner);
	}

	public boolean has_t(){
		return functions.tbox_hast(this._inner);
	}



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



	public TBox round() throws SQLException {
		return this.round(0);
	}

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

	
	public boolean is_adjacent_tbox(TBox other) {
		return functions.adjacent_tbox_tbox(this._inner, other._inner);
	}


	public boolean is_contained_in(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.contained_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean contains(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.contains_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean overlaps(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.overlaps_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_same(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.same_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}



	public boolean is_left(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.left_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_over_or_left(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.overleft_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_right(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.right_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}

	public boolean is_over_or_right(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.overright_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_before(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.before_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_over_or_before(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.overbefore_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public boolean is_after(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.after_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}

	public boolean is_over_or_after(Object obj) throws SQLException {
		boolean result = false;
		if(obj instanceof TBox){
			result = functions.overafter_tbox_tbox(this._inner, ((TBox) obj).get_inner());
		}
		/*
		else if(obj instanceof Tnumber){
			result = functions.tbox_expand_value(this._inner,(float)obj);
		}
		 */
		return result;
	}


	public TBox union(TBox other) throws SQLException {
		return new TBox(functions.union_tbox_tbox(this._inner, other._inner));
	}

	public TBox add(TBox other) throws SQLException {
		return this.union(other);
	}


	public TBox intersection(TBox other) throws SQLException {
		return new TBox(functions.intersection_tbox_tbox(this._inner,other.get_inner()));
	}

	public TBox mul(TBox other) throws SQLException {
		return this.intersection(other);
	}
	
	
	public float nearest_approach_distance(TBox other) {
		return (float) functions.nad_tbox_tbox(this._inner, other._inner);
	}




	public boolean equals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof TBox ? functions.tbox_eq(this._inner,((TBox) other).get_inner()) : false;
		return result;
	}

	public boolean notEquals(TemporalObject<?> other) throws SQLException{
		boolean result;
		result = other instanceof TBox ? functions.stbox_ne(this._inner,((TBox) other).get_inner()) : true;
		return result;
	}

	public boolean lessThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_lt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean lessThanOrEqual(TemporalObject<?> other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_le(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	public boolean greaterThan(TemporalObject<?> other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_gt(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}


	public boolean greaterThanOrEqual(TemporalObject<?> other) throws SQLException{
		if (other instanceof TBox){
			return functions.tbox_ge(this._inner,((TBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}








	
	
	@Override
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
	
	@Override
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
