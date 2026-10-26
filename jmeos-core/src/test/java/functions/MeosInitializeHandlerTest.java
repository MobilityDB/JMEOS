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
