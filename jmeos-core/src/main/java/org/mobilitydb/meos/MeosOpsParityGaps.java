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

/**
 * Forwarding facade methods for MEOS public-surface functions not emitted
 * by the tier-aware code generator. Each method delegates to its JMEOS
 * {@code functions.GeneratedFunctions} export under the shared
 * {@link MeosOpsRuntime#MEOS_AVAILABLE} guard.
 */
public final class MeosOpsParityGaps {

    private MeosOpsParityGaps() { /* utility */ }

    /** MEOS {@code acontains_geo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int acontains_geo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acontains_geo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acontains_geo_trgeo(arg0, arg1);
    }

    /** MEOS {@code acovers_geo_tgeo} — meos_geo.h · scalar / stateless. */
    public static int acovers_geo_tgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_geo_tgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_geo_tgeo(arg0, arg1);
    }

    /** MEOS {@code acovers_geo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int acovers_geo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_geo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_geo_trgeo(arg0, arg1);
    }

    /** MEOS {@code acovers_tcbuffer_tcbuffer} — meos_cbuffer.h · scalar / stateless. */
    public static int acovers_tcbuffer_tcbuffer(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_tcbuffer_tcbuffer requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_tcbuffer_tcbuffer(arg0, arg1);
    }

    /** MEOS {@code acovers_tgeo_geo} — meos_geo.h · scalar / stateless. */
    public static int acovers_tgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_tgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_tgeo_geo(arg0, arg1);
    }

    /** MEOS {@code acovers_tgeo_tgeo} — meos_geo.h · scalar / stateless. */
    public static int acovers_tgeo_tgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_tgeo_tgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_tgeo_tgeo(arg0, arg1);
    }

    /** MEOS {@code acovers_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int acovers_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("acovers_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.acovers_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code adisjoint_tgeoarr_tgeoarr} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer adisjoint_tgeoarr_tgeoarr(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3, jnr.ffi.Pointer arg4) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("adisjoint_tgeoarr_tgeoarr requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.adisjoint_tgeoarr_tgeoarr(arg0, arg1, arg2, arg3, arg4);
    }

    /** MEOS {@code adisjoint_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int adisjoint_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("adisjoint_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.adisjoint_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code adisjoint_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int adisjoint_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("adisjoint_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.adisjoint_trgeo_trgeo(arg0, arg1);
    }

    /** MEOS {@code adwithin_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int adwithin_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("adwithin_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.adwithin_trgeo_geo(arg0, arg1, arg2);
    }

    /** MEOS {@code adwithin_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int adwithin_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("adwithin_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.adwithin_trgeo_trgeo(arg0, arg1, arg2);
    }

    /** MEOS {@code aintersects_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int aintersects_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("aintersects_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.aintersects_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code aintersects_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int aintersects_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("aintersects_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.aintersects_trgeo_trgeo(arg0, arg1);
    }

    /** MEOS {@code atouches_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int atouches_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("atouches_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.atouches_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code bearing_tpoint_point} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer bearing_tpoint_point(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("bearing_tpoint_point requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.bearing_tpoint_point(arg0, arg1, arg2);
    }

    /** MEOS {@code bearing_tpoint_tpoint} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer bearing_tpoint_tpoint(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("bearing_tpoint_tpoint requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.bearing_tpoint_tpoint(arg0, arg1);
    }

    /** MEOS {@code bigintspan_to_floatspan} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer bigintspan_to_floatspan(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("bigintspan_to_floatspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.bigintspan_to_floatspan(arg0);
    }

    /** MEOS {@code bigintspan_to_intspan} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer bigintspan_to_intspan(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("bigintspan_to_intspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.bigintspan_to_intspan(arg0);
    }

    /** MEOS {@code econtains_geo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int econtains_geo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("econtains_geo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.econtains_geo_trgeo(arg0, arg1);
    }

    /** MEOS {@code ecovers_geo_tcbuffer} — meos_cbuffer.h · scalar / stateless. */
    public static int ecovers_geo_tcbuffer(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ecovers_geo_tcbuffer requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ecovers_geo_tcbuffer(arg0, arg1);
    }

    /** MEOS {@code ecovers_geo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int ecovers_geo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ecovers_geo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ecovers_geo_trgeo(arg0, arg1);
    }

    /** MEOS {@code ecovers_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int ecovers_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ecovers_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ecovers_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code edisjoint_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int edisjoint_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("edisjoint_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.edisjoint_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code edisjoint_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int edisjoint_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("edisjoint_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.edisjoint_trgeo_trgeo(arg0, arg1);
    }

    /** MEOS {@code edwithin_tgeoarr_tgeoarr} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer edwithin_tgeoarr_tgeoarr(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3, double arg4, jnr.ffi.Pointer arg5) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("edwithin_tgeoarr_tgeoarr requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.edwithin_tgeoarr_tgeoarr(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /** MEOS {@code edwithin_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int edwithin_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("edwithin_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.edwithin_trgeo_geo(arg0, arg1, arg2);
    }

    /** MEOS {@code edwithin_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int edwithin_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("edwithin_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.edwithin_trgeo_trgeo(arg0, arg1, arg2);
    }

    /** MEOS {@code eintersects_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int eintersects_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("eintersects_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.eintersects_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code eintersects_trgeo_trgeo} — meos_rgeo.h · scalar / stateless. */
    public static int eintersects_trgeo_trgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("eintersects_trgeo_trgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.eintersects_trgeo_trgeo(arg0, arg1);
    }

    /** MEOS {@code etouches_trgeo_geo} — meos_rgeo.h · scalar / stateless. */
    public static int etouches_trgeo_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("etouches_trgeo_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.etouches_trgeo_geo(arg0, arg1);
    }

    /** MEOS {@code floatspan_to_bigintspan} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer floatspan_to_bigintspan(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("floatspan_to_bigintspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.floatspan_to_bigintspan(arg0);
    }

    /** MEOS {@code geogpoint_make2d} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer geogpoint_make2d(int arg0, double arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geogpoint_make2d requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geogpoint_make2d(arg0, arg1, arg2);
    }

    /** MEOS {@code geogpoint_make3dz} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer geogpoint_make3dz(int arg0, double arg1, double arg2, double arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geogpoint_make3dz requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geogpoint_make3dz(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code geomeas_to_tpoint} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer geomeas_to_tpoint(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geomeas_to_tpoint requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geomeas_to_tpoint(arg0);
    }

    /** MEOS {@code geompoint_make2d} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer geompoint_make2d(int arg0, double arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geompoint_make2d requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geompoint_make2d(arg0, arg1, arg2);
    }

    /** MEOS {@code geompoint_make3dz} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer geompoint_make3dz(int arg0, double arg1, double arg2, double arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geompoint_make3dz requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geompoint_make3dz(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code geompoint_to_npoint} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer geompoint_to_npoint(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("geompoint_to_npoint requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.geompoint_to_npoint(arg0);
    }

    /** MEOS {@code intersection_cbuffer_set} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer intersection_cbuffer_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("intersection_cbuffer_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.intersection_cbuffer_set(arg0, arg1);
    }

    /** MEOS {@code intersection_npoint_set} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer intersection_npoint_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("intersection_npoint_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.intersection_npoint_set(arg0, arg1);
    }

    /** MEOS {@code intersection_pose_set} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer intersection_pose_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("intersection_pose_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.intersection_pose_set(arg0, arg1);
    }

    /** MEOS {@code intspan_to_bigintspan} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer intspan_to_bigintspan(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("intspan_to_bigintspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.intspan_to_bigintspan(arg0);
    }

    /** MEOS {@code line_interpolate_point} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer line_interpolate_point(jnr.ffi.Pointer arg0, double arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("line_interpolate_point requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.line_interpolate_point(arg0, arg1, arg2);
    }

    /** MEOS {@code line_locate_point} — meos_geo.h · scalar / stateless. */
    public static double line_locate_point(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("line_locate_point requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.line_locate_point(arg0, arg1);
    }

    /** MEOS {@code line_point_n} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer line_point_n(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("line_point_n requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.line_point_n(arg0, arg1);
    }

    /** MEOS {@code line_substring} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer line_substring(jnr.ffi.Pointer arg0, double arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("line_substring requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.line_substring(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_initialize_noexit_error_handler} — meos.h · scalar / stateless. */
    public static void meos_initialize_noexit_error_handler() {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_initialize_noexit_error_handler requires libmeos — set -Dmeos.enabled=true");
        functions.GeneratedFunctions.meos_initialize_noexit_error_handler();
    }

    /** MEOS {@code meos_initialize_pointcloud} — meos.h · scalar / stateless. */
    public static void meos_initialize_pointcloud() {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_initialize_pointcloud requires libmeos — set -Dmeos.enabled=true");
        functions.GeneratedFunctions.meos_initialize_pointcloud();
    }

    /** MEOS {@code meos_set_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_set_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_set_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_set_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_set_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_set_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_set_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_set_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_set_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_set_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_set_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_set_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_set_ways_csv} — meos.h · scalar / stateless. */
    public static void meos_set_ways_csv(java.lang.String arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_set_ways_csv requires libmeos — set -Dmeos.enabled=true");
        functions.GeneratedFunctions.meos_set_ways_csv(arg0);
    }

    /** MEOS {@code meos_span_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_span_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_span_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_span_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_span_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_span_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_span_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_span_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_span_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_span_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_span_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_span_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_spanset_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_spanset_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_spanset_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_spanset_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_spanset_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_spanset_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_spanset_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_spanset_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_spanset_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_spanset_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_spanset_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_spanset_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_stbox_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_stbox_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_stbox_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_stbox_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_stbox_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_stbox_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_stbox_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_stbox_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_stbox_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_stbox_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_stbox_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_stbox_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_tbox_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_tbox_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_tbox_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_tbox_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_tbox_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_tbox_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_tbox_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_tbox_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_tbox_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_tbox_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_tbox_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_tbox_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code meos_temporal_arrow_roundtrip} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_temporal_arrow_roundtrip(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_temporal_arrow_roundtrip requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_temporal_arrow_roundtrip(arg0);
    }

    /** MEOS {@code meos_temporal_from_arrow} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer meos_temporal_from_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_temporal_from_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_temporal_from_arrow(arg0, arg1);
    }

    /** MEOS {@code meos_temporal_to_arrow} — meos.h · scalar / stateless. */
    public static boolean meos_temporal_to_arrow(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("meos_temporal_to_arrow requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.meos_temporal_to_arrow(arg0, arg1, arg2);
    }

    /** MEOS {@code mindistance_tcbuffer_tcbuffer} — meos_cbuffer.h · scalar / stateless. */
    public static double mindistance_tcbuffer_tcbuffer(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mindistance_tcbuffer_tcbuffer requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mindistance_tcbuffer_tcbuffer(arg0, arg1, arg2);
    }

    /** MEOS {@code mindistance_tgeo_tgeo} — meos_geo.h · scalar / stateless. */
    public static double mindistance_tgeo_tgeo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, double arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mindistance_tgeo_tgeo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mindistance_tgeo_tgeo(arg0, arg1, arg2);
    }

    /** MEOS {@code mindistance_tgeoarr_tgeoarr} — meos_geo.h · scalar / stateless. */
    public static double mindistance_tgeoarr_tgeoarr(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mindistance_tgeoarr_tgeoarr requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mindistance_tgeoarr_tgeoarr(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code mul_float_tfloat} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer mul_float_tfloat(double arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mul_float_tfloat requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mul_float_tfloat(arg0, arg1);
    }

    /** MEOS {@code mul_int_tint} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer mul_int_tint(int arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mul_int_tint requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mul_int_tint(arg0, arg1);
    }

    /** MEOS {@code mul_tfloat_float} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer mul_tfloat_float(jnr.ffi.Pointer arg0, double arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mul_tfloat_float requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mul_tfloat_float(arg0, arg1);
    }

    /** MEOS {@code mul_tint_int} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer mul_tint_int(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mul_tint_int requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mul_tint_int(arg0, arg1);
    }

    /** MEOS {@code mul_tnumber_tnumber} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer mul_tnumber_tnumber(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("mul_tnumber_tnumber requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.mul_tnumber_tnumber(arg0, arg1);
    }

    /** MEOS {@code nsegment_end_position} — meos_npoint.h · scalar / stateless. */
    public static double nsegment_end_position(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("nsegment_end_position requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.nsegment_end_position(arg0);
    }

    /** MEOS {@code nsegment_start_position} — meos_npoint.h · scalar / stateless. */
    public static double nsegment_start_position(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("nsegment_start_position requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.nsegment_start_position(arg0);
    }

    /** MEOS {@code pose_angular_distance} — meos_pose.h · scalar / stateless. */
    public static double pose_angular_distance(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_angular_distance requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_angular_distance(arg0, arg1);
    }

    /** MEOS {@code pose_apply_geo} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer pose_apply_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_apply_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_apply_geo(arg0, arg1);
    }

    /** MEOS {@code pose_as_geopose} — meos_pose.h · scalar / stateless. */
    public static java.lang.String pose_as_geopose(jnr.ffi.Pointer arg0, int arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_as_geopose requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_as_geopose(arg0, arg1, arg2);
    }

    /** MEOS {@code pose_from_geopose} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer pose_from_geopose(java.lang.String arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_from_geopose requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_from_geopose(arg0);
    }

    /** MEOS {@code pose_normalise} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer pose_normalise(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_normalise requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_normalise(arg0);
    }

    /** MEOS {@code pose_pitch} — meos_pose.h · scalar / stateless. */
    public static double pose_pitch(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_pitch requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_pitch(arg0);
    }

    /** MEOS {@code pose_roll} — meos_pose.h · scalar / stateless. */
    public static double pose_roll(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_roll requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_roll(arg0);
    }

    /** MEOS {@code pose_yaw} — meos_pose.h · scalar / stateless. */
    public static double pose_yaw(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("pose_yaw requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.pose_yaw(arg0);
    }

    /** MEOS {@code route_geom} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer route_geom(long arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("route_geom requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.route_geom(arg0);
    }

    /** MEOS {@code rtree_insert_temporal_split} — meos.h · scalar / stateless. */
    public static void rtree_insert_temporal_split(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2, int arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("rtree_insert_temporal_split requires libmeos — set -Dmeos.enabled=true");
        functions.GeneratedFunctions.rtree_insert_temporal_split(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code rtree_search_temporal_dedup} — meos.h · scalar / stateless. */
    public static int rtree_search_temporal_dedup(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3, jnr.ffi.Pointer arg4) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("rtree_search_temporal_dedup requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.rtree_search_temporal_dedup(arg0, arg1, arg2, arg3, arg4);
    }

    /** MEOS {@code spatialset_set_srid} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer spatialset_set_srid(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("spatialset_set_srid requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.spatialset_set_srid(arg0, arg1);
    }

    /** MEOS {@code spatialset_transform} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer spatialset_transform(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("spatialset_transform requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.spatialset_transform(arg0, arg1);
    }

    /** MEOS {@code spatialset_transform_pipeline} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer spatialset_transform_pipeline(jnr.ffi.Pointer arg0, java.lang.String arg1, int arg2, boolean arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("spatialset_transform_pipeline requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.spatialset_transform_pipeline(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code super_union_span_span} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer super_union_span_span(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("super_union_span_span requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.super_union_span_span(arg0, arg1, arg2);
    }

    /** MEOS {@code tand_bool_tbool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tand_bool_tbool(boolean arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tand_bool_tbool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tand_bool_tbool(arg0, arg1);
    }

    /** MEOS {@code tand_tbool_bool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tand_tbool_bool(jnr.ffi.Pointer arg0, boolean arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tand_tbool_bool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tand_tbool_bool(arg0, arg1);
    }

    /** MEOS {@code tand_tbool_tbool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tand_tbool_tbool(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tand_tbool_tbool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tand_tbool_tbool(arg0, arg1);
    }

    /** MEOS {@code tbool_tand_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tbool_tand_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tbool_tand_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tbool_tand_combinefn(arg0, arg1);
    }

    /** MEOS {@code tbool_tor_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tbool_tor_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tbool_tor_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tbool_tor_combinefn(arg0, arg1);
    }

    /** MEOS {@code tboolseq_from_base_tstzset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tboolseq_from_base_tstzset(boolean arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tboolseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tboolseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tboolseq_from_base_tstzspan} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tboolseq_from_base_tstzspan(boolean arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tboolseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tboolseq_from_base_tstzspan(arg0, arg1);
    }

    /** MEOS {@code tboolseqset_from_base_tstzspanset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tboolseqset_from_base_tstzspanset(boolean arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tboolseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tboolseqset_from_base_tstzspanset(arg0, arg1);
    }

    /** MEOS {@code tbox_to_bigintspan} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tbox_to_bigintspan(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tbox_to_bigintspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tbox_to_bigintspan(arg0);
    }

    /** MEOS {@code tcbuffer_convex_hull} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_convex_hull(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_convex_hull requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_convex_hull(arg0);
    }

    /** MEOS {@code tcbuffer_end_value} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_end_value(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_end_value requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_end_value(arg0);
    }

    /** MEOS {@code tcbuffer_from_base_temp} — meos_cbuffer.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tcbuffer_from_base_temp(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_from_base_temp requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_from_base_temp(arg0, arg1);
    }

    /** MEOS {@code tcbuffer_start_value} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_start_value(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_start_value requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_start_value(arg0);
    }

    /** MEOS {@code tcbuffer_traversed_area} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_traversed_area(jnr.ffi.Pointer arg0, boolean arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_traversed_area requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_traversed_area(arg0, arg1);
    }

    /** MEOS {@code tcbuffer_value_at_timestamptz} — meos_cbuffer.h · scalar / stateless. */
    public static boolean tcbuffer_value_at_timestamptz(jnr.ffi.Pointer arg0, java.time.OffsetDateTime arg1, boolean arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_value_at_timestamptz requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_value_at_timestamptz(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tcbuffer_value_n} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_value_n(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_value_n requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_value_n(arg0, arg1);
    }

    /** MEOS {@code tcbuffer_values} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer tcbuffer_values(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbuffer_values requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbuffer_values(arg0, arg1);
    }

    /** MEOS {@code tcbufferinst_make} — meos_cbuffer.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tcbufferinst_make(jnr.ffi.Pointer arg0, java.time.OffsetDateTime arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbufferinst_make requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbufferinst_make(arg0, arg1);
    }

    /** MEOS {@code tcbufferseq_from_base_tstzset} — meos_cbuffer.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tcbufferseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbufferseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbufferseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tcbufferseq_from_base_tstzspan} — meos_cbuffer.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tcbufferseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbufferseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbufferseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tcbufferseqset_from_base_tstzspanset} — meos_cbuffer.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tcbufferseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tcbufferseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tcbufferseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code tdistance_tnpoint_geo} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer tdistance_tnpoint_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tdistance_tnpoint_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tdistance_tnpoint_geo(arg0, arg1);
    }

    /** MEOS {@code tdistance_tpose_geo} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tdistance_tpose_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tdistance_tpose_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tdistance_tpose_geo(arg0, arg1);
    }

    /** MEOS {@code tdwithin_tgeoarr_tgeoarr} — meos_geo.h · scalar / stateless. */
    public static jnr.ffi.Pointer tdwithin_tgeoarr_tgeoarr(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3, double arg4, jnr.ffi.Pointer arg5, jnr.ffi.Pointer arg6) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tdwithin_tgeoarr_tgeoarr requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tdwithin_tgeoarr_tgeoarr(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /** MEOS {@code temporal_basetype_name} — meos.h · scalar / stateless. */
    public static java.lang.String temporal_basetype_name(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("temporal_basetype_name requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.temporal_basetype_name(arg0);
    }

    /** MEOS {@code temporal_tcount_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer temporal_tcount_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("temporal_tcount_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.temporal_tcount_combinefn(arg0, arg1);
    }

    /** MEOS {@code tfloat_tmax_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tfloat_tmax_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloat_tmax_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloat_tmax_combinefn(arg0, arg1);
    }

    /** MEOS {@code tfloat_tmin_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tfloat_tmin_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloat_tmin_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloat_tmin_combinefn(arg0, arg1);
    }

    /** MEOS {@code tfloat_tsum_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tfloat_tsum_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloat_tsum_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloat_tsum_combinefn(arg0, arg1);
    }

    /** MEOS {@code tfloatbox_time_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tfloatbox_time_tiles(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, java.time.OffsetDateTime arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatbox_time_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatbox_time_tiles(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tfloatbox_value_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tfloatbox_value_tiles(jnr.ffi.Pointer arg0, double arg1, double arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatbox_value_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatbox_value_tiles(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tfloatbox_value_time_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tfloatbox_value_time_tiles(jnr.ffi.Pointer arg0, double arg1, jnr.ffi.Pointer arg2, double arg3, java.time.OffsetDateTime arg4, jnr.ffi.Pointer arg5) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatbox_value_time_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatbox_value_time_tiles(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /** MEOS {@code tfloatseq_from_base_tstzset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tfloatseq_from_base_tstzset(double arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tfloatseq_from_base_tstzspan} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tfloatseq_from_base_tstzspan(double arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tfloatseqset_from_base_tstzspanset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tfloatseqset_from_base_tstzspanset(double arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tfloatseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tfloatseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code tgeoseq_from_base_tstzset} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tgeoseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tgeoseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tgeoseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tgeoseq_from_base_tstzspan} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tgeoseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tgeoseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tgeoseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tgeoseqset_from_base_tstzspanset} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tgeoseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tgeoseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tgeoseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code tint_tmax_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tint_tmax_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tint_tmax_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tint_tmax_combinefn(arg0, arg1);
    }

    /** MEOS {@code tint_tmin_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tint_tmin_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tint_tmin_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tint_tmin_combinefn(arg0, arg1);
    }

    /** MEOS {@code tint_tsum_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tint_tsum_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tint_tsum_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tint_tsum_combinefn(arg0, arg1);
    }

    /** MEOS {@code tintbox_time_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tintbox_time_tiles(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, java.time.OffsetDateTime arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintbox_time_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintbox_time_tiles(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tintbox_value_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tintbox_value_tiles(jnr.ffi.Pointer arg0, int arg1, int arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintbox_value_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintbox_value_tiles(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tintbox_value_time_tiles} — meos.h · multidimensional tiling (windowed). */
    public static jnr.ffi.Pointer tintbox_value_time_tiles(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2, int arg3, java.time.OffsetDateTime arg4, jnr.ffi.Pointer arg5) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintbox_value_time_tiles requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintbox_value_time_tiles(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /** MEOS {@code tintseq_from_base_tstzset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tintseq_from_base_tstzset(int arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tintseq_from_base_tstzspan} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tintseq_from_base_tstzspan(int arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintseq_from_base_tstzspan(arg0, arg1);
    }

    /** MEOS {@code tintseqset_from_base_tstzspanset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tintseqset_from_base_tstzspanset(int arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tintseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tintseqset_from_base_tstzspanset(arg0, arg1);
    }

    /** MEOS {@code tnot_tbool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnot_tbool(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnot_tbool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnot_tbool(arg0);
    }

    /** MEOS {@code tnpoint_end_value} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnpoint_end_value(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_end_value requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_end_value(arg0);
    }

    /** MEOS {@code tnpoint_from_base_temp} — meos_npoint.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tnpoint_from_base_temp(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_from_base_temp requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_from_base_temp(arg0, arg1);
    }

    /** MEOS {@code tnpoint_start_value} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnpoint_start_value(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_start_value requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_start_value(arg0);
    }

    /** MEOS {@code tnpoint_value_at_timestamptz} — meos_npoint.h · scalar / stateless. */
    public static boolean tnpoint_value_at_timestamptz(jnr.ffi.Pointer arg0, java.time.OffsetDateTime arg1, boolean arg2, jnr.ffi.Pointer arg3) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_value_at_timestamptz requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_value_at_timestamptz(arg0, arg1, arg2, arg3);
    }

    /** MEOS {@code tnpoint_value_n} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnpoint_value_n(jnr.ffi.Pointer arg0, int arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_value_n requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_value_n(arg0, arg1);
    }

    /** MEOS {@code tnpoint_values} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnpoint_values(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpoint_values requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpoint_values(arg0, arg1);
    }

    /** MEOS {@code tnpointseq_from_base_tstzset} — meos_npoint.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tnpointseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpointseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpointseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tnpointseq_from_base_tstzspan} — meos_npoint.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tnpointseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpointseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpointseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tnpointseqset_from_base_tstzspanset} — meos_npoint.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tnpointseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnpointseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnpointseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code tnumber_tavg_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tnumber_tavg_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tnumber_tavg_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tnumber_tavg_combinefn(arg0, arg1);
    }

    /** MEOS {@code tor_bool_tbool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tor_bool_tbool(boolean arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tor_bool_tbool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tor_bool_tbool(arg0, arg1);
    }

    /** MEOS {@code tor_tbool_bool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tor_tbool_bool(jnr.ffi.Pointer arg0, boolean arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tor_tbool_bool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tor_tbool_bool(arg0, arg1);
    }

    /** MEOS {@code tor_tbool_tbool} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer tor_tbool_tbool(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tor_tbool_tbool requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tor_tbool_tbool(arg0, arg1);
    }

    /** MEOS {@code tpointseq_from_base_tstzset} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tpointseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpointseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpointseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tpointseq_from_base_tstzspan} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tpointseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpointseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpointseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tpointseq_make_coords} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tpointseq_make_coords(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2, jnr.ffi.Pointer arg3, int arg4, int arg5, boolean arg6, boolean arg7, boolean arg8, int arg9, boolean arg10) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpointseq_make_coords requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpointseq_make_coords(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }

    /** MEOS {@code tpointseqset_from_base_tstzspanset} — meos_geo.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tpointseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpointseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpointseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code tpose_angular_speed} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_angular_speed(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_angular_speed requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_angular_speed(arg0);
    }

    /** MEOS {@code tpose_apply_geo} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_apply_geo(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_apply_geo requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_apply_geo(arg0, arg1);
    }

    /** MEOS {@code tpose_as_geopose} — meos_pose.h · scalar / stateless. */
    public static java.lang.String tpose_as_geopose(jnr.ffi.Pointer arg0, int arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_as_geopose requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_as_geopose(arg0, arg1, arg2);
    }

    /** MEOS {@code tpose_from_base_temp} — meos_pose.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tpose_from_base_temp(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_from_base_temp requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_from_base_temp(arg0, arg1);
    }

    /** MEOS {@code tpose_from_geopose} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_from_geopose(java.lang.String arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_from_geopose requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_from_geopose(arg0);
    }

    /** MEOS {@code tpose_from_mfjson} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_from_mfjson(java.lang.String arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_from_mfjson requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_from_mfjson(arg0);
    }

    /** MEOS {@code tpose_pitch} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_pitch(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_pitch requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_pitch(arg0);
    }

    /** MEOS {@code tpose_roll} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_roll(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_roll requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_roll(arg0);
    }

    /** MEOS {@code tpose_speed} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_speed(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_speed requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_speed(arg0);
    }

    /** MEOS {@code tpose_yaw} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer tpose_yaw(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tpose_yaw requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tpose_yaw(arg0);
    }

    /** MEOS {@code tposeinst_make} — meos_pose.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tposeinst_make(jnr.ffi.Pointer arg0, java.time.OffsetDateTime arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tposeinst_make requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tposeinst_make(arg0, arg1);
    }

    /** MEOS {@code tposeseq_from_base_tstzset} — meos_pose.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tposeseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tposeseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tposeseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code tposeseq_from_base_tstzspan} — meos_pose.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tposeseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tposeseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tposeseq_from_base_tstzspan(arg0, arg1, arg2);
    }

    /** MEOS {@code tposeseqset_from_base_tstzspanset} — meos_pose.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tposeseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, int arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tposeseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tposeseqset_from_base_tstzspanset(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_at_geom} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_at_geom(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_at_geom requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_at_geom(arg0, arg1);
    }

    /** MEOS {@code trgeometry_at_stbox} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_at_stbox(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_at_stbox requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_at_stbox(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_body_point_trajectory} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_body_point_trajectory(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_body_point_trajectory requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_body_point_trajectory(arg0, arg1);
    }

    /** MEOS {@code trgeometry_centroid} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_centroid(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_centroid requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_centroid(arg0);
    }

    /** MEOS {@code trgeometry_convex_hull} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_convex_hull(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_convex_hull requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_convex_hull(arg0);
    }

    /** MEOS {@code trgeometry_cumulative_length} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_cumulative_length(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_cumulative_length requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_cumulative_length(arg0);
    }

    /** MEOS {@code trgeometry_dyntimewarp_distance} — meos_rgeo.h · scalar / stateless. */
    public static double trgeometry_dyntimewarp_distance(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_dyntimewarp_distance requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_dyntimewarp_distance(arg0, arg1);
    }

    /** MEOS {@code trgeometry_dyntimewarp_path} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_dyntimewarp_path(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_dyntimewarp_path requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_dyntimewarp_path(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_frechet_distance} — meos_rgeo.h · scalar / stateless. */
    public static double trgeometry_frechet_distance(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_frechet_distance requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_frechet_distance(arg0, arg1);
    }

    /** MEOS {@code trgeometry_frechet_path} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_frechet_path(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_frechet_path requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_frechet_path(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_hausdorff_distance} — meos_rgeo.h · scalar / stateless. */
    public static double trgeometry_hausdorff_distance(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_hausdorff_distance requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_hausdorff_distance(arg0, arg1);
    }

    /** MEOS {@code trgeometry_length} — meos_rgeo.h · scalar / stateless. */
    public static double trgeometry_length(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_length requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_length(arg0);
    }

    /** MEOS {@code trgeometry_minus_geom} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_minus_geom(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_minus_geom requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_minus_geom(arg0, arg1);
    }

    /** MEOS {@code trgeometry_minus_stbox} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_minus_stbox(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_minus_stbox requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_minus_stbox(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_space_boxes} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_space_boxes(jnr.ffi.Pointer arg0, double arg1, double arg2, double arg3, jnr.ffi.Pointer arg4, boolean arg5, boolean arg6, jnr.ffi.Pointer arg7) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_space_boxes requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_space_boxes(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    /** MEOS {@code trgeometry_space_time_boxes} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_space_time_boxes(jnr.ffi.Pointer arg0, double arg1, double arg2, double arg3, jnr.ffi.Pointer arg4, jnr.ffi.Pointer arg5, java.time.OffsetDateTime arg6, boolean arg7, boolean arg8, jnr.ffi.Pointer arg9) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_space_time_boxes requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_space_time_boxes(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    /** MEOS {@code trgeometry_speed} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_speed(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_speed requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_speed(arg0);
    }

    /** MEOS {@code trgeometry_split_each_n_stboxes} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_split_each_n_stboxes(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_split_each_n_stboxes requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_split_each_n_stboxes(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_split_n_stboxes} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_split_n_stboxes(jnr.ffi.Pointer arg0, int arg1, jnr.ffi.Pointer arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_split_n_stboxes requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_split_n_stboxes(arg0, arg1, arg2);
    }

    /** MEOS {@code trgeometry_stboxes} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_stboxes(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_stboxes requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_stboxes(arg0, arg1);
    }

    /** MEOS {@code trgeometry_to_tgeometry} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_to_tgeometry(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_to_tgeometry requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_to_tgeometry(arg0);
    }

    /** MEOS {@code trgeometry_twcentroid} — meos_rgeo.h · scalar / stateless. */
    public static jnr.ffi.Pointer trgeometry_twcentroid(jnr.ffi.Pointer arg0) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("trgeometry_twcentroid requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.trgeometry_twcentroid(arg0);
    }

    /** MEOS {@code tsequence_make} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tsequence_make(jnr.ffi.Pointer arg0, int arg1, boolean arg2, boolean arg3, int arg4, boolean arg5) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tsequence_make requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tsequence_make(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /** MEOS {@code tsequenceset_make} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer tsequenceset_make(jnr.ffi.Pointer arg0, int arg1, boolean arg2) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("tsequenceset_make requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.tsequenceset_make(arg0, arg1, arg2);
    }

    /** MEOS {@code ttext_tmax_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer ttext_tmax_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ttext_tmax_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ttext_tmax_combinefn(arg0, arg1);
    }

    /** MEOS {@code ttext_tmin_combinefn} — meos.h · scalar / stateless. */
    public static jnr.ffi.Pointer ttext_tmin_combinefn(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ttext_tmin_combinefn requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ttext_tmin_combinefn(arg0, arg1);
    }

    /** MEOS {@code ttextseq_from_base_tstzset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer ttextseq_from_base_tstzset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ttextseq_from_base_tstzset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ttextseq_from_base_tstzset(arg0, arg1);
    }

    /** MEOS {@code ttextseq_from_base_tstzspan} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer ttextseq_from_base_tstzspan(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ttextseq_from_base_tstzspan requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ttextseq_from_base_tstzspan(arg0, arg1);
    }

    /** MEOS {@code ttextseqset_from_base_tstzspanset} — meos.h · whole-sequence constructor — not a per-event op. */
    public static jnr.ffi.Pointer ttextseqset_from_base_tstzspanset(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("ttextseqset_from_base_tstzspanset requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.ttextseqset_from_base_tstzspanset(arg0, arg1);
    }

    /** MEOS {@code union_cbuffer_set} — meos_cbuffer.h · scalar / stateless. */
    public static jnr.ffi.Pointer union_cbuffer_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("union_cbuffer_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.union_cbuffer_set(arg0, arg1);
    }

    /** MEOS {@code union_npoint_set} — meos_npoint.h · scalar / stateless. */
    public static jnr.ffi.Pointer union_npoint_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("union_npoint_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.union_npoint_set(arg0, arg1);
    }

    /** MEOS {@code union_pose_set} — meos_pose.h · scalar / stateless. */
    public static jnr.ffi.Pointer union_pose_set(jnr.ffi.Pointer arg0, jnr.ffi.Pointer arg1) {
        if (!MeosOpsRuntime.MEOS_AVAILABLE)
            throw new UnsupportedOperationException("union_pose_set requires libmeos — set -Dmeos.enabled=true");
        return functions.GeneratedFunctions.union_pose_set(arg0, arg1);
    }

}
