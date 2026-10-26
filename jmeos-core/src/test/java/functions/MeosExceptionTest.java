/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package functions;

import functions.GeneratedFunctions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import types.basic.tpoint.tgeom.TGeomPointInst;
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

    private static final error_handler_fn HANDLER = new MeosErrorHandler();

    @BeforeAll
    static void initMeos() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @BeforeEach
    void resetHandlerState() {
        try { MeosErrorHandler.checkError(); } catch (MeosException ignored) {}
    }

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

    @Nested
    @DisplayName("Native MEOS trigger")
    class NativeTrigger {

        @Test
        @DisplayName("well-formed TGeomPointInst does not throw")
        void validInput_doesNotThrow() {
            assertDoesNotThrow(() -> {
                TGeomPointInst inst = new TGeomPointInst(
                        "SRID=4326;POINT(4.3517 50.8503)@2024-06-01 08:00:00+00");
                assertNotNull(inst);
            });
        }

        @Test
        @DisplayName("TGeomPointInst('not-a-wkt') is catchable as MeosException")
        void invalidWkt_catchableAtBaseLevel() {
            assertThrows(MeosException.class,
                    () -> new TGeomPointInst("not-a-wkt"));
        }

        @Test
        @DisplayName("MeosException thrown by MEOS carries a non-null message")
        void meosException_hasNonNullMessage() {
            MeosException ex = assertThrows(MeosException.class,
                    () -> new TGeomPointInst("not-a-wkt"));
            assertNotNull(ex.getMessage());
        }

        @Test
        @DisplayName("MeosException thrown by MEOS carries a non-zero code")
        void meosException_hasNonZeroCode() {
            MeosException ex = assertThrows(MeosException.class,
                    () -> new TGeomPointInst("not-a-wkt"));
            assertNotEquals(0, ex.getCode());
        }
    }
}