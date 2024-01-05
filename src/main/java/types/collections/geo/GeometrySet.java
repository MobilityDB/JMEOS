package types.collections.geo;

import jnr.ffi.Pointer;
import functions.functions;

public class GeometrySet extends GeoSet{
    private Pointer _inner;
    private final String type="Geom";

    public GeometrySet(){
    }

    public GeometrySet(Pointer inner){
        super(inner);
        this._inner = inner;
    }

    public GeometrySet(String str){
        super(str);
        this._inner = functions.geomset_in(str);
    }

    public String getType(){return type;}

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
