package utils;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

public class PointerUtils {
    public abstract static class PeriodPointer extends Pointer{

        protected PeriodPointer(Runtime runtime, long address, boolean direct) {
            super(runtime, address, direct);
        }
    }
}
