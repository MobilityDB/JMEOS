package types.temporal.generated;

import functions.GeneratedFunctions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.junit.jupiter.api.Test;
import types.basic.tfloat.TFloatSeq;
import types.basic.tint.TIntSeq;
import types.temporal.Temporal;
import types.temporal.TemporalType;
import utils.ConversionUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

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

    @Test
    void intValueSplitMatchesTheLibrary() {
        TIntSeq a = new TIntSeq("[1@2019-09-01, 3@2019-09-02, 5@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTInt g = genInt(a);
        Runtime rt = Runtime.getSystemRuntime();

        // valueSplit folds the fragment array and the parallel int bins into (number, fragment) records.
        Pointer bins = Memory.allocate(rt, Long.BYTES);
        Pointer count = Memory.allocate(rt, Integer.BYTES);
        Pointer frags = GeneratedFunctions.tint_value_split(p, 2, 0, bins, count);
        Pointer binsArr = bins.getPointer(0);
        List<GeneratedTInt.ValueSplit> split = g.valueSplit(2, 0);

        assertEquals(count.getInt(0), split.size());
        for (int i = 0; i < split.size(); i++) {
            assertEquals(binsArr.getInt((long) i * Integer.BYTES), split.get(i).number());
            assertEquals(id(frags.getPointer((long) i * Long.BYTES)), id(split.get(i).fragment()));
        }
    }

    @Test
    void floatValueTimeSplitMatchesTheLibrary() {
        TFloatSeq a = new TFloatSeq("[1@2019-09-01, 3@2019-09-02, 5@2019-09-03]");
        Pointer p = a.getInner();
        GeneratedTFloat g = genFloat(a);
        Runtime rt = Runtime.getSystemRuntime();

        Duration duration = Duration.ofDays(1);
        OffsetDateTime torigin = OffsetDateTime.parse("2019-09-01T00:00:00Z");

        // valueTimeSplit is 2-D: the fragment array zips with a value-bin array and a time-bin array.
        Pointer vbins = Memory.allocate(rt, Long.BYTES);
        Pointer tbins = Memory.allocate(rt, Long.BYTES);
        Pointer count = Memory.allocate(rt, Integer.BYTES);
        Pointer frags = GeneratedFunctions.tfloat_value_time_split(p, 2.0,
                ConversionUtils.timedelta_to_interval(duration), 0.0, torigin, vbins, tbins, count);
        Pointer vbinsArr = vbins.getPointer(0);
        Pointer tbinsArr = tbins.getPointer(0);
        List<GeneratedTFloat.ValueTimeSplit> split = g.valueTimeSplit(2.0, duration, 0.0, torigin);

        assertEquals(count.getInt(0), split.size());
        for (int i = 0; i < split.size(); i++) {
            assertEquals(vbinsArr.getDouble((long) i * Double.BYTES), split.get(i).number());
            assertEquals(utils.TimestampTzConverter.toOffsetDateTime(tbinsArr.getLong((long) i * Long.BYTES)),
                    split.get(i).time());
            assertEquals(id(frags.getPointer((long) i * Long.BYTES)), id(split.get(i).fragment()));
        }
    }
}
