package utils.meosCatalog;
import functions.MeosType;


/**
 * Class for setting temporal MeosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class TemptypeCatalogStruct {
    private final MeosType temptype;
    private final MeosType basetype;

    public TemptypeCatalogStruct(MeosType temptype, MeosType basetype) {
        this.temptype = temptype;
        this.basetype = basetype;
    }

    public MeosType getTemptype() {
        return temptype;
    }

    public MeosType getBasetype() {
        return basetype;
    }
}
