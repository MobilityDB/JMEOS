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
import types.basic.tpoint.tgeom.TGeomPointSeq;
import types.boxes.STBox;
import types.collections.geo.GeometrySet;
import types.collections.number.IntSpan;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * as_wkb and as_hexwkb write plain WKB, which carries no SRID, as MobilityDB's asBinary and
 * asHexWKB; as_ewkb and as_hexewkb write the SRID, as asEWKB and asHexEWKB.
 */
@ExtendWith(TestLogger.class)
public class WkbEwkbTest {

    @BeforeAll
    static void utc() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    private static final TGeomPointSeq TRIP =
        new TGeomPointSeq("SRID=4326;[Point(1 1)@2001-01-01, Point(2 2)@2001-01-02]");

    @Test
    void testWkbCarriesNoSrid() {
        TGeomPointSeq read = new TGeomPointSeq(GeneratedFunctions.temporal_from_wkb(TRIP.as_wkb()));
        assertEquals(0, read.srid());
        assertEquals(TRIP.to_string(), read.to_string());
    }

    @Test
    void testHexwkbCarriesNoSrid() {
        TGeomPointSeq read = new TGeomPointSeq(GeneratedFunctions.temporal_from_hexwkb(TRIP.as_hexwkb()));
        assertEquals(0, read.srid());
        assertEquals(TRIP.to_string(), read.to_string());
    }

    @Test
    void testEwkbCarriesTheSrid() {
        TGeomPointSeq read = new TGeomPointSeq(GeneratedFunctions.temporal_from_wkb(TRIP.as_ewkb()));
        assertEquals(4326, read.srid());
        assertEquals(TRIP.as_ewkt(15), read.as_ewkt(15));
    }

    @Test
    void testHexewkbCarriesTheSrid() {
        TGeomPointSeq read = new TGeomPointSeq(GeneratedFunctions.temporal_from_hexwkb(TRIP.as_hexewkb()));
        assertEquals(4326, read.srid());
        assertEquals(TRIP.as_ewkt(15), read.as_ewkt(15));
    }

    @Test
    void testGeometrySetEwkb() {
        GeometrySet set = new GeometrySet("SRID=3812;{\"Point(1 1)\", \"Point(2 2)\"}");
        assertEquals(3812, GeneratedFunctions.spatialset_srid(GeometrySet.from_ewkb(set.as_ewkb()).get_inner()));
        assertEquals(3812, GeneratedFunctions.spatialset_srid(GeometrySet.from_hexewkb(set.as_hexewkb()).get_inner()));
        assertEquals(0, GeneratedFunctions.spatialset_srid(GeneratedFunctions.set_from_wkb(set.as_wkb())));
    }

    @Test
    void testSTBoxEwkb() {
        STBox box = new STBox("SRID=4326;STBOX X((1, 1),(2, 2))");
        assertEquals(4326, STBox.from_ewkb(box.as_ewkb()).srid());
        assertEquals(4326, STBox.from_hexewkb(box.as_hexewkb()).srid());
    }

    @Test
    void testSpanHexwkb() {
        IntSpan span = new IntSpan("[1, 5)");
        assertEquals(span.toString(), new IntSpan(GeneratedFunctions.span_from_hexwkb(span.as_hexwkb())).toString());
        assertEquals(span.toString(), new IntSpan(GeneratedFunctions.span_from_wkb(span.as_wkb())).toString());
    }
}
