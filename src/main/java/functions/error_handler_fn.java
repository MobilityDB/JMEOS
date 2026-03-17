package functions;

import jnr.ffi.annotations.Delegate;

public interface error_handler_fn {
    @Delegate
    void apply(int errorLevel, int errorCode, String errorMessage);
}