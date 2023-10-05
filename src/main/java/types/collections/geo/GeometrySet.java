package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeometrySet extends GeoSet{
    private Pointer _inner;
    public GeometrySet(String str){
        super();
        this._inner = functions.geomset_in(str);
    }

}
