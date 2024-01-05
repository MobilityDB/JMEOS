package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;

public class SpantypeCatalogStruct {
    private final meosType spantype;
    private final meosType basetype;

    public SpantypeCatalogStruct(meosType spantype, meosType basetype) {
        this.spantype = spantype;
        this.basetype = basetype;
    }

    public meosType getSpantype() {
        return spantype;
    }

    public meosType getBasetype() {
        return basetype;
    }
}
