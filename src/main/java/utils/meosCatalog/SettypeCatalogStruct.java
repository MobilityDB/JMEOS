package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;

public class SettypeCatalogStruct {
    private meosType settype;
    private meosType basetype;

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
