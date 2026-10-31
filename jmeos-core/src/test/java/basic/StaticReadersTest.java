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

package basic;

import functions.GeneratedFunctions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import types.basic.tint.TInt;
import types.basic.tint.TIntInst;
import types.basic.tint.TIntSeq;
import types.basic.tint.TIntSeqSet;
import types.basic.tpoint.tgeom.TGeomPoint;
import types.basic.tpoint.tgeom.TGeomPointSeq;
import types.boxes.STBox;
import types.boxes.TBox;
import types.collections.base.Set;
import types.collections.number.IntSet;
import types.collections.number.IntSpan;
import types.collections.time.tstzspanset;
import types.temporal.Temporal;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The readers of every class take the format alone and need no value to be called on: each
 * reads what the writer of the class writes, into an object of the class of the value read.
 */
@ExtendWith(TestLogger.class)
public class StaticReadersTest {

    @BeforeAll
    static void utc() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    @Test
    void testTemporalFromWkb() {
        TIntSeq seq = new TIntSeq("[1@2001-01-01, 2@2001-01-02]");
        Temporal t = Temporal.from_wkb(seq.as_wkb());
        assertInstanceOf(TIntSeq.class, t);
        assertEquals(seq.to_string(), ((TIntSeq) t).to_string());
    }

    @Test
    void testTemporalFromHexwkb() {
        TIntSeqSet ss = new TIntSeqSet("{[1@2001-01-01], [2@2001-01-03]}");
        Temporal t = Temporal.from_hexwkb(GeneratedFunctions.temporal_as_hexwkb(ss.getInner(), (byte) 0));
        assertInstanceOf(TIntSeqSet.class, t);
        assertEquals(ss.to_string(), ((TIntSeqSet) t).to_string());
    }

    @Test
    void testTIntFromMfjson() {
        TIntInst inst = new TIntInst("1@2001-01-01");
        TInt t = TInt.from_mfjson(inst.as_mfjson());
        assertInstanceOf(TIntInst.class, t);
        assertEquals(inst.to_string(), t.to_string());
    }

    @Test
    void testTGeomPointFromMfjson() {
        TGeomPointSeq seq = new TGeomPointSeq("[Point(1 1)@2001-01-01, Point(2 2)@2001-01-02]");
        TGeomPoint t = TGeomPoint.from_mfjson(seq.as_mfjson());
        assertInstanceOf(TGeomPointSeq.class, t);
        assertEquals(seq.to_string(), t.to_string());
    }

    @Test
    void testIntSpanFromWkb() {
        IntSpan span = new IntSpan("[1, 5)");
        assertEquals(span.toString(), IntSpan.from_wkb(span.as_wkb()).toString());
        assertEquals(span.toString(), IntSpan.from_hexwkb(span.as_hexwkb()).toString());
    }

    @Test
    void testSetFromWkb() throws Exception {
        IntSet set = new IntSet("{1, 2, 3}");
        assertEquals(set.toString(), Set.from_wkb(set.as_wkb(), IntSet.class).toString());
    }

    @Test
    void testTstzspansetFromHexwkb() {
        tstzspanset ss = new tstzspanset("{[2001-01-01, 2001-01-02], [2001-01-03, 2001-01-04]}");
        assertEquals(ss.toString(), tstzspanset.from_hexwkb(ss.as_hexwkb()).toString());
    }

    @Test
    void testTBoxWkb() {
        TBox box = new TBox("TBOXFLOAT XT([1.5, 2.5],[2001-01-01, 2001-01-02])");
        assertEquals(box.toString(), TBox.from_wkb(box.as_wkb()).toString());
        assertEquals(box.toString(), TBox.from_hexwkb(box.as_hexwkb()).toString());
    }

    @Test
    void testSTBoxWkb() {
        STBox box = new STBox("STBOX XT(((1, 1),(2, 2)),[2001-01-01, 2001-01-02])");
        assertEquals(box.toString(15), STBox.from_wkb(box.as_wkb()).toString(15));
        assertEquals(box.toString(15), STBox.from_hexwkb(box.as_hexwkb()).toString(15));
    }

    @Test
    void testFactoryRefusesATypeWithoutClass() {
        assertThrows(IllegalArgumentException.class,
            () -> Temporal.from_hexwkb(GeneratedFunctions.temporal_as_hexwkb(
                GeneratedFunctions.tbigint_in("1@2001-01-01"), (byte) 0)));
    }
}
