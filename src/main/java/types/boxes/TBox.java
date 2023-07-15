package types.boxes;

import jnr.ffi.Pointer;
import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.OffsetDateTime;

import static function.functions.*;

//import static function.functions.stbox_from_hexwkb;

/**
 * Class that represents the MobilityDB type TBox
 */
@TypeName(name = "tbox")
public class TBox extends DataType {
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


    public TBox(Pointer inner){
        this(inner,true,true,true,true);
    }

    public TBox(Pointer inner, boolean xmin_inc, boolean xmax_inc, boolean tmax_inc, boolean
                tmin_inc){
        super();
        this._inner = inner;
        this.xmin_inc = xmin_inc;
        this.xmax_inc = xmax_inc;
        this.tmin_inc = tmin_inc;
        this.tmax_inc = tmax_inc;
    }

    /**
     * The string constructor
     * @param value - the string with the TBox value
     * @throws SQLException
     */
    public TBox(final String value, Pointer inner, boolean tmin_inc, boolean tmax_inc, boolean xmax_inc, boolean xmin_inc ) throws SQLException {
        super();
        setValue(value);

        this.tmin_inc = tmin_inc;
        this.tmax_inc = tmax_inc;
        this.xmin_inc = xmin_inc;
        this.xmax_inc = xmax_inc;
        this._inner = inner;
    }

    /**
     * The constructor for only value dimension x
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
     * @param hexwkb
     * @return a JNR-FFI pointer
     */

    public TBox from_hexwkb(String hexwkb){
        Pointer result = tbox_from_hexwkb(hexwkb);
        return new TBox(result);
    }



    public TBox expand_stbox(TBox stbox, TBox other){
        Pointer result = tbox_copy(this._inner);
        tbox_expand(other._inner,result);
        return new TBox(result);
    }

    public TBox expand_float(STBox stbox, float other){
        Pointer result = tbox_expand_value(this._inner,other);
        return new TBox(result);
    }

    /*
    //Add the timedelta function
    public STBox expand_timedelta(STBox stbox, Duration duration){
        Pointer result = tbox_expand_temporal(this._inner, timedelta_to_interval(duration));
        return new TBox(result);
    }

     */


    public TBox union(TBox other){
        return new TBox(union_tbox_tbox(this._inner,other._inner));
    }

    public boolean is_adjacent_tbox(TBox other){
        return adjacent_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_adjacent_tnumber(TNumber other){
        return adjacent_tbox_tnumber(this._inner, other._inner);
    }

     */

    public boolean is_contained_in_tbox(TBox other){
        return contained_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_contained_in_tnumber(TNumber other){
        return contained_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean contains_in_tbox(TBox other){
        return contains_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean contains_in_tnumber(TNumber other){
        return contains_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean overlaps_in_tbox(TBox other){
        return overlaps_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean overlaps_in_tnumber(TNumber other){
        return overlaps_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean is_same_tbox(TBox other){
        return same_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_same_tnumber(TNumber other){
        return same_tbox_tnumber(this._inner, other._inner);
    }

     */

    public boolean is_left_tbox(TBox other){
        return left_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_left_tnumber(TNumber other){
        return left_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean isover_or_left_tbox(TBox other){
        return overleft_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean isover_or_left_tnumber(TNumber other){
        return overleft_tbox_tnumber(this._inner, other._inner);
    }

     */

    public boolean is_right_tbox(TBox other){
        return right_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_right_tnumber(TNumber other){
        return right_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean isover_or_right_tbox(TBox other){
        return overright_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean isover_or_right_tnumber(TNumber other){
        return overright_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean is_before_tbox(TBox other){
        return before_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_before_tnumber(TNumber other){
        return before_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean isover_or_before_tbox(TBox other){
        return overbefore_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean isover_or_before_tnumber(TNumber other){
        return overbefore_tbox_tnumber(this._inner, other._inner);
    }

     */


    public boolean is_after_tbox(TBox other){
        return after_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean is_after_tnumber(TNumber other){
        return after_tbox_tnumber(this._inner, other._inner);
    }

     */

    public boolean isover_or_after_tbox(TBox other){
        return overafter_tbox_tbox(this._inner, other._inner);
    }

    /*
    //Add for tnumber
    public boolean isover_or_after_tnumber(TNumber other){
        return overafter_tbox_tnumber(this._inner, other._inner);
    }

     */


    public float nearest_approach_distance(TBox other){
        return (float)nad_tbox_tbox(this._inner, other._inner);
    }














    @Override
    public String getValue() {
        if (xmin != 0.0f && tmin != null) {
            return String.format("TBOX((%f, %s), (%f, %s))",
                    xmin,
                    DateTimeFormatHelper.getStringFormat(tmin),
                    xmax,
                    DateTimeFormatHelper.getStringFormat(tmax));
        } else if(xmin != 0.0f) {
            return String.format("TBOX((%f, ), (%f, ))", xmin, xmax);
        } else if(tmin != null) {
            return String.format("TBOX((, %s), (, %s))", DateTimeFormatHelper.getStringFormat(tmin),
                    DateTimeFormatHelper.getStringFormat(tmax));
        } else {
            return null;
        }

    }

    @Override
    public void setValue(String value) throws SQLException {
        value = value.replace("TBOX","")
                .replace("(","")
                .replace(")","");
        String[] values = value.split(",", -1);

        if (values.length != 4 ) {
            throw new SQLException("Could not parse TBox value, invalid number of arguments.");
        }
        if(values[0].trim().length() > 0) {
            if (values[2].trim().length() > 0) {
                this.xmin = Double.parseDouble(values[0]);
                this.xmax = Double.parseDouble(values[2]);
            } else {
                throw new SQLException("Xmax should have a value.");
            }
        } else if(values[2].trim().length() > 0) {
            throw new SQLException("Xmin should have a value.");
        }
        if (values[1].trim().length() > 0) {
            if (values[3].trim().length() > 0) {
                this.tmin = DateTimeFormatHelper.getDateTimeFormat(values[1].trim());
                this.tmax = DateTimeFormatHelper.getDateTimeFormat(values[3].trim());
            } else {
                throw new SQLException("Tmax should have a value.");
            }
        } else if(values[3].trim().length() > 0) {
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
}
