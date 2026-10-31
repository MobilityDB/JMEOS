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

package collections.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import types.collections.geo.GeoSet;
import types.collections.geo.GeometrySet;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The text writers of a geometry set, which write it through the spatial set functions
 * spatialset_as_text and spatialset_as_ewkt, as MobilityDB's asText and asEWKT of a geomset.
 */
@ExtendWith(TestLogger.class)
public class GeometrySetTest {

    private final GeometrySet gset = new GeometrySet("SRID=3812;{\"Point(1 1)\", \"Point(2 2)\"}");

    @Test
    public void testAsText() {
        assertEquals("{\"POINT(1 1)\", \"POINT(2 2)\"}", gset.as_text());
    }

    @Test
    public void testAsTextDecimals() {
        GeometrySet s = new GeometrySet("{\"Point(1.123456 2.654321)\"}");
        assertEquals("{\"POINT(1.12 2.65)\"}", s.as_text(2));
    }

    @Test
    public void testAsEWKT() {
        assertEquals("SRID=3812;{\"POINT(1 1)\", \"POINT(2 2)\"}", gset.as_ewkt());
    }

    @Test
    public void testToStringIsAsText() {
        assertEquals(gset.as_text(), gset.toString());
    }

    @Test
    public void testSrid() {
        assertEquals(3812, gset.srid());
    }

    @Test
    public void testFromPointer() {
        GeometrySet s = new GeometrySet(gset.get_inner());
        assertEquals("SRID=3812;{\"POINT(1 1)\", \"POINT(2 2)\"}", s.as_ewkt());
    }

    @Test
    public void testRound() {
        GeometrySet s = new GeometrySet("{\"Point(1.123456 2.654321)\"}");
        GeoSet r = s.round(2, s.getType());
        assertEquals("{\"POINT(1.12 2.65)\"}", r.as_text());
    }
}
