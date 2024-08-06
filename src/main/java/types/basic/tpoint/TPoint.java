package types.basic.tpoint;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.basic.tbool.TBool;
import types.basic.tfloat.TFloat;
import types.basic.tfloat.TFloatSeqSet;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tpoint.tgeog.TGeogPoint;
import types.basic.tpoint.tgeom.TGeomPoint;
import types.boxes.STBox;
import types.collections.geo.GeoSet;
import types.collections.time.Time;
import types.temporal.*;
import functions.functions;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.geom.Point;
import utils.ConversionUtils;

import javax.naming.OperationNotSupportedException;
import java.io.Serializable;

/**
 * Class that represents the MobilityDB type TPoint used for {@link TPointInst}, {@link TPointSeq} and {@link TPointSeqSet}
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public interface TPoint extends Serializable {
	Pointer getPointInner();
	String getCustomType();
	TemporalType getTemporalType();

    /* ------------------------- Output ---------------------------------------- */

	/**
	 * Returns the string representation of the temporal point.
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_out</li>
	 *
	 * @return A new {@link String} representing the temporal point.
	 */
	default String to_string(){
		return functions.tpoint_as_text(getPointInner(),15);
	}


	/**
	 * Returns the temporal point as a WKT string.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_out</li>
	 *
	 * @param decimals The precision of the returned geometry.
	 * @return A new {@link String} representing the temporal point.
	 */
	default String as_wkt(int decimals){
		return functions.tpoint_as_text(getPointInner(),decimals);
	}


	/**
	 * Returns the temporal point as an EWKT string.
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_as_ewkt</li>
	 *
	 * @param decimals The precision of the returned geometry.
	 * @return A new {@link String} representing the temporal point.
	 */
	default String as_ewkt(int decimals){
		return functions.tpoint_as_ewkt(getPointInner(),decimals);
	}


	/**
	 * Returns the trajectory of the temporal point as a GeoJSON string.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>gserialized_as_geojson</li>
	 *
	 * @param option The option to use when serializing the trajectory.
	 * @param precision The precision of the returned geometry.
	 * @param srs The spatial reference system of the returned geometry.
	 * @return A new GeoJSON string representing the trajectory of the temporal point.
	 */
	default String as_geojson(int option, int precision, String srs){
		return functions.gserialized_as_geojson(functions.tpoint_trajectory(getPointInner()),option,precision,srs);
	}


	/**
	 * Returns the trajectory of the temporal point as a Shapely geometry.
	 *
	 *   <p>
	 *
	 *         MEOS Functions:
	 *             <li>gserialized_to_shapely_geometry</li>
	 *
	 * @param precision The precision of the returned geometry.
	 * @return A new {@link Geometry} representing the
	 * 	 *             trajectory.
	 * @throws ParseException
	 */
	default Geometry to_shapely_geometry(int precision) throws ParseException {
		return ConversionUtils.gserialized_to_shapely_geometry(getPointInner(),precision);
	}


    /* ------------------------- Accessors ------------------------------------- */

	/**
	 * Returns the bounding box of the "this".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_to_stbox</li>
	 * @return An {@link STBox} representing the bounding box.
	 */
	default STBox bounding_box_point(){
		return new STBox(functions.tpoint_to_stbox(getPointInner()));
	}

	/**
	 * Returns the start value of the temporal point.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_start_value</li>
	 * @param precision The precision of the returned point.
	 * @return A {@link Point} with the start value.
	 * @throws ParseException
	 */
	default Point start_value(int precision) throws ParseException {
		return ConversionUtils.gserialized_to_shapely_point(functions.tpoint_start_value(getPointInner()),precision);
	}


	/**
	 * Returns the end value of the temporal point.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_end_value</li>
	 * @param precision The precision of the returned point.
	 * @return A {@link Point} with the end value.
	 * @throws ParseException
	 */
	default Point end_value(int precision) throws ParseException {
		return ConversionUtils.gserialized_to_shapely_point(functions.tpoint_end_value(getPointInner()),precision);
	}


	/**
	 * Returns the length of the trajectory.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_length</li>
	 * @return A {@link Float} with the length of the trajectory.
	 */
	default float length(){
		return (float) functions.tpoint_length(getPointInner());
	}


	/**
	 * Returns the cumulative length of the trajectory.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tpoint_cumulative_length</li>
	 * @return A {@link TFloat} with the cumulative length of the trajectory.
	 */
	default TFloat cumulative_length(){
		return (TFloat) Factory.create_temporal(functions.tpoint_cumulative_length(getPointInner()),"Float",getTemporalType());
	}


	/**
	 * Returns the speed of the temporal point.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tpoint_speed</li>
	 * @return A {@link TFloat} with the speed of the temporal point.
	 */
	default TFloat speed(){
		return (TFloat) Factory.create_temporal(functions.tpoint_speed(getPointInner()),"Float",getTemporalType());
	}


	/**
	 * Returns the x coordinate of the temporal point.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_get_x</li>
	 * @return A {@link TFloat} with the x coordinate of the temporal point.
	 */
	default TFloat x(){
		return (TFloat) Factory.create_temporal(functions.tpoint_get_x(getPointInner()),"Float",getTemporalType());

	}


	/**
	 * Returns the y coordinate of the temporal point.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_get_y</li>
	 * @return A {@link TFloat} with the y coordinate of the temporal point.
	 */
	default TFloat y(){
		return (TFloat) Factory.create_temporal(functions.tpoint_get_y(getPointInner()),"Float",getTemporalType());

	}


	/**
	 * Returns the z coordinate of the temporal point.
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tpoint_get_z</li>
	 * @return A {@link TFloat} with the z coordinate of the temporal point.
	 */
	default TFloat z(){
		return (TFloat) Factory.create_temporal(functions.tpoint_get_z(getPointInner()),"Float",getTemporalType());

	}


	/**
	 * Returns whether the temporal point has a z coordinate.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_start_value</li>
	 * @return A {@link Boolean} indicating whether the temporal point has a z coordinate.
	 */
	default boolean has_z(){
		return this.bounding_box_point().has_z();
	}


	/**
	 * Returns whether the temporal point is simple. That is, whether it does not self-intersect.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_is_simple</li>
	 * @return A {@link Boolean} indicating whether the temporal point is simple.
	 */
	default boolean is_simple(){
		return functions.tpoint_is_simple(getPointInner());
	}


	/**
	 * Returns the temporal bearing between the temporal point and "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>bearing_tpoint_point</li>
	 *             <li>bearing_tpoint_tpoint</li>
	 *
	 * @param other An object to check the bearing to.
	 * @return A new {@link TFloat} indicating the temporal bearing between the temporal point and "other".
	 */
	default TFloat bearing_point(TPoint other){
		return (TFloat) Factory.create_temporal(functions.bearing_tpoint_tpoint(getPointInner(),other.getPointInner()),"Float",getTemporalType());

	}


	/**
	 * Returns the azimuth of the temporal point between the start and end locations.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_direction</li>
	 * @return A new {@link TFloatSeqSet} indicating the direction of the temporal point.
	 */
	default TFloatSeqSet direction(){
		return (TFloatSeqSet) Factory.create_temporal(functions.tpoint_direction(getPointInner()),"Float",getTemporalType());
	}


	/**
	 * Returns the temporal azimuth of the temporal point.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_azimuth</li>
	 * @return A new {@link TFloatSeqSet} indicating the temporal azimuth of the temporal point.
	 */
	default TFloatSeqSet azimuth(){
		return (TFloatSeqSet) Factory.create_temporal(functions.tpoint_azimuth(getPointInner()),"Float",getTemporalType());
	}


	/**
	 * Returns the angular_difference of the temporal point.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_angular_difference</li>
	 * @return A new {@link TFloatSeqSet} indicating the temporal angular_difference of the temporal point.
	 */
	default TFloatSeqSet angular_difference(){
		return (TFloatSeqSet) Factory.create_temporal(functions.tpoint_angular_difference(getPointInner()),"Float", TemporalType.TEMPORAL_SEQUENCE_SET);
	}


	/**
	 * Returns the time weighted centroid of the temporal point.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tpoint_twcentroid</li>
	 * @param precision The precision of the returned geometry.
	 * @return A new {@link Geometry} indicating the time weighted centroid of the temporal point.
	 * @throws ParseException
	 */
	default Point time_weighted_centroid(int precision) throws ParseException {
		return (Point) ConversionUtils.gserialized_to_shapely_geometry(functions.tpoint_twcentroid(getPointInner()),precision);
	}



    /* ------------------------- Spatial Reference System ---------------------- */

	/**
	 * Returns the SRID.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tpoint_srid</li>
	 * @return An {@link Integer} representing the SRID.
	 */
	default int srid(){
		return functions.tpoint_srid(getPointInner());
	}


	/**
	 * Returns a new TPoint with the given SRID.
	 * <p>
	 *     MEOS Functions:
	 *             <li>tpoint_set_srid</li>
	 * @param srid int value
	 * @return Returns a new TPoint with the given SRID.
	 */
	default TPoint set_srid(int srid){
		return (TPoint) Factory.create_temporal(functions.tpoint_set_srid(getPointInner(),srid),getCustomType(),getTemporalType());
	}



    /* ------------------------- Transformations ------------------------------- */


	/**
	 * Round the coordinate values to a number of decimal places.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_round</li>
	 * @param max_decimals number of decimals
	 * @return A new {@link TPoint} object.
	 */
	default TPoint round(int max_decimals){
		return (TPoint) Factory.create_temporal(functions.tpoint_round(getPointInner(),max_decimals),getCustomType(),getTemporalType());
	}


	/**
	 * Expands "this" with "other".
	 *         The result is equal to "this" but with the spatial dimensions
	 *         expanded by "other" in all directions.
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>tpoint_expand_space</li>
	 * @param other The object to expand "this" with.
	 * @return A new {@link STBox} instance.
	 */
	default STBox expand(float other){
		return new STBox(functions.tpoint_expand_space(getPointInner(),other));
	}



    /* ------------------------- Restrictions ---------------------------------- */


	/**
	 * Returns a new temporal object with the values of "this" restricted to "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *         <ul>
	 *             <li>tpoint_at_value</li>
	 *             <li>tpoint_at_stbox</li>
	 *             <li>temporal_at_values</li>
	 *             <li>temporal_at_timestamp</li>
	 *             <li>temporal_at_timestampset</li>
	 *             <li>temporal_at_period</li>
	 *             <li>temporal_at_periodset</li>
	 *         </ul>
	 * @param other An object to restrict the values of "this" to.
	 * @return A new {@link TPoint} with the values of "this" restricted to "other".
	 * @throws OperationNotSupportedException
	 */
	default TPoint at(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			boolean geodetic = this instanceof TGeomPoint;
			return (TPoint) Factory.create_temporal(functions.tpoint_at_value(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, geodetic)),getCustomType(),getTemporalType());
		} else if (other instanceof GeoSet) {
			return (TPoint) Factory.create_temporal(functions.temporal_at_values(getPointInner(),((GeoSet) other).get_inner()),getCustomType(),getTemporalType());
		} else if (other instanceof STBox) {
			return (TPoint) Factory.create_temporal(functions.tpoint_at_stbox(getPointInner(),((STBox) other).get_inner(),true),getCustomType(),getTemporalType());
		}
		else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns a new temporal object with the values of "this" restricted to the complement of "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_minus_value</li>
	 *             <li>tpoint_minus_stbox</li>
	 *             <li>temporal_minus_values</li>
	 *             <li>temporal_minus_timestamp</li>
	 *             <li>temporal_minus_timestampset</li>
	 *             <li>temporal_minus_period</li>
	 *             <li>temporal_minus_periodset</li>
	 * @param other An object to restrict the values of "this" to the complement of.
	 * @return A {@link TPoint} with the values of "this" restricted to the complement of "other".
	 * @throws OperationNotSupportedException
	 */
	default TPoint minus(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			boolean geodetic = this instanceof TGeomPoint;
			return (TPoint) Factory.create_temporal(functions.tpoint_minus_value(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, geodetic)),getCustomType(),getTemporalType());
		} else if (other instanceof GeoSet) {
			return (TPoint) Factory.create_temporal(functions.temporal_minus_values(getPointInner(),((GeoSet) other).get_inner()),getCustomType(),getTemporalType());
		} else if (other instanceof STBox) {
			return (TPoint) Factory.create_temporal(functions.tpoint_minus_stbox(getPointInner(),((STBox) other).get_inner(),true),getCustomType(),getTemporalType());
		}
		else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


    /* ------------------------- Position Operations --------------------------- */


	/**
	 * Returns whether the bounding box of "this" is left to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if left, False otherwise.
	 */
	default boolean is_left(TemporalObject other){
		return this.bounding_box_point().is_left(other);
	}



	/**
	 * Returns whether the bounding box of "this" is over or left to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or left, False otherwise.
	 */
	default boolean is_over_or_left(TemporalObject other){
		return this.bounding_box_point().is_over_or_left(other);
	}


	/**
	 * Returns whether the bounding box of "this" is right to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if right, False otherwise.
	 */
	default boolean is_right(TemporalObject other){
		return this.bounding_box_point().is_right(other);
	}




	/**
	 * Returns whether the bounding box of "this" is over or right to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or right, False otherwise.
	 */
	default boolean is_over_or_right(TemporalObject other){
		return this.bounding_box_point().is_over_or_right(other);
	}



	/**
	 * Returns whether the bounding box of "this" is below to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if below, False otherwise.
	 */
	default boolean is_below(TemporalObject other){
		return this.bounding_box_point().is_below(other);
	}


	/**
	 * Returns whether the bounding box of "this" is over or below to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or below, False otherwise.
	 */
	default boolean is_over_or_below(TemporalObject other){
		return this.bounding_box_point().is_over_or_below(other);
	}





	/**
	 * Returns whether the bounding box of "this" is above to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if above, False otherwise.
	 */
	default boolean is_above(TemporalObject other){
		return this.bounding_box_point().is_above(other);
	}



	/**
	 * Returns whether the bounding box of "this" is over or above to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or above, False otherwise.
	 */
	default boolean is_over_or_above(TemporalObject other){
		return this.bounding_box_point().is_over_or_above(other);
	}




	/**
	 * Returns whether the bounding box of "this" is front to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if front, False otherwise.
	 */
	default boolean is_front(TemporalObject other){
		return this.bounding_box_point().is_front(other);
	}





	/**
	 * Returns whether the bounding box of "this" is over or front to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_before(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or front, False otherwise.
	 */
	default boolean is_over_or_front(TemporalObject other){
		return this.bounding_box_point().is_over_or_front(other);
	}





	/**
	 * Returns whether the bounding box of "this" is behind to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if behind, False otherwise.
	 */
	default boolean is_behind(TemporalObject other){
		return this.bounding_box_point().is_behind(other);
	}





	/**
	 * Returns whether the bounding box of "this" is over or behind to the bounding box of "other".
	 *
	 * <p>
	 *
	 *         See Also:
	 *             {@link types.collections.time.Period#is_over_or_after(TemporalObject)}
	 * @param other A box or a temporal object to compare to "this".
	 * @return True if over or behind, False otherwise.
	 */
	default boolean is_over_or_behind(TemporalObject other){
		return this.bounding_box_point().is_over_or_behind(other);
	}



    /* ------------------------- Ever Spatial Relationships -------------------- */


	/**
	 * Returns whether the temporal point is ever contained by "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>econtains_geo_tpoint</li>
	 * @param other An object to check for containing "this".
	 * @return  A {@link Boolean} indicating whether the temporal point is ever contained by "other".
	 * @throws OperationNotSupportedException
	 */
	default boolean is_ever_contained_in(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return 1 == functions.econtains_geo_tpoint(ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint), getPointInner());
		} else if (other instanceof STBox) {
			return 1 == functions.econtains_geo_tpoint(functions.stbox_to_geo(((STBox) other).get_inner()),getPointInner());
		}
		else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns whether the temporal point is ever disjoint from "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>edisjoint_tpoint_geo</li>
	 *             <li>edisjoint_tpoint_tpoint</li>
	 * @param other An object to check for disjointness with.
	 * @return  A {@link Boolean} indicating whether the temporal point is ever disjoint from "other".
	 * @throws OperationNotSupportedException
	 */
	default boolean is_ever_disjoint(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return 1 == functions.edisjoint_tpoint_geo(getPointInner(), ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint));
		} else if (other instanceof STBox) {
			return 1 == functions.edisjoint_tpoint_geo(getPointInner(), functions.stbox_to_geo(((STBox) other).get_inner()));
		} else if (other instanceof TPoint) {
			return 1 == functions.edisjoint_tpoint_tpoint(getPointInner(), ((TPoint) other).getPointInner());
		} else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns whether the temporal point is ever within "distance" of "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>edwithin_tpoint_geo</li>
	 *             <li>edwithin_tpoint_tpoint</li>
	 * @param other An object to check the distance to.
	 * @param distance The distance to check in units of the spatial reference system.
	 * @return A {@link Boolean} indicating whether the temporal point is ever within "distance" of "other".
	 * @throws OperationNotSupportedException
	 */
	default boolean is_ever_within_distance(Object other, float distance) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return 1 == functions.edwithin_tpoint_geo( getPointInner(), ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint), distance);
		} else if (other instanceof STBox) {
			return 1 == functions.edwithin_tpoint_geo(getPointInner(), functions.stbox_to_geo(((STBox) other).get_inner()), distance);
		} else if (other instanceof TPoint) {
			return 1 == functions.edwithin_tpoint_tpoint(getPointInner(), ((TPoint) other).getPointInner(), distance);
		} else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns whether the temporal point ever intersects "other".
	 *
	 * <P>
	 *
	 *         MEOS Functions:
	 *             <li>eintersects_tpoint_geo</li>
	 *             <li>eintersects_tpoint_tpoint</li>
	 * @param other An object to check for intersection with.
	 * @return A {@link Boolean} indicating whether the temporal point ever intersects "other".
	 * @throws OperationNotSupportedException
	 */
	default boolean ever_intersects(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return 1 == functions.eintersects_tpoint_geo( getPointInner(), ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint));
		} else if (other instanceof STBox) {
			return 1 == functions.eintersects_tpoint_geo(getPointInner(), functions.stbox_to_geo(((STBox) other).get_inner()));
		} else if (other instanceof TPoint) {
			return 1 == functions.eintersects_tpoint_tpoint(getPointInner(), ((TPoint) other).getPointInner());
		} else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns whether the temporal point ever touches "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>etouches_tpoint_geo</li>
	 * @param other An object to check for touching with.
	 * @return A {@link Boolean} indicating whether the temporal point ever touches "other".
	 * @throws OperationNotSupportedException
	 */
	default boolean ever_touches(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return 1 == functions.etouches_tpoint_geo( getPointInner(), ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint));
		} else if (other instanceof STBox) {
			return 1 == functions.etouches_tpoint_geo(getPointInner(), functions.stbox_to_geo(((STBox) other).get_inner()));
		}  else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


    /* ------------------------- Temporal Spatial Relationships ---------------- */


	/**
	 * Returns a new temporal boolean indicating whether the temporal point is contained by "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tcontains_geo_tpoint</li>
	 * @param other An object to check for containing "this".
	 * @return A {@link TBool} indicating whether the temporal point is contained by "other".
	 * @throws OperationNotSupportedException
	 */
	default TBool is_spatially_contained_in(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TBool) Factory.create_temporal(functions.tcontains_geo_tpoint(ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint), getPointInner(),false,false), "Boolean", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TBool) Factory.create_temporal(functions.tcontains_geo_tpoint(functions.stbox_to_geo(((STBox) other).get_inner()), getPointInner(), false,false), "Boolean", getTemporalType()  );
		}  else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns a new temporal boolean indicating whether the temporal point intersects "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tdisjoint_tpoint_geo</li>
	 * @param other An object to check for intersection with.
	 * @return A {@link TBool} indicating whether the temporal point intersects "other".
	 * @throws OperationNotSupportedException
	 */
	default TBool disjoint(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TBool) Factory.create_temporal(functions.tdisjoint_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint),false,false), "Boolean", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TBool) Factory.create_temporal(functions.tdisjoint_tpoint_geo(getPointInner(),functions.stbox_to_geo(((STBox) other).get_inner()), false,false), "Boolean", getTemporalType()  );
		}  else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns a new temporal boolean indicating whether the temporal point is within "distance" of "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>tdwithin_tpoint_geo</li>
	 *             <li>tdwithin_tpoint_tpoint</li>
	 * @param other An object to check the distance to.
	 * @param distance The distance to check in units of the spatial reference system.
	 * @return A {@link TBool} indicating whether the temporal point is within "distance" of "other".
	 * @throws OperationNotSupportedException
	 */
	default TBool within_distance(Object other, float distance) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TBool) Factory.create_temporal(functions.tdwithin_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint), distance, false,false), "Boolean", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TBool) Factory.create_temporal(functions.tdwithin_tpoint_geo(getPointInner(),functions.stbox_to_geo(((STBox) other).get_inner()), distance,false,false), "Boolean", getTemporalType()  );
		} else if(other instanceof TPoint){
			return (TBool) Factory.create_temporal(functions.tdwithin_tpoint_tpoint(getPointInner(),((TPoint) other).getPointInner(), distance,false,false), "Boolean", getTemporalType()  );
		}else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns a new temporal boolean indicating whether the temporal point intersects "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tintersects_tpoint_geo</li>
	 * @param other An object to check for intersection with.
	 * @return A {@link TBool} indicating whether the temporal point intersects "other".
	 * @throws OperationNotSupportedException
	 */
	default TBool intersects(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TBool) Factory.create_temporal(functions.tintersects_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint),false,false), "Boolean", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TBool) Factory.create_temporal(functions.tintersects_tpoint_geo(getPointInner(),functions.stbox_to_geo(((STBox) other).get_inner()), false,false), "Boolean", getTemporalType()  );
		}  else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns a new temporal boolean indicating whether the temporal point touches "other".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>ttouches_tpoint_geo</li>
	 * @param other An object to check for touching with.
	 * @return A {@link TBool} indicating whether the temporal point touches "other".
	 * @throws OperationNotSupportedException
	 */
	default TBool touches(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TBool) Factory.create_temporal(functions.ttouches_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint),false,false), "Boolean", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TBool) Factory.create_temporal(functions.ttouches_tpoint_geo(getPointInner(),functions.stbox_to_geo(((STBox) other).get_inner()), false,false), "Boolean", getTemporalType()  );
		}  else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}

    /* ------------------------- Distance Operations --------------------------- */


	/**
	 * Returns the temporal distance between the temporal point and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>distance_tpoint_point</li>
	 *             <li>distance_tpoint_tpoint</li>
	 * @param other An object to check the distance to.
	 * @return A new {@link TFloat} indicating the temporal distance between the temporal point and "other".
	 * @throws OperationNotSupportedException
	 */
	default TFloat distance(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TFloat) Factory.create_temporal(functions.distance_tpoint_point(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint)), "Float", getTemporalType() ) ;
		} else if (other instanceof STBox) {
			return (TFloat) Factory.create_temporal(functions.distance_tpoint_point(getPointInner(),functions.stbox_to_geo(((STBox) other).get_inner())), "Float", getTemporalType()  );
		} else if(other instanceof TPoint){
			return (TFloat) Factory.create_temporal(functions.distance_tpoint_tpoint(getPointInner(),((TPoint) other).getPointInner()), "Float", getTemporalType()  );
		}else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns the nearest approach distance between the temporal point and "other".
	 *
	 * <p>
	 *         MEOS Functions:
	 *             <li>nad_tpoint_geo</li>
	 *             <li>nad_tpoint_stbox</li>
	 *             <li>nad_tpoint_tpoint</li>
	 * @param other An object to check the nearest approach distance to.
	 * @return A {@link Float} indicating the nearest approach distance between the temporal point and "other".
	 * @throws OperationNotSupportedException
	 */
	default float nearest_approach_distance(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (float) functions.nad_tpoint_geo( getPointInner(), ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint));
		} else if (other instanceof STBox) {
			return (float) functions.nad_tpoint_stbox(getPointInner(), functions.stbox_to_geo(((STBox) other).get_inner()));
		} else if (other instanceof TPoint) {
			return (float) functions.nad_tpoint_tpoint(getPointInner(), ((TPoint) other).getPointInner());
		} else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}


	/**
	 * Returns the nearest approach instant between the temporal point and "other".
	 *
	 *  <p>
	 *         MEOS Functions:
	 *             <li>nai_tpoint_geo</li>
	 *             <li>nai_tpoint_tpoint</li>
	 * @param other An object to check the nearest approach instant to.
	 * @return A new temporal instant indicating the nearest approach instant between the temporal point and "other".
	 * @throws OperationNotSupportedException
	 */
	default TInstant nearest_approach_instant(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TInstant) Factory.create_temporal(functions.nai_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint)), getCustomType(), getTemporalType() ) ;
		} else if(other instanceof TPoint){
			return (TInstant) Factory.create_temporal(functions.nai_tpoint_tpoint(getPointInner(),((TPoint) other).getPointInner()), getCustomType(), getTemporalType()  );
		}else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}



	/*
	public default Geometry shortest_line(Object other) throws OperationNotSupportedException {
		if (other instanceof Geometry){
			return (TInstant) Factory.create_temporal(functions.nai_tpoint_geo(getPointInner(),ConversionUtils.geo_to_gserialized((Geometry) other, this instanceof TGeogPoint)), getCustomType(), getTemporalType() ) ;
		} else if(other instanceof TPoint){
			return (TInstant) Factory.create_temporal(functions.nai_tpoint_tpoint(getPointInner(),((TPoint) other).getPointInner()), getCustomType(), getTemporalType()  );
		}else{
			throw new OperationNotSupportedException("Operand not supported");
		}
	}

	 */





}
