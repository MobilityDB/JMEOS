package functions;

// Invalid arguments
public class MeosArgumentError extends MeosException {
    public MeosArgumentError(String m, int c) {
        super(m, c);
    }
}
