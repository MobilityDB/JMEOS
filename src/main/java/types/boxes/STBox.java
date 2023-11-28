package types.boxes;

import functions.functions;
import jnr.ffi.Memory;
import jnr.ffi.Runtime;
import org.locationtech.jts.io.ParseException;
import types.TemporalObject;
import types.collections.time.Time;
import types.core.DateTimeFormatHelper;
import types.core.TypeName;
import jnr.ffi.Pointer;
import org.locationtech.jts.geom.Geometry;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Arrays;

import utils.ConversionUtils;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.TimestampSet;


/**
 * Class for representing a spatio-temporal box. Temporal bounds may be inclusive or exclusive.
 * <pre>
 *     ``STBox`` objects can be created with a single argument of type string as in MobilityDB.
 *         >>> STBox('STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])')
 *</pre>
 * <pre>
 *     Another possibility is to provide the different dimensions with the corresponding parameters:
 *         - ``xmin``, ``xmax``, ``ymin``, ``ymax`` for spatial dimension
 *         - ``zmin``, ``zmax`` for the third spatial dimension
 *         - ``tmin``, ``tmax`` for temporal dimension
 *         - ``tmin_inc``, ``tmax_inc`` to specify if the temporal bounds are inclusive or exclusive
 *         - ``geodetic`` to specify if the spatial dimension is geodetic
 *         - ``srid`` to specify the spatial reference system identifier
 *</pre>
 *     Note that at least the 2D spatial dimension or the temporal dimension must be provided.
 *<p>
 *         >>> STBox(xmin=1.0, xmax=4.0, ymin=2.0, ymax=5.0, tmin=datetime(2001, 1, 1), tmax=datetime(2001, 1, 2))
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
@TypeName(name = "stbox")
public class STBox implements Box {
	private Point pMin = null;
	private Point pMax = null;
	private OffsetDateTime tMin = null;
	private OffsetDateTime tMax = null;
	private boolean isGeodetic = false;
	private int srid = 0;
	private Pointer _inner = null;
	private boolean tmin_inc = true;
	private boolean tmax_inc = true;
	private Runtime runtime = Runtime.getSystemRuntime();

	public STBox _get_box(TemporalObject<?> other) throws SQLException {
		return this._get_box(other,true,false);
	}

	public STBox _get_box(Object other, boolean allow_space_only, boolean allow_time_only) throws SQLException {
		STBox other_box=null;

		if (other instanceof STBox){

		}

		if (allow_time_only) {
			switch (other) {
				case STBox st -> other_box = new STBox(st.get_inner());
				case Period p -> other_box = new STBox(functions.period_to_stbox(p.get_inner()));
				case PeriodSet ps -> other_box = new STBox(functions.periodset_to_stbox(ps.get_inner()));
				case TimestampSet ts -> other_box = new STBox(functions.timestampset_to_stbox(ts.get_inner()));
				default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
			}
		}
		return other_box;
	}

	/** ------------------------- Constructors ---------------------------------- */


	/**
	 * The default constructor
	 */
	public STBox() {
		super();
	}
	
	public STBox(Pointer inner) throws SQLException {
		this(inner, true, true, false);
	}
	
	public STBox(Pointer inner, boolean tmin_inc, boolean tmax_inc, boolean geodetic) throws SQLException {
		super();
		this._inner = inner;
		this.tmin_inc = tmin_inc;
		this.tmax_inc = tmax_inc;
		this.isGeodetic = geodetic;
		String str = functions.stbox_out(this._inner,2);
		//setValue(str);
	}
	
	/**
	 * The string constructor
	 *
	 * @param value - STBox value
	 * @throws SQLException
	 */
	
	public STBox(final String value) throws SQLException {
		super();
		//setValue(value);
		this._inner = functions.stbox_in(value);

	}
	
	/**
	 * The constructor for only value dimension (x,y) or (x,y,z)
	 *
	 * @param pMin - coordinates for minimum bound
	 * @param pMax - coordinates for maximum bound
	 * @throws SQLException
	 */
	public STBox(Point pMin, Point pMax, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
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
	public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.tMin = tMin;
		this.tMax = tMax;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for only time dimension
	 *
	 * @param tMin - minimum time dimension
	 * @param tMax - maximum time dimension
	 * @throws SQLException
	 */
	public STBox(OffsetDateTime tMin, OffsetDateTime tMax, Pointer inner) throws SQLException {
		super();
		this.tMin = tMin;
		this.tMax = tMax;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for value dimension (x,y,z) with geodetic coordinates
	 *
	 * @param pMin       - coordinates for minimum bound
	 * @param pMax       - coordinates for maximum bound
	 * @param isGeodetic - if the coordinates are spherical
	 * @throws SQLException
	 */
	public STBox(Point pMin, Point pMax, boolean isGeodetic, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.isGeodetic = isGeodetic;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for value dimension (x,y,z) with geodetic coordinates and time dimension
	 *
	 * @param pMin       - coordinates for minimum bound
	 * @param tMin       - minimum time dimension
	 * @param pMax       - coordinates for maximum bound
	 * @param tMax       - maximum time dimension
	 * @param isGeodetic - if the coordinates are spherical
	 * @throws SQLException
	 */
	public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, boolean isGeodetic, Pointer inner)
			throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.tMin = tMin;
		this.tMax = tMax;
		this.isGeodetic = isGeodetic;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for geodetic box with only time dimension
	 *
	 * @param tMin       - minimum time dimension
	 * @param tMax       - maximum time dimension
	 * @param isGeodetic - if the coordinates are spherical
	 * @throws SQLException
	 */
	public STBox(OffsetDateTime tMin, OffsetDateTime tMax, boolean isGeodetic, Pointer inner) throws SQLException {
		super();
		this.tMin = tMin;
		this.tMax = tMax;
		this.isGeodetic = isGeodetic;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for only value dimension (x,y) or (x,y,z) and SRID
	 *
	 * @param pMin - coordinates for minimum bound
	 * @param pMax - coordinates for maximum bound
	 * @param srid - spatial reference identifier
	 * @throws SQLException
	 */
	public STBox(Point pMin, Point pMax, int srid, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
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
	public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, int srid, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.tMin = tMin;
		this.tMax = tMax;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for only time dimension and SRID
	 *
	 * @param tMin - minimum time dimension
	 * @param tMax - maximum time dimension
	 * @param srid - spatial reference identifier
	 * @throws SQLException
	 */
	public STBox(OffsetDateTime tMin, OffsetDateTime tMax, int srid, Pointer inner) throws SQLException {
		super();
		this.tMin = tMin;
		this.tMax = tMax;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for value dimension (x,y,z) with geodetic coordinates and SRID
	 *
	 * @param pMin       - coordinates for minimum bound
	 * @param pMax       - coordinates for maximum bound
	 * @param isGeodetic - if the coordinates are spherical
	 * @param srid       - spatial reference identifier
	 * @throws SQLException
	 */
	public STBox(Point pMin, Point pMax, boolean isGeodetic, int srid, Pointer inner) throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.isGeodetic = isGeodetic;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for value dimension (x,y,z) with geodetic coordinates, time dimension and SRID
	 *
	 * @param pMin       - coordinates for minimum bound
	 * @param tMin       - minimum time dimension
	 * @param pMax       - coordinates for maximum bound
	 * @param tMax       - maximum time dimension
	 * @param isGeodetic - if the coordinates are spherical
	 * @param srid       - spatial reference identifier
	 * @throws SQLException
	 */
	public STBox(Point pMin, OffsetDateTime tMin, Point pMax, OffsetDateTime tMax, boolean isGeodetic, int srid, Pointer inner)
			throws SQLException {
		super();
		this.pMin = pMin;
		this.pMax = pMax;
		this.tMin = tMin;
		this.tMax = tMax;
		this.isGeodetic = isGeodetic;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}
	
	/**
	 * The constructor for geodetic box with only time dimension and SRID
	 *
	 * @param tMin       - minimum time dimension
	 * @param tMax       - maximum time dimension
	 * @param isGeodetic - if the coordinates are spherical
	 * @param srid       - spatial reference identifier
	 * @throws SQLException
	 */
	public STBox(OffsetDateTime tMin, OffsetDateTime tMax, boolean isGeodetic, int srid, Pointer inner) throws SQLException {
		super();
		this.tMin = tMin;
		this.tMax = tMax;
		this.isGeodetic = isGeodetic;
		this.srid = srid;
		validate();
		if (inner != null) {
			this._inner = inner;
		}
	}

	/**
	 * Returns a copy of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_copy</li>
	 * @return a STBox instance
	 * @throws SQLException
	 */
	public STBox copy() throws SQLException {
		return new STBox(functions.stbox_copy(this._inner));
	}

	/**
	 * Returns a "STBox" from its WKB representation in hex-encoded ASCII.
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_from_hexwkb</li>
	 * @param hexwkb WKB representation in hex-encoded ASCII
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public static STBox from_hexwkb(String hexwkb) throws SQLException {
		Pointer result = functions.stbox_from_hexwkb(hexwkb);
		return new STBox(result);
	}
	
	
	//To modify for pointer
    /*
    public String as_hexwkb(){
        return stbox_as_hexwkb(this._inner,-1,this._inner.size())[0];
    }
   */

	/**
	 * Returns a "STBox" from a "Geometry".
	 * <p>
	 *         MEOS Functions:
	 *             <li>gserialized_in, geo_to_stbox </li>
	 *
	 * @param geom A `Geometry` instance.
	 * @param geodetic Whether to create a geodetic or geometric `STBox`.
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public static STBox from_geometry(Geometry geom, boolean geodetic) throws SQLException {
		return new STBox(functions.geo_to_stbox(ConversionUtils.geo_to_gserialized(geom,geodetic)));
	}

	public static STBox from_geometry(Geometry geom) throws SQLException {
		boolean geodetic = false;
		return new STBox(functions.geo_to_stbox(ConversionUtils.geo_to_gserialized(geom,geodetic)));
	}





    /*
    //Add the datetime_to_timestamptz function to the class
    public STBox from_datetime(Pointer time){
        Pointer result;
        //Add the datetime_to_timestamptz function to the class
        result = timestamp_to_stbox(datetime_to_timestamptz(time));
        return new STBox(result);
    }

     */


	/**
	 * Returns a "STBox" from a "Time" instance.
	 *<p>
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>timestamp_to_stbox</li>
	 *             <li>timestampset_to_stbox</li>
	 *             <li>period_to_stbox</li>
	 *             <li>periodset_to_stbox</li>
	 *         </ul>
	 * @param other a Time instance
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public static STBox from_time(Time other) throws SQLException{
		STBox returnValue;
		switch (other){
			case Period p -> returnValue = new STBox(functions.period_to_stbox(p.get_inner()));
			case PeriodSet ps -> returnValue = new STBox(functions.periodset_to_stbox(ps.get_inner()));
			case TimestampSet ts -> returnValue = new STBox(functions.timestampset_to_stbox(ts.get_inner()));
			default -> throw new TypeNotPresentException(other.getClass().toString(), new Throwable("Operation not supported with this type"));
		}
		return returnValue;
	}
	
	/*
	public STBox from_expanding_bounding_box_geom(Geometry value, float expansion) {
		Pointer gs = functions.gserialized_in(value.toString(), -1);
		Pointer result = functions.geo_expand_spatial(gs, expansion);
		return new STBox(result);
	}

	 */


    /* Modify Tpoint type
    public STBox from_expanding_bounding_box_tpoint(TPoint value, float expansion){
        Pointer result = tpoint_expand_spatial(value._inner, expansion);
        return new STBox(result);
    }

     */

    /*
    //Modify the from geometry datetime function
    public STBox from_space_datetime(Geometry value, Pointer time){
        return from_geometry_datetime(value,time);
    }

    public STBox from_space_period(Geometry value, Pointer time ){
        return from_geometry_period(value,time);
    }

     */


    /*
    //Modify Period type
    public STBox from_geometry_datetime(Geometry geometry, Pointer datetime){
        Pointer gs = gserialized_in(geometry.toString(),-1);
        Pointer result = geo_timestamp_to_stbox(gs,datetime_to_timestamptz(datetime));
        return new STBox(result);
    }

    public STBox from_geometry_period(Geometry geometry, Pointer period){
        Pointer gs = gserialized_in(geometry.toString(),-1);
        Pointer result = geo_period_to_stbox(gs,period._inner);
        return new STBox(result);
    }

    public STBox from_tpoint(TPoint temporal){
        return new STBox(tpoint_to_stbox(temporal._inner));
    }

     */
	
	

    public Geometry to_geometry(int precision) throws ParseException {
        return ConversionUtils.gserialized_to_shapely_geometry(functions.stbox_to_geo(this._inner),precision);
    }




	/** ------------------------- Output ---------------------------------------- */

	/**
	 *  Returns a string representation of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_out</li>
	 * @param max_decimals number of decimals
	 * @return a String instance
	 */
	public String toString(int max_decimals){
		return functions.stbox_out(this._inner,max_decimals);
	}



	/** ------------------------- Conversions ---------------------------------- */

	/**
	 * Returns the temporal dimension of "this" as a "Period" instance.
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_to_period</li>
	 * @return a new Period instance
	 * @throws SQLException
	 */
    public Period to_period() throws SQLException {
        Pointer result = functions.stbox_to_period(this._inner);
        return new Period(result);
    }


	/** ------------------------- Accessors ------------------------------------- */

	/**
	 * Returns whether "this" has a spatial (XY) dimension.
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_hasx</li>
	 * @return True if "this" has a spatial dimension, False otherwise.
	 */
	public boolean has_xy() {
		return functions.stbox_hasx(this._inner);
	}


	/**
	 * Returns whether "this" has a Z dimension.
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_hasz</li>
	 * @return True if "this" has a Z dimension, False otherwise.
	 */
	public boolean has_z() {
		return functions.stbox_hasz(this._inner);
	}


	/**
	 * Returns whether "this" has a time dimension.
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_hast</li>
	 * @return True if "this" has a time dimension, False otherwise.
	 */
	public boolean has_t() {
		return functions.stbox_hast(this._inner);
	}

	/**
	 * Returns whether "this" is geodetic.
	 *<p>
	 *         MEOS Functions:
	 *             <li> stbox_isgeodetic </li>
	 * @return True if "this" is geodetic, False otherwise.
	 */
	public boolean geodetic() {
		return functions.stbox_isgeodetic(this._inner);
	}


	/**
	 * Returns the minimum X coordinate of ``self``.
	 *
	 *         Returns:
	 *             A :class:`float` with the minimum X coordinate of ``self``.
	 *
	 *         MEOS Functions:
	 *             stbox_xmin
	 * @return
	 */
    public boolean xmin(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_xmin(this._inner, result);
    }
    public boolean ymin(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_ymin(this._inner, result);
    }
    public boolean zmin(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_zmin(this._inner, result);
    }
    public boolean tmin(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_tmin(this._inner, result);
    }
    public boolean xmax(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_xmax(this._inner, result);
    }
    public boolean ymax(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_ymax(this._inner, result);
    }
    public boolean zmax(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_zmax(this._inner, result);
    }
    public boolean tmax(){
		Pointer result = Memory.allocate(runtime,Double.BYTES);
		return false;
        //return functions.stbox_tmax(this._inner, result);
    }

	public Pointer get_inner(){
		return this._inner;
	}


	/** ------------------------- Spatial Reference System ---------------------- */

	/**
	 * Returns the SRID of "this".
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_srid</li>
	 * @return an Integer with the SRID of "this"
	 */
	public int srid(){
		return functions.stbox_srid(this._inner);
	}


	/**
	 * Returns a copy of "this" with the SRID set to "value".
	 *<p>
	 *         MEOS Functions:
	 *             <li>stbox_set_srid</li>
	 * @param value The new SRID.
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public STBox set_srid(int value) throws SQLException {
		return new STBox(functions.stbox_set_srid(this._inner,value));
	}
	

	/** ------------------------- Transformations ------------------------------- */


	public STBox expand_stbox(STBox stbox, STBox other) throws SQLException {
		Pointer result = functions.stbox_copy(this._inner);
		functions.stbox_expand(other._inner, result);
		return new STBox(result);
	}

	public STBox expand_numerical(Number value) throws SQLException {
		STBox result = null;
		if(Integer.class.isInstance(value) || Float.class.isInstance(value)){
			result = new STBox(functions.stbox_expand_space(this.get_inner(), value.floatValue()));
		}
		return result;
	}

	 /*
    //Add the timedelta function
    public STBox expand_timedelta(STBox stbox, Duration duration){
        Pointer result = stbox_expand_temporal(this._inner, timedelta_to_interval(duration));
        return new STBox(result);
    }

     */


	/**
	 * Returns "this" rounded to the given number of decimal digits.
	 *<p>
	 *         MEOS Functions:
	 *            <li>stbox_round </li>
	 *
	 * @param maxdd Maximum number of decimal digits.
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public STBox round(int maxdd) throws SQLException {
		Pointer new_inner = functions.stbox_copy(this._inner);
		functions.stbox_round(new_inner,maxdd);
		return new STBox(new_inner);
	}






	/** ------------------------- Set Operations -------------------------------- */


	/**
	 * Returns the union of "this" with "other". Fails if the union is not contiguous.
	 *<p>
	 *         MEOS Functions:
	 *             <li>union_stbox_stbox</li>
	 * @param other spatiotemporal box to merge with
	 * @param strict included or not
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public STBox union(STBox other, boolean strict) throws SQLException {
		return new STBox(functions.union_stbox_stbox(this._inner, other._inner, strict));
	}


	/**
	 * Returns the union of "this" with "other". Fails if the union is not contiguous.
	 *<p>
	 *         MEOS Functions:
	 *             <li>union_stbox_stbox</li>
	 * @param other spatiotemporal box to merge with
	 * @return a new STBox instance
	 * @throws SQLException
	 */
	public STBox add(STBox other) throws SQLException {
		return this.union(other, true);
	}


	/**
	 * Returns the intersection of "this" with "other".
	 *<p>
	 *         MEOS Functions:
	 *             <li>intersection_stbox_stbox </li>
	 *
	 * @param other temporal object to merge with
	 * @return a new STBox instance if the instersection is not empty, `None` otherwise.
	 * @throws SQLException
	 */
	public STBox intersection(STBox other) throws SQLException {
		return new STBox(functions.intersection_stbox_stbox(this._inner,other.get_inner()));
	}


	/**
	 * Returns the intersection of "this" with "other".
	 *<p>
	 *         MEOS Functions:
	 *             <li>intersection_stbox_stbox </li>
	 *
	 * @param other temporal object to merge with
	 * @return a new STBox instance if the instersection is not empty, `None` otherwise.
	 * @throws SQLException
	 */
	public STBox mul(STBox other) throws SQLException {
		return this.intersection(other);
	}


	/**
	 * Returns whether ``self`` and `other` are adjacent. Two spatiotemporal boxes are adjacent if they share n
	 *         dimensions and the intersection is of at most n-1 dimensions. Note that for `TPoint` instances, the bounding box
	 *         of the temporal point is used.
	 *<p></p>
	 *<p>
	 *         MEOS Functions:
	 *             <li>adjacent_stbox_stbox</li>
	 * @param other The other spatiotemporal object to check adjacency with "this".
	 * @return "true" if "this" and "other" are adjacent, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_adjacent(TemporalObject<?> other) throws SQLException {
		return functions.adjacent_stbox_stbox(this._inner,this._get_box(other,true,true).get_inner());
	}

	/**
	 * Returns whether "this" is contained in "other". Note that for "TPoint" instances, the bounding
	 *         box of the temporal point is used.
	 *<p>
	 *         MEOS Functions:
	 *             <li>contained_stbox_stbox</li>
	 * @param other The spatiotemporal object to check containment with "this.
	 * @return "true" if "this" is contained in "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_contained_in(TemporalObject<?> other) throws SQLException {
		return functions.contained_stbox_stbox(this._inner,this._get_box(other,true,true).get_inner());
	}

	/**
	 * Returns whether "this" contains `content`. Note that for "TPoint" instances, the bounding box of
	 *         the temporal point is used.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>contains_stbox_stbox </li>
	 * @param other The spatiotemporal object to check containment with "this".
	 * @return "true" if "this" contains "other", "false otherwise.
	 * @throws SQLException
	 */
	public boolean contains(TemporalObject<?> other) throws SQLException {
		return functions.contains_stbox_stbox(this._inner,this._get_box(other,true,true).get_inner());
	}

	/**
	 * Returns whether "this" overlaps "other". Note that for "TPoint" instances, the bounding box of
	 *         the temporal point is used.
	 *<p>
	 *         MEOS Functions:
	 *             <li>overlaps_stbox_stbox </li>
	 * @param other The spatiotemporal object to check overlap with "this".
	 * @return "true" if "this" overlaps "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean overlaps(TemporalObject<?> other) throws SQLException {
		return functions.overlaps_stbox_stbox(this._inner,this._get_box(other,true,true).get_inner());
	}

	/**
	 * Returns whether "this" is the same as "other". Note that for "TPoint" instances, the bounding box of
	 *         the temporal point is used.
	 *<p>
	 *         MEOS Functions:
	 *             <li>same_stbox_stbox</li>
	 * @param other The spatiotemporal object to check equality with "this".
	 * @return "true" if "this" is the same as "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_same(TemporalObject<?> other) throws SQLException {
		return functions.same_stbox_stbox(this._inner,this._get_box(other,true,true).get_inner());
	}

	/**
	 * Returns whether "this" is strictly to the left  of "other". Checks the X dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>left_stbox_stbox </li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly to the left of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_left(TemporalObject<?> other) throws SQLException {
		return functions.left_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}

	/**
	 * Returns whether "this" is to the left "other" allowing for overlap. That is, "this" does not extend
	 *         to the right of "other. Checks the X dimension.
	 *<p>
	 *         MEOS Functions:
	 *             <li>overleft_stbox_stbox, tpoint_to_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is to the left of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_left(TemporalObject<?> other) throws SQLException {
		return functions.overleft_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}

	/**
	 * Returns whether "this" is strictly to the right of "other". Checks the X dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>right_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly to the right of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_right(TemporalObject<?> other) throws SQLException {
		return functions.right_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}

	/**
	 * Returns whether "this" is to the right of "other" allowing for overlap. That is, "this" does not
	 *         extend to the left of "other". Checks the X dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overright_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is to the right of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_right(TemporalObject<?> other) throws SQLException {
		return functions.overright_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}

	/**
	 * Returns whether "this" is strictly below "other". Checks the Y dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>below_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly below of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_below(TemporalObject<?> other) throws SQLException {
		return functions.below_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is below "other" allowing for overlap. That is, "this" does not extend
	 *         above "other". Checks the Y dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overbelow_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is below "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_below(TemporalObject<?> other) throws SQLException {
		return functions.overbelow_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is strictly above "other". Checks the Y dimension.
	 *
	 *<p>
	 *         MEOS Functions:
	 *             <li>above_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly above of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_above(TemporalObject<?> other) throws SQLException {
		return functions.above_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is above "other" allowing for overlap. That is, "this" does not extend
	 *         below "other". Checks the Y dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overabove_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is above "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_above(TemporalObject<?> other) throws SQLException {
		return functions.overabove_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is strictly in front of "other". Checks the Z dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>front_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly in front of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_front(TemporalObject<?> other) throws SQLException {
		return functions.front_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is in front of "other" allowing for overlap. That is, "this" does not extend
	 *         behind "other". Checks the Z dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overfront_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is in front of "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_front(TemporalObject<?> other) throws SQLException {
		return functions.overfront_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is strictly behind "other". Checks the Z dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>back_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly behind of "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_behind(TemporalObject<?> other) throws SQLException {
		return functions.back_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 * Returns whether "this" is behind "other" allowing for overlap. That is, "this" does not extend
	 *         in front of "other". Checks the Z dimension.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>overback_stbox_stbox</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is behind of "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_behind(TemporalObject<?> other) throws SQLException {
		return functions.overback_stbox_stbox(this._inner,this._get_box(other).get_inner());
	}


	/**
	 *  Returns whether "this" is strictly before "other". Checks the time dimension.
	 *
	 * <p>
	 *         See Also:
	 * 				{@link Period#is_before(Time)}
	 * 	<p>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly before "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_before(TemporalObject<?> other) throws Exception {
		return this.to_period().is_before(other);
	}


	/**
	 *  Returns whether "this" is before "other" allowing for overlap. That is, "this" does not extend
	 *         after "other". Checks the time dimension.
	 *
	 * <p>
	 *     See Also:
	 * 	 			{@link Period#is_over_or_before(Time)}
	 * 	 </p>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is before "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_before(TemporalObject<?> other) throws Exception {
		return this.to_period().is_over_or_before(other);
	}


	/**
	 * Returns whether "this" is strictly after "other". Checks the time dimension.
	 *
	 * <p>
	 *      See Also:
	 * 	 			{@link Period#is_after(Time)}
	 *     </p>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is strictly after "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_after(TemporalObject<?> other) throws Exception {
		return this.to_period().is_after(other);
	}


	/**
	 * Returns whether "this" is after "other" allowing for overlap. That is, "this does not extend
	 *         before "other". Checks the time dimension.
	 *
	 *  <p>
	 *      See Also:
	 * 				{@link Period#is_over_or_after(Time)}
	 *      </p>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is after "other" allowing for overlap, "false" otherwise.
	 * @throws SQLException
	 */
	public boolean is_over_or_after(TemporalObject<?> other) throws Exception {
		return this.to_period().is_over_or_after(other);
	}

	/** ------------------------- Distance Operations --------------------------- */


	/**
	 * Returns the distance between the nearest points of "this" and "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>nad_stbox_geo</li>
	 *             <li>nad_stbox_stbox</li>
	 *          </ul>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return a Float instance with the distance between the nearest points of "this" and "``other``".
	 */
	/*
	public float nearest_approach_distance_geom(Geometry other) {
		Pointer gs = functions.gserialized_in(other.toString(), -1);
		return (float) functions.nad_stbox_geo(this._inner, gs);
	}

	 */
	
	public float nearest_approach_distance_stbox(STBox other) {
		return (float) functions.nad_stbox_stbox(this._inner, other._inner);
	}



	/** ------------------------- Comparisons ----------------------------------- */

	/**
	 * Returns whether "this" is equal to "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>stbox_eq</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is equal to "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean equals(Box other) throws SQLException{
		boolean result;
		result = other instanceof STBox ? functions.stbox_eq(this._inner,((STBox) other).get_inner()) : false;
		return result;
	}


	/**
	 * Returns whether "this" is not equal to "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>stbox_ne</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is not equal to "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean notEquals(Box other) throws SQLException{
		boolean result;
		result = other instanceof STBox ? functions.stbox_ne(this._inner,((STBox) other).get_inner()) : true;
		return result;
	}

	/**
	 * Returns whether "this" is less than "other". Compares first the SRID, then the time dimension,
	 *         and finally the spatial dimension (X, then Y then Z lower bounds and then the upper bounds).
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>stbox_lt</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is less than "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean lessThan(Box other) throws SQLException{
		if (other instanceof STBox){
			return functions.stbox_lt(this._inner,((STBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}


	/**
	 * Returns whether "this" is less than or equal to "other". Compares first the SRID, then the time dimension,
	 *         and finally the spatial dimension (X, then Y then Z lower bounds and then the upper bounds).
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>stbox_le</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is less than or equal "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean lessThanOrEqual(Box other) throws SQLException{
		if (other instanceof STBox){
			return functions.stbox_le(this._inner,((STBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}


	/**
	 * Returns whether "this" is greater than "other". Compares first the SRID, then the time dimension,
	 *         and finally the spatial dimension (X, then Y then Z lower bounds and then the upper bounds).
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>stbox_gt</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is greater "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean greaterThan(Box other) throws SQLException{
		if (other instanceof STBox){
			return functions.stbox_gt(this._inner,((STBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

	/**
	 *  Returns whether "this" is greater than or equal to "other". Compares first the SRID, then the time dimension,
	 *         and finally the spatial dimension (X, then Y then Z lower bounds and then the upper bounds).
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>stbox_ge</li>
	 * @param other The spatiotemporal object to compare with "this".
	 * @return "true" if "this" is greater or equal "other", "false" otherwise.
	 * @throws SQLException
	 */
	public boolean greaterThanOrEqual(Box other) throws SQLException{
		if (other instanceof STBox){
			return functions.stbox_ge(this._inner,((STBox) other).get_inner());
		}
		else{
			throw new SQLException("Operation not supported with this type.");
		}
	}

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
		int nonEmpty = (int) Arrays.stream(values).filter(x -> !x.isEmpty()).count();
		if (nonEmpty == 2) {
			String[] removedNull = Arrays.stream(values)
					.filter(x -> !x.isEmpty())
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

	
	/**
	 * Compares if the values in time dimension are the same
	 *
	 * @param other - a STBox to compare
	 * @return true if they are equals; otherwise false
	 */
	public boolean tIsEqual(STBox other) {
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

	public boolean get_tmin_inc(){
		return tmin_inc;
	}

	public boolean get_tmax_inc(){
		return tmax_inc;
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
	 *
	 * @throws SQLException
	 */
	private void validate() throws SQLException {
		if (tMin == null ^ tMax == null) {
			throw new SQLException("Both tmin and tmax should have a value.");
		}
		
		if (pMin != null && pMax != null) {
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
	 *
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
	 *
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
