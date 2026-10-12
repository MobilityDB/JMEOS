package utils.meosCatalog;
import functions.MeosType;


/**
 * Class for setting span MeosType in an object.
 *
 * @author Nidhal Mareghni
 * @since 18/10/2023
 */
public class SpantypeCatalogStruct {
    private final MeosType spantype;
    private final MeosType basetype;

    public SpantypeCatalogStruct(MeosType spantype, MeosType basetype) {
        this.spantype = spantype;
        this.basetype = basetype;
    }

    public MeosType getSpantype() {
        return spantype;
    }

    public MeosType getBasetype() {
        return basetype;
    }
}
