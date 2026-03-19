package examples;

import functions.*;
import jnr.ffi.Pointer;
import types.basic.tfloat.TFloatInst;
import types.basic.tpoint.tgeom.TGeomPointInst;

/**
 * Demonstration of the JMEOS error-handling system.
 *
 * Each test method triggers one specific MeosException subclass and explains
 * the scenario that would produce it.
 *
 * Exception hierarchy (see functions/MeosErrorHandler):
 *
 *   MeosException  (base, code + message)
 *   ├── MeosInternalError          (1)  - internal MEOS bug
 *   ├── MeosInternalTypeError      (2)  - internal type mismatch          [test 7]
 *   ├── MeosValueOutOfRangeError   (3)  - mathematical domain error
 *   ├── MeosDivisionByZeroError    (4)  - division by zero
 *   ├── MeosMemoryAllocError       (5)  - memory allocation failure
 *   ├── MeosAggregationError       (6)  - aggregation inconsistency
 *   ├── MeosDirectoryError         (7)  - directory not found
 *   ├── MeosFileError              (8)  - file I/O error
 *   ├── MeosInvalidArgValueError   (12) - argument value out of logical range  [tests 2, 3, 4]
 *   ├── MeosFeatureNotSupported    (13) - operation not supported for this type
 *   ├── MeosMfJsonInputError       (20) - malformed MF-JSON input          [test 5]
 *   ├── MeosTextInputError         (22) - malformed WKT / text input       [test 1]
 *   ├── MeosWkbInputError          (24) - malformed WKB / hexWKB input     [test 6]
 *   └── ...
 */
public class N01_ErrorHandlingDemo {

    private static final error_handler_fn ERROR_HANDLER = new MeosErrorHandler();

    // Colours for cleaner console output
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD   = "\u001B[1m";
    private static final String RESET  = "\u001B[0m";

    public static void main(String[] args) {
        functions.meos_initialize();
        functions.meos_initialize_timezone("UTC");
        functions.meos_initialize_error_handler(ERROR_HANDLER);

        int passed = 0;
        int total  = 0;

        System.out.println(BOLD + "\n╔══════════════════════════════════════════════╗");
        System.out.println(      "║      JMEOS - Error Handling Demo             ║");
        System.out.println(      "╚══════════════════════════════════════════════╝" + RESET);

        //passed += test1_TextInputError()               ? 1 : 0; total++;
        passed += test2_InvalidArgValueError_span()    ? 1 : 0; total++;
        passed += test3_InvalidArgValueError_divZero() ? 1 : 0; total++;
        passed += test4_MfJsonInputError()             ? 1 : 0; total++;
        passed += test5_WkbInputError()                ? 1 : 0; total++;
        passed += test6_InternalTypeError()            ? 1 : 0; total++;
        passed += test7_TextInputError()               ? 1 : 0; total++;
        passed += test8_ValidCase()                    ? 1 : 0; total++;

        System.out.println(BOLD + "\n──────────────────────────────────────────────");
        System.out.printf("%s  Results: %d / %d tests caught the expected exception%s%n",
                passed == total ? GREEN : YELLOW,
                passed, total, RESET);
        System.out.println(BOLD + "──────────────────────────────────────────────" + RESET);

        functions.meos_finalize();
    }

    // =========================================================================
    // TEST 1 - MeosTextInputError (code 22)
    //
    // At the moment (26-03-17) this test is broken since MEOS doesn't currently
    //      emit an error via meos_error(level, code, message) but just prints it.
    // However, as soon as this behavior is fixed in MEOS, this test will work properly.
    //
    // See test nb°7 for another example of another MeosTextInputError case
    // =========================================================================
    /**
     * The geometry itself is well-formed but the timestamp field is incorrect
     * ("not-a-timestamp"). MEOS parses the geometry successfully, then fails
     * on the temporal part → text input error.
     */
    /*
    private static boolean test1_TextInputError() {
        printHeader(1, "MeosTextInputError (22)",
                "Valid geometry, garbage timestamp from a corrupt AIS record");
        try {
            // Geometry is fine; "not-a-timestamp" makes the temporal parser fail.
            TGeomPointInst inst = new TGeomPointInst("POINT(1.0 2.0)@not-a-timestamp");
            System.out.println(RED + "  ✗ No exception raised: got: " + inst + RESET);
            return false;
        } catch (MeosTextInputError e) {
            printCaught(e);
            return true;
        } catch (MeosException e) {
            printWrongType(e, "MeosTextInputError");
            return false;
        }
    }
    */

    // =========================================================================
    // TEST 2 - MeosInvalidArgValueError (code 12): inverted span bounds
    // =========================================================================
    /**
     * Scenario: a route-planning service builds a temporal window as an integer
     * span of day-of-year values. A bug inverts start/end: lower (300) > upper (100).
     * MEOS rejects spans where lower > upper.
     */
    private static boolean test2_InvalidArgValueError_span() {
        printHeader(2, "MeosInvalidArgValueError (12): inverted span",
                "IntSpan with lower (300) > upper (100)");
        try {
            Pointer span = functions.intspan_make(300, 100, true, true);
            System.out.println(RED + "  ✗ No exception raised: pointer: " + span + RESET);
            return false;
        } catch (MeosInvalidArgValueError e) {
            printCaught(e);
            return true;
        } catch (MeosException e) {
            printWrongType(e, "MeosInvalidArgValueError");
            return false;
        }
    }

    // =========================================================================
    // TEST 3 - MeosInvalidArgValueError (code 12): division by zero
    // =========================================================================
    /**
     * At the moment, the error emitted by MEOS for this operation is MeosInvalidArgValueError
     * but a fix in MEOS is currently in the works. Therefore, the caught exception here
     * should then be switched to MeosDivisionByZeroError when the fix will be completed.
     *
     * NOTE: although MeosDivisionByZeroError (code 4) exists in the exception
     * map, this version of libmeos reports division by zero with code 12
     * (MeosInvalidArgValueError) and the message "Division by zero".
     */
    private static boolean test3_InvalidArgValueError_divZero() {
        printHeader(3, "MeosInvalidArgValueError (12): division by zero",
                "Temporal SOG divided by a zero scaling factor (config bug)");
        try {
            TFloatInst sog = new TFloatInst("12.5@2024-06-01 08:00:00+00");
            Pointer result = functions.div_tfloat_float(sog.getInner(), 0.0);
            System.out.println(RED + "  ✗ No exception raised - pointer: " + result + RESET);
            return false;
        } catch (MeosDivisionByZeroError e) {
            printCaught(e);
            return true;
        } catch (MeosInvalidArgValueError e) {
            // Verify this is indeed the division-by-zero case, not a bounds error.
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("division")) {
                printCaught(e);
                System.out.println(YELLOW
                        + "   ℹ  Note: MEOS uses code 12 (InvalidArgValue) for div/0,"
                        + " not code 4 (DivisionByZero)" + RESET);
                return true;
            }
            System.out.printf(YELLOW
                            + "   ⚠ MeosInvalidArgValueError raised but unexpected message: %s%s%n",
                    e.getMessage(), RESET);
            return false;
        } catch (MeosException e) {
            printWrongType(e, "MeosInvalidArgValueError");
            return false;
        }
    }

    // =========================================================================
    // TEST 4 - MeosMfJsonInputError (code 20)
    // =========================================================================
    /**
     * Scenario: a REST endpoint returns MF-JSON describing vessel trajectories.
     * A middleware bug truncates the payload during the transmission → invalid JSON.
     */
    private static boolean test4_MfJsonInputError() {
        printHeader(4, "MeosMfJsonInputError (20)",
                "Truncated MF-JSON payload from a REST API");
        String truncatedMfJson =
                "{\"type\":\"MovingPoint\",\"coordinates\":[[1.0,2.0"; // cut off
        try {
            Pointer traj = functions.tgeompoint_from_mfjson(truncatedMfJson);
            System.out.println(RED + "  ✗ No exception raised - pointer: " + traj + RESET);
            return false;
        } catch (MeosMfJsonInputError e) {
            printCaught(e);
            return true;
        } catch (MeosException e) {
            printWrongType(e, "MeosMfJsonInputError");
            return false;
        }
    }

    // =========================================================================
    // TEST 5 - MeosWkbInputError (code 24)
    // =========================================================================
    /**
     * A storage bug flips bytes in one record → invalid WKB structure.
     */
    private static boolean test5_WkbInputError() {
        printHeader(5, "MeosWkbInputError (24)",
                "Bit-flipped hexWKB blob from the data warehouse");
        String corruptHexWkb = "DEADBEEFCAFE0123456789ABCDEF";
        try {
            Pointer temp = functions.temporal_from_hexwkb(corruptHexWkb);
            System.out.println(RED + "  ✗ No exception raised - pointer: " + temp + RESET);
            return false;
        } catch (MeosWkbInputError e) {
            printCaught(e);
            return true;
        } catch (MeosException e) {
            printWrongType(e, "MeosWkbInputError");
            return false;
        }
    }

    // =========================================================================
    // TEST 6 - MeosInternalTypeError (code 2)
    // =========================================================================
    /**
     * Scenario: the WKT itself is syntactically broken: missing closing parenthesis on the geometry.
     * MEOS fails at the geometry level before ever reaching the timestamp
     * parser, raising an internal type error rather than a text-input error.
     */
    private static boolean test6_InternalTypeError() {
        printHeader(6, "MeosInternalTypeError (2)",
                "Syntactically broken geometry (missing closing parenthesis)");
        try {
            // Missing ')' before '@': MEOS cannot form a valid geometry object.
            TGeomPointInst inst = new TGeomPointInst("POINT(181.0 91.0@not-a-date");
            System.out.println(RED + "  ✗ No exception raised - got: " + inst + RESET);
            return false;
        } catch (MeosInternalTypeError e) {
            printCaught(e);
            return true;
        } catch (MeosException e) {
            printWrongType(e, "MeosInternalTypeError");
            return false;
        }
    }

    // =========================================================================
    // TEST 7 - MeosTextInputError (code 22)
    // =========================================================================
    /**
     * Scenario: the WKT itself is syntactically broken: missing closing parenthesis on the geometry.
     * MEOS fails at the geometry level before ever reaching the timestamp
     * parser, raising an internal type error rather than a text-input error.
     */
    private static boolean test7_TextInputError() {
        printHeader(7, "MeosTextInputError", "Completely broken type");
        try {
            TGeomPointInst inst = new TGeomPointInst("not-a-wkt"); // wrong type
            System.out.println(RED + "  ✗ No exception raised - got: " + inst + RESET);
            System.out.println(inst);
            return false;
        } catch (MeosTextInputError e) {
            printCaught(e);
            return true;
        } catch (MeosInvalidArgError e) {
            printWrongType(e, "MeosTextInputError");
            return false;
        } catch (MeosException e) {
            printWrongType(e, "MeosTextInputError");
            System.err.println("MEOS error (code " + e.getCode() + ") : " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // TEST 8 - Valid case (no exception expected)
    // =========================================================================
    /**
     * A well-formed AIS instant is parsed correctly.
     * Verifies that the error handler does not interfere with valid input.
     */
    private static boolean test8_ValidCase() {
        printHeader(8, "Valid case",
                "Well-formed AIS TGeomPointInst - no exception expected");
        try {
            TGeomPointInst inst = new TGeomPointInst(
                    "SRID=4326;POINT(4.3517 50.8503)@2024-06-01 08:00:00+00");
            System.out.println(GREEN + "  ✓ Parsed successfully: " + inst + RESET);
            return true;
        } catch (MeosException e) {
            System.out.println(RED + "  ✗ Unexpected exception: " + e + RESET);
            return false;
        }
    }

    // =========================================================================
    // Formatting helpers
    // =========================================================================

    private static void printHeader(int n, String exceptionName, String scenario) {
        System.out.printf("%n%s── Test %d : %s%s%n", BOLD, n, exceptionName, RESET);
        System.out.println("   Scenario : " + scenario);
    }

    private static void printCaught(MeosException e) {
        System.out.printf(GREEN + "   ✓ Caught %s (code %d): %s%s%n",
                e.getClass().getSimpleName(), e.getCode(), e.getMessage(), RESET);
    }

    private static void printWrongType(MeosException e, String expected) {
        System.out.printf(YELLOW + "   ⚠ Wrong type: expected %s but got %s (code %d): %s%s%n",
                expected, e.getClass().getSimpleName(), e.getCode(), e.getMessage(), RESET);
    }
}