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
package org.mobilitydb.meos;

import functions.GeneratedFunctions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * Shared set-set spatial-join helper over the MEOS {@code *_tgeoarr_tgeoarr}
 * kernel family. It marshals two arrays of already-parsed temporal-geometry
 * handles into the native pointer arrays the kernel prunes in C, then reads
 * back the flattened {@code int*} pair list. The kernel pair indices are
 * 0-based and returned verbatim (the array-language convention shared by every
 * JVM binding); SQL-like surfaces add their own +1.
 *
 * <p>The input {@code Pointer[]} are MEOS-owned and remain owned by the caller
 * (it frees them); this helper frees only what the kernel allocates. The native
 * argument buffers are GC-managed and kept reachable across the native call via
 * {@link Reference#reachabilityFence} — the documented fix for the set-set
 * SIGSEGV under JVM GC pressure.</p>
 */
public final class MeosSetSetJoin {

    private static final Unsafe UNSAFE;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private MeosSetSetJoin() { /* utility */ }

    /** Result of {@link #tDwithinPairs}: the 0-based index pairs and, per pair,
     *  the hex-WKB of the {@code tstzspanset} of times the pair is within distance. */
    public static final class TDwithin {
        public final int[][] pairs;
        public final String[] periodsHexwkb;
        TDwithin(int[][] pairs, String[] periodsHexwkb) {
            this.pairs = pairs;
            this.periodsHexwkb = periodsHexwkb;
        }
    }

    /** Index pairs {@code {i,j}} (0-based) ever within {@code dist} of each other. */
    public static int[][] eDwithinPairs(Pointer[] a, Pointer[] b, double dist) {
        if (a == null || b == null || a.length == 0 || b.length == 0) return new int[0][];
        Runtime rt = Runtime.getSystemRuntime();
        Pointer arr1 = marshal(a, rt), arr2 = marshal(b, rt);
        Pointer countPtr = Memory.allocateDirect(rt, Integer.BYTES);
        Pointer res = GeneratedFunctions.edwithin_tgeoarr_tgeoarr(
            arr1, a.length, arr2, b.length, dist, countPtr);
        Reference.reachabilityFence(arr1);
        Reference.reachabilityFence(arr2);
        return readPairsAndFree(res, countPtr.getInt(0));
    }

    /** Index pairs {@code {i,j}} (0-based) always spatially disjoint (never intersecting). */
    public static int[][] aDisjointPairs(Pointer[] a, Pointer[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0) return new int[0][];
        Runtime rt = Runtime.getSystemRuntime();
        Pointer arr1 = marshal(a, rt), arr2 = marshal(b, rt);
        Pointer countPtr = Memory.allocateDirect(rt, Integer.BYTES);
        Pointer res = GeneratedFunctions.adisjoint_tgeoarr_tgeoarr(
            arr1, a.length, arr2, b.length, countPtr);
        Reference.reachabilityFence(arr1);
        Reference.reachabilityFence(arr2);
        return readPairsAndFree(res, countPtr.getInt(0));
    }

    /** Index pairs ever within {@code dist}, each with the {@code tstzspanset} of in-range times. */
    public static TDwithin tDwithinPairs(Pointer[] a, Pointer[] b, double dist) {
        if (a == null || b == null || a.length == 0 || b.length == 0)
            return new TDwithin(new int[0][], new String[0]);
        Runtime rt = Runtime.getSystemRuntime();
        Pointer arr1 = marshal(a, rt), arr2 = marshal(b, rt);
        Pointer countPtr = Memory.allocateDirect(rt, Integer.BYTES);
        Pointer periodsPtr = Memory.allocateDirect(rt, Long.BYTES);
        Pointer res = GeneratedFunctions.tdwithin_tgeoarr_tgeoarr(
            arr1, a.length, arr2, b.length, dist, countPtr, periodsPtr);
        Reference.reachabilityFence(arr1);
        Reference.reachabilityFence(arr2);
        int cnt = countPtr.getInt(0);
        if (res == null || cnt == 0) return new TDwithin(new int[0][], new String[0]);
        Pointer ssArr = periodsPtr.getPointer(0);
        int[][] pairs = new int[cnt][2];
        String[] periods = new String[cnt];
        for (int k = 0; k < cnt; k++) {
            pairs[k][0] = res.getInt((long) (2 * k) * Integer.BYTES);
            pairs[k][1] = res.getInt((long) (2 * k + 1) * Integer.BYTES);
            Pointer ss = ssArr == null ? null : ssArr.getPointer((long) k * Long.BYTES);
            periods[k] = ss == null ? null : GeneratedFunctions.spanset_as_hexwkb(ss, (byte) 0);
            free(ss);
        }
        free(ssArr);
        free(res);
        return new TDwithin(pairs, periods);
    }

    private static Pointer marshal(Pointer[] xs, Runtime rt) {
        Pointer buf = Memory.allocateDirect(rt, xs.length * Long.BYTES);
        for (int i = 0; i < xs.length; i++) buf.putPointer((long) i * Long.BYTES, xs[i]);
        return buf;
    }

    private static int[][] readPairsAndFree(Pointer res, int cnt) {
        if (res == null || cnt == 0) return new int[0][];
        int[][] out = new int[cnt][2];
        for (int k = 0; k < cnt; k++) {
            out[k][0] = res.getInt((long) (2 * k) * Integer.BYTES);
            out[k][1] = res.getInt((long) (2 * k + 1) * Integer.BYTES);
        }
        free(res);
        return out;
    }

    private static void free(Pointer p) {
        if (p != null) UNSAFE.freeMemory(p.address());
    }
}
