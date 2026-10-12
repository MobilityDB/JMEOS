package utils.meosCatalog;
import functions.MeosType;


/**
 * Class for setting set MeosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class SettypeCatalogStruct {
    private final MeosType settype;
    private final MeosType basetype;

    public SettypeCatalogStruct(MeosType settype, MeosType basetype) {
        this.settype = settype;
        this.basetype = basetype;
    }

    public MeosType getSettype() {
        return settype;
    }

    public MeosType getBasetype() {
        return basetype;
    }
}
