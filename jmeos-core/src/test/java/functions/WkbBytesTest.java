package functions;

import java.util.HexFormat;

import jnr.ffi.Pointer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The WKB of a MEOS value as Java bytes: each {@code *_as_wkb} returns the bytes MEOS wrote,
 * whose hex is the value's {@code *_as_hexwkb}, and each {@code *_from_wkb} reads them back
 * to the same value.
 */
@DisplayName("WKB as Java bytes")
@ExtendWith(TestLogger.class)
class WkbBytesTest {

    private static final byte EXTENDED = 4;

    @BeforeAll
    static void initMeos() {
        GeneratedFunctions.meos_initialize_timezone("UTC");
    }

    private static byte[] unhex(String hex) {
        return HexFormat.of().parseHex(hex);
    }

    @Test
    @DisplayName("temporal: bytes are the hex WKB and read back to the same value")
    void temporal() {
        Pointer t = GeneratedFunctions.tgeompoint_in(
            "SRID=4326;[Point(1 1)@2000-01-01, Point(2 3)@2000-01-02]");
        byte[] wkb = GeneratedFunctions.temporal_as_wkb(t, EXTENDED);
        String hex = GeneratedFunctions.temporal_as_hexwkb(t, EXTENDED);
        assertArrayEquals(unhex(hex), wkb);
        Pointer back = GeneratedFunctions.temporal_from_wkb(wkb);
        assertTrue(GeneratedFunctions.temporal_eq(t, back));
    }

    @Test
    @DisplayName("set: bytes are the hex WKB and read back to the same value")
    void set() {
        Pointer s = GeneratedFunctions.floatset_in("{1.5, 2.5, 3.5}");
        byte[] wkb = GeneratedFunctions.set_as_wkb(s, EXTENDED);
        assertArrayEquals(unhex(GeneratedFunctions.set_as_hexwkb(s, EXTENDED)), wkb);
        assertTrue(GeneratedFunctions.set_eq(s, GeneratedFunctions.set_from_wkb(wkb)));
    }

    @Test
    @DisplayName("span and span set: bytes are the hex WKB and read back to the same value")
    void spanAndSpanset() {
        Pointer sp = GeneratedFunctions.floatspan_in("[1.5, 2.5)");
        byte[] w1 = GeneratedFunctions.span_as_wkb(sp, EXTENDED);
        assertArrayEquals(unhex(GeneratedFunctions.span_as_hexwkb(sp, EXTENDED)), w1);
        assertTrue(GeneratedFunctions.span_eq(sp, GeneratedFunctions.span_from_wkb(w1)));

        Pointer ss = GeneratedFunctions.floatspanset_in("{[1.5, 2.5), [3.5, 4.5]}");
        byte[] w2 = GeneratedFunctions.spanset_as_wkb(ss, EXTENDED);
        assertArrayEquals(unhex(GeneratedFunctions.spanset_as_hexwkb(ss, EXTENDED)), w2);
        assertTrue(GeneratedFunctions.spanset_eq(ss, GeneratedFunctions.spanset_from_wkb(w2)));
    }

    @Test
    @DisplayName("boxes: bytes are the hex WKB and read back to the same value")
    void boxes() {
        Pointer tb = GeneratedFunctions.tbox_in("TBOXFLOAT XT([1.5, 2.5),[2000-01-01, 2000-01-02])");
        byte[] w1 = GeneratedFunctions.tbox_as_wkb(tb, EXTENDED);
        assertArrayEquals(unhex(GeneratedFunctions.tbox_as_hexwkb(tb, EXTENDED)), w1);
        assertTrue(GeneratedFunctions.tbox_eq(tb, GeneratedFunctions.tbox_from_wkb(w1)));

        Pointer sb = GeneratedFunctions.stbox_in("SRID=4326;STBOX XT(((1,1),(2,2)),[2000-01-01, 2000-01-02])");
        byte[] w2 = GeneratedFunctions.stbox_as_wkb(sb, EXTENDED);
        assertArrayEquals(unhex(GeneratedFunctions.stbox_as_hexwkb(sb, EXTENDED)), w2);
        assertTrue(GeneratedFunctions.stbox_eq(sb, GeneratedFunctions.stbox_from_wkb(w2)));
    }
}
