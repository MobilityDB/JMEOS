package functions;

public class error_handler implements error_handler_fn {
    @Override
    public void apply(int errorCode, int errorLevel, String errorMessage) {
        System.out.println("Error Code: " + errorCode + ", Level: " + errorLevel + ", Message: " + errorMessage);
    }
}
