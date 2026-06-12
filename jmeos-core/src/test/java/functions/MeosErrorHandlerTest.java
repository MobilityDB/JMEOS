package functions;

import functions.GeneratedFunctions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link MeosErrorHandler}.
 *
 * These tests call {@code apply()} directly (simulating what MEOS does natively)
 * then call {@code checkError()} to verify that the right exception subclass
 * is thrown, that error levels are respected, and that the handler's state
 * is correctly reset after each event.
 *
 * MEOS must be initialized before running these tests because checkError()
 * is designed to be called after native MEOS function wrappers.
 */
@DisplayName("MeosErrorHandler")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestLogger.class)
class MeosErrorHandlerTest {

    private static final error_handler_fn HANDLER = new MeosErrorHandler();

    // MEOS lifecycle

    @BeforeAll
    static void initMeos() {
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(HANDLER);
        GeneratedFunctions.meos_initialize_collation();
    }

    /*
     meos_finalize() is intentionally NOT called here.

     From what I understand: Surefire runs all test classes in the same forked JVM. If another test class
     has already called meos_finalize() before this one, calling it again causes
     a double-free of MEOS global resources, which triggers a native SIGABRT
     (exit code 134) and then crashes the forked JVM.

    @AfterAll
    static void finalizeMeos() {
        functions.meos_finalize();
    }

    */

    // Reset handler state between tests (in case a previous test left dirty state)
    @BeforeEach
    void resetState() {
        // Consume any leftover error silently
        try { MeosErrorHandler.checkError(); } catch (MeosException ignored) {}
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /** Simulates MEOS calling the error callback, then triggers checkError(). */
    private static MeosException triggerAndCheck(int level, int code, String message) {
        HANDLER.apply(level, code, message);
        return assertThrows(MeosException.class, MeosErrorHandler::checkError);
    }

    /** Simulates MEOS calling the error callback at ERROR level. */
    private static MeosException error(int code, String message) {
        return triggerAndCheck(MeosErrorHandler.ERROR, code, message);
    }

    // =========================================================================
    // 1. Error levels
    // =========================================================================

    @Nested
    @DisplayName("Error levels")
    class ErrorLevelTest {

        @Test
        @Order(1)
        @DisplayName("NOTICE level does not throw")
        void noticeLevelDoesNotThrow() {
            HANDLER.apply(MeosErrorHandler.NOTICE, MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "notice msg");
            assertDoesNotThrow(MeosErrorHandler::checkError);
        }

        @Test
        @Order(2)
        @DisplayName("WARNING level does not throw")
        void warningLevelDoesNotThrow() {
            HANDLER.apply(MeosErrorHandler.WARNING, MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "warning msg");
            assertDoesNotThrow(MeosErrorHandler::checkError);
        }

        @Test
        @Order(3)
        @DisplayName("ERROR level throws")
        void errorLevelThrows() {
            HANDLER.apply(MeosErrorHandler.ERROR, MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "error msg");
            assertThrows(MeosException.class, MeosErrorHandler::checkError);
        }

        @Test
        @Order(4)
        @DisplayName("Unknown level throws as fallback")
        void unknownLevelThrows() {
            HANDLER.apply(99, MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "unknown level");
            assertThrows(MeosException.class, MeosErrorHandler::checkError);
        }
    }

    // =========================================================================
    // 2. No error state
    // =========================================================================

    @Test
    @Order(10)
    @DisplayName("checkError() with no pending error does not throw")
    void checkErrorWithNoPendingError() {
        assertDoesNotThrow(MeosErrorHandler::checkError);
    }

    // =========================================================================
    // 3. State reset after throw
    // =========================================================================

    @Test
    @Order(11)
    @DisplayName("State is reset after checkError() throws (no double exception)")
    void stateResetAfterThrow() {
        HANDLER.apply(MeosErrorHandler.ERROR, MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "first error");
        assertThrows(MeosException.class, MeosErrorHandler::checkError);
        // Second call must not throw: state was cleared by the first throw
        assertDoesNotThrow(MeosErrorHandler::checkError);
    }

    // =========================================================================
    // 4. Message and code forwarding
    // =========================================================================

    @Test
    @Order(12)
    @DisplayName("Exception carries the original message and code")
    void exceptionCarriesMessageAndCode() {
        MeosException ex = error(MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "my specific message");
        assertEquals("my specific message", ex.getMessage());
        assertEquals(MeosErrorHandler.MEOS_ERR_TEXT_INPUT, ex.getCode());
    }

    // =========================================================================
    // 5. Code → exception subclass mapping
    // =========================================================================

    @Nested
    @DisplayName("Code → exception subclass mapping")
    class CodeMappingTest {

        // Internal branch

        @Test
        @DisplayName("code 1 → MeosInternalError")
        void code1() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_INTERNAL_ERROR, "internal");
            assertInstanceOf(MeosInternalError.class, ex);
            assertInstanceOf(MeosInternalError.class, ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 2 → MeosInternalTypeError")
        void code2() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_INTERNAL_TYPE_ERROR, "type error");
            assertInstanceOf(MeosInternalTypeError.class, ex);
            assertInstanceOf(MeosInternalError.class,     ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 3 → MeosValueOutOfRangeError")
        void code3() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_VALUE_OUT_OF_RANGE, "out of range");
            assertInstanceOf(MeosValueOutOfRangeError.class, ex);
            assertInstanceOf(MeosInternalError.class,        ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 4 → MeosDivisionByZeroError")
        void code4() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_DIVISION_BY_ZERO, "div zero");
            assertInstanceOf(MeosDivisionByZeroError.class, ex);
            assertInstanceOf(MeosInternalError.class,       ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 5 → MeosMemoryAllocError")
        void code5() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_MEMORY_ALLOC_ERROR, "alloc failed");
            assertInstanceOf(MeosMemoryAllocError.class, ex);
            assertInstanceOf(MeosInternalError.class,    ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 6 → MeosAggregationError")
        void code6() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_AGGREGATION_ERROR, "agg error");
            assertInstanceOf(MeosAggregationError.class, ex);
            assertInstanceOf(MeosInternalError.class,    ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 7 → MeosDirectoryError")
        void code7() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_DIRECTORY_ERROR, "dir error");
            assertInstanceOf(MeosDirectoryError.class, ex);
            assertInstanceOf(MeosInternalError.class,  ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 8 → MeosFileError")
        void code8() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_FILE_ERROR, "file error");
            assertInstanceOf(MeosFileError.class,    ex);
            assertInstanceOf(MeosInternalError.class, ex);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        // Argument branch

        @Test
        @DisplayName("code 10 → MeosInvalidArgError")
        void code10() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_INVALID_ARG, "invalid arg");
            assertInstanceOf(MeosInvalidArgError.class,  ex);
            assertInstanceOf(MeosArgumentError.class,    ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 11 → MeosInvalidArgTypeError")
        void code11() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_INVALID_ARG_TYPE, "bad arg type");
            assertInstanceOf(MeosInvalidArgTypeError.class, ex);
            assertInstanceOf(MeosArgumentError.class,       ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosIoError);
        }

        @Test
        @DisplayName("code 12 → MeosInvalidArgValueError")
        void code12() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_INVALID_ARG_VALUE, "bad arg value");
            assertInstanceOf(MeosInvalidArgValueError.class, ex);
            assertInstanceOf(MeosArgumentError.class,        ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosIoError);
        }

        // Feature not supported

        @Test
        @DisplayName("code 13 → MeosFeatureNotSupported")
        void code13() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_FEATURE_NOT_SUPPORTED, "not supported");
            assertInstanceOf(MeosFeatureNotSupported.class, ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }

        // I/O branch

        @Test
        @DisplayName("code 20 → MeosMfJsonInputError")
        void code20() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_MFJSON_INPUT, "bad mfjson");
            assertInstanceOf(MeosMfJsonInputError.class, ex);
            assertInstanceOf(MeosIoError.class,          ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 21 → MeosMfJsonOutputError")
        void code21() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_MFJSON_OUTPUT, "mfjson out error");
            assertInstanceOf(MeosMfJsonOutputError.class, ex);
            assertInstanceOf(MeosIoError.class,           ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 22 → MeosTextInputError")
        void code22() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_TEXT_INPUT, "bad text");
            assertInstanceOf(MeosTextInputError.class, ex);
            assertInstanceOf(MeosIoError.class,        ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 23 → MeosTextOutputError")
        void code23() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_TEXT_OUTPUT, "text out error");
            assertInstanceOf(MeosTextOutputError.class, ex);
            assertInstanceOf(MeosIoError.class,         ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 24 → MeosWkbInputError")
        void code24() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_WKB_INPUT, "bad wkb");
            assertInstanceOf(MeosWkbInputError.class, ex);
            assertInstanceOf(MeosIoError.class,       ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 25 → MeosWkbOutputError")
        void code25() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_WKB_OUTPUT, "wkb out error");
            assertInstanceOf(MeosWkbOutputError.class, ex);
            assertInstanceOf(MeosIoError.class,        ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 26 → MeosGeoJsonInputError")
        void code26() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_GEOJSON_INPUT, "bad geojson");
            assertInstanceOf(MeosGeoJsonInputError.class, ex);
            assertInstanceOf(MeosIoError.class,           ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        @Test
        @DisplayName("code 27 → MeosGeoJsonOutputError")
        void code27() {
            MeosException ex = error(MeosErrorHandler.MEOS_ERR_GEOJSON_OUTPUT, "geojson out error");
            assertInstanceOf(MeosGeoJsonOutputError.class, ex);
            assertInstanceOf(MeosIoError.class,            ex);
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
        }

        // Unknown code --> fallback

        @Test
        @DisplayName("unknown code → fallback MeosException (not a subclass)")
        void unknownCodeFallback() {
            MeosException ex = error(999, "unknown error code");
            assertEquals(MeosException.class, ex.getClass(),
                    "Unknown code must fall back to the base MeosException, not a subclass");
            assertEquals(999, ex.getCode());
            assertFalse(ex instanceof MeosInternalError);
            assertFalse(ex instanceof MeosArgumentError);
            assertFalse(ex instanceof MeosIoError);
        }
    }
}