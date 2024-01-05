package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;

public class SettypeCatalogStruct {
    private final meosType settype;
    private final meosType basetype;

    public SettypeCatalogStruct(meosType settype, meosType basetype) {
        this.settype = settype;
        this.basetype = basetype;
    }

    public meosType getSettype() {
        return settype;
    }

    public meosType getBasetype() {
        return basetype;
    }
}
