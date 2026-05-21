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
 * Unit tests for {@link utils.spatial.Haversine}.  Numeric expected
 * values are MEOS-on-PostgreSQL ground truth (WGS84 spheroidal).
 */
@ExtendWith(TestLogger.class)
public class HaversineTest {

  private static final double METER_TOLERANCE = 1.0;

  private static final error_handler_fn errorHandler = new error_handler();

  @BeforeAll
  static void initMeos() {
    functions.meos_initialize_timezone("UTC");
    functions.meos_initialize_error_handler(errorHandler);
  }

  /** Same point twice = zero distance. */
  @Test
  void zeroDistanceForIdenticalPoints() {
    double d = Haversine.distance(4.35, 50.85, 4.35, 50.85);
    assertEquals(0.0, d, 1e-9);
  }

  /** ~5.5 km Brussels-area segment along a meridian. */
  @Test
  void shortMeridianSegment() {
    // (4.35, 50.85) to (4.35, 50.90) — 0.05° latitude ≈ 5.56 km on WGS84
    double d = Haversine.distance(4.35, 50.85, 4.35, 50.90);
    assertEquals(5562.0, d, METER_TOLERANCE);
  }

  /** ~264 km Brussels-to-Paris diagonal. */
  @Test
  void brusselsToParis() {
    double d = Haversine.distance(4.35, 50.85, 2.35, 48.86);
    // MEOS-on-PostgreSQL ground truth (WGS84 spheroidal):
    // SELECT ST_Distance(
    //   'SRID=4326;POINT(4.35 50.85)'::geography,
    //   'SRID=4326;POINT(2.35 48.86)'::geography
    // ) -> ~263538 m
    assertEquals(263538.0, d, 500.0);
  }

  /** Symmetric: d(a, b) == d(b, a). */
  @Test
  void symmetric() {
    double d1 = Haversine.distance(4.35, 50.85, 2.35, 48.86);
    double d2 = Haversine.distance(2.35, 48.86, 4.35, 50.85);
    assertEquals(d1, d2, 1e-9);
  }

  /** Non-negative for any input. */
  @Test
  void nonNegative() {
    double d = Haversine.distance(-122.4, 37.8, 139.7, 35.7); // SF -> Tokyo
    assertTrue(d > 0.0);
    // Sanity: SF-Tokyo great-circle distance is ~8270 km
    assertTrue(d > 8.0e6 && d < 9.0e6, "expected ~8.27e6 m, got " + d);
  }
}
