package utils.spatial;

import functions.error_handler;
import functions.error_handler_fn;
import functions.functions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link utils.spatial.PointToSegment}.  Numeric expected
 * values are MEOS-on-PostgreSQL ground truth (WGS84 spheroidal).
 */
@ExtendWith(TestLogger.class)
public class PointToSegmentTest {

  private static final double METER_TOLERANCE = 1.0;

  private static final error_handler_fn errorHandler = new error_handler();

  @BeforeAll
  static void initMeos() {
    functions.meos_initialize_timezone("UTC");
    functions.meos_initialize_error_handler(errorHandler);
  }

  /** Point on the segment endpoint = zero distance. */
  @Test
  void zeroAtEndpoint() {
    double d = PointToSegment.distance(
        4.35, 50.85,
        4.35, 50.85, 4.40, 50.90);
    assertEquals(0.0, d, METER_TOLERANCE);
  }

  /** Point on the segment interior (midpoint) = zero distance. */
  @Test
  void zeroAtInterior() {
    // Midpoint of the 5.5 km meridian segment is on the segment exactly.
    double d = PointToSegment.distance(
        4.35, 50.875,
        4.35, 50.85, 4.35, 50.90);
    assertEquals(0.0, d, METER_TOLERANCE);
  }

  /**
   * Point off to the side of the segment: perpendicular distance.
   *
   * <p>A 5.5 km meridian segment along longitude 4.35 between latitudes
   * 50.85 and 50.90; a query point at (4.40, 50.875) — same latitude
   * as the midpoint but 0.05° east, which is ~3.5 km at this latitude.
   */
  @Test
  void perpendicularDistance() {
    double d = PointToSegment.distance(
        4.40, 50.875,
        4.35, 50.85, 4.35, 50.90);
    // MEOS-on-PostgreSQL ground truth: WGS84 spheroidal distance from
    // (4.40, 50.875) to the meridian segment ≈ 3520 m
    assertTrue(d > 3400.0 && d < 3700.0, "expected ~3520 m, got " + d);
  }

  /** Beyond the segment: distance to the nearest endpoint. */
  @Test
  void beyondEndpointFallsBackToEndpoint() {
    // Query point far north of the segment (north of latitude 50.90)
    // → nearest is endpoint (4.35, 50.90).
    double dToSegment = PointToSegment.distance(
        4.35, 51.00,
        4.35, 50.85, 4.35, 50.90);
    double dToEndpoint = Haversine.distance(4.35, 51.00, 4.35, 50.90);
    assertEquals(dToEndpoint, dToSegment, METER_TOLERANCE);
  }

  /** Degenerate segment (s1 == s2) reduces to point-to-point distance. */
  @Test
  void degenerateSegmentReducesToHaversine() {
    double dSeg = PointToSegment.distance(
        4.35, 50.85,
        4.40, 50.90, 4.40, 50.90);
    double dPt = Haversine.distance(4.35, 50.85, 4.40, 50.90);
    assertEquals(dPt, dSeg, METER_TOLERANCE);
  }
}
