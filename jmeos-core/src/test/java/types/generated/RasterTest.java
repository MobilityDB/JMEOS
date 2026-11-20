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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The members ObjectClassGenerator gives the classes of the RASTER family, tested as
 * #GeneratedClassRoundTripTest tests the readers and writers: a method the object model assigns, an
 * operator overloaded on its operand's type, a writer taking a list of options and each aggregate
 * over a class's values. Each answer is the one MobilityDB's 500_raster test states for the same
 * tiles, as PyMEOS's tests/raster/raster_test.py states them.
 */
public class RasterTest {

    /**
     * The example of the manual: two 2x2 tiles of one 32BF band, the second one pixel east and one
     * south of the first, holding 1 and 3.
     */
    private static List<Raster> tiles() {
        return List.of(
                Raster.makeEmpty(2, 2, 0, 2, 1, -1, 0, 0, 3857).addBand(1, "32BF", 1, -9999, true),
                Raster.makeEmpty(2, 2, 1, 1, 1, -1, 0, 0, 3857).addBand(1, "32BF", 3, -9999, true));
    }

    @Test
    void statisticsOfABandSpanTheTiles() {
        BandStats stats = Raster.summaryStatsAgg(tiles(), 1, true);
        assertEquals(8L, stats.count());
        assertEquals(16.0, stats.sum());
        assertEquals(2.0, stats.mean());
        assertEquals(1.0, stats.stddev());
        assertEquals(1.0, stats.min());
        assertEquals(3.0, stats.max());
    }

    @Test
    void unionTakesTheMeanWhereTheTilesOverlap() {
        // {{1,1,NULL},{1,2,3},{NULL,3,3}}: seven pixels of value, summing to 14.
        BandStats stats = Raster.rasterUnion(tiles(), 1, "MEAN").summaryStats(1, true);
        assertEquals(7L, stats.count());
        assertEquals(14.0, stats.sum());
        assertEquals(1.0, stats.min());
        assertEquals(3.0, stats.max());
    }

    @Test
    void anAggregateOverNoValuesIsNull() {
        assertNull(Raster.summaryStatsAgg(List.of(), 1, true));
        assertNull(Raster.rasterUnion(List.of(), 1, "MEAN"));
    }

    @Test
    void anOperatorIsOverloadedOnItsOperand() {
        Raster tile = tiles().get(0);
        assertEquals(2.5, tile.add(1.5).summaryStats(1, true).mean());
        assertEquals(2.0, tile.add(tile).summaryStats(1, true).mean());
    }

    @Test
    void aWriterTakesItsOptions() {
        // GTiff is the writer every GDAL MEOS links carries; a TIFF opens with II or MM.
        Raster tile = Raster.makeEmpty(2, 2, 0, 2, 1, -1, 0, 0, 3857).addBand(1, "8BUI", 7, 0, false);
        byte[] tiff = tile.asTiff(List.of("TILED=NO"), 3857);
        assertTrue((tiff[0] == 'I' && tiff[1] == 'I') || (tiff[0] == 'M' && tiff[1] == 'M'));
    }
}
