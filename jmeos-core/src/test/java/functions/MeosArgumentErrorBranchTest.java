package functions;

import functions.GeneratedFunctions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import types.basic.tfloat.TFloatInst;
import types.basic.tpoint.tgeom.TGeomPointInst;
import types.temporal.TInterpolation;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeosArgumentError} branch of the JMEOS exception hierarchy.
 *
 * Branch structure:
 *   MeosException
 *   └── MeosArgumentError
 *       ├── MeosInvalidArgError      (10)
 *       ├── MeosInvalidArgTypeError  (11)
 *       └── MeosInvalidArgValueError (12)
 *
 * Each nested class tests one exception. Coverage per class:
 * - Constructor stores code and message
 * - getCode() / getMessage()
 * - toString() format
 * - Positive instanceof chain (self → MeosArgumentError → MeosException → RuntimeException)
 * - Negative instanceof: NOT MeosInternalError, NOT MeosIoError
 * - Can be thrown and caught at each level of the hierarchy
 */
@DisplayName("MeosArgumentError branch")
@ExtendWith(TestLogger.class)
class MeosArgumentErrorBranchTest {

    private static final error_handler_fn HANDLER = new MeosErrorHandler();

    @BeforeAll
    static void initMeos() {
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(HANDLER);
        GeneratedFunctions.meos_initialize_collation();
    }

    @BeforeEach
    void resetHandlerState() {
        try { MeosErrorHandler.checkError(); } catch (MeosException ignored) {}
    }


    // =========================================================================
    // MeosArgumentError
    // =========================================================================

    @Nested
    @DisplayName("MeosArgumentError (root)")
    class MeosArgumentErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosArgumentError ex = new MeosArgumentError("bad argument", 10);
            assertEquals(10,             ex.getCode());
            assertEquals("bad argument", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosArgumentError (10): bad argument",
                    new MeosArgumentError("bad argument", 10).toString());
        }

        @Test
        @DisplayName("instanceof MeosArgumentError")
        void instanceofSelf() {
            assertInstanceOf(MeosArgumentError.class, new MeosArgumentError("x", 10));
        }

        @Test
        @DisplayName("instanceof MeosException")
        void instanceofMeosException() {
            assertInstanceOf(MeosException.class, new MeosArgumentError("x", 10));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosArgumentError("x", 10));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosArgumentError("x", 10)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosArgumentError("x", 10)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgError (child inheritor class)")
        void notInstanceofChild_MeosInvalidArgError() {
            assertFalse(MeosInvalidArgError.class.isInstance(new MeosArgumentError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgTypeError (child inheritor class)")
        void notInstanceofChild_MeosInvalidArgTypeError() {
            assertFalse(MeosInvalidArgTypeError.class.isInstance(new MeosArgumentError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgValueError (child inheritor class)")
        void notInstanceofChild_MeosInvalidArgValueError() {
            assertFalse(MeosInvalidArgValueError.class.isInstance(new MeosArgumentError("x", 12)));
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosArgumentError.class, () -> { throw new MeosArgumentError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosArgumentError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosArgumentError("x", 1); });
        }

        // MeosArgumentError is the abstract parent of the argument branch.
        // MeosInvalidArgValueError (code 12) IS-A MeosArgumentError.
        // The inverted-span trigger below propagates up and is catchable as MeosArgumentError.

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("intspan_make(300, 100) catchable as MeosArgumentError")
            void invertedSpan_catchableAsMeosArgumentError() {
                assertThrows(MeosArgumentError.class,
                        () -> functions.intspan_make(300, 100, true, true));
            }

            @Test
            @DisplayName("intspan_make(300, 100) catchable as MeosException")
            void invertedSpan_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.intspan_make(300, 100, true, true));
            }
        }
    }

    // =========================================================================
    // MeosInvalidArgError (code 10)
    // =========================================================================

    @Nested
    @DisplayName("MeosInvalidArgError (10)")
    class MeosInvalidArgErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosInvalidArgError ex = new MeosInvalidArgError("null argument", 10);
            assertEquals(10,               ex.getCode());
            assertEquals("null argument",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosInvalidArgError (10): null argument",
                    new MeosInvalidArgError("null argument", 10).toString());
        }

        @Test
        @DisplayName("instanceof MeosInvalidArgError")
        void instanceofSelf() {
            assertInstanceOf(MeosInvalidArgError.class, new MeosInvalidArgError("x", 10));
        }

        @Test
        @DisplayName("instanceof MeosArgumentError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosArgumentError.class, new MeosInvalidArgError("x", 10));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosInvalidArgError("x", 10));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInvalidArgError("x", 10));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosInvalidArgError("x", 10)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosInvalidArgError("x", 10)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgTypeError (sibling, not parent)")
        void notInstanceofSibling() {
            assertFalse(MeosInvalidArgTypeError.class.isInstance(new MeosInvalidArgError("x", 11)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgValueError (sibling, not parent)")
        void notInstanceofOtherSibling() {
            assertFalse(MeosInvalidArgValueError.class.isInstance(new MeosInvalidArgTypeError("x", 12)));
        }

        @Test
        @DisplayName("can be caught as MeosInvalidArgError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosInvalidArgError.class, () -> { throw new MeosInvalidArgError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosArgumentError")
        void canBeCaughtAsParent() {
            assertThrows(MeosArgumentError.class, () -> { throw new MeosInvalidArgError("x", 10); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosInvalidArgError("x", 10); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntime() {
            assertThrows(RuntimeException.class, () -> { throw new MeosInvalidArgError("x", 10); });
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("creating a sequence from no instants and 0 counts: MeosInvalidArgError")
            void sequenceFromNull_throwsMeosInvalidArgError() {
                assertThrows(MeosInvalidArgError.class,
                        () -> functions.tsequence_make(null, 0, true, true, TInterpolation.LINEAR.getValue(), false));
            }

            @Test
            @DisplayName("creating a sequence from no instants and 0 counts: MeosException")
            void sequenceFromNull_throwsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.tsequence_make(null, 0, true, true, TInterpolation.LINEAR.getValue(), false));
            }
        }
    }

    // =========================================================================
    // MeosInvalidArgTypeError (code 11)
    // =========================================================================

    @Nested
    @DisplayName("MeosInvalidArgTypeError (11)")
    class MeosInvalidArgTypeErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosInvalidArgTypeError ex = new MeosInvalidArgTypeError("wrong type", 11);
            assertEquals(11,           ex.getCode());
            assertEquals("wrong type", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosInvalidArgTypeError (11): wrong type",
                    new MeosInvalidArgTypeError("wrong type", 11).toString());
        }

        @Test
        @DisplayName("instanceof MeosInvalidArgTypeError")
        void instanceofSelf() {
            assertInstanceOf(MeosInvalidArgTypeError.class, new MeosInvalidArgTypeError("x", 11));
        }

        @Test
        @DisplayName("instanceof MeosArgumentError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosArgumentError.class, new MeosInvalidArgTypeError("x", 11));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosInvalidArgTypeError("x", 11));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInvalidArgTypeError("x", 11));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosInvalidArgTypeError("x", 11)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosInvalidArgTypeError("x", 11)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgError (sibling, not parent)")
        void notInstanceofSibling() {
            assertFalse(MeosInvalidArgError.class.isInstance(new MeosInvalidArgTypeError("x", 11)));
        }


        @Test
        @DisplayName("NOT instanceof MeosInvalidArgValueError (sibling, not parent)")
        void notInstanceofOtherSibling() {
            assertFalse(MeosInvalidArgValueError.class.isInstance(new MeosInvalidArgTypeError("x", 12)));


        }

        @Test
        @DisplayName("can be caught as MeosInvalidArgTypeError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosInvalidArgTypeError.class, () -> { throw new MeosInvalidArgTypeError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosArgumentError")
        void canBeCaughtAsParent() {
            assertThrows(MeosArgumentError.class, () -> { throw new MeosInvalidArgTypeError("x", 11); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosInvalidArgTypeError("x", 11); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntime() {
            assertThrows(RuntimeException.class, () -> { throw new MeosInvalidArgError("x", 10); });
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("TGeomPointInst(LINESTRING): MeosInvalidArgTypeError")
            void wrongGeometryType_throwsMeosInvalidArgTypeError() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("LINESTRING(0 0, 1 1)@2024-01-01 00:00:00+00"));
            }

            @Test
            @DisplayName("TGeomPointInst(LINESTRING): MeosException")
            void wrongGeometryType_throwsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("LINESTRING(0 0, 1 1)@2024-01-01 00:00:00+00"));
            }
        }
    }

    // =========================================================================
    // MeosInvalidArgValueError (code 12)
    // =========================================================================

    @Nested
    @DisplayName("MeosInvalidArgValueError (12)")
    class MeosInvalidArgValueErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosInvalidArgValueError ex = new MeosInvalidArgValueError("value out of range", 12);
            assertEquals(12,                    ex.getCode());
            assertEquals("value out of range",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosInvalidArgValueError (12): value out of range",
                    new MeosInvalidArgValueError("value out of range", 12).toString());
        }

        @Test
        @DisplayName("instanceof MeosInvalidArgValueError")
        void instanceofSelf() {
            assertInstanceOf(MeosInvalidArgValueError.class, new MeosInvalidArgValueError("x", 12));
        }

        @Test
        @DisplayName("instanceof MeosArgumentError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosArgumentError.class, new MeosInvalidArgValueError("x", 12));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosInvalidArgValueError("x", 12));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInvalidArgValueError("x", 12));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalError")
        void notInstanceofMeosInternalError() {
            assertFalse(MeosInternalError.class.isInstance(new MeosInvalidArgValueError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosInvalidArgValueError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgError (sibling, not parent)")
        void notInstanceofSibling() {
            assertFalse(MeosInvalidArgError.class.isInstance(new MeosInvalidArgValueError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInvalidArgTypeError (sibling, not parent)")
        void notInstanceofOtherSibling() {
            assertFalse(MeosInvalidArgTypeError.class.isInstance(new MeosInvalidArgValueError("x", 12)));
        }

        @Test
        @DisplayName("can be caught as MeosInvalidArgValueError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosInvalidArgValueError.class, () -> { throw new MeosInvalidArgValueError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosArgumentError")
        void canBeCaughtAsParent() {
            assertThrows(MeosArgumentError.class, () -> { throw new MeosInvalidArgValueError("x", 12); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsGrandparent() {
            assertThrows(MeosException.class, () -> { throw new MeosInvalidArgValueError("x", 12); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntime() {
            assertThrows(RuntimeException.class, () -> { throw new MeosInvalidArgError("x", 10); });
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("intspan_make(300, 100): MeosInvalidArgValueError (inverted bounds)")
            void invertedSpan_throwsMeosInvalidArgValueError() {
                assertThrows(MeosInvalidArgValueError.class,
                        () -> functions.intspan_make(300, 100, true, true));
            }

            @Test
            @DisplayName("intspan_make(300, 100) catchable as MeosArgumentError")
            void invertedSpan_catchableAsMeosArgumentError() {
                assertThrows(MeosArgumentError.class,
                        () -> functions.intspan_make(300, 100, true, true));
            }

            @Test
            @DisplayName("intspan_make(300, 100) catchable as MeosException")
            void invertedSpan_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> functions.intspan_make(300, 100, true, true));
            }
        }
    }
}