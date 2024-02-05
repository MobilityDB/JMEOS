package types.collections.geo;
import functions.functions;
import jnr.ffi.Pointer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import types.collections.base.Set;
import utils.ConversionUtils;


/**
 * Abstract class representing a set of geo object inheriting from set type.
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class GeoSet extends Set<Geometry> {
    protected Pointer _inner;

    public GeoSet(){
        super();

    }

    /**
     * Pointer constructor
     * @param inner Pointer
     */
    public GeoSet(Pointer inner){
        super(inner);
        this._inner = createInner(inner);
    }

    /**
     * The string constructor
     *
     * @param value - the string with the TBoolInst value
     */
    public GeoSet(String value){
        super(value);
        this._inner = createStringInner(value);
    }

    /* ------------------------- Constructors ---------------------------------- */

    public abstract Pointer createInner(Pointer inner);
    public abstract Pointer createStringInner(String str);

    public static GeoSet factory(String type, Pointer inner){
        if (type.equals("Geom")){
            return new GeometrySet(inner);
        } else if (type.equals("Geog")) {
            return new GeographySet(inner);
        }
        return null;
    }

    /* ------------------------- Output ---------------------------------------- */


    /**
     * Return the string representation of the content of "this".
     *  <p>
     *         MEOS Functions:
     *             <li>geoset_out</li>
     * @return A new {@link String} instance
     */
    public String toString(){
        int max_decimals = 15;
        return functions.geoset_out(this._inner,max_decimals);
    }



    /**
     * Returns the EWKT representation of "this".
     *
     *
     *         MEOS Functions:
     *             <li>geoset_as_ewkt</li>
     * @return A {@link String} instance.
     */
    protected String as_ewkt(){
        int max_decimals = 15;
        return functions.geoset_as_ewkt(this._inner,max_decimals);
    }

    /**
     * Returns the WKT representation of "this".
     *
     *
     *         MEOS Functions:
     *             <li>geoset_as_text</li>
     * @return A {@link String} instance.
     */
    protected String as_wkt(){
        int max_decimals = 15;
        return functions.geoset_as_text(this._inner,max_decimals);
    }

    /**
     * Returns the WKT representation of "this".
     *
     *         MEOS Functions:
     *             <li>geoset_as_text</li>
     *
     * @return A {@link String} instance.
     */
    protected String as_text(){
        int max_decimals = 15;
        return functions.geoset_as_text(this._inner,max_decimals);
    }

    /* ------------------------- Accessors ------------------------------------- */


    public abstract Pointer get_inner();

    /**
     * Returns the first element in "this".
     *
     * <p>
     *         MEOS Functions:
     *             <li>geoset_start_value</li>
     * @return A {@link Geometry} instance
     */
    @Override
    public Geometry start_element() throws ParseException {
        return ConversionUtils.gserialized_to_shapely_geometry(functions.geoset_start_value(this._inner),15);
    }


    /**
     * Returns the last element in "this".
     *
     * <p>
     *         MEOS Functions:
     *             <li>geoset_end_value</li>
     * @return A {@link Geometry} instance
     */
    @Override
    public Geometry end_element() throws ParseException {
        return ConversionUtils.gserialized_to_shapely_geometry(functions.geoset_end_value(this._inner),15);
    }


    /**
     * Returns the SRID of "this".
     *
     *         MEOS Functions:
     *             <li>geoset_srid</li>
     *
     * @return An integer
     */
    protected int srid(){
        return functions.geoset_srid(this._inner);
    }


    /* ------------------------- Topological Operations -------------------------------- */

    public boolean contains(GeoSet other) throws Exception {
        return super.contains(other);
    }

    /* ------------------------- Set Operations -------------------------------- */


    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>intersection_geoset_geo</li>
     *             <li>intersection_set_set</li>
     * @param geom A {@link GeoSet} or {@link Geometry} instance
     * @return An object of the same type as "other" or "None" if the intersection is empty.
     * @throws ParseException
     */
    public Geometry intersection_geom(Geometry geom) throws ParseException {
        return ConversionUtils.gserialized_to_shapely_geometry(
                functions.intersection_geoset_geo(this._inner, ConversionUtils.geometry_to_gserialized(geom)),15);
    }

    /**
     * Returns the intersection of "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>intersection_geoset_geo</li>
     *             <li>intersection_set_set</li>
     * @param geo A {@link GeoSet} or {@link Geometry} instance
     * @param type the type of GeoSet
     * @return An object of the same type as "other" or "None" if the intersection is empty.
     * @throws ParseException
     */
    public GeoSet intersection_geoset(GeoSet geo, String type){
        return factory(type,functions.intersection_set_set(this._inner, geo._inner));
    }


    /**
     * Returns the difference of "this" and "other".
     *
     * <p>
     *         MEOS Functions:
     *             <li>minus_geoset_geo</li>
     *             <li>minus_set_set</li>
     *

     * @param geo A {@link GeoSet} or {@link Geometry} instance
     * @param type the type of GeoSet
     * @return A {@link GeoSet} instance or "null" if the difference is empty.
     */
    public GeoSet minus(Object geo, String type){
        if(geo instanceof Geometry){
            return factory(type, functions.minus_geoset_geo(this._inner, ConversionUtils.geometry_to_gserialized((Geometry) geo)));
        } else if (geo instanceof GeoSet) {
            return factory(type, functions.minus_set_set(this._inner, ((GeoSet)geo)._inner));
        }
        return null;
    }


    /**
     * Returns the union of "this" and "other".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>union_geoset_geo</li>
     *             <li>union_set_set</li>
     * @param geo A {@link GeoSet} or {@link Geometry} instance
     * @param type the type of GeoSet
     * @return A {@link GeoSet} instance.
     */
    public GeoSet union(Object geo, String type){
        if(geo instanceof Geometry){
            return factory(type, functions.union_geoset_geo(this._inner, ConversionUtils.geometry_to_gserialized((Geometry) geo)));
        } else if (geo instanceof GeoSet) {
            return factory(type, functions.union_set_set(this._inner, ((GeoSet)geo)._inner));
        }
        return null;
    }




    /* ------------------------- Transformations ------------------------------------ */


    /**
     * Rounds the coordinate values to a number of decimal places.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>tpoint_roundgeoset_round</li>
     * @param decimals The number of decimal places to use for the coordinates.
     * @param type the type of GeoSet
     * @return A new {@link GeoSet} object of the same subtype of "this".
     */
    public GeoSet round(int decimals, String type){
        return factory(type, functions.geoset_round(this._inner,decimals));
    }


}
