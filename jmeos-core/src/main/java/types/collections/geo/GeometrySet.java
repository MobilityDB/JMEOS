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
import functions.GeneratedFunctions;
import jnr.ffi.Pointer;

/**
 * Class representing a set of geography object inheriting from GeoSet
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class GeometrySet extends GeoSet{
    private Pointer _inner;
    private final String type="Geom";

    public GeometrySet(){
    }

    /**
     * Pointer constructor
     * @param inner Pointer
     */
    public GeometrySet(Pointer inner){
        super(inner);
        this._inner = inner;
    }

    /**
     * The string constructor
     *
     * @param str - the string with the TBoolInst value
     */
    public GeometrySet(String str){
        super(str);
        this._inner = GeneratedFunctions.geomset_in(str);
    }

    public String getType(){return type;}

    @Override
    public Pointer get_inner(){
        return this._inner;
    }

    @Override
    public Pointer createStringInner(String str){
        return GeneratedFunctions.geomset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return inner;
    }

}
