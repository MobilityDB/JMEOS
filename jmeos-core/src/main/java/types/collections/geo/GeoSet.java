/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package types.collections.geo;
import jnr.ffi.Pointer;
import org.checkerframework.checker.units.qual.C;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import types.collections.base.Set;
import utils.ConversionUtils;
import functions.GeneratedFunctions;


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
     * Return the string representation of the content of "this", its WKT.
     *  <p>
     *         MEOS Functions:
     *             <li>spatialset_as_text</li>
     * @return A new {@link String} instance
     */
    @Override
    public String toString(){
        return as_text();
    }

    /**
     * Returns the EWKT representation of "this", its WKT preceded by its SRID.
     *
     *         MEOS Functions:
     *             <li>spatialset_as_ewkt</li>
     * @return A {@link String} instance.
     */
    public String as_ewkt(){
        return as_ewkt(15);
    }

    /**
     * Returns the EWKT representation of "this", its WKT preceded by its SRID.
     *
     *         MEOS Functions:
     *             <li>spatialset_as_ewkt</li>
     * @param max_decimals The maximum number of decimal digits of a coordinate
     * @return A {@link String} instance.
     */
    public String as_ewkt(int max_decimals){
        return GeneratedFunctions.spatialset_as_ewkt(this._inner, max_decimals);
    }

    /**
     * Returns the WKT representation of "this".
     *
     *         MEOS Functions:
     *             <li>spatialset_as_text</li>
     *
     * @return A {@link String} instance.
     */
    public String as_text(){
        return as_text(15);
    }

    /**
     * Returns the WKT representation of "this".
     *
     *         MEOS Functions:
     *             <li>spatialset_as_text</li>
     *
     * @param max_decimals The maximum number of decimal digits of a coordinate
     * @return A {@link String} instance.
     */
    public String as_text(int max_decimals){
        return GeneratedFunctions.spatialset_as_text(this._inner, max_decimals);
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
        return ConversionUtils.gserialized_to_shapely_geometry(GeneratedFunctions.geoset_start_value(this._inner),15);
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
        return ConversionUtils.gserialized_to_shapely_geometry(GeneratedFunctions.geoset_end_value(this._inner),15);
    }


    /**
     * Returns the SRID of "this".
     *
     *         MEOS Functions:
     *             <li>spatialset_srid</li>
     *
     * @return An integer
     */
    public int srid(){
        return GeneratedFunctions.spatialset_srid(this._inner);
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
                GeneratedFunctions.intersection_set_geo(this._inner, ConversionUtils.geometry_to_gserialized(geom)),15);
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
        return factory(type,GeneratedFunctions.intersection_set_set(this._inner, geo._inner));
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
            return factory(type, GeneratedFunctions.minus_set_geo(this._inner, ConversionUtils.geometry_to_gserialized((Geometry) geo)));
        } else if (geo instanceof GeoSet) {
            return factory(type, GeneratedFunctions.minus_set_set(this._inner, ((GeoSet)geo)._inner));
        }
        return null;
    }

/*
        Returns the difference of ``other`` and ``self``.

        Args:
            other: A :class:`Geometry` instance

        Returns:
            A :class:`Geometry` instance.

        MEOS Functions:
            minus_geo_set

        See Also:
            :meth:`minus`
*/
    public Geometry subtract_from(Object geo, String type) throws ParseException {
        Pointer result= GeneratedFunctions.minus_geo_set(ConversionUtils.geometry_to_gserialized((Geometry) geo), this._inner);
        if(result != null) {
            return ConversionUtils.gserialized_to_shapely_geometry(result, 15);
        }
        else {
            return null;
        }
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
            return factory(type, GeneratedFunctions.union_set_geo(this._inner, ConversionUtils.geometry_to_gserialized((Geometry) geo)));
        } else if (geo instanceof GeoSet) {
            return factory(type, GeneratedFunctions.union_set_set(this._inner, ((GeoSet)geo)._inner));
        }
        return null;
    }




    /* ------------------------- Transformations ------------------------------------ */


    /**
     * Rounds the coordinate values to a number of decimal places.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>set_round</li>
     * @param decimals The number of decimal places to use for the coordinates.
     * @param type the type of GeoSet
     * @return A new {@link GeoSet} object of the same subtype of "this".
     */
    public GeoSet round(int decimals, String type){
        return factory(type, GeneratedFunctions.set_round(this._inner,decimals));
    }


}
