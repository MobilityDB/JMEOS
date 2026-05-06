package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import types.basic.tpoint.tgeom.TGeomPointInst;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeosIoError} branch of the JMEOS exception hierarchy.
 *
 * Branch structure:
 *   MeosException
 *   └── MeosIoError
 *       ├── MeosMfJsonInputError   (20)
 *       ├── MeosMfJsonOutputError  (21)
 *       ├── MeosTextInputError     (22)
 *       ├── MeosTextOutputError    (23)
 *       ├── MeosWkbInputError      (24)
 *       ├── MeosWkbOutputError     (25)
 *       ├── MeosGeoJsonInputError  (26)
 *       └── MeosGeoJsonOutputError (27)
 *
 * Each nested class tests one exception. Coverage per class:
 * - Constructor stores code and message
 * - getCode() / getMessage()
 * - toString() format
 * - Positive instanceof chain (self → MeosIoError → MeosException → RuntimeException)
 * - Negative instanceof: NOT MeosInternalError, NOT MeosArgumentError
 * - Siblings are NOT instanceof each other
 * - Can be thrown and caught at each level of the hierarchy
 */
@DisplayName("MeosIoError branch")
@ExtendWith(TestLogger.class)
class MeosIoErrorBranchTest {

    private static final error_handler_fn HANDLER = new MeosErrorHandler();

    @BeforeAll
    static void initMeos() {
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(HANDLER);
    }

    @BeforeEach
    void resetHandlerState() {
        try { MeosErrorHandler.checkError(); } catch (MeosException ignored) {}
    }


    // =========================================================================
    // MeosIoError
    // =========================================================================

    @Nested
    @DisplayName("MeosIoError (root)")
    class MeosIoErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosIoError ex = new MeosIoError("io error", 20);
            assertEquals(20,         ex.getCode());
            assertEquals("io error", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosIoError (20): io error",
                    new MeosIoError("io error", 20).toString());
        }

        @Test
        @DisplayName("instanceof MeosIoError")
        void instanceofSelf() {
            assertInstanceOf(MeosIoError.class, new MeosIoError("x", 20));
        }

        @Test
        @DisplayName("instanceof MeosException")
        void instanceofMeosException() {
            assertInstanceOf(MeosException.class, new MeosIoError("x", 20));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosIoError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof Meos")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosIoError("x", 20)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosIoError("x", 20)));
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosIoError.class, () -> { throw new MeosIoError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosIoError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosIoError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (child inheritor class)")
        void notInstanceofChild_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (child inheritor class)")
        void notInstanceofChild_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (child inheritor class)")
        void notInstanceofChild_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (child inheritor class)")
        void notInstanceofChild_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (child inheritor class)")
        void notInstanceofChild_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (child inheritor class)")
        void notInstanceofChild_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (child inheritor class)")
        void notInstanceofChild_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosIoError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (child inheritor class)")
        void notInstanceofChild_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosIoError("x", 12)));
        }

        // MeosIoError is the abstract parent of the I/O branch.
        // MeosTextInputError (code 22) IS-A MeosIoError: the invalid-WKT trigger
        // below propagates up and is catchable as MeosIoError.

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("TGeomPointInst('not-a-wkt') catchable as MeosIoError (parent)")
            void invalidWkt_catchableAsMeosIoError() {
                assertThrows(MeosIoError.class,
                        () -> new TGeomPointInst("not-a-wkt"));
            }

            @Test
            @DisplayName("TGeomPointInst('not-a-wkt') catchable as MeosException")
            void invalidWkt_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("not-a-wkt"));
            }
        }
    }

    // =========================================================================
    // MeosMfJsonInputError (code 20)
    // =========================================================================

    @Nested
    @DisplayName("MeosMfJsonInputError (20)")
    class MeosMfJsonInputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosMfJsonInputError ex = new MeosMfJsonInputError("bad mfjson", 20);
            assertEquals(20,           ex.getCode());
            assertEquals("bad mfjson", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosMfJsonInputError (20): bad mfjson",
                    new MeosMfJsonInputError("bad mfjson", 20).toString());
        }

        @Test
        @DisplayName("instanceof MeosMfJsonInputError")
        void instanceofSelf() {
            assertInstanceOf(MeosMfJsonInputError.class, new MeosMfJsonInputError("x", 20));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosMfJsonInputError("x", 20));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosMfJsonInputError("x", 20));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosMfJsonInputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosMfJsonInputError("x", 20)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosMfJsonInputError("x", 20)));
        }

        @Test
        @DisplayName("can be caught as MeosMfJsonInputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosMfJsonInputError.class, () -> { throw new MeosMfJsonInputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosMfJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosMfJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosMfJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosMfJsonInputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            private static final String TRUNCATED =
                    "{\"type\":\"MovingPoint\",\"coordinates\":[[1.0,2.0";

            @Test
            @DisplayName("tgeompoint_from_mfjson(truncated) → MeosMfJsonInputError (code 20)")
            void truncatedMfJson_throwsMeosMfJsonInputError() {
                assertThrows(MeosMfJsonInputError.class,
                        () -> functions.tgeompoint_from_mfjson(TRUNCATED));
            }

            @Test
            @DisplayName("tgeompoint_from_mfjson(truncated) catchable as MeosIoError")
            void truncatedMfJson_catchableAsMeosIoError() {
                assertThrows(MeosIoError.class,
                        () -> functions.tgeompoint_from_mfjson(TRUNCATED));
            }

            @Test
            @DisplayName("tgeompoint_from_mfjson(truncated) catchable as MeosException")
            void truncatedMfJson_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.tgeompoint_from_mfjson(TRUNCATED));
            }
        }
    }

    // =========================================================================
    // MeosMfJsonOutputError (code 21)
    // =========================================================================

    @Nested
    @DisplayName("MeosMfJsonOutputError (21)")
    class MeosMfJsonOutputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosMfJsonOutputError ex = new MeosMfJsonOutputError("mfjson output error", 21);
            assertEquals(21,                     ex.getCode());
            assertEquals("mfjson output error",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosMfJsonOutputError (21): mfjson output error",
                    new MeosMfJsonOutputError("mfjson output error", 21).toString());
        }

        @Test
        @DisplayName("instanceof MeosMfJsonOutputError")
        void instanceofSelf() {
            assertInstanceOf(MeosMfJsonOutputError.class, new MeosMfJsonOutputError("x", 21));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosMfJsonOutputError("x", 21));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosMfJsonOutputError("x", 21));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosMfJsonOutputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosMfJsonOutputError("x", 21)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosMfJsonOutputError("x", 21)));
        }

        @Test
        @DisplayName("can be caught as MeosMfJsonOutputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosMfJsonOutputError.class, () -> { throw new MeosMfJsonOutputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosMfJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosMfJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosMfJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosMfJsonOutputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("")
            void a_throwsMeosMfJsonOutputError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosIoError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosException() {
                // TODO
            }
        }
    }

    // =========================================================================
    // MeosTextInputError (code 22)
    // =========================================================================

    @Nested
    @DisplayName("MeosTextInputError (22)")
    class MeosTextInputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosTextInputError ex = new MeosTextInputError("bad wkt", 22);
            assertEquals(22,        ex.getCode());
            assertEquals("bad wkt", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosTextInputError (22): bad wkt",
                    new MeosTextInputError("bad wkt", 22).toString());
        }

        @Test
        @DisplayName("instanceof MeosTextInputError")
        void instanceofSelf() {
            assertInstanceOf(MeosTextInputError.class, new MeosTextInputError("x", 22));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosTextInputError("x", 22));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosTextInputError("x", 22));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosTextInputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosTextInputError("x", 22)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosTextInputError("x", 22)));
        }

        @Test
        @DisplayName("can be caught as MeosTextInputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosTextInputError.class, () -> { throw new MeosTextInputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosTextInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosTextInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosTextInputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosTextInputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("TGeomPointInst('not-a-wkt'): MeosTextInputError (code 22)")
            void invalidWkt_throwsMeosTextInputError() {
                assertThrows(MeosTextInputError.class,
                        () -> new TGeomPointInst("not-a-wkt"));
            }

            @Test
            @DisplayName("TGeomPointInst('not-a-wkt') catchable as MeosIoError")
            void invalidWkt_catchableAsMeosIoError() {
                assertThrows(MeosIoError.class,
                        () -> new TGeomPointInst("not-a-wkt"));
            }

            @Test
            @DisplayName("TGeomPointInst('not-a-wkt') catchable as MeosException")
            void invalidWkt_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("not-a-wkt"));
            }
        }
    }

    // =========================================================================
    // MeosTextOutputError (code 23)
    // =========================================================================

    @Nested
    @DisplayName("MeosTextOutputError (23)")
    class MeosTextOutputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosTextOutputError ex = new MeosTextOutputError("text output error", 23);
            assertEquals(23,                   ex.getCode());
            assertEquals("text output error",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosTextOutputError (23): text output error",
                    new MeosTextOutputError("text output error", 23).toString());
        }

        @Test
        @DisplayName("instanceof MeosTextOutputError")
        void instanceofSelf() {
            assertInstanceOf(MeosTextOutputError.class, new MeosTextOutputError("x", 23));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosTextOutputError("x", 23));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosTextOutputError("x", 22));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosTextOutputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosTextOutputError("x", 23)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosTextOutputError("x", 23)));
        }

        @Test
        @DisplayName("can be caught as MeosTextOutputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosTextOutputError.class, () -> { throw new MeosTextOutputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosTextOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosTextOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosTextOutputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosTextOutputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("")
            void a_throwsMeosTextOutputError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosIoError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosException() {
                // TODO
            }
        }
    }

    // =========================================================================
    // MeosWkbInputError (code 24)
    // =========================================================================

    @Nested
    @DisplayName("MeosWkbInputError (24)")
    class MeosWkbInputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosWkbInputError ex = new MeosWkbInputError("bad wkb", 24);
            assertEquals(24,        ex.getCode());
            assertEquals("bad wkb", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosWkbInputError (24): bad wkb",
                    new MeosWkbInputError("bad wkb", 24).toString());
        }

        @Test
        @DisplayName("instanceof MeosWkbInputError")
        void instanceofSelf() {
            assertInstanceOf(MeosWkbInputError.class, new MeosWkbInputError("x", 24));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosWkbInputError("x", 24));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosWkbInputError("x", 22));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosWkbInputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosWkbInputError("x", 24)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosWkbInputError("x", 24)));
        }

        @Test
        @DisplayName("can be caught as MeosWkbInputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosWkbInputError.class, () -> { throw new MeosWkbInputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosWkbInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosWkbInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosWkbInputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosWkbInputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            private static final String CORRUPT = "DEADBEEFCAFE0123456789ABCDEF";

            @Test
            @DisplayName("temporal_from_hexwkb(corrupt): MeosWkbInputError")
            void corruptWkb_throwsMeosWkbInputError() {
                assertThrows(MeosWkbInputError.class,
                        () -> functions.temporal_from_hexwkb(CORRUPT));
            }

            @Test
            @DisplayName("temporal_from_hexwkb(corrupt) catchable as MeosIoError")
            void corruptWkb_catchableAsMeosIoError() {
                assertThrows(MeosIoError.class,
                        () -> functions.temporal_from_hexwkb(CORRUPT));
            }

            @Test
            @DisplayName("temporal_from_hexwkb(corrupt) catchable as MeosException")
            void corruptWkb_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.temporal_from_hexwkb(CORRUPT));
            }
        }
    }

    // =========================================================================
    // MeosWkbOutputError (code 25)
    // =========================================================================

    @Nested
    @DisplayName("MeosWkbOutputError (25)")
    class MeosWkbOutputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosWkbOutputError ex = new MeosWkbOutputError("wkb output error", 25);
            assertEquals(25,                  ex.getCode());
            assertEquals("wkb output error",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosWkbOutputError (25): wkb output error",
                    new MeosWkbOutputError("wkb output error", 25).toString());
        }

        @Test
        @DisplayName("instanceof MeosWkbOutputError")
        void instanceofSelf() {
            assertInstanceOf(MeosWkbOutputError.class, new MeosWkbOutputError("x", 25));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosWkbOutputError("x", 25));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosWkbOutputError("x", 22));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosWkbOutputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosWkbOutputError("x", 25)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosWkbOutputError("x", 25)));
        }

        @Test
        @DisplayName("can be caught as MeosWkbOutputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosWkbOutputError.class, () -> { throw new MeosWkbOutputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosWkbOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosWkbOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosWkbOutputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosWkbOutputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("")
            void a_throwsMeosWkbOutputError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosIoError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosException() {
                // TODO
            }
        }
    }

    // =========================================================================
    // MeosGeoJsonInputError (code 26)
    // =========================================================================

    @Nested
    @DisplayName("MeosGeoJsonInputError (26)")
    class MeosGeoJsonInputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosGeoJsonInputError ex = new MeosGeoJsonInputError("bad geojson", 26);
            assertEquals(26,             ex.getCode());
            assertEquals("bad geojson",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosGeoJsonInputError (26): bad geojson",
                    new MeosGeoJsonInputError("bad geojson", 26).toString());
        }

        @Test
        @DisplayName("instanceof MeosGeoJsonInputError")
        void instanceofSelf() {
            assertInstanceOf(MeosGeoJsonInputError.class, new MeosGeoJsonInputError("x", 26));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosGeoJsonInputError("x", 26));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosGeoJsonInputError("x", 26));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosGeoJsonInputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosGeoJsonInputError("x", 26)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosGeoJsonInputError("x", 26)));
        }

        @Test
        @DisplayName("can be caught as MeosGeoJsonInputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosGeoJsonInputError.class, () -> { throw new MeosGeoJsonInputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosGeoJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosGeoJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosGeoJsonInputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonOutputError() {
            assertFalse(MeosGeoJsonOutputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosGeoJsonInputError("x", 12)));
        }

        /*@Nested FIXME
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            private static final String INVALID_GEOJSON = "{\"type\":\"Point\",\"coord"; // truncated

            @Test
            @DisplayName("tgeompoint_from_geojson(truncated) → MeosGeoJsonInputError (code 26)")
            void invalidGeoJson_throwsMeosGeoJsonInputError() {
                assertThrows(MeosGeoJsonInputError.class,
                        () -> functions.geo_from_geojson(INVALID_GEOJSON));
            }

            @Test
            @DisplayName("tgeompoint_from_geojson(truncated) catchable as MeosIoError")
            void invalidGeoJson_catchableAsMeosIoError() {
                assertThrows(MeosIoError.class,
                        () -> functions.geo_from_geojson(INVALID_GEOJSON));
            }

            @Test
            @DisplayName("tgeompoint_from_geojson(truncated) catchable as MeosException")
            void invalidGeoJson_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.geo_from_geojson(INVALID_GEOJSON));
            }
        }*/
    }

    // =========================================================================
    // MeosGeoJsonOutputError (code 27)
    // =========================================================================

    @Nested
    @DisplayName("MeosGeoJsonOutputError (27)")
    class MeosGeoJsonOutputErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosGeoJsonOutputError ex = new MeosGeoJsonOutputError("geojson output error", 27);
            assertEquals(27,                      ex.getCode());
            assertEquals("geojson output error",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosGeoJsonOutputError (27): geojson output error",
                    new MeosGeoJsonOutputError("geojson output error", 27).toString());
        }

        @Test
        @DisplayName("instanceof MeosGeoJsonOutputError")
        void instanceofSelf() {
            assertInstanceOf(MeosGeoJsonOutputError.class, new MeosGeoJsonOutputError("x", 27));
        }

        @Test
        @DisplayName("instanceof MeosIoError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosIoError.class, new MeosGeoJsonOutputError("x", 27));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosGeoJsonOutputError("x", 27));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosGeoJsonOutputError("x", 20));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosGeoJsonOutputError("x", 27)));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosGeoJsonOutputError("x", 27)));
        }

        @Test
        @DisplayName("can be caught as MeosGeoJsonOutputError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosGeoJsonOutputError.class, () -> { throw new MeosGeoJsonOutputError("x", 10);});
        }

        @Test
        @DisplayName("can be caught as MeosIoError")
        void canBeCaughtAsParent() {
            assertThrows(MeosIoError.class, () -> { throw new MeosGeoJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosGeoJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosGeoJsonOutputError("x", 20); });
        }

        @Test
        @DisplayName("NOT instanceof MeosGeoJsonInputError (sibling class)")
        void notInstanceofSibling_MeosGeoJsonInputError() {
            assertFalse(MeosGeoJsonInputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonInputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonInputError() {
            assertFalse(MeosMfJsonInputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMfJsonOutputError (sibling class)")
        void notInstanceofSibling_MeosMfJsonOutputError() {
            assertFalse(MeosMfJsonOutputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextInputError (sibling class)")
        void notInstanceofSibling_MeosTextInputError() {
            assertFalse(MeosTextInputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosTextOutputError (sibling class)")
        void notInstanceofSibling_MeosTextOutputError() {
            assertFalse(MeosTextOutputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbInputError (sibling class)")
        void notInstanceofSibling_MeosWkbInputError() {
            assertFalse(MeosWkbInputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosWkbOutputError (sibling class)")
        void notInstanceofSibling_MeosWkbOutputError() {
            assertFalse(MeosWkbOutputError.class.isInstance(new MeosGeoJsonOutputError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("")
            void a_throwsMeosGeoJsonOutputError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosIoError() {
                // TODO
            }

            @Test
            @DisplayName("")
            void a_throwsMeosException() {
                // TODO
            }
        }
    }
}