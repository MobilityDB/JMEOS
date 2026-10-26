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
