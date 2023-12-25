package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;

public class SpansettypeCatalogStruct {
    private meosType spansettype;
    private meosType spantype;

    public SpansettypeCatalogStruct(meosType spansettype, meosType spantype) {
        this.spansettype = spansettype;
        this.spantype = spantype;
    }

    public meosType getSpansettype() {
        return spansettype;
    }

    public meosType getSpantype() {
        return spantype;
    }
}
