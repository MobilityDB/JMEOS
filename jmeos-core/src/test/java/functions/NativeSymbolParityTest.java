package functions;

import com.kenai.jffi.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
 *
 * <p>The invariant holds over the symbols MEOS itself declares. MEOS links a vendored library as a
 * static archive, so the linker takes only the objects MEOS references while that library's header
 * goes on declaring the whole of it: pc_api.h declares both pc_patch_uncompress, which libmeos
 * exports, and pc_patch_sort, which it does not. Requiring an export for the second asserts
 * something untrue of a library MEOS deliberately links a part of, and dropping those headers would
 * take the vendored functions that ARE exported with them. The catalog marks each declaration's
 * provenance, and the ones it marks vendored are reported rather than required. A catalog carrying
 * no such mark leaves every declaration required, so the check can only be stricter than the
 * catalog describes, never weaker.
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

    /** The names the catalog marks as declared by a vendored project's header. */
    private static Set<String> vendoredNames() {
        Set<String> names = new HashSet<>();
        Path catalog = Paths.get("..", "codegen", "input", "meos-idl.json");
        if (!Files.isReadable(catalog)) {
            return names; // no catalog to read: require an export for every declaration
        }
        try {
            JsonNode root = new ObjectMapper().readTree(catalog.toFile());
            for (JsonNode fn : root.path("functions")) {
                if (fn.path("vendored").asBoolean(false)) {
                    names.add(fn.path("name").asText());
                }
            }
        } catch (Exception e) {
            return new HashSet<>(); // unreadable catalog: require an export for every declaration
        }
        return names;
    }

    @Test
    @DisplayName("every declared FFI method resolves to an exported libmeos symbol")
    void everyDeclaredNativeSymbolIsExported() {
        Library library = Library.getCachedInstance(LIBRARY_PATH,
                Library.LAZY | Library.GLOBAL);
        assertTrue(library != null, "libmeos could not be loaded: " + Library.getLastError());

        Set<String> vendored = vendoredNames();
        TreeSet<String> missing = new TreeSet<>();
        TreeSet<String> vendoredMissing = new TreeSet<>();
        int checked = 0;
        for (Class<?> iface : ffiInterfaces()) {
            for (Method method : iface.getDeclaredMethods()) {
                checked++;
                if (library.getSymbolAddress(method.getName()) == 0L) {
                    (vendored.contains(method.getName()) ? vendoredMissing : missing)
                            .add(method.getName());
                }
            }
        }

        assertTrue(checked > 0, "no FFI methods were discovered");
        if (!vendoredMissing.isEmpty()) {
            System.out.println(vendoredMissing.size() + " vendored declaration(s) name an object "
                    + "the linker did not take from the archive: " + vendoredMissing);
        }
        assertTrue(missing.isEmpty(),
                missing.size() + " of " + checked + " declared native symbols are absent from "
                        + "libmeos: " + missing);
    }
}
