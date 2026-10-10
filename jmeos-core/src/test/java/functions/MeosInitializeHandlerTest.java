package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A program calling {@code meos_initialize()} after the binding has initialised MEOS keeps the
 * binding's error handler: a MEOS error reaches it as the exception it maps to instead of MEOS's
 * default handler ending the JVM. Isolated, since {@code meos_initialize()} replaces the
 * process-wide handler that every other test relies on while it runs.
 */
@DisplayName("meos_initialize and the binding's error handler")
@Isolated
@ExtendWith(TestLogger.class)
class MeosInitializeHandlerTest {

    @BeforeAll
    static void initMeos() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @AfterEach
    void restoreTimezone() {
        // meos_initialize() resets the calling thread's timezone to MEOS's default
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @Test
    @DisplayName("a MEOS error after an explicit meos_initialize reaches the caller")
    void errorSurfacesAfterExplicitInitialize() {
        GeneratedFunctions.meos_initialize();
        MeosTextInputError e = assertThrows(MeosTextInputError.class,
                () -> GeneratedFunctions.tgeompoint_in("garbage"));
        assertTrue(e.getMessage().contains("garbage"), e.getMessage());
    }

    @Test
    @DisplayName("a MEOS error after repeated meos_initialize calls reaches the caller")
    void errorSurfacesAfterRepeatedInitialize() {
        GeneratedFunctions.meos_initialize();
        GeneratedFunctions.meos_initialize();
        assertThrows(MeosTextInputError.class, () -> GeneratedFunctions.tgeompoint_in("garbage"));
    }
}
