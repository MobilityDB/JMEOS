package functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestLogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Asserts that the hand-written FFI facade and the generated one declare the same call shape for
 * every function they both bind.
 *
 * <p>{@code GeneratedFunctions} takes its parameter lists from the MEOS-API catalog, so it states
 * what the library accepts. {@code functions.MeosLibrary} is maintained by hand, so it drifts
 * whenever MEOS adds, drops or reorders a parameter. A declaration carrying a stale parameter list
 * still resolves its symbol, so a check on names alone passes while the call disagrees with the
 * function it reaches.
 *
 * <p>Comparing the two interfaces needs no further input: where they name the same function, they
 * have to agree. The check covers the hand facade for as long as it exists and holds vacuously once
 * the surface is generated alone, which is the intended end state.
 */
@DisplayName("Declared native signatures agree across the two facades")
@ExtendWith(TestLogger.class)
class NativeSignatureParityTest {

    /** Parameter lists declared by the generated interfaces, keyed by function name. */
    private static Map<String, List<Integer>> generatedArities() {
        Map<String, List<Integer>> byName = new TreeMap<>();
        for (Class<?> nested : GeneratedFunctions.class.getDeclaredClasses()) {
            if (!nested.isInterface()) {
                continue;
            }
            for (Method m : nested.getDeclaredMethods()) {
                byName.computeIfAbsent(m.getName(), k -> new ArrayList<>())
                        .add(m.getParameterCount());
            }
        }
        return byName;
    }

    @Test
    @DisplayName("a function bound by both facades takes the same number of arguments in each")
    void sharedFunctionsAgreeOnArity() {
        Map<String, List<Integer>> generated = generatedArities();
        TreeMap<String, String> disagreements = new TreeMap<>();
        int compared = 0;

        for (Method hand : functions.MeosLibrary.class.getDeclaredMethods()) {
            List<Integer> candidates = generated.get(hand.getName());
            if (candidates == null) {
                continue; // bound by the hand facade alone; the symbol check covers its existence
            }
            compared++;
            int handArity = hand.getParameterCount();
            if (!candidates.contains(handArity)) {
                disagreements.put(hand.getName(),
                        "hand takes " + handArity + ", generated takes " + candidates);
            }
        }

        assertTrue(compared > 0, "no shared function was compared");
        assertTrue(disagreements.isEmpty(),
                disagreements.size() + " of " + compared + " shared functions disagree on how many "
                        + "arguments the library takes: " + disagreements);
    }
}
