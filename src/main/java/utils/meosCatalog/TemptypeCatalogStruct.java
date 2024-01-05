package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;

public class TemptypeCatalogStruct {
    private final meosType temptype;
    private final meosType basetype;

    public TemptypeCatalogStruct(meosType temptype, meosType basetype) {
        this.temptype = temptype;
        this.basetype = basetype;
    }

    public meosType getTemptype() {
        return temptype;
    }

    public meosType getBasetype() {
        return basetype;
    }
}
