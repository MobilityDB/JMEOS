package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;


/**
 * Class for setting temporal meosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
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
