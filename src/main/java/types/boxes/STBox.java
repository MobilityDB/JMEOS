package types.boxes;

import types.core.DataType;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;

/**
 * Class that represents the MobilityDB type STBox
 */
@TypeName(name = "stbox")
public class STBox extends DataType {
    private Point pMin = null;
    private Point pMax = null;
    private OffsetDateTime tMin = null;
    private OffsetDateTime tMax = null;
    private boolean isGeodetic = false;
    private int srid = 0;

    /**
     * The default constructor
     */
    public STBox() {
        super();
    }

    /**
     * The string constructor
     * @param value - STBox value
     * @throws SQLException
     */
    public STBox(final String value) throws SQLException {
        super();
        setValue(value);
    }

    /**
     * The constructor for only value dimension (x,y) or (x,y,z)
     *
     * @param pMin - coordinates for minimum bound
     * @param pMax - coordinates for maximum bound
     * @throws SQLException
     */
    public STBox(Point pMin, Point pMax) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        validate();
    }

    /**
     * The constructor for value dimension (x,y) or (x,y,z) and time dimension
     *
     * @param pMin - coordinates for minimum bound
     * @param tMin - minimum time dimension
     * @param pMax - coordinates for maximum bound
     * @param tMax - maximum time dimension
     * @throws SQLException
     */
    public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.tMin = tMin;
        this.tMax = tMax;
        validate();
    }

    /**
     * The constructor for only time dimension
     *
     * @param tMin - minimum time dimension
     * @param tMax - maximum time dimension
     * @throws SQLException
     */
    public STBox(OffsetDateTime tMin, OffsetDateTime tMax) throws SQLException {
        super();
        this.tMin = tMin;
        this.tMax = tMax;
        validate();
    }

    /**
     * The constructor for value dimension (x,y,z) with geodetic coordinates
     *
     * @param pMin - coordinates for minimum bound
     * @param pMax - coordinates for maximum bound
     * @param isGeodetic - if the coordinates are spherical
     * @throws SQLException
     */
    public STBox(Point pMin, Point pMax, boolean isGeodetic) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.isGeodetic = isGeodetic;
        validate();
    }

    /**
     * The constructor for value dimension (x,y,z) with geodetic coordinates and time dimension
     *
     * @param pMin - coordinates for minimum bound
     * @param tMin - minimum time dimension
     * @param pMax - coordinates for maximum bound
     * @param tMax - maximum time dimension
     * @param isGeodetic - if the coordinates are spherical
     * @throws SQLException
     */
    public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, boolean isGeodetic)
            throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.tMin = tMin;
        this.tMax = tMax;
        this.isGeodetic = isGeodetic;
        validate();
    }

    /**
     * The constructor for geodetic box with only time dimension
     *
     * @param tMin - minimum time dimension
     * @param tMax - maximum time dimension
     * @param isGeodetic - if the coordinates are spherical
     * @throws SQLException
     */
    public STBox(OffsetDateTime tMin, OffsetDateTime tMax, boolean isGeodetic) throws SQLException {
        super();
        this.tMin = tMin;
        this.tMax = tMax;
        this.isGeodetic = isGeodetic;
        validate();
    }

    /**
     * The constructor for only value dimension (x,y) or (x,y,z) and SRID
     *
     * @param pMin - coordinates for minimum bound
     * @param pMax - coordinates for maximum bound
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(Point pMin, Point pMax, int srid) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.srid = srid;
        validate();
    }

    /**
     * The constructor for value dimension (x,y) or (x,y,z), time dimension and SRID
     *
     * @param pMin - coordinates for minimum bound
     * @param tMin - minimum time dimension
     * @param pMax - coordinates for maximum bound
     * @param tMax - maximum time dimension
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, int srid) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.tMin = tMin;
        this.tMax = tMax;
        this.srid = srid;
        validate();
    }

    /**
     * The constructor for only time dimension and SRID
     *
     * @param tMin - minimum time dimension
     * @param tMax - maximum time dimension
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(OffsetDateTime tMin, OffsetDateTime tMax, int srid) throws SQLException {
        super();
        this.tMin = tMin;
        this.tMax = tMax;
        this.srid = srid;
        validate();
    }

    /**
     * The constructor for value dimension (x,y,z) with geodetic coordinates and SRID
     *
     * @param pMin - coordinates for minimum bound
     * @param pMax - coordinates for maximum bound
     * @param isGeodetic - if the coordinates are spherical
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(Point pMin, Point pMax, boolean isGeodetic, int srid) throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.isGeodetic = isGeodetic;
        this.srid = srid;
        validate();
    }

    /**
     * The constructor for value dimension (x,y,z) with geodetic coordinates, time dimension and SRID
     *
     * @param pMin - coordinates for minimum bound
     * @param tMin - minimum time dimension
     * @param pMax - coordinates for maximum bound
     * @param tMax - maximum time dimension
     * @param isGeodetic - if the coordinates are spherical
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, boolean isGeodetic, int srid)
            throws SQLException {
        super();
        this.pMin = pMin;
        this.pMax = pMax;
        this.tMin = tMin;
        this.tMax = tMax;
        this.isGeodetic = isGeodetic;
        this.srid = srid;
        validate();
    }

    /**
     * The constructor for geodetic box with only time dimension and SRID
     *
     * @param tMin - minimum time dimension
     * @param tMax - maximum time dimension
     * @param isGeodetic - if the coordinates are spherical
     * @param srid - spatial reference identifier
     * @throws SQLException
     */
    public STBox(OffsetDateTime tMin, OffsetDateTime tMax, boolean isGeodetic, int srid) throws SQLException {
        super();
        this.tMin = tMin;
        this.tMax = tMax;
        this.isGeodetic = isGeodetic;
        this.srid = srid;
        validate();
    }

    @Override
    public String getValue() {
        String sridPrefix = "";
        if (srid != 0) {
            sridPrefix = String.format("SRID=%s;", srid);
        }
        if (isGeodetic) {
            return getGeodeticValue(sridPrefix);
        } else {
            return getNonGeodeticValue(sridPrefix);
        }
    }

    @Override
    public void setValue(String value) throws SQLException {
        boolean hasZ;
        boolean hasT;
        if (value.startsWith("SRID")) {
            String[] initialValues = value.split(";");
            srid = Integer.parseInt(initialValues[0].split("=")[1]);
            value = initialValues[1];
        }

        if (value.contains("GEODSTBOX")) {
            isGeodetic = true;
            value = value.replace("GEODSTBOX", "");
            hasZ = true;
            hasT = value.contains("T");
        } else if (value.startsWith("STBOX")) {
            value = value.replace("STBOX", "");
            hasZ = value.contains("Z");
            hasT = value.contains("T");
        } else {
            throw new SQLException("Could not parse STBox value");
        }
        value = value.replace("Z", "")
                .replace("T", "")
                .replace("(", "")
                .replace(")", "");
        String[] values = value.split(",");
        int nonEmpty = (int) Arrays.stream(values).filter(x -> !x.isBlank()).count();
        if (nonEmpty == 2) {
            String[] removedNull = Arrays.stream(values)
                    .filter(x -> !x.isBlank())
                    .toArray(String[]::new);
            this.tMin = DateTimeFormatHelper.getDateTimeFormat(removedNull[0].trim());
            this.tMax = DateTimeFormatHelper.getDateTimeFormat(removedNull[1].trim());
        } else {
            this.pMin = new Point();
            this.pMax = new Point();
            if (nonEmpty >= 4) {
                this.pMin.setX(Double.parseDouble(values[0]));
                this.pMax.setX(Double.parseDouble(values[nonEmpty / 2]));
                this.pMin.setY(Double.parseDouble(values[1]));
                this.pMax.setY(Double.parseDouble(values[1 + nonEmpty / 2]));
            } else {
                throw new SQLException("Could not parse STBox value, invalid number of parameters.");
            }
            if (hasZ) {
                this.pMin.setZ(Double.parseDouble(values[2]));
                this.pMax.setZ(Double.parseDouble(values[2 + nonEmpty / 2]));
            }
            if (hasT) {
                this.tMin = DateTimeFormatHelper.getDateTimeFormat(values[nonEmpty / 2 - 1].trim());
                this.tMax = DateTimeFormatHelper.getDateTimeFormat(values[(nonEmpty / 2 - 1) + nonEmpty / 2].trim());
            }
        }
        validate();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof STBox) {
            STBox other = (STBox) obj;

            boolean pMinIsEqual;
            boolean pMaxIsEqual;

            if (pMin != null && other.pMin != null) {
                pMinIsEqual = pMin.equals(other.pMin);
            } else {
                pMinIsEqual = pMin == other.pMin;
            }

            if (pMax != null && other.pMax != null) {
                pMaxIsEqual = pMax.equals(other.pMax);
            } else {
                pMaxIsEqual = pMax == other.pMax;
            }

            return  tIsEqual(other) && pMinIsEqual && pMaxIsEqual && isGeodetic == other.isGeodetic();
        }
        return false;
    }

    /**
     * Compares if the values in time dimension are the same
     * @param other - a STBox to compare
     * @return true if they are equals; otherwise false
     */
    public boolean tIsEqual(STBox other){
        boolean tMinIsEqual;
        boolean tMaxIsEqual;

        if (tMin != null && other.getTMin() != null) {
            tMinIsEqual = tMin.isEqual(other.getTMin());
        } else {
            tMinIsEqual = tMin == other.getTMin();
        }

        if (tMax != null && other.getTMax() != null) {
            tMaxIsEqual = tMax.isEqual(other.getTMax());
        } else {
            tMaxIsEqual = tMax == other.getTMax();
        }

        return tMinIsEqual && tMaxIsEqual;
    }

    @Override
    public int hashCode() {
        String value = getValue();
        return value != null ? value.hashCode() : 0;
    }

    public Double getXmin() {
        return pMin != null ? pMin.getX() : null;
    }

    public Double getXmax() {
        return pMax != null ? pMax.getX() : null;
    }

    public Double getYmin() {
        return pMin != null ? pMin.getY() : null;
    }

    public Double getYmax() {
        return pMax != null ? pMax.getY() : null;
    }

    public Double getZmin() {
        return pMin != null ? pMin.getZ() : null;
    }

    public Double getZmax() {
        return pMax != null ? pMax.getZ() : null;
    }

    public OffsetDateTime getTMin() {
        return tMin;
    }

    public OffsetDateTime getTMax() {
        return tMax;
    }

    public boolean isGeodetic() {
        return isGeodetic;
    }

    public int getSrid() {
        return srid;
    }

    /**
     * Verifies that the received fields are valid
     * @throws SQLException
     */
    private void validate() throws SQLException {
        if (tMin == null ^ tMax == null) {
            throw new SQLException("Both tmin and tmax should have a value.");
        }

        if(pMin != null && pMax != null) {
            if ((pMin.getX() == null ^ pMax.getX() == null) ^ (pMin.getY() == null ^ pMax.getY() == null)) {
                throw new SQLException("Both x and y coordinates should have a value.");
            }

            if (pMin.getZ() == null ^ pMax.getZ() == null) {
                throw new SQLException("Both zmax and zmin should have a value.");
            }

            if (isGeodetic && pMin.getZ() == null) {
                throw new SQLException("Geodetic coordinates need z value.");
            }

            if (pMin.getX() == null && tMin == null) {
                throw new SQLException("Could not parse STBox value, invalid number of arguments.");
            }
        }
    }

    /**
     * Gets a string for geodetic values
     * @param sridPrefix - a string that contains the SRID
     * @return the STBox string with geodetic values
     */
    private String getGeodeticValue(String sridPrefix) {
        if (tMin != null) {
            if (pMin != null) {
                return String.format("%sGEODSTBOX T((%f, %f, %f, %s), (%f, %f, %f, %s))", sridPrefix,
                        pMin.getX(), pMin.getY(), pMin.getZ(), tMin, pMax.getX(), pMax.getY(), pMax.getZ(), tMax);
            }
            return String.format("%sGEODSTBOX T((, %s), (, %s))", sridPrefix, tMin, tMax);
        }
        return String.format("%sGEODSTBOX((%f, %f, %f), (%f, %f, %f))", sridPrefix,
                pMin.getX(), pMin.getY(), pMin.getZ(), pMax.getX(), pMax.getY(), pMax.getZ());
    }

    /**
     * Gets a string for non-geodetic values
     * @param sridPrefix - a string that contains the SRID
     * @return the STBox string with non-geodetic values
     */
    private String getNonGeodeticValue(String sridPrefix) {
        if (pMin == null) {
            if (tMin != null) {
                return String.format("%sSTBOX T((, %s), (, %s))", sridPrefix, tMin, tMax);
            }
        } else if (pMin.getZ() == null) {
            if (tMin == null) {
                return String.format("%sSTBOX ((%f, %f), (%f, %f))", sridPrefix,
                        pMin.getX(), pMin.getY(), pMax.getX(), pMax.getY());
            }
            return String.format("%sSTBOX T((%f, %f, %s), (%f, %f, %s))", sridPrefix,
                    pMin.getX(), pMin.getY(), tMin, pMax.getX(), pMax.getY(), tMax);
        } else {
            if (tMin == null) {
                return String.format("%sSTBOX Z((%f, %f, %s), (%f, %f, %s))", sridPrefix,
                        pMin.getX(), pMin.getY(), pMin.getZ(), pMax.getX(), pMax.getY(), pMax.getZ());
            }
            return String.format("%sSTBOX ZT((%f, %f, %f, %s), (%f, %f, %f, %s))", sridPrefix,
                    pMin.getX(), pMin.getY(), pMin.getZ(), tMin, pMax.getX(), pMax.getY(), pMax.getZ(), tMax);
        }
        return null;
    }
}
