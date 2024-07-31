package functions;

import jnr.ffi.annotations.Delegate;

public interface error_handler_fn {
    @Delegate
    void apply(int errorCode, int errorLevel, String errorMessage);
}