package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;


/**
 * Class for setting set meosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
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
