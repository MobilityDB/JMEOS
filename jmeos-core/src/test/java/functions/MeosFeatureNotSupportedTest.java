package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link MeosFeatureNotSupported} (code 13).
 *
 * This class sits directly under MeosException and belongs to none of the
 * three branches (MeosInternalError, MeosArgumentError, MeosIoError).
 * It is raised when a valid MEOS operation is requested but not implemented
 * for the specific type combination given: e.g. requesting a feature that
 * exists conceptually but has no implementation for the supplied temporal type.
 *
 * Coverage:
 * - Constructor stores code and message
 * - getCode() / getMessage()
 * - toString() format
 * - Positive instanceof: MeosFeatureNotSupported → MeosException → RuntimeException
 * - Negative instanceof: NOT any of the three branches
 * - Can be thrown and caught at each level
 */
@DisplayName("MeosFeatureNotSupported (13)")
@ExtendWith(TestLogger.class)
class MeosFeatureNotSupportedTest {

    // Construction

    @Test
    @DisplayName("stores code and message")
    void storesCodeAndMessage() {
        MeosFeatureNotSupported ex = new MeosFeatureNotSupported("operation not supported", 13);
        assertEquals(13,                        ex.getCode());
        assertEquals("operation not supported", ex.getMessage());
    }

    @Test
    @DisplayName("code 13 is stored exactly")
    void codeIsExactly13() {
        assertEquals(13, new MeosFeatureNotSupported("x", 13).getCode());
    }

    @Test
    @DisplayName("empty message is accepted")
    void emptyMessage() {
        MeosFeatureNotSupported ex = new MeosFeatureNotSupported("", 13);
        assertEquals("", ex.getMessage());
    }

    // toString

    @Test
    @DisplayName("toString() follows 'ClassName (code): message' format")
    void toStringFormat() {
        MeosFeatureNotSupported ex = new MeosFeatureNotSupported("operation not supported", 13);
        assertEquals("MeosFeatureNotSupported (13): operation not supported", ex.toString());
    }

    @Test
    @DisplayName("toString() with empty message")
    void toStringEmptyMessage() {
        assertEquals("MeosFeatureNotSupported (13): ",
                new MeosFeatureNotSupported("", 13).toString());
    }

    // Positive instanceof chain

    @Test
    @DisplayName("instanceof MeosFeatureNotSupported")
    void instanceofSelf() {
        assertInstanceOf(MeosFeatureNotSupported.class, new MeosFeatureNotSupported("x", 13));
    }

    @Test
    @DisplayName("instanceof MeosException (direct parent)")
    void instanceofMeosException() {
        assertInstanceOf(MeosException.class, new MeosFeatureNotSupported("x", 13));
    }

    @Test
    @DisplayName("instanceof RuntimeException")
    void instanceofRuntimeException() {
        assertInstanceOf(RuntimeException.class, new MeosFeatureNotSupported("x", 13));
    }

    @Test
    @DisplayName("instanceof Exception")
    void instanceofException() {
        assertInstanceOf(Exception.class, new MeosFeatureNotSupported("x", 13));
    }

    // Negative instanceof: must NOT belong to any branch

    @Test
    @DisplayName("NOT instanceof MeosInternalError")
    void notInstanceofMeosInternalError() {
        assertFalse(MeosInternalError.class.isInstance(new MeosFeatureNotSupported("x", 13)));
    }

    @Test
    @DisplayName("NOT instanceof MeosArgumentError")
    void notInstanceofMeosArgumentError() {
        assertFalse(MeosArgumentError.class.isInstance(new MeosFeatureNotSupported("x", 13)));
    }

    @Test
    @DisplayName("NOT instanceof MeosIoError")
    void notInstanceofMeosIoError() {
        assertFalse(MeosIoError.class.isInstance(new MeosFeatureNotSupported("x", 13)));
    }

    // Throw and catch

    @Test
    @DisplayName("can be thrown and caught as MeosFeatureNotSupported")
    void canBeCaughtAsSelf() {
        MeosFeatureNotSupported caught = assertThrows(
                MeosFeatureNotSupported.class,
                () -> { throw new MeosFeatureNotSupported("not supported", 13); });
        assertEquals(13,               caught.getCode());
        assertEquals("not supported",  caught.getMessage());
    }

    @Test
    @DisplayName("can be caught as MeosException")
    void canBeCaughtAsMeosException() {
        assertThrows(MeosException.class,
                () -> { throw new MeosFeatureNotSupported("x", 13); });
    }

    @Test
    @DisplayName("can be caught as RuntimeException")
    void canBeCaughtAsRuntimeException() {
        assertThrows(RuntimeException.class,
                () -> { throw new MeosFeatureNotSupported("x", 13); });
    }
}
