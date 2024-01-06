package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;


/**
 * Class representing a set of geography object inheriting from GeoSet
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class GeographySet extends GeoSet{
    private Pointer _inner;
    private final String type="Geog";

    public GeographySet(){
    }

    /**
     * Pointer constructor
     * @param inner Pointer
     */
    public GeographySet(Pointer inner){
        super(inner);
        this._inner = inner;
    }

    /**
     * The string constructor
     *
     * @param str - the string with the TBoolInst value
     */
    public GeographySet(String str){
        super(str);
        this._inner = functions.geogset_in(str);
    }

    public String getType(){return type;}

    @Override
    public Pointer get_inner(){
        return this._inner;
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.geogset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return _inner;
    }
}
