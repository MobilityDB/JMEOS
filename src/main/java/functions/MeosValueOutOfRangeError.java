package functions;

public class MeosValueOutOfRangeError extends MeosInternalError {
    public MeosValueOutOfRangeError(String m, int c) {
        super(m, c);
    }
}
