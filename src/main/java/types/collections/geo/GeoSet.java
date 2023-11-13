package types.collections.geo;
import functions.functions;
import jnr.ffi.Pointer;
import types.collections.base.Set;


public abstract class GeoSet extends Set {
    protected Pointer _inner;

    protected GeoSet(){}

    protected GeoSet(Pointer inner){
        super();
        this._inner = inner;
    }

    /* ------------------------- Constructors ---------------------------------- */

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


    /* ------------------------- Transformations ------------------------------------ */

}
