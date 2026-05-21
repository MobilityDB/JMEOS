package utils.spatial;

import functions.functions;
import jnr.ffi.Pointer;

/**
 * Geodesic ("haversine") distance between two WGS84 (longitude, latitude)
 * points, computed via MEOS {@code geog_distance} on ephemeral geography
 * point objects.  This wrapper is the JMEOS-backed replacement for
 * pure-Java haversine formulas used by stream-side consumers (MobilityFlink
 * / MobilityKafka per-event spatial-predicate call sites).
 *
 * <p>Semantics match MEOS-on-PostgreSQL {@code ST_Distance(geography,
 * geography)}: WGS84 spheroidal (not spherical) geodesic distance in
 * meters.  This is the canonical reference shared with every other MEOS
 * spatial operator in the pipeline; the wrapper exists so consumers do
 * not maintain a parallel pure-Java haversine that semantically drifts.
 *
 * <p>Cost: two {@code geog_in} allocations per call, one
 * {@code geog_distance} invocation.  The JNR-FFI pointers go to the JVM
 * for GC; the underlying GSERIALIZED bytes follow MEOS's existing
 * memory-management contract (see {@code functions.functions.geog_in}'s
 * pattern; no explicit free path is required by current JMEOS callers).
 */
public final class Haversine {

  private Haversine() {
    // Utility class — not instantiable.
  }

  /**
   * Geodesic distance in meters between two WGS84 points.
   *
   * <p>The coordinates are interpreted as (longitude, latitude) in
   * degrees, in SRID 4326 (WGS84).
   *
   * @param lon1 longitude of point 1, degrees in {@code (-180, 180]}
   * @param lat1 latitude  of point 1, degrees in {@code [-90, 90]}
   * @param lon2 longitude of point 2, degrees in {@code (-180, 180]}
   * @param lat2 latitude  of point 2, degrees in {@code [-90, 90]}
   * @return geodesic distance in meters, non-negative
   */
  public static double distance(double lon1, double lat1,
                                double lon2, double lat2) {
    // Build EWKT strings with Java's locale-independent Double.toString
    // (avoids "%f" which depends on Locale.getDefault() for the
    // decimal separator and would corrupt WKT in fr-FR / de-DE etc.).
    String wkt1 = "SRID=4326;POINT(" + lon1 + " " + lat1 + ")";
    String wkt2 = "SRID=4326;POINT(" + lon2 + " " + lat2 + ")";
    Pointer g1 = functions.geog_in(wkt1, -1);
    Pointer g2 = functions.geog_in(wkt2, -1);
    return functions.geog_distance(g1, g2);
  }
}
