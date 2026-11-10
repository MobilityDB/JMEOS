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

package types.generated;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The classes ObjectClassGenerator generates read a value from its text, write it through
 * {@code toString} (its {@code asText}), and read back from {@code asHexWKB} the value that writes
 * the same text, for a type of each family the hand layer has no class for, as
 * #GeneratedTemporalParityTest checks the generated surface of the hand classes.
 */
public class GeneratedClassRoundTripTest {

    /** The text of v, and that of the value read back from its plain hex WKB. */
    private static void roundTrip(MeosObject v, String hexBack) {
        assertNotNull(v, "the reader reads the text");
        assertNotNull(hexBack, "the hex WKB reads back");
        assertEquals(v.toString(), hexBack);
    }

    @Test
    void circularBuffer() {
        CBuffer v = CBuffer.fromText("Cbuffer(Point(1 1),0.5)");
        roundTrip(v, CBuffer.fromHexWKB(v.asHexWKB((byte) 0)).toString());
        TCBuffer t = TCBuffer.fromText("[Cbuffer(Point(1 1),0.5)@2001-01-01, Cbuffer(Point(2 2),0.5)@2001-01-02]");
        roundTrip(t, TCBuffer.fromHexWKB(t.asHexWKB((byte) 0)).toString());
        CBufferSet s = CBufferSet.fromText("{\"Cbuffer(Point(1 1),0.5)\", \"Cbuffer(Point(2 2),0.5)\"}");
        roundTrip(s, CBufferSet.fromHexWKB(s.asHexWKB((byte) 0)).toString());
    }

    @Test
    void networkPoint() {
        NPoint v = NPoint.fromText("NPoint(1,0.5)");
        roundTrip(v, NPoint.fromHexWKB(v.asHexWKB((byte) 0)).toString());
        TNPoint t = TNPoint.fromText("[NPoint(1,0.2)@2001-01-01, NPoint(1,0.5)@2001-01-02]");
        roundTrip(t, TNPoint.fromHexWKB(t.asHexWKB((byte) 0)).toString());
    }

    @Test
    void pose() {
        Pose v = Pose.fromText("Pose(Point(1 1),0.5)");
        roundTrip(v, Pose.fromHexWKB(v.asHexWKB((byte) 0)).toString());
        TPose t = TPose.fromText("[Pose(Point(1 1),0.5)@2001-01-01, Pose(Point(2 2),0.5)@2001-01-02]");
        roundTrip(t, TPose.fromHexWKB(t.asHexWKB((byte) 0)).toString());
    }

    @Test
    void bigint() {
        TBigInt t = TBigInt.fromText("[1@2001-01-01, 2@2001-01-02]");
        roundTrip(t, TBigInt.fromHexWKB(t.asHexWKB((byte) 0)).toString());
        BigIntSet s = BigIntSet.fromText("{1, 2, 3}");
        roundTrip(s, BigIntSet.fromHexWKB(s.asHexWKB((byte) 0)).toString());
        BigIntSpan p = BigIntSpan.fromText("[1, 3)");
        roundTrip(p, BigIntSpan.fromHexWKB(p.asHexWKB((byte) 0)).toString());
        BigIntSpanSet ps = BigIntSpanSet.fromText("{[1, 3), [5, 7)}");
        roundTrip(ps, BigIntSpanSet.fromHexWKB(ps.asHexWKB((byte) 0)).toString());
    }

    @Test
    void geometrySets() {
        GeomSet s = GeomSet.fromText("{Point(1 1), Point(2 2)}");
        roundTrip(s, GeomSet.fromHexWKB(s.asHexWKB((byte) 0)).toString());
    }
}
