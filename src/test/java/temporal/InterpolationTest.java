package temporal;

import functions.functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;
import types.temporal.TInterpolation;
import functions.*;
import types.temporal.Temporal;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InterpolationTest {

    error_handler_fn errorHandler = new error_handler();

    Stream<Arguments> TInterp() throws SQLException {
        functions.meos_initialize("UTC", errorHandler);
        return Stream.of(
                Arguments.of("discrete", TInterpolation.DISCRETE),
                Arguments.of("linear", TInterpolation.LINEAR),
                Arguments.of("stepwise", TInterpolation.STEPWISE),
                Arguments.of("step", TInterpolation.STEPWISE),
                Arguments.of("none", TInterpolation.NONE)
        );
    }

    @ParameterizedTest(name = "Test TInterpolation class.")
    @MethodSource("TInterp")
    public void testFromString(String base, TInterpolation expected) {
        functions.meos_initialize("UTC", errorHandler);
        assertEquals(TInterpolation.fromString(base),expected);
    }
}
