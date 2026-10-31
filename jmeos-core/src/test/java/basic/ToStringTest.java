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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.basic.tbool.*;
import types.basic.tfloat.*;
import types.basic.tint.*;
import types.basic.tpoint.tgeog.*;
import types.basic.tpoint.tgeom.*;
import types.basic.ttext.*;
import types.temporal.Temporal;
import utils.TestLogger;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The string of every temporal class is its as_text: the points as WKT through
 * tspatial_as_text for the temporal points, the type's own *_out for the others, as
 * MobilityDB's asText writes them.
 */
@ExtendWith(TestLogger.class)
public class ToStringTest {

    @BeforeAll
    static void utc() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    static Stream<Arguments> values() {
        return Stream.of(
            Arguments.of(new TBoolInst("t@2001-01-01"), "t@2001-01-01 00:00:00+00"),
            Arguments.of(new TBoolSeq("[t@2001-01-01, f@2001-01-02]"),
                "[t@2001-01-01 00:00:00+00, f@2001-01-02 00:00:00+00]"),
            Arguments.of(new TBoolSeqSet("{[t@2001-01-01], [f@2001-01-03]}"),
                "{[t@2001-01-01 00:00:00+00], [f@2001-01-03 00:00:00+00]}"),
            Arguments.of(new TIntInst("1@2001-01-01"), "1@2001-01-01 00:00:00+00"),
            Arguments.of(new TIntSeq("[1@2001-01-01, 2@2001-01-02]"),
                "[1@2001-01-01 00:00:00+00, 2@2001-01-02 00:00:00+00]"),
            Arguments.of(new TIntSeqSet("{[1@2001-01-01], [2@2001-01-03]}"),
                "{[1@2001-01-01 00:00:00+00], [2@2001-01-03 00:00:00+00]}"),
            Arguments.of(new TFloatInst("1.5@2001-01-01"), "1.5@2001-01-01 00:00:00+00"),
            Arguments.of(new TFloatSeq("[1.5@2001-01-01, 2.5@2001-01-02]"),
                "[1.5@2001-01-01 00:00:00+00, 2.5@2001-01-02 00:00:00+00]"),
            Arguments.of(new TFloatSeqSet("{[1.5@2001-01-01], [2.5@2001-01-03]}"),
                "{[1.5@2001-01-01 00:00:00+00], [2.5@2001-01-03 00:00:00+00]}"),
            Arguments.of(new TTextInst("AAA@2001-01-01"), "\"AAA\"@2001-01-01 00:00:00+00"),
            Arguments.of(new TTextSeq("[AAA@2001-01-01, BBB@2001-01-02]"),
                "[\"AAA\"@2001-01-01 00:00:00+00, \"BBB\"@2001-01-02 00:00:00+00]"),
            Arguments.of(new TTextSeqSet("{[AAA@2001-01-01], [BBB@2001-01-03]}"),
                "{[\"AAA\"@2001-01-01 00:00:00+00], [\"BBB\"@2001-01-03 00:00:00+00]}"),
            Arguments.of(new TGeomPointInst("Point(1 1)@2001-01-01"),
                "POINT(1 1)@2001-01-01 00:00:00+00"),
            Arguments.of(new TGeomPointSeq("[Point(1 1)@2001-01-01, Point(2 2)@2001-01-02]"),
                "[POINT(1 1)@2001-01-01 00:00:00+00, POINT(2 2)@2001-01-02 00:00:00+00]"),
            Arguments.of(new TGeomPointSeqSet("{[Point(1 1)@2001-01-01], [Point(2 2)@2001-01-03]}"),
                "{[POINT(1 1)@2001-01-01 00:00:00+00], [POINT(2 2)@2001-01-03 00:00:00+00]}"),
            Arguments.of(new TGeogPointInst("Point(1 1)@2001-01-01"),
                "POINT(1 1)@2001-01-01 00:00:00+00"),
            Arguments.of(new TGeogPointSeq("[Point(1 1)@2001-01-01, Point(2 2)@2001-01-02]"),
                "[POINT(1 1)@2001-01-01 00:00:00+00, POINT(2 2)@2001-01-02 00:00:00+00]"),
            Arguments.of(new TGeogPointSeqSet("{[Point(1 1)@2001-01-01], [Point(2 2)@2001-01-03]}"),
                "{[POINT(1 1)@2001-01-01 00:00:00+00], [POINT(2 2)@2001-01-03 00:00:00+00]}")
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("values")
    void testToStringIsAsText(Temporal<?> value, String expected) {
        assertEquals(expected, value.toString());
    }

    @org.junit.jupiter.api.Test
    void testAsTextDecimals() {
        assertEquals("1.12@2001-01-01 00:00:00+00", new TFloatInst("1.123456@2001-01-01").as_text(2));
        assertEquals("POINT(1.12 2.65)@2001-01-01 00:00:00+00",
            new TGeomPointInst("Point(1.123456 2.654321)@2001-01-01").as_text(2));
    }
}
