package types.temporal.generated;

import functions.GeneratedFunctions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;
import types.basic.tfloat.TFloatSeq;
import types.basic.tint.TIntSeq;
import types.temporal.Temporal;
import types.temporal.TemporalType;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves the generated concrete numeric surfaces ({@link GeneratedTInt}, {@link GeneratedTFloat})
 * delegate correctly, each method checked against a direct call to the underlying
 * {@code GeneratedFunctions} wrapper. The concrete surfaces resolve the base value type from the
 * catalog — {@code int} for tint, {@code double} for tfloat — so the base scalars, base arguments and
 * conversions marshal directly, with no per-class configuration.
 *
 * <p>Each surface is exercised through an adapter over its hand temporal, so the test needs no change to
 * the hand classes; the interface inherits the three contract methods from {@code GeneratedTemporal}.
 */
public class GeneratedConcreteNumberParityTest {

    private static GeneratedTInt genInt(Temporal t) {
        return new GeneratedTInt() {
            @Override public Pointer getInner() { return t.getInner(); }
            @Override public String getCustomType() { return t.getCustomType(); }
            @Override public TemporalType getTemporalType() { return t.getTemporalType(); }
        };
    }

    private static GeneratedTFloat genFloat(Temporal t) {
        return new GeneratedTFloat() {
            @Override public Pointer getInner() { return t.getInner(); }
            @Override public String getCustomType() { return t.getCustomType(); }
            @Override public TemporalType getTemporalType() { return t.getTemporalType(); }
        };
    }

    private static long id(Pointer inner) {
        return Integer.toUnsignedLong(GeneratedFunctions.temporal_hash(inner));
    }

    private static long id(Temporal t) {
        return id(t.getInner());
    }

    @Test
    void intSurfaceMatchesTheLibrary() {
        TIntSeq a = new TIntSeq("[1@2019-09-01, 3@2019-09-02, 2@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTInt g = genInt(a);

        // Base int accessors resolve directly from the concrete catalog return type.
        assertEquals(GeneratedFunctions.tint_min_value(p), g.minValue());
        assertEquals(GeneratedFunctions.tint_max_value(p), g.maxValue());
        assertEquals(GeneratedFunctions.tint_start_value(p), g.startValue());
        assertEquals(GeneratedFunctions.tint_end_value(p), g.endValue());

        // Base int arguments and a conversion, wrapped back into the object layer.
        assertEquals(id(GeneratedFunctions.tint_at_value(p, 2)), id(g.atValue(2)));
        assertEquals(id(GeneratedFunctions.tint_minus_value(p, 2)), id(g.minusValue(2)));
        assertEquals(id(GeneratedFunctions.tint_shift_value(p, 5)), id(g.shiftValue(5)));
        assertEquals(id(GeneratedFunctions.tint_to_tfloat(p)), id(g.toTfloat()));

        assertEquals(GeneratedFunctions.tint_out(p), g.out());
    }

    @Test
    void floatSurfaceMatchesTheLibrary() {
        // Step interpolation, so the conversion to a temporal integer is defined.
        TFloatSeq a = new TFloatSeq("Interp=Step;[1@2019-09-01, 3@2019-09-02, 2@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTFloat g = genFloat(a);

        // Base double accessors.
        assertEquals(GeneratedFunctions.tfloat_min_value(p), g.minValue());
        assertEquals(GeneratedFunctions.tfloat_max_value(p), g.maxValue());

        // Base double arguments, a math accessor and a conversion.
        assertEquals(id(GeneratedFunctions.tfloat_at_value(p, 2.0)), id(g.atValue(2.0)));
        assertEquals(id(GeneratedFunctions.tfloat_minus_value(p, 2.0)), id(g.minusValue(2.0)));
        assertEquals(id(GeneratedFunctions.tfloat_shift_value(p, 5.0)), id(g.shiftValue(5.0)));
        assertEquals(id(GeneratedFunctions.tfloat_sin(p)), id(g.sin()));
        assertEquals(id(GeneratedFunctions.tfloat_to_tint(p)), id(g.toTint()));

        assertEquals(GeneratedFunctions.tfloat_out(p, 6), g.out(6));
    }
}
