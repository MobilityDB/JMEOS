package functions;

public class MeosException extends RuntimeException {
    private final int code;
    public MeosException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int getCode() { return code; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " (" + code + "): " + getMessage();
    }
}

