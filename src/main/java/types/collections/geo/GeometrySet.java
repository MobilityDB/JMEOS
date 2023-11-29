package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeometrySet extends GeoSet{
    private Pointer _inner;
    public GeometrySet(String str){
        super();
        this._inner = functions.geomset_in(str);
    }
    @Override
    public Pointer get_inner(){
        return this._inner;
    }

    @Override
    public Pointer createStringInner(String str){
        return functions.geomset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return _inner;
    }

}
