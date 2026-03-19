package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link MeosException}, the base class of the JMEOS exception hierarchy.
 *
 * Covers:
 * - Constructor: code and message are stored correctly
 * - getCode() and getMessage() return the right values
 * - toString() follows the format: "ClassName (code): message"
 * - Is a RuntimeException
 * - Edge cases: empty message, zero code
 */
@DisplayName("MeosException - base class")
@ExtendWith(TestLogger.class)
class MeosExceptionTest {

    // Construction

    @Test
    @DisplayName("getCode() returns the code passed to constructor")
    void getCodeReturnsConstructorValue() {
        MeosException ex = new MeosException("msg", 42);
        assertEquals(42, ex.getCode());
    }

    @Test
    @DisplayName("getMessage() returns the message passed to constructor")
    void getMessageReturnsConstructorValue() {
        MeosException ex = new MeosException("something failed", 1);
        assertEquals("something failed", ex.getMessage());
    }

    @Test
    @DisplayName("code 0 is accepted")
    void codeZeroIsAccepted() {
        MeosException ex = new MeosException("no error", 0);
        assertEquals(0, ex.getCode());
    }

    @Test
    @DisplayName("empty message is accepted")
    void emptyMessageIsAccepted() {
        MeosException ex = new MeosException("", 5);
        assertEquals("", ex.getMessage());
    }

    @Test
    @DisplayName("negative code is accepted")
    void negativeCodeIsAccepted() {
        MeosException ex = new MeosException("msg", -1);
        assertEquals(-1, ex.getCode());
    }

    @Test
    @DisplayName("Integer.MAX_VALUE is accepted")
    void integerMaxValueIsAccepted() {
        MeosException ex = new MeosException("msg", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, ex.getCode());
    }

    @Test
    @DisplayName("Long.MAX_VALUE is not consistent")
    void longMaxValueIsNotConsistent() {
        MeosException ex = new MeosException("msg", (int) Long.MAX_VALUE);
        assertNotEquals(Long.MAX_VALUE, ex.getCode());
    }

    // toString

    @Test
    @DisplayName("toString() follows 'ClassName (code): message' format")
    void toStringFormat() {
        MeosException ex = new MeosException("something went wrong", 99);
        assertEquals("MeosException (99): something went wrong", ex.toString());
    }

    @Test
    @DisplayName("toString() includes the code even when message is empty")
    void toStringWithEmptyMessage() {
        MeosException ex = new MeosException("", 7);
        assertEquals("MeosException (7): ", ex.toString());
    }

    @Test
    @DisplayName("toString() reflects the concrete subclass name, not MeosException")
    void toStringUsesConcreteClassName() {
        MeosException sub = new MeosInternalError("internal failure", 1);
        assertTrue(sub.toString().startsWith("MeosInternalError"),
                "toString() must use the concrete class name: " + sub.toString());
    }

    // Type hierarchy

    @Test
    @DisplayName("is a RuntimeException (unchecked)")
    void isRuntimeException() {
        assertInstanceOf(RuntimeException.class, new MeosException("x", 0));
    }

    @Test
    @DisplayName("is an Exception")
    void isException() {
        assertInstanceOf(Exception.class, new MeosException("x", 0));
    }

    @Test
    @DisplayName("is a Throwable")
    void isThrowable() {
        assertInstanceOf(Throwable.class, new MeosException("x", 0));
    }

    // Throw and catch

    @Test
    @DisplayName("can be thrown and caught as MeosException")
    void canBeThrownAndCaught() {
        MeosException caught = assertThrows(MeosException.class, () -> {
            throw new MeosException("thrown", 10);
        });
        assertEquals(10,       caught.getCode());
        assertEquals("thrown", caught.getMessage());
    }

    @Test
    @DisplayName("can be thrown and caught as RuntimeException")
    void canBeCaughtAsRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new MeosException("thrown", 10);
        });
    }
}
