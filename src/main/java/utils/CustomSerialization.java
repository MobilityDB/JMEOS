package utils;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import functions.functions.MeosLibrary;


/**
 * Serialization class for JNR-FFI Pointer type.
 *
 * @author Nidhal Mareghni
 * @since 18/08/2023
 */
public class CustomSerialization {
    public static long getInnerAddress(Pointer pointer){
        return pointer.address();
    }

    public static Pointer constructPointer(long inner_adress){
        Runtime runtime = Runtime.getRuntime(MeosLibrary.INSTANCE);
        Pointer pointer = Pointer.wrap(runtime,inner_adress);
        return pointer;
    }
}
