package types.boxes;

import jnr.ffi.Pointer;
import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.OffsetDateTime;

import static function.builder.functions.stbox_from_hexwkb;

/**
 * Class that represents the MobilityDB type TBox
 */
@TypeName(name = "tbox")
public class TBox extends DataType {
    private double xmin = 0.0f;
    private double xmax = 0.0f;
    private OffsetDateTime tmin;
    private OffsetDateTime tmax;

    /**
     * The default constructor
     */
    public TBox() {
        super();
    }

    /**
     * The string constructor
     * @param value - the string with the TBox value
     * @throws SQLException
     */
    public TBox(final String value) throws SQLException {
        super();
        setValue(value);
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
    public STBox from_hexwkb(String hexwkb){
        Pointer result = stbox_from_hexwkb(hexwkb);
        return new STBox(result);
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
