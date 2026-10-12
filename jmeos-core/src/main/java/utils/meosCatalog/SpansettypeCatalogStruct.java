package utils.meosCatalog;
import functions.MeosType;


/**
 * Class for setting spanSet MeosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class SpansettypeCatalogStruct {
    private final MeosType spansettype;
    private final MeosType spantype;

    public SpansettypeCatalogStruct(MeosType spansettype, MeosType spantype) {
        this.spansettype = spansettype;
        this.spantype = spantype;
    }

    public MeosType getSpansettype() {
        return spansettype;
    }

    public MeosType getSpantype() {
        return spantype;
    }
}
