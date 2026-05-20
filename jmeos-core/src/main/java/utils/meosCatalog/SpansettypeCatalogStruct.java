package utils.meosCatalog;
import utils.meosCatalog.MeosEnums.meosType;


/**
 * Class for setting spanSet meosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class SpansettypeCatalogStruct {
    private final meosType spansettype;
    private final meosType spantype;

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
