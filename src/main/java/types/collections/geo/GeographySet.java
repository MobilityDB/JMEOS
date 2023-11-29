package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeographySet extends GeoSet{
    private Pointer _inner;
    public GeographySet(String str){
        super();
        this._inner = functions.geogset_in(str);
    }
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
