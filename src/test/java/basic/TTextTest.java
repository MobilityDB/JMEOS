package basic;

import functions.functions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.basic.ttext.TText;
import types.basic.ttext.TTextInst;
import types.basic.ttext.TTextSeq;
import types.basic.ttext.TTextSeqSet;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.Time;
import types.collections.time.TimestampSet;
import types.temporal.TInterpolation;
import types.temporal.TSequence;
import types.temporal.Temporal;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TTextTest {



    static Stream<Arguments> TText_string_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("AAA@2019-09-01", "TTextInst", TInterpolation.NONE, "\"AAA\"@2019-09-01 00:00:00+00"),
                Arguments.of("{AAA@2019-09-01, BBB@2019-09-02}", "TTextSeq", TInterpolation.DISCRETE, "{\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00}"),
                Arguments.of("[AAA@2019-09-01, BBB@2019-09-02]", "TTextSeq", TInterpolation.STEPWISE, "[\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00]"),
                Arguments.of("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}", "TTextSeqSet", TInterpolation.STEPWISE, "{[\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00], [\"AAA\"@2019-09-03 00:00:00+00, \"AAA\"@2019-09-05 00:00:00+00]}")
        );
    }

    static Stream<Arguments> TText_base_time_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"), "TTextSeq", TInterpolation.DISCRETE),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"), "TTextSeq", TInterpolation.STEPWISE),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TTextSeqSet", TInterpolation.STEPWISE)
        );
    }


    static Stream<Arguments> TText_copy_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", TInterpolation.NONE),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", TInterpolation.DISCRETE),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", TInterpolation.STEPWISE),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", TInterpolation.STEPWISE)
        );
    }


    static Stream<Arguments> TText_string() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", "\"AAA\"@2019-09-01 00:00:00+00"),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq","{\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00}"),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",  "[\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00]"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", "{[\"AAA\"@2019-09-01 00:00:00+00, \"BBB\"@2019-09-02 00:00:00+00], [\"AAA\"@2019-09-03 00:00:00+00, \"AAA\"@2019-09-05 00:00:00+00]}")
        );
    }


    static Stream<Arguments> TText_bounding() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst",new Period("[2019-09-01, 2019-09-01]")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new Period("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",  new Period("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new Period("[2019-09-01, 2019-09-05]"))
        );
    }


    static Stream<Arguments> TText_interp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", TInterpolation.NONE),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", TInterpolation.DISCRETE),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",  TInterpolation.STEPWISE),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", TInterpolation.STEPWISE)
        );
    }

    static Stream<Arguments> TText_start() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", "AAA"),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", "AAA"),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", "AAA"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", "AAA")
        );
    }


    static Stream<Arguments> TText_end() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", "AAA"),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", "BBB"),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",  "BBB"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", "AAA")
        );
    }


    static Stream<Arguments> TText_time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", new PeriodSet("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new PeriodSet("{[2019-09-01, 2019-09-01], [2019-09-02, 2019-09-02]}")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",  new PeriodSet("{[2019-09-01, 2019-09-02]}")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}"))
        );
    }


    static Stream<Arguments> TText_numinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", 1),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", 2),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", 2),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", 4)
        );
    }



    static Stream<Arguments> TText_startinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new TTextInst("AAA@2019-09-01"))
        );
    }


    static Stream<Arguments> TText_endinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new TTextInst("AAA@2019-09-05"))
        );
    }



    static Stream<Arguments> TText_mininst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new TTextInst("AAA@2019-09-01"))
        );
    }


    static Stream<Arguments> TText_maxinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq", new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq", new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", new TTextInst("BBB@2019-09-02"))
        );
    }

    static Stream<Arguments> TText_instn() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", 0, new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq",1, new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",1, new TTextInst("BBB@2019-09-02")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet",2, new TTextInst("AAA@2019-09-03"))
        );
    }


    static Stream<Arguments> TText_numtmstp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", 1),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq",2),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",2),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet",4)
        );
    }


    static Stream<Arguments> TText_starttmstp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq",LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet",LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }

    static Stream<Arguments> TText_endtmstp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq",LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet",LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    static Stream<Arguments> TText_hash() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), "TTextInst", 1893808825),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), "TTextSeq",1223816819),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), "TTextSeq",1223816819),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet", 2199213310l)
        );
    }


    static Stream<Arguments> TText_toinstant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"),new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01}"), new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01}"), new TTextInst("AAA@2019-09-01")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01]}"), new TTextInst("AAA@2019-09-01"))
        );
    }


    static Stream<Arguments> TText_tosequence() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), TInterpolation.STEPWISE, new TTextSeq("[AAA@2019-09-01]")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), TInterpolation.DISCRETE,  new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), TInterpolation.STEPWISE, new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02]}"), TInterpolation.STEPWISE, new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"))
        );
    }


    static Stream<Arguments> TText_tosequenceset() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), TInterpolation.STEPWISE, new TTextSeqSet("{[AAA@2019-09-01]}")),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), TInterpolation.STEPWISE,  new TTextSeqSet("{[AAA@2019-09-01], [BBB@2019-09-02]}")),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), TInterpolation.STEPWISE, new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02]}")),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02]}"), TInterpolation.STEPWISE, new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02]}"))
        );
    }



    static Stream<Arguments> TText_insert() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), new TTextSeq("{AAA@2019-09-03}"), new TTextSeq("{AAA@2019-09-01, AAA@2019-09-03}"), "TTextInst"),
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), new TTextSeq("{AAA@2019-09-03}"), new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02, AAA@2019-09-03}"), "TTextSeq"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), new TTextSeq("{AAA@2019-09-06}"), new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05],[AAA@2019-09-06]}"), "TTextSeqSet")
        );
    }



    static Stream<Arguments> TText_update() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextInst("AAA@2019-09-01"), new TTextInst("BBB@2019-09-01"), new TTextInst("BBB@2019-09-01"), "TTextInst" ),
                Arguments.of(new TTextSeq("[AAA@2019-09-01, BBB@2019-09-02]"), new TTextInst("BBB@2019-09-01"), new TTextSeqSet("{[BBB@2019-09-01], (AAA@2019-09-01, BBB@2019-09-02]}"), "TTextSeq"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), new TTextInst("BBB@2019-09-01"), new TTextSeqSet("{[BBB@2019-09-01], (AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), "TTextSeqSet")
        );
    }



    static Stream<Arguments> TText_appendseq() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02}"), new TTextSeq("{AAA@2019-09-03}"),  new TTextSeq("{AAA@2019-09-01, BBB@2019-09-02, AAA@2019-09-03}"), "TTextSeq"),
                Arguments.of(new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05]}"), new TTextSeq("[AAA@2019-09-06]"), new TTextSeqSet("{[AAA@2019-09-01, BBB@2019-09-02],[AAA@2019-09-03, AAA@2019-09-05],[AAA@2019-09-06]}"), "TTextSeqSet")
        );
    }









    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------------- */





    @ParameterizedTest(name = "Test from string constructor.")
    @MethodSource("TText_string_constructor")
    public void testFromStringConstructor(String value, String type, TInterpolation interp, String repr) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            TTextInst tb = new TTextInst(value);
            assertTrue(tb instanceof TTextInst);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TTextSeq") {
            TTextSeq tb = new TTextSeq(value);
            assertTrue(tb instanceof TTextSeq);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TTextSeqSet") {
            TTextSeqSet tb = new TTextSeqSet(value);
            assertTrue(tb instanceof TTextSeqSet);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        }
    }


    @ParameterizedTest(name = "Test from time constructor.")
    @MethodSource("TText_base_time_constructor")
    public void testFromBaseTimeConstructor(Time base, String type, TInterpolation interp) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            TTextInst tb = (TTextInst) TText.from_base_time("AAA", base);
            assertTrue(tb instanceof TTextInst);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TTextSeq") {
            TTextSeq tb = (TTextSeq) TText.from_base_time("AAA", base);
            assertTrue(tb instanceof TTextSeq);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TTextSeqSet") {
            TTextSeqSet tb = (TTextSeqSet) TText.from_base_time("AAA", base);
            assertTrue(tb instanceof TTextSeqSet);
            assertTrue(tb.interpolation() == interp);
        }
    }




    @ParameterizedTest(name = "Test copy constructor.")
    @MethodSource("TText_copy_constructor")
    public void testCopyConstructor(Temporal base, String type, TInterpolation interp) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            TTextInst tb = (TTextInst) base.copy();
            assertEquals(tb.to_string(),(((TTextInst) base).to_string()));
        } else if (type == "TTextSeq") {
            TTextSeq tb = (TTextSeq) base.copy();
            assertEquals(tb.to_string(),(((TTextSeq) base).to_string()));
        } else if (type == "TTextSeqSet") {
            TTextSeqSet tb = (TTextSeqSet) base.copy();
            assertEquals(tb.to_string(),(((TTextSeqSet) base).to_string()));
        }
    }



    @ParameterizedTest(name = "Test string.")
    @MethodSource("TText_string")
    public void testString(Temporal base, String type, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            TTextInst tb = (TTextInst) base.copy();
            assertEquals(tb.to_string(),expected);
        } else if (type == "TTextSeq") {
            TTextSeq tb = (TTextSeq) base.copy();
            assertEquals(tb.to_string(),expected);
        } else if (type == "TTextSeqSet") {
            TTextSeqSet tb = (TTextSeqSet) base.copy();
            assertEquals(tb.to_string(),expected);
        }
    }


    @ParameterizedTest(name = "Test bounding box method.")
    @MethodSource("TText_bounding")
    public void testBoundingBox(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.bounding_box().toString(),expected.toString());
    }



    @ParameterizedTest(name = "Test interpolation method.")
    @MethodSource("TText_interp")
    public void testInterpolation(Temporal base, String type, TInterpolation expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.interpolation(),expected);
    }


    @ParameterizedTest(name = "Test start values method.")
    @MethodSource("TText_start")
    public void testStartValues(Temporal base, String type, String expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TText) base).start_value() ,expected);
    }


    @ParameterizedTest(name = "Test end values method.")
    @MethodSource("TText_end")
    public void testEndValues(Temporal base, String type, String expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TText) base).end_value() ,expected);
    }


    @ParameterizedTest(name = "Test time method.")
    @MethodSource("TText_time")
    public void testTime(Temporal base, String type, PeriodSet expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.time().toString() ,expected.toString());
    }


    @ParameterizedTest(name = "Test period method.")
    @MethodSource("TText_bounding")
    public void testPeriod(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.period().toString() ,expected.toString());
    }


    @ParameterizedTest(name = "Test span method.")
    @MethodSource("TText_bounding")
    public void testSpan(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.timespan().toString(),expected.toString());
    }

    @ParameterizedTest(name = "Test numinst method.")
    @MethodSource("TText_numinst")
    public void testNumInst(Temporal base, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.num_instants(),expected);
    }


    @ParameterizedTest(name = "Test startinst method.")
    @MethodSource("TText_startinst")
    public void testStartInst(Temporal base, String type, TTextInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TTextInst)base.start_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test endinst method.")
    @MethodSource("TText_endinst")
    public void testEndInst(Temporal base, String type, TTextInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TTextInst)base.end_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test mininst method.")
    @MethodSource("TText_mininst")
    public void testMinInst(Temporal base, String type, TTextInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TTextInst)base.min_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test maxinst method.")
    @MethodSource("TText_maxinst")
    public void testMaxInst(Temporal base, String type, TTextInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TTextInst)base.max_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test instn method.")
    @MethodSource("TText_instn")
    public void testInstN(Temporal base, String type, int n, TTextInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TTextInst)base.instant_n(n)).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test num timestamp method.")
    @MethodSource("TText_numtmstp")
    public void testNumtmstmp(Temporal base, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.num_timestamps(),expected);
    }


    @ParameterizedTest(name = "Test start timestamp method.")
    @MethodSource("TText_starttmstp")
    public void testStarttmstmp(Temporal base, String type, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.start_timestamp(),expected);
    }


    @ParameterizedTest(name = "Test end timestamp method.")
    @MethodSource("TText_endtmstp")
    public void testEndtmstmp(Temporal base, String type, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.end_timestamp(),expected);
    }


    @ParameterizedTest(name = "Test Hash method.")
    @MethodSource("TText_hash")
    public void testHash(Temporal base, String type, long expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.hash(),expected);
    }


    @ParameterizedTest(name = "Test to instant method.")
    @MethodSource("TText_toinstant")
    public void testToInstant(Temporal base, TTextInst type) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_instant();
        assertTrue(tmp instanceof TTextInst);
        assertEquals(((TTextInst) tmp).to_string(), type.to_string());
    }


    @ParameterizedTest(name = "Test to sequence method.")
    @MethodSource("TText_tosequence")
    public void testToSequence(Temporal base, TInterpolation interp, TTextSeq type) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_sequence(interp);
        assertTrue(tmp instanceof TTextSeq);
        assertEquals(((TTextSeq) tmp).to_string(), type.to_string());
    }



    @ParameterizedTest(name = "Test to sequence method.")
    @MethodSource("TText_tosequenceset")
    public void testToSequenceSet(Temporal base, TInterpolation interp, TTextSeqSet type) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_sequenceset(interp);
        assertTrue(tmp instanceof TTextSeqSet);
        assertEquals(((TTextSeqSet) tmp).to_string(), type.to_string());
    }



    @ParameterizedTest(name = "Test insert method.")
    @MethodSource("TText_insert")
    public void testInsert(Temporal base, Temporal base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            assertEquals(((TTextInst)base.insert(base2)).to_string(), ((TTextSeq) tseq).to_string());
        } else if (type == "TTextSeq") {
            assertEquals(((TTextSeq)base.insert(base2)).to_string(), ((TTextSeq) tseq).to_string());
        } else if (type == "TTextSeqSet") {
            assertEquals(((TTextSeqSet)base.insert(base2)).to_string(), ((TTextSeqSet) tseq).to_string());
        }
    }


    @ParameterizedTest(name = "Test update method.")
    @MethodSource("TText_update")
    public void testUpdate(Temporal base, Temporal base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TTextInst") {
            assertEquals(((TTextInst)base.update(base2)).to_string(), ((TTextInst) tseq).to_string());
        } else if (type == "TTextSeq") {
            assertEquals(((TTextSeq)base.update(base2)).to_string(), ((TTextSeqSet) tseq).to_string());
        } else if (type == "TTextSeqSet") {
            assertEquals(((TTextSeqSet)base.update(base2)).to_string(), ((TTextSeqSet) tseq).to_string());
        }
    }



    @ParameterizedTest(name = "Test append sequence method.")
    @MethodSource("TText_appendseq")
    public void testAppendSeq(Temporal base, TSequence base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TTextSeq") {
            assertEquals(((TTextSeq)base.append_sequence(base2)).to_string(), ((TTextSeq) tseq).to_string());
        } else if (type == "TTextSeqSet") {
            assertEquals(((TTextSeqSet)base.append_sequence(base2)).to_string(), ((TTextSeqSet) tseq).to_string());
        }
    }







}
