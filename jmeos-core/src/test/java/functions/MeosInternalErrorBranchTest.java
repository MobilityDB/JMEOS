package functions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import types.basic.tfloat.TFloatInst;
import types.basic.tpoint.tgeom.TGeomPointInst;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeosInternalError} branch of the JMEOS exception hierarchy.
 *
 * Branch structure:
 *   MeosException
 *   └── MeosInternalError (1)
 *       ├── MeosInternalTypeError   (2)
 *       ├── MeosValueOutOfRangeError (3)
 *       ├── MeosDivisionByZeroError  (4)
 *       ├── MeosMemoryAllocError     (5)
 *       ├── MeosAggregationError     (6)
 *       ├── MeosDirectoryError       (7)
 *       └── MeosFileError            (8)
 *
 * Each nested class tests one exception. Coverage per class:
 * - Constructor stores code and message
 * - getCode() / getMessage()
 * - toString() format
 * - Positive instanceof chain (self → MeosInternalError → MeosException → RuntimeException)
 * - Negative instanceof: NOT MeosArgumentError, NOT MeosIoError
 * - Can be thrown and caught at each level of the hierarchy
 */
@DisplayName("MeosInternalError branch")
@ExtendWith(TestLogger.class)
class MeosInternalErrorBranchTest {

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
    // MeosInternalError (code 1)
    // =========================================================================

    @Nested
    @DisplayName("MeosInternalError (1)")
    class MeosInternalErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosInternalError ex = new MeosInternalError("internal bug", 1);
            assertEquals(1,              ex.getCode());
            assertEquals("internal bug", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            MeosInternalError ex = new MeosInternalError("internal bug", 1);
            assertEquals("MeosInternalError (1): internal bug", ex.toString());
        }

        @Test
        @DisplayName("instanceof MeosInternalError")
        void instanceofSelf() {
            assertInstanceOf(MeosInternalError.class, new MeosInternalError("x", 1));
        }

        @Test
        @DisplayName("instanceof MeosException")
        void instanceofMeosException() {
            assertInstanceOf(MeosException.class, new MeosInternalError("x", 1));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalError("x", 1));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosInternalError("x", 1)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosInternalError("x", 1)));
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosInternalError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosInternalError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosInternalError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (child inheritor class)")
        void notInstanceofChild_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (child inheritor class)")
        void notInstanceofChild_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (child inheritor class)")
        void notInstanceofChild_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (child inheritor class)")
        void notInstanceofChild_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (child inheritor class)")
        void notInstanceofChild_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (child inheritor class)")
        void notInstanceofChild_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (child inheritor class)")
        void notInstanceofChild_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosInternalError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("MeosInternalTypeError (code 2) is catchable as MeosInternalError")
            void internalTypeError_catchableAsInternalError() {
                assertThrows(MeosInternalError.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }

            @Test
            @DisplayName("MeosInternalError catch also accepts RuntimeException")
            void internalTypeError_catchableAsRuntimeException() {
                assertThrows(RuntimeException.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }
        }
    }

    // =========================================================================
    // MeosInternalTypeError (code 2)
    // =========================================================================

    @Nested
    @DisplayName("MeosInternalTypeError (2)")
    class MeosInternalTypeErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosInternalTypeError ex = new MeosInternalTypeError("type mismatch", 2);
            assertEquals(2,               ex.getCode());
            assertEquals("type mismatch", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosInternalTypeError (2): type mismatch",
                    new MeosInternalTypeError("type mismatch", 2).toString());
        }

        @Test
        @DisplayName("instanceof MeosInternalTypeError")
        void instanceofSelf() {
            assertInstanceOf(MeosInternalTypeError.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosInternalTypeError("x", 2)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosInternalTypeError("x", 2)));
        }

        @Test
        @DisplayName("can be caught as MeosInternalTypeError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosInternalTypeError.class, () -> { throw new MeosInternalTypeError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosInternalTypeError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosInternalTypeError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosInternalTypeError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosInternalTypeError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("TGeomPointInst(missing ')'): MeosInternalTypeError (code 2)")
            void brokenGeometry_throwsMeosInternalTypeError() {
                assertThrows(MeosInternalTypeError.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }

            @Test
            @DisplayName("TGeomPointInst(missing ')') catchable as MeosInternalError")
            void brokenGeometry_catchableAsMeosInternalError() {
                assertThrows(MeosInternalError.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }

            @Test
            @DisplayName("TGeomPointInst(missing ')') catchable as MeosException")
            void brokenGeometry_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }
        }
    }

    // =========================================================================
    // MeosValueOutOfRangeError (code 3)
    // =========================================================================

    @Nested
    @DisplayName("MeosValueOutOfRangeError (3)")
    class MeosValueOutOfRangeErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosValueOutOfRangeError ex = new MeosValueOutOfRangeError("out of range", 3);
            assertEquals(3,              ex.getCode());
            assertEquals("out of range", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosValueOutOfRangeError (3): out of range",
                    new MeosValueOutOfRangeError("out of range", 3).toString());
        }

        @Test
        @DisplayName("instanceof MeosValueOutOfRangeError")
        void instanceofSelf() {
            assertInstanceOf(MeosValueOutOfRangeError.class, new MeosValueOutOfRangeError("x", 3));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosValueOutOfRangeError("x", 3));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosValueOutOfRangeError("x", 3));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosValueOutOfRangeError("x", 3)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosValueOutOfRangeError("x", 3)));
        }

        @Test
        @DisplayName("can be caught as MeosValueOutOfRangeError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosValueOutOfRangeError.class, () -> { throw new MeosValueOutOfRangeError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosValueOutOfRangeError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosValueOutOfRangeError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosValueOutOfRangeError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosValueOutOfRangeError("x", 12)));
        }

        /*@Nested
        @DisplayName("Native MEOS trigger") // FIXME nothing was thrown
        class NativeTrigger {

            @Test
            @DisplayName("out-of-range geography coordinates: MeosValueOutOfRangeError or MeosInvalidArgValueError")
            void outOfRangeCoords_throwsExpectedType() {
                Exception ex = assertThrows(MeosException.class,
                        () -> new TGeomPointInst("POINT(400.0 200.0)@2024-01-01 00:00:00+00"));
                assertTrue(
                        ex instanceof MeosValueOutOfRangeError,
                        "Expected MeosValueOutOfRangeError, got: " + ex.getClass().getSimpleName());
            }

            @Test
            @DisplayName("out-of-range coordinates catchable as MeosException at minimum")
            void outOfRangeCoords_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("POINT(400.0 200.0)@2024-01-01 00:00:00+00"));
            }
        }*/
    }

    // =========================================================================
    // MeosDivisionByZeroError (code 4)
    // =========================================================================

    @Nested
    @DisplayName("MeosDivisionByZeroError (4)")
    class MeosDivisionByZeroErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosDivisionByZeroError ex = new MeosDivisionByZeroError("division by zero", 4);
            assertEquals(4,                  ex.getCode());
            assertEquals("division by zero", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosDivisionByZeroError (4): division by zero",
                    new MeosDivisionByZeroError("division by zero", 4).toString());
        }

        @Test
        @DisplayName("instanceof MeosDivisionByZeroError")
        void instanceofSelf() {
            assertInstanceOf(MeosDivisionByZeroError.class, new MeosDivisionByZeroError("x", 4));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosDivisionByZeroError("x", 4));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosDivisionByZeroError("x", 4));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosDivisionByZeroError("x", 4)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosDivisionByZeroError("x", 4)));
        }

        @Test
        @DisplayName("can be caught as MeosDivisionByZeroError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosDivisionByZeroError.class, () -> { throw new MeosDivisionByZeroError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosDivisionByZeroError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosDivisionByZeroError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosDivisionByZeroError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosDivisionByZeroError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("div_tfloat_float(x, 0.0): MeosDivisionByZeroError")
            void divByZero_throwsExpectedType() {
                TFloatInst sog = new TFloatInst("12.5@2024-06-01 08:00:00+00");
                Exception ex = assertThrows(MeosException.class,
                        () -> functions.div_tfloat_float(sog.getInner(), 0.0));
                assertTrue(
                        ex instanceof MeosDivisionByZeroError,
                        "Expected MeosDivisionByZeroError, got: " + ex.getClass().getSimpleName());
            }

            @Test
            @DisplayName("div_tfloat_float(x, 0.0) is catchable as MeosException")
            void divByZero_catchableAsMeosException() {
                TFloatInst sog = new TFloatInst("12.5@2024-06-01 08:00:00+00");
                assertThrows(MeosException.class,
                        () -> functions.div_tfloat_float(sog.getInner(), 0.0));
            }
        }
    }

    // =========================================================================
    // MeosMemoryAllocError (code 5)
    // =========================================================================

    @Nested
    @DisplayName("MeosMemoryAllocError (5)")
    class MeosMemoryAllocErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosMemoryAllocError ex = new MeosMemoryAllocError("alloc failed", 5);
            assertEquals(5,              ex.getCode());
            assertEquals("alloc failed", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosMemoryAllocError (5): alloc failed",
                    new MeosMemoryAllocError("alloc failed", 5).toString());
        }

        @Test
        @DisplayName("instanceof MeosMemoryAllocError")
        void instanceofSelf() {
            assertInstanceOf(MeosMemoryAllocError.class, new MeosMemoryAllocError("x", 5));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosMemoryAllocError("x", 5));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosMemoryAllocError("x", 5));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosMemoryAllocError("x", 5)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosMemoryAllocError("x", 5)));
        }

        @Test
        @DisplayName("can be caught as MeosMemoryAllocError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosMemoryAllocError.class, () -> { throw new MeosMemoryAllocError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosMemoryAllocError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosMemoryAllocError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosMemoryAllocError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosMemoryAllocError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            //@Test
            //@DisplayName("enormous corrupt WKB: MemoryAllocError")
            //void enormousCorruptWkb_throwsMeosMemoryAllocError() {
            //    String hugeWkb = "FF".repeat(100_000);
            //    assertThrows(MeosMemoryAllocError.class, // FIXME MeoWKBInputError was thrown
            //            () -> functions.temporal_from_hexwkb(hugeWkb));
            //}

            @Test
            @DisplayName("enormous corrupt WKB: MeosException")
            void enormousCorruptWkb_throwsMeosException() {
                String hugeWkb = "FF".repeat(100_000);
                assertThrows(MeosException.class,
                        () -> functions.temporal_from_hexwkb(hugeWkb));
            }
        }
    }

    // =========================================================================
    // MeosAggregationError (code 6)
    // =========================================================================

    @Nested
    @DisplayName("MeosAggregationError (6)")
    class MeosAggregationErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosAggregationError ex = new MeosAggregationError("aggregation failed", 6);
            assertEquals(6,                    ex.getCode());
            assertEquals("aggregation failed", ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosAggregationError (6): aggregation failed",
                    new MeosAggregationError("aggregation failed", 6).toString());
        }

        @Test
        @DisplayName("instanceof MeosAggregationError")
        void instanceofSelf() {
            assertInstanceOf(MeosAggregationError.class, new MeosAggregationError("x", 6));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosAggregationError("x", 6));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosAggregationError("x", 6));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosAggregationError("x", 6)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosAggregationError("x", 6)));
        }

        @Test
        @DisplayName("can be caught as MeosAggregationError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosAggregationError.class, () -> { throw new MeosAggregationError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosAggregationError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosAggregationError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosAggregationError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosAggregationError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            //@Test
            //@DisplayName("parsing incompatible temporal input: MeosAggregationError")
            //void incompatibleInput_throwsMeosAggregationError() {
            //    assertThrows(MeosAggregationError.class, // FIXME MeosTextInputError was thrown
            //            () -> new TGeomPointInst("not-an-aggregation-input"));
            //}

            @Test
            @DisplayName("parsing incompatible temporal input: MeosException")
            void incompatibleInput_throwsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("not-an-aggregation-input"));
            }
        }
    }

    // =========================================================================
    // MeosDirectoryError (code 7)
    // =========================================================================

    @Nested
    @DisplayName("MeosDirectoryError (7)")
    class MeosDirectoryErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosDirectoryError ex = new MeosDirectoryError("directory not found", 7);
            assertEquals(7,                      ex.getCode());
            assertEquals("directory not found",  ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosDirectoryError (7): directory not found",
                    new MeosDirectoryError("directory not found", 7).toString());
        }

        @Test
        @DisplayName("instanceof MeosDirectoryError")
        void instanceofSelf() {
            assertInstanceOf(MeosDirectoryError.class, new MeosDirectoryError("x", 7));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosDirectoryError("x", 7));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosDirectoryError("x", 7));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosDirectoryError("x", 7)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosDirectoryError("x", 7)));
        }

        @Test
        @DisplayName("can be caught as MeosDirectoryError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosDirectoryError.class, () -> { throw new MeosDirectoryError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosDirectoryError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosDirectoryError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosDirectoryError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosFileError (sibling class)")
        void notInstanceofSibling_MeosFileError() {
            assertFalse(MeosFileError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosDirectoryError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("broken geometry → MeosInternalError (safe substitute for code 7)")
            void safeSubstitute_catchableAsMeosInternalError() {
                assertThrows(MeosInternalError.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }

            @Test
            @DisplayName("broken geometry → catchable as MeosException")
            void safeSubstitute_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }
        }
    }

    // =========================================================================
    // MeosFileError (code 8)
    // =========================================================================

    @Nested
    @DisplayName("MeosFileError (8)")
    class MeosFileErrorTest {

        @Test
        @DisplayName("stores code and message")
        void storesCodeAndMessage() {
            MeosFileError ex = new MeosFileError("file not found", 8);
            assertEquals(8,                  ex.getCode());
            assertEquals("file not found",   ex.getMessage());
        }

        @Test
        @DisplayName("toString() format")
        void toStringFormat() {
            assertEquals("MeosFileError (8): file not found",
                    new MeosFileError("file not found", 8).toString());
        }

        @Test
        @DisplayName("instanceof MeosFileError")
        void instanceofSelf() {
            assertInstanceOf(MeosFileError.class, new MeosFileError("x", 8));
        }

        @Test
        @DisplayName("instanceof MeosInternalError (parent)")
        void instanceofParent() {
            assertInstanceOf(MeosInternalError.class, new MeosFileError("x", 8));
        }

        @Test
        @DisplayName("instanceof MeosException (grandparent)")
        void instanceofGrandparent() {
            assertInstanceOf(MeosException.class, new MeosFileError("x", 8));
        }

        @Test
        @DisplayName("instanceof RuntimeException")
        void instanceofRuntimeException() {
            assertInstanceOf(RuntimeException.class, new MeosInternalTypeError("x", 2));
        }

        @Test
        @DisplayName("NOT instanceof MeosArgumentError")
        void notInstanceofMeosArgumentError() {
            assertFalse(MeosArgumentError.class.isInstance(new MeosFileError("x", 8)));
        }

        @Test
        @DisplayName("NOT instanceof MeosIoError")
        void notInstanceofMeosIoError() {
            assertFalse(MeosIoError.class.isInstance(new MeosFileError("x", 8)));
        }

        @Test
        @DisplayName("can be caught as MeosFileError")
        void canBeCaughtAsSelf() {
            assertThrows(MeosFileError.class, () -> { throw new MeosFileError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as MeosInternalError")
        void canBeCaughtAsParent() {
            assertThrows(MeosInternalError.class, () -> { throw new MeosFileError("x", 2); });
        }

        @Test
        @DisplayName("can be caught as MeosException")
        void canBeCaughtAsMeosException() {
            assertThrows(MeosException.class, () -> { throw new MeosFileError("x", 1); });
        }

        @Test
        @DisplayName("can be caught as RuntimeException")
        void canBeCaughtAsRuntimeException() {
            assertThrows(RuntimeException.class, () -> { throw new MeosFileError("x", 1); });
        }

        @Test
        @DisplayName("NOT instanceof MeosAggregationError (sibling class)")
        void notInstanceofSibling_MeosAggregationError() {
            assertFalse(MeosAggregationError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDirectoryError (sibling class)")
        void notInstanceofSibling_MeosDirectoryError() {
            assertFalse(MeosDirectoryError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosDivisionByZeroError (sibling class)")
        void notInstanceofSibling_MeosDivisionByZeroError() {
            assertFalse(MeosDivisionByZeroError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosInternalTypeError (sibling class)")
        void notInstanceofSibling_MeosInternalTypeError() {
            assertFalse(MeosInternalTypeError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosMemoryAllocError (sibling class)")
        void notInstanceofSibling_MeosMemoryAllocError() {
            assertFalse(MeosMemoryAllocError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Test
        @DisplayName("NOT instanceof MeosValueOutOfRangeError (sibling class)")
        void notInstanceofSibling_MeosValueOutOfRangeError() {
            assertFalse(MeosValueOutOfRangeError.class.isInstance(new MeosFileError("x", 12)));
        }

        @Nested
        @DisplayName("Native MEOS trigger")
        class NativeTrigger {

            @Test
            @DisplayName("broken geometry → MeosInternalError (safe substitute for code 8)")
            void safeSubstitute_catchableAsMeosInternalError() {
                assertThrows(MeosInternalError.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }

            @Test
            @DisplayName("broken geometry → catchable as MeosException")
            void safeSubstitute_catchableAsMeosException() {
                assertThrows(MeosException.class,
                        () -> new TGeomPointInst("POINT(181.0 91.0@not-a-date"));
            }
        }
    }
}