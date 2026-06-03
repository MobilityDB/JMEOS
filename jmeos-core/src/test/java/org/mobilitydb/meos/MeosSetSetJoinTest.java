package org.mobilitydb.meos;

import functions.GeneratedFunctions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the shared set-set spatial-join helper against libmeos. Requires a
 * libmeos that exports the {@code *_tgeoarr_tgeoarr} family, so it runs only
 * with {@code -Dmeos.setset.enabled=true} (set where such a build is on the
 * library path).
 */
@EnabledIfSystemProperty(named = "meos.setset.enabled", matches = "true")
class MeosSetSetJoinTest {

    @BeforeAll static void init() { GeneratedFunctions.meos_initialize(); }
    @AfterAll  static void fini() { GeneratedFunctions.meos_finalize(); }

    private static Pointer[] tgeo(String... lits) {
        Pointer[] a = new Pointer[lits.length];
        for (int i = 0; i < lits.length; i++) a[i] = GeneratedFunctions.tgeompoint_in(lits[i]);
        return a;
    }

    @Test
    void setSetSpatialJoin() {
        // A[0] and B[0] coincide at (0,0); the rest are far apart.
        Pointer[] a = tgeo("POINT(0 0)@2000-01-01", "POINT(10 10)@2000-01-01");
        Pointer[] b = tgeo("POINT(0 0)@2000-01-01", "POINT(100 100)@2000-01-01");

        int[][] within = MeosSetSetJoin.eDwithinPairs(a, b, 1.0);
        assertArrayEquals(new int[]{0, 0}, within[0]);
        assertEquals(1, within.length, "only the coincident pair is ever within 1m");

        int[][] disjoint = MeosSetSetJoin.aDisjointPairs(a, b);
        assertEquals(3, disjoint.length, "every pair but the coincident one is always disjoint");
        assertTrue(Arrays.stream(disjoint).noneMatch(p -> p[0] == 0 && p[1] == 0));

        MeosSetSetJoin.TDwithin t = MeosSetSetJoin.tDwithinPairs(a, b, 1.0);
        assertEquals(1, t.pairs.length);
        assertArrayEquals(new int[]{0, 0}, t.pairs[0]);
        assertNotNull(t.periodsHexwkb[0], "in-range period spanset is returned");
    }
}
