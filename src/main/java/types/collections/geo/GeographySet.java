package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeographySet extends GeoSet{
    private Pointer _inner;
    public GeographySet(String str){
        super();
        this._inner = functions.geogset_in(str);
    }
}
