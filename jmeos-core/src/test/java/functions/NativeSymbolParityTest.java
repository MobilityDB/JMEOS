package functions;

import com.kenai.jffi.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Asserts that every native function JMEOS declares is exported by the libmeos it is built
 * against.
 *
 * <p>jnr-ffi binds an interface method lazily, so a declaration naming a symbol that libmeos does
 * not export compiles cleanly and fails only when that one method is first called, with
 * {@code UnsatisfiedLinkError: unknown}. Such a declaration is therefore invisible to every test
 * that does not happen to exercise it.
 *
 * <p>This test resolves each declared FFI method name against the loaded library up front, so a
 * build against a MEOS revision that renamed or dropped a symbol fails immediately and names the
 * symbols concerned.
 */
@DisplayName("Declared native symbols exist in libmeos")
@ExtendWith(TestLogger.class)
class NativeSymbolParityTest {

    /** The library the generated surface binds. */
    private static final String LIBRARY_PATH = "libmeos.so";

    /** The FFI interfaces whose methods are 1:1 with C symbols. */
    private static List<Class<?>> ffiInterfaces() {
        List<Class<?>> interfaces = new ArrayList<>();
        for (Class<?> nested : GeneratedFunctions.class.getDeclaredClasses()) {
            if (nested.isInterface()) {
                interfaces.add(nested);
            }
        }
        return interfaces;
    }

    @Test
    @DisplayName("every declared FFI method resolves to an exported libmeos symbol")
    void everyDeclaredNativeSymbolIsExported() {
        Library library = Library.getCachedInstance(LIBRARY_PATH,
                Library.LAZY | Library.GLOBAL);
        assertTrue(library != null, "libmeos could not be loaded: " + Library.getLastError());

        TreeSet<String> missing = new TreeSet<>();
        int checked = 0;
        for (Class<?> iface : ffiInterfaces()) {
            for (Method method : iface.getDeclaredMethods()) {
                checked++;
                if (library.getSymbolAddress(method.getName()) == 0L) {
                    missing.add(method.getName());
                }
            }
        }

        assertTrue(checked > 0, "no FFI methods were discovered");
        assertTrue(missing.isEmpty(),
                missing.size() + " of " + checked + " declared native symbols are absent from "
                        + "libmeos: " + missing);
    }
}
