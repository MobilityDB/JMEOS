package functions;

import java.lang.ref.WeakReference;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * An error handler a caller registers without keeping a reference to it, as in
 * {@code meos_initialize_error_handler(new MeosErrorHandler())}, still receives the errors MEOS
 * raises after the collector has run: the binding holds the handler while MEOS can call it.
 * Isolated, as MeosInitializeHandlerTest is: the binding holds only the latest handler MEOS
 * receives, so a handler another test registers meanwhile, or the other test of this class, would
 * replace the one a test checks and leave it to the collector.
 */
@DisplayName("Error handler registration")
@Isolated
@ExtendWith(TestLogger.class)
class ErrorHandlerRegistrationTest {

    @BeforeAll
    static void initMeos() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @AfterAll
    static void restoreBindingHandler() {
        GeneratedFunctions.meos_initialize_error_handler(GeneratedFunctions.ERROR_HANDLER);
    }

    /** Runs the collector until the referent of a weak reference is gone, or gives up. */
    private static void collect(WeakReference<?> ref) {
        for (int i = 0; i < 50 && ref.get() != null; i++) {
            byte[][] garbage = new byte[1000][];
            for (int k = 0; k < garbage.length; k++) {
                garbage[k] = new byte[1024];
            }
            System.gc();
        }
    }

    /** Registers a handler the caller keeps no reference to and returns a weak view of it. */
    private static WeakReference<error_handler_fn> registerUnreferencedHandler() {
        error_handler_fn handler = new MeosErrorHandler();
        GeneratedFunctions.meos_initialize_error_handler(handler);
        return new WeakReference<>(handler);
    }

    @Test
    @DisplayName("the binding keeps an unreferenced handler reachable")
    void unreferencedHandlerStaysReachable() {
        WeakReference<error_handler_fn> ref = registerUnreferencedHandler();
        collect(ref);
        assertNotNull(ref.get(), "the registered handler was collected while MEOS holds it");
    }

    @Test
    @DisplayName("a MEOS error reaches the caller through an unreferenced handler after a collection")
    void errorSurfacesAfterCollection() {
        WeakReference<error_handler_fn> ref = registerUnreferencedHandler();
        collect(ref);
        MeosTextInputError e = assertThrows(MeosTextInputError.class,
                () -> GeneratedFunctions.tgeompoint_in("garbage"));
        assertTrue(e.getMessage().contains("garbage"), e.getMessage());
    }
}
