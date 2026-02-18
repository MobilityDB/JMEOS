package examples;

import jnr.ffi.Pointer;

import java.io.*;

import static functions.functions.*;

/**
 * Demonstrates MEOS aggregate functions using the transfn/finalfn pattern.
 * Shows three types of aggregation:
 *
 * 1. IntSpan Aggregation - Aggregate multiple spans into a single spanset
 * 2. FloatSpanSet Aggregation - Group and aggregate spansets (GROUP BY pattern)
 * 3. TextSet Aggregation - Group and aggregate sets with expandable structures
 *
 * Pattern: transfn/finalfn
 * - transfn: Accumulates values (state = transfn(state, new_value))
 * - finalfn: Produces final result (result = finalfn(state))
 */
public class N13_Aggregation_Demo {

    // Configuration
    private static final String DATA_DIR = "src/main/java/examples/data/";
    private static final int NO_GROUPS = 10;

    /**
     * SECTION 1: IntSpan Aggregation
     * Demonstrates simple aggregation: Span → SpanSet
     *
     * Pattern:
     * 1. Read IntSpan values from CSV
     * 2. Accumulate using span_union_transfn()
     * 3. Finalize using spanset_union_finalfn()
     *
     */
    private static void demonstrateIntSpanAggregation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 1: IntSpan Aggregation (Span → SpanSet)");
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();
        Pointer state = null;  // Accumulator state
        int recordCount = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader(DATA_DIR + "intspan.csv"))) {

            // Skip header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // Parse: k,"[lower,upper]"
                String[] parts = line.split(",\"");
                if (parts.length != 2) continue;

                int k = Integer.parseInt(parts[0]);
                String spanStr = parts[1].replace("\"", "");

                // Parse IntSpan
                Pointer span = intspan_in(spanStr);

                // Aggregate: state = span_union_transfn(state, span)
                state = span_union_transfn(state, span);

                String spanOut = intspan_out(span);
                System.out.println("  Record " + (++recordCount) +
                        " - k: " + k + ", span: " + spanOut);
            }

            System.out.println("\n" + recordCount + " records aggregated.");

            // Finalize: result = spanset_union_finalfn(state)
            Pointer result = spanset_union_finalfn(state);
            String resultStr = intspanset_out(result);

            long elapsed = System.currentTimeMillis() - startTime;

            System.out.println("\n┌─ AGGREGATION RESULT ─────────────────────");
            System.out.println("│ Union of all IntSpans:");
            System.out.println("│ " + resultStr);
            System.out.println("│ Time: " + elapsed + " ms");
            System.out.println("└──────────────────────────────────────────");

        } catch (IOException e) {
            System.err.println("Error reading intspan.csv: " + e.getMessage());
        }
    }

    /**
     * SECTION 2: FloatSpanSet Grouped Aggregation
     * Demonstrates grouped aggregation with multiple groups
     *
     * Pattern:
     * 1. Read FloatSpanSet values from CSV
     * 2. Group by (k % NO_GROUPS)
     * 3. Accumulate per group using spanset_union_transfn()
     * 4. Finalize each group using spanset_union_finalfn()
     */
    private static void demonstrateFloatSpanSetAggregation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 2: FloatSpanSet Grouped Aggregation");
        System.out.println("Pattern: GROUP BY k % " + NO_GROUPS);
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();
        Pointer[] state = new Pointer[NO_GROUPS];  // One state per group
        int recordCount = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader(DATA_DIR + "floatspanset.csv"))) {

            // Skip header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // Parse: k,"{[lower1,upper1],[lower2,upper2]}"
                String[] parts = line.split(",\"");
                if (parts.length != 2) continue;

                int k = Integer.parseInt(parts[0]);
                String spansetStr = parts[1].replace("\"", "");

                // Parse FloatSpanSet
                Pointer spanset = floatspanset_in(spansetStr);

                // Determine group
                int groupId = k % NO_GROUPS;

                // Aggregate: state[group] = spanset_union_transfn(state[group], spanset)
                state[groupId] = spanset_union_transfn(state[groupId], spanset);

                String spansetOut = floatspanset_out(spanset, 3);
                System.out.println("  Record " + (++recordCount) +
                        " - k: " + k + " → Group " + groupId +
                        ", spanset: " + spansetOut);
            }

            System.out.println("\n" + recordCount + " records aggregated into " +
                    NO_GROUPS + " groups.");

            long elapsed = System.currentTimeMillis() - startTime;

            // Finalize each group
            System.out.println("\n┌─ AGGREGATION RESULTS ────────────────────");
            for (int i = 0; i < NO_GROUPS; i++) {
                if (state[i] == null) {
                    System.out.println("│ Group " + i + ": (empty)");
                    continue;
                }

                Pointer result = spanset_union_finalfn(state[i]);
                String resultStr = floatspanset_out(result, 3);

                System.out.println("│ Group " + i + ": " + resultStr);
            }
            System.out.println("│ Time: " + elapsed + " ms");
            System.out.println("└──────────────────────────────────────────");

        } catch (IOException e) {
            System.err.println("Error reading floatspanset.csv: " + e.getMessage());
        }
    }

    /**
     * SECTION 3: TextSet Grouped Aggregation
     * Demonstrates set aggregation with expandable structures
     *
     * Pattern:
     * 1. Read TextSet values from CSV
     * 2. Group by (k % NO_GROUPS)
     * 3. Accumulate per group using set_union_transfn()
     * 4. Finalize each group using set_union_finalfn()
     */
    private static void demonstrateTextSetAggregation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SECTION 3: TextSet Grouped Aggregation (Expandable Sets)");
        System.out.println("Pattern: GROUP BY k % " + NO_GROUPS);
        System.out.println("=".repeat(60));

        long startTime = System.currentTimeMillis();
        Pointer[] state = new Pointer[NO_GROUPS];  // One state per group
        int recordCount = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader(DATA_DIR + "textset.csv"))) {

            // Skip header
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // Parse: k,'{text1,text2,text3}'
                String[] parts = line.split(",'");
                if (parts.length != 2) continue;

                int k = Integer.parseInt(parts[0]);
                String setStr = parts[1].replace("'", "");

                // Parse TextSet
                Pointer textset = textset_in(setStr);

                // Determine group
                int groupId = k % NO_GROUPS;

                // Aggregate: state[group] = set_union_transfn(state[group], textset)
                state[groupId] = set_union_transfn(state[groupId], textset);

                String setOut = textset_out(textset);
                System.out.println("  Record " + (++recordCount) +
                        " - k: " + k + " → Group " + groupId +
                        ", set: " + setOut);
            }

            System.out.println("\n" + recordCount + " records aggregated into " +
                    NO_GROUPS + " groups.");

            long elapsed = System.currentTimeMillis() - startTime;

            // Finalize each group and show statistics
            System.out.println("\n┌─ AGGREGATION RESULTS ────────────────────");
            for (int i = 0; i < NO_GROUPS; i++) {
                if (state[i] == null) {
                    System.out.println("│ Group " + i + ": (empty)");
                    continue;
                }

                Pointer result = set_union_finalfn(state[i]);

                // Get number of elements using MEOS function
                int numElements = set_num_values(result);
                String resultStr = textset_out(result);

                System.out.println("│ Group " + i + " (" + numElements +
                        " elements): " + resultStr);
            }
            System.out.println("│ Time: " + elapsed + " ms");
            System.out.println("└──────────────────────────────────────────");

        } catch (IOException e) {
            System.err.println("Error reading textset.csv: " + e.getMessage());
        }
    }

    /**
     * Demonstrates the transfn/finalfn pattern concept
     */
    private static void explainAggregationPattern() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║          MEOS AGGREGATION PATTERN EXPLAINED              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("The transfn/finalfn pattern is used for aggregate functions:");
        System.out.println();
        System.out.println("┌─ TRANSITION FUNCTION (transfn) ──────────────────────────");
        System.out.println("│ • Accumulates values incrementally");
        System.out.println("│ • Signature: state = transfn(state, new_value)");
        System.out.println("│ • Called for each input row");
        System.out.println("│ • Examples:");
        System.out.println("│   - span_union_transfn(state, span)");
        System.out.println("│   - spanset_union_transfn(state, spanset)");
        System.out.println("│   - set_union_transfn(state, set)");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ FINALIZE FUNCTION (finalfn) ────────────────────────────");
        System.out.println("│ • Produces final aggregate result");
        System.out.println("│ • Signature: result = finalfn(state)");
        System.out.println("│ • Called once after all rows processed");
        System.out.println("│ • Examples:");
        System.out.println("│   - spanset_union_finalfn(state)");
        System.out.println("│   - set_union_finalfn(state)");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
        System.out.println("┌─ USAGE EXAMPLE ──────────────────────────────────────────");
        System.out.println("│");
        System.out.println("│ Pointer state = null;");
        System.out.println("│");
        System.out.println("│ // Accumulate phase");
        System.out.println("│ for (each row) {");
        System.out.println("│     Pointer value = parse_row();");
        System.out.println("│     state = span_union_transfn(state, value);");
        System.out.println("│ }");
        System.out.println("│");
        System.out.println("│ // Finalize phase");
        System.out.println("│ Pointer result = spanset_union_finalfn(state);");
        System.out.println("│");
        System.out.println("└──────────────────────────────────────────────────────────");
        System.out.println();
    }

    public static void main(String[] args) {
        // Initialize MEOS
        meos_initialize();
        meos_initialize_timezone("UTC");

        try {
            // Explain the pattern first
            explainAggregationPattern();

            // Run all demonstrations
            demonstrateIntSpanAggregation();
            demonstrateFloatSpanSetAggregation();
            demonstrateTextSetAggregation();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("ALL AGGREGATION DEMONSTRATIONS COMPLETED!");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Finalize MEOS
            meos_finalize();
        }
    }
}