package types.basic.tpoint.tgeom;


import functions.functions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.locationtech.jts.io.ParseException;
import types.basic.tbool.TBool;
import types.basic.tpoint.tgeog.TGeogPoint;
import types.basic.tpoint.tgeog.TGeogPointInst;
import types.basic.tpoint.tgeog.TGeogPointSeq;
import types.basic.tpoint.tgeog.TGeogPointSeqSet;
import types.collections.geo.GeographySet;
import types.collections.geo.GeometrySet;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;
import types.collections.time.Time;
import types.collections.time.tstzspanset;
import types.temporal.Factory;
import types.temporal.TInterpolation;
import types.temporal.Temporal;
import types.basic.tpoint.TPoint;
import types.temporal.TemporalType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import utils.ConversionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the MobilityDB type TGeomPoint used for {@link TGeomPointInst}, {@link TGeomPointSeq} and {@link TGeomPointSeqSet}
 * @author ARIJIT SAMAL
 */
public interface TGeomPoint extends TPoint {
	String customType = "Geom";
	Pointer getPointInner();
	String getCustomType();
	TemporalType getTemporalType();

    /*  ------------------------- Output ---------------------------------------- */


	/**
	 * Creates a temporal geometric point from a base geometry and the time
	 *         frame of another temporal object.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_from_base_temp</li>
	 * @param value The base geometry.
	 * @param base The temporal object defining the time frame
	 * @return A new {@link TGeomPoint} object.
	 */
	default TGeomPoint from_base_temporal(Geometry value, Temporal base){
		return (TGeomPoint) Factory.create_temporal(functions.tpoint_from_base_temp(ConversionUtils.geometry_to_gserialized(value),base.getInner()),getCustomType(),getTemporalType());
	}


	/**
	 * Creates a temporal geometric point from a base geometry and a time value.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpointinst_make</li>
	 *             <li>tpointseq_from_base_tstzspanset</li>
	 *             <li>tpointseq_from_base_tstzset</li>
	 *             <li>tpointseqset_from_base_tstzspan</li>
	 *
	 * @param value The base geometry.
	 * @param base The time value.
	 * @param interp  The interpolation method.
	 * @return A new {@link TGeogPoint} object.
	 */
	static TGeomPoint from_base_time(Geometry value, Time base, TInterpolation interp){
		if (base instanceof tstzset){
			return new TGeomPointSeq(functions.tpointseq_from_base_tstzset(ConversionUtils.geometry_to_gserialized(value), ((tstzset) base).get_inner()));
		} else if (base instanceof tstzspan) {
			return new TGeomPointSeqSet(functions.tpointseq_from_base_tstzspan(ConversionUtils.geometry_to_gserialized(value), ((tstzspan) base).get_inner(), interp.getValue()));
		} else if (base instanceof tstzspanset) {
			return new TGeomPointSeq(functions.tpointseq_from_base_tstzset(ConversionUtils.geometry_to_gserialized(value), ((tstzspanset) base).get_inner()));
		}
		else{
			throw new UnsupportedOperationException("Operation not supported with type " + base.getClass());
		}
	}



    /* ------------------------- Conversions ---------------------------------- */


	/**
	 * Returns a copy of "this" converted to geographic coordinates.
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tgeompoint_to_tgeogpoint</li>
	 * @return A new {@link TGeogPoint} object.
	 */
	default TGeogPoint to_geographic(){
		return (TGeogPoint) Factory.create_temporal(functions.tgeompoint_to_tgeogpoint(getPointInner()),getCustomType(),getTemporalType());

	}

    /* ------------------------- Ever and Always Comparisons ------------------- */


	/**
	 * Returns whether "this" is always equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_always_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "this" is always equal to "value", False otherwise.
	 */
	default boolean always_equal(Geometry value){
		return functions.always_eq_tpoint_point(getPointInner(),ConversionUtils.geometry_to_gserialized(value)) > 0;

	}

	/**
	 * Returns whether "this" is always different to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_ever_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "this" is always different to "value", False otherwise.
	 */
	default boolean always_not_equal(Geometry value){
		return (functions.always_ne_tpoint_point(getPointInner(),ConversionUtils.geometry_to_gserialized(value)) > 0);
	}


	/**
	 * Returns whether "this" is ever equal to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_ever_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "this" is ever equal to "value", False otherwise.
	 */
	default boolean ever_equal(Geometry value){
		return  functions.ever_eq_tpoint_point(getPointInner(),ConversionUtils.geometry_to_gserialized(value)) > 0;
	}


	/**
	 * Returns whether "this" is ever different to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_always_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "this" is ever different to "value", False otherwise.
	 */
	default boolean ever_not_equal(Geometry value){
		return functions.ever_ne_tpoint_point(getPointInner(),ConversionUtils.geometry_to_gserialized(value)) > 0;

	}


	/**
	 * Returns whether "this" is never equal to "value".
	 *
	 * <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_ever_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "this" is never equal to "value", False otherwise.
	 */
	default boolean never_equal(Geometry value){
		return ! (this.ever_equal(value));
	}


	/**
	 * Returns whether "self" is never different to "value".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tpoint_always_eq</li>
	 * @param value The geometry to compare with.
	 * @return True if "self" is never different to "value", False otherwise.
	 */
	default boolean never_not_equal(Geometry value){
		return !(this.ever_not_equal(value));
	}


    /* ------------------------- Temporal Comparisons -------------------------- */


	/**
	 * Returns the temporal equality relation between "this" and "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>teq_tpoint_point</li>
	 *             <li>teq_temporal_temporal</li>
	 * @param other A temporal object to compare to "this".
	 * @return A {@link TBool} with the result of the temporal equality relation.
	 */
	default TBool temporal_equal(Point other){
		return (TBool) Factory.create_temporal(functions.teq_tpoint_point(getPointInner(), ConversionUtils.geometry_to_gserialized(other)),getCustomType(),getTemporalType());
	}




	/**
	 * Returns the temporal inequality relation between "this" and "other".
	 *
	 *  <p>
	 *
	 *         MEOS Functions:
	 *             <li>tne_tpoint_point</li>
	 *             <li>tne_temporal_temporal</li>
	 * @param other A temporal object to compare to "this".
	 * @return A {@link TBool} with the result of the temporal inequality relation.
	 */
	default TBool temporal_not_equal(Point other){
		return (TBool) Factory.create_temporal(functions.tne_tpoint_point(getPointInner(), ConversionUtils.geometry_to_gserialized(other)),getCustomType(),getTemporalType());
	}

	default GeometrySet value_set(int precision) throws ParseException {
		// Create a JNR-FFI runtime instance
		Runtime runtime = Runtime.getSystemRuntime();
		// Allocate memory for an integer (4 bytes) but do not set a value
		Pointer intPointer = Memory.allocate(Runtime.getRuntime(runtime), 4);
		Pointer resPointer= functions.tpoint_values(this.getPointInner(), intPointer);
		List<TPoint> pointList= new ArrayList<>();
		int count= intPointer.getInt(Integer.BYTES);
		StringBuilder sb = null;
		sb.append("{");
		for(int i=0;i<count;i++) {
			Point p= ConversionUtils.gserialized_to_shapely_point(resPointer.getPointer((long) i *Long.BYTES), precision);
			sb.append(p);
			if(i<count-1){
				sb.append(", ");
			}
		}
		sb.append("}");
		System.out.println(sb.toString());
		return new GeometrySet(sb.toString());
	}


    /* ------------------------- Database Operations --------------------------- */

}
