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

package regen;

import functions.functions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Regen-correctness sanity check. Exercises one DIRECT-classified and
 * one INDIR-classified bool-out wrapper end-to-end so any regression in
 * the post_regen_patch.py classification fails the build instead of
 * silently producing JVM-crashing wrappers.
 *
 * Background: FunctionsGenerator emits the same wrapper for two
 * different C signatures —
 *
 *   bool foo(args, T  *result)   // DIRECT  — value out-param
 *   bool foo(args, T **result)   // INDIR   — pointer out-param
 *
 * For DIRECT wrappers the buffer must be returned to the caller so they
 * can read the value with r.getDouble(0) / getInt(0) / getByte(0) etc.
 * For INDIR wrappers the buffer holds a pointer that must be
 * dereferenced via getPointer(0). The generator's default emission is
 * INDIR-shaped, which is why the post-regen patch flips the DIRECT
 * cases and leaves INDIR alone.
 *
 * If a future MEOS bump adds a new bool-out function and the patcher
 * misclassifies it (or misses it entirely), one of these two test
 * cases will fail with a wrong value or a JVM crash.
 */
class RegenWrapperSanityTest {

    @BeforeAll
    static void initMeos() {
        functions.meos_initialize();
        functions.meos_initialize_timezone("UTC");
    }

    /**
     * DIRECT case — stbox_xmin reads a double via the out-param.
     * If the patcher mis-classifies this as INDIR, getDouble(0) would
     * read the IEEE bits of the buffer's address (random double).
     */
    @Test
    void stbox_xmin_returns_value_buffer_directly() {
        Pointer box = functions.stbox_in("STBOX X((1.5,2.5),(3.5,4.5))");
        assertNotNull(box, "stbox_in should round-trip a literal");
        Pointer xmin = functions.stbox_xmin(box);
        assertNotNull(xmin, "stbox_xmin should return a usable buffer");
        assertEquals(1.5, xmin.getDouble(0), 1e-9,
            "stbox_xmin DIRECT-wrapper must let caller getDouble(0) the value");
    }

    /**
     * INDIR case — ttext_value_n returns a text* via the out-param;
     * the wrapper must dereference once with getPointer(0) so that the
     * caller can pass the result straight to text_out().
     */
    @Test
    void ttext_value_n_returns_text_pointer_via_indirection() {
        Pointer t = functions.ttext_in("{\"hello\"@2020-01-01 00:00:00+00}");
        assertNotNull(t, "ttext_in should parse a single-instant set");
        Pointer txt = functions.ttext_value_n(t, 1);
        assertNotNull(txt, "ttext_value_n should return text*");
        // MEOS text_out wraps the value in double quotes.
        String value = functions.text_out(txt);
        assertEquals("\"hello\"", value,
            "ttext_value_n INDIR-wrapper must hand text_out a real text*");
    }
}
