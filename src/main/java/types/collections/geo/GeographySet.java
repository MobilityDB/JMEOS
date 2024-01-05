package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeographySet extends GeoSet{
    private Pointer _inner;
    private final String type="Geog";

    public GeographySet(){
    }

    public GeographySet(Pointer inner){
        super(inner);
        this._inner = inner;
    }

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
