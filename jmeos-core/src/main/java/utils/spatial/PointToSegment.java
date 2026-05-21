package utils.spatial;

import functions.functions;
import jnr.ffi.Pointer;

/**
 * Minimum geodesic distance between a WGS84 point and a 2-vertex line
 * segment (s1 → s2), computed via MEOS {@code geog_distance} on
 * ephemeral geography point + linestring objects.  This wrapper is the
 * JMEOS-backed replacement for the planar equirectangular
 * point-to-segment fallback used by stream-side consumers
 * (MobilityFlink {@code SegmentDistance.java}, MobilityKafka).
 *
 * <p>Semantics match MEOS-on-PostgreSQL {@code ST_Distance(POINT,
 * LINESTRING)} on the geography flavour: WGS84 spheroidal geodesic
 * distance in meters, including the segment-perpendicular case
 * (closest point falls between the endpoints).  This is the canonical
 * reference shared with every other MEOS spatial operator in the
 * pipeline; the wrapper exists so consumers do not maintain a parallel
 * planar fallback that semantically drifts.
 *
 * <p>Cost: two {@code geog_in} allocations per call (point + 2-vertex
 * linestring), one {@code geog_distance} invocation.  The JNR-FFI
 * pointers go to the JVM for GC; the underlying GSERIALIZED bytes
 * follow MEOS's existing memory-management contract.
 */
public final class PointToSegment {

  private PointToSegment() {
    // Utility class — not instantiable.
  }

  /**
   * Minimum geodesic distance in meters from {@code p} to the line
   * segment {@code s1 → s2}.
   *
   * <p>All coordinates are interpreted as (longitude, latitude) in
   * degrees, SRID 4326 (WGS84).  The segment is degenerate if
   * {@code s1 == s2}; in that case this reduces to point-to-point
   * geodesic distance.
   *
   * @param pLon  longitude of the query point, degrees
   * @param pLat  latitude  of the query point, degrees
   * @param s1Lon longitude of segment endpoint 1, degrees
   * @param s1Lat latitude  of segment endpoint 1, degrees
   * @param s2Lon longitude of segment endpoint 2, degrees
   * @param s2Lat latitude  of segment endpoint 2, degrees
   * @return minimum geodesic distance in meters, non-negative
   */
  public static double distance(double pLon, double pLat,
                                double s1Lon, double s1Lat,
                                double s2Lon, double s2Lat) {
    String ptWkt =
        "SRID=4326;POINT(" + pLon + " " + pLat + ")";
    String segWkt =
        "SRID=4326;LINESTRING(" + s1Lon + " " + s1Lat
            + "," + s2Lon + " " + s2Lat + ")";
    Pointer pt = functions.geog_in(ptWkt, -1);
    Pointer seg = functions.geog_in(segWkt, -1);
    return functions.geog_distance(pt, seg);
  }
}
