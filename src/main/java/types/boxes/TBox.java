package types.boxes;

import functions.functions;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import jnr.ffi.Pointer;
import types.time.Period;

import java.sql.SQLException;
import java.time.OffsetDateTime;


/**
 * Class that represents the MobilityDB type TBox
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
	
	
	/**
	 * The default constructor
	 */
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
	 * Function that produces an TBox from a string through MEOS.
	 *
	 * @param hexwkb
	 * @return a JNR-FFI pointer
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

	/*
	public TBox from_value_range(){
		;
	}

	 */


	public String to_str(){
		return this.to_str(15);
	}
	public String to_str(int max_decimals){
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


	public TBox intersection(TBox other) throws SQLException {
		return new TBox(functions.intersection_tbox_tbox(this._inner,other.get_inner()));
	}
	
	
	public float nearest_approach_distance(TBox other) {
		return (float) functions.nad_tbox_tbox(this._inner, other._inner);
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
	public boolean equals(Object obj) {
		if (obj instanceof TBox) {
			TBox other = (TBox) obj;
			
			if (xmin != other.getXmin()) {
				return false;
			}
			
			if (xmax != other.getXmax()) {
				return false;
			}
			
			boolean xminIsEqual;
			boolean xmaxIsEqual;
			
			if (tmin != null && other.getTmin() != null) {
				xminIsEqual = tmin.isEqual(other.getTmin());
			} else {
				xminIsEqual = tmin == other.getTmin();
			}
			
			if (tmax != null && other.getTmax() != null) {
				xmaxIsEqual = tmax.isEqual(other.getTmax());
			} else {
				xmaxIsEqual = tmax == other.getTmax();
			}
			
			return xminIsEqual && xmaxIsEqual;
		}
		return false;
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
