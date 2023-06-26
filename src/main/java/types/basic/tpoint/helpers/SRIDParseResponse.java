package types.basic.tpoint.helpers;

/**
 * Wrapper class for SRID parse response
 */
public class SRIDParseResponse {
    private int srid;
    private String parsedValue;

    /**
     * The SRID and parsedValue constructor
     * @param srid - spatial reference identifier
     * @param parsedValue - a string witht the parsed value
     */
    public SRIDParseResponse(int srid, String parsedValue) {
        this.srid = srid;
        this.parsedValue = parsedValue;
    }

    public int getSRID() {
        return srid;
    }

    public String getParsedValue() {
        return parsedValue;
    }
}
