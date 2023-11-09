package utils.MeosCatalog;
import utils.MeosCatalog.MeosEnums.meosType;

public class SpantypeCatalogStruct {
    private meosType spantype;
    private meosType basetype;

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
