package functions;

public class error_handler implements error_handler_fn {
    @Override
    public void apply(int errorLevel, int errorCode, String errorMessage) {
        System.out.println("Level: " + errorLevel + ", Code: " + errorCode + ", Message: " + errorMessage);
    }
}
