package basic;
import functions.functions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import types.basic.tbool.TBool;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;
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


public class TBoolTest {

    TBoolInst tbi = new TBoolInst("True@2019-09-01");
    TBoolSeq tbds = new TBoolSeq("{True@2019-09-01, False@2019-09-02}");
    TBoolSeq tbs = new TBoolSeq("[True@2019-09-01, False@2019-09-02]");
    TBoolSeqSet tbss = new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}");



    static Stream<Arguments> TBool_copy_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst"),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq"),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq"),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet")
        );
    }


    static Stream<Arguments> TBool_string_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of("True@2019-09-01", "TBoolInst", TInterpolation.NONE, "t@2019-09-01 00:00:00+00"),
                Arguments.of("{True@2019-09-01, False@2019-09-02}", "TBoolSeq", TInterpolation.DISCRETE, "{t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00}"),
                Arguments.of("[True@2019-09-01, False@2019-09-02]", "TBoolSeq", TInterpolation.STEPWISE, "[t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00]"),
                Arguments.of("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}", "TBoolSeqSet", TInterpolation.STEPWISE, "{[t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00], [t@2019-09-03 00:00:00+00, t@2019-09-05 00:00:00+00]}")
        );
    }


    static Stream<Arguments> TBool_base_time_constructor() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"), "TBoolSeq", TInterpolation.DISCRETE),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"), "TBoolSeq", TInterpolation.STEPWISE),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TBoolSeqSet", TInterpolation.STEPWISE)
        );
    }


    static Stream<Arguments> TBool_string() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", "t@2019-09-01 00:00:00+00"),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", "{t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00}"),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", "[t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00]"),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", "{[t@2019-09-01 00:00:00+00, f@2019-09-02 00:00:00+00], [t@2019-09-03 00:00:00+00, t@2019-09-05 00:00:00+00]}")
        );
    }

    static Stream<Arguments> TBool_bounding() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new Period("[2019-09-01, 2019-09-01]")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new Period("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new Period("[2019-09-01, 2019-09-02]")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new Period("[2019-09-01, 2019-09-05]"))
        );
    }


    static Stream<Arguments> TBool_interp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", TInterpolation.NONE),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", TInterpolation.DISCRETE),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", TInterpolation.STEPWISE),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", TInterpolation.STEPWISE)
        );
    }

    static Stream<Arguments> TBool_start() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", true),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", true),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", true)
        );
    }


    static Stream<Arguments> TBool_end() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", false),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", false),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", true)
        );
    }


    static Stream<Arguments> TBool_time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new PeriodSet("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new PeriodSet("{[2019-09-01, 2019-09-01], [2019-09-02, 2019-09-02]}")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new PeriodSet("{[2019-09-01, 2019-09-02]}")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02], [2019-09-03, 2019-09-05]}"))
        );
    }


    static Stream<Arguments> TBool_numinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", 1),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",2),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", 2),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", 4)
        );
    }


    static Stream<Arguments> TBool_startinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new TBoolInst("True@2019-09-01"))
        );
    }


    static Stream<Arguments> TBool_endinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new TBoolInst("True@2019-09-05"))
        );
    }


    static Stream<Arguments> TBool_mininst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new TBoolInst("False@2019-09-02"))
        );
    }


    static Stream<Arguments> TBool_maxinst() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new TBoolInst("True@2019-09-01"))
        );
    }



    static Stream<Arguments> TBool_instn() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", 0, new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",1, new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", 1, new TBoolInst("False@2019-09-02")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", 2, new TBoolInst("True@2019-09-03"))
        );
    }


    static Stream<Arguments> TBool_startmstp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", LocalDateTime.of(2019, 9, 1, 0, 0,0))
        );
    }


    static Stream<Arguments> TBool_endtmstp() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", LocalDateTime.of(2019, 9, 1, 0, 0,0)),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", LocalDateTime.of(2019, 9, 2, 0, 0,0)),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", LocalDateTime.of(2019, 9, 5, 0, 0,0))
        );
    }


    static Stream<Arguments> TBool_hash() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", 440045287),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",2385901957l),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", 2385901957l),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet",1543175996)
        );
    }


    static Stream<Arguments> TBool_instant() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("{True@2019-09-01}"), new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeq("[True@2019-09-01]"), new TBoolInst("True@2019-09-01")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01]}"), new TBoolInst("True@2019-09-01"))
        );
    }


    static Stream<Arguments> TBool_tosequence() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), TInterpolation.STEPWISE, new TBoolSeq("[True@2019-09-01]")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), TInterpolation.DISCRETE , new TBoolSeq("{True@2019-09-01, False@2019-09-02}")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), TInterpolation.STEPWISE, new TBoolSeq("[True@2019-09-01, False@2019-09-02]")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02]}"), TInterpolation.STEPWISE, new TBoolSeq("[True@2019-09-01, False@2019-09-02]"))
        );
    }


    static Stream<Arguments> TBool_tosequenceset() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), TInterpolation.STEPWISE, new TBoolSeqSet("{[True@2019-09-01]}")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), TInterpolation.STEPWISE , new TBoolSeqSet("{[True@2019-09-01], [False@2019-09-02]}")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), TInterpolation.STEPWISE, new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02]}")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02]}"), TInterpolation.STEPWISE, new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02]}"))
        );
    }

    static Stream<Arguments> TBool_insert() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), new TBoolSeq("{True@2019-09-03}"), new TBoolSeq("{True@2019-09-01, True@2019-09-03}"), "TBoolInst"),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), new TBoolSeq("{True@2019-09-03}") , new TBoolSeq("{True@2019-09-01, False@2019-09-02, True@2019-09-03}"), "TBoolSeq"),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), new TBoolSeq("{True@2019-09-06}"), new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05],[True@2019-09-06]}"), "TBoolSeqSet")
        );
    }


    static Stream<Arguments> TBool_update() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), new TBoolInst("False@2019-09-01"), new TBoolInst("False@2019-09-01"), "TBoolInst"),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), new TBoolInst("False@2019-09-01") , new TBoolSeq("{False@2019-09-01, False@2019-09-02}"), "TBoolSeq"),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), new TBoolInst("False@2019-09-01"), new TBoolSeqSet("{[False@2019-09-01], (True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet")
        );
    }

    static Stream<Arguments> TBool_appendseq() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), new TBoolSeq("{True@2019-09-03}") , new TBoolSeq("{True@2019-09-01, False@2019-09-02, True@2019-09-03}"), "TBoolSeq"),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), new TBoolSeq("[True@2019-09-06]"), new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05],[True@2019-09-06]}"), "TBoolSeqSet")
        );
    }



    static Stream<Arguments> TBool_whentrue() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", new PeriodSet("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq" , new PeriodSet("{[2019-09-01, 2019-09-01]}")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new PeriodSet("{[2019-09-01, 2019-09-02)}")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new PeriodSet("{[2019-09-01, 2019-09-02),[2019-09-03, 2019-09-05]}"))
        );
    }

    static Stream<Arguments> TBool_whenfalse() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq" , new PeriodSet("{[2019-09-02, 2019-09-02]}")),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", new PeriodSet("{[2019-09-02, 2019-09-02]}")),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", new PeriodSet("{[2019-09-02, 2019-09-02]}"))
        );
    }


    static Stream<Arguments> TBool_alwaystrue() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",false),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", false),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", false)
        );
    }


    static Stream<Arguments> TBool_alwaysfalse() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",false),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", false),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", false)
        );
    }


    static Stream<Arguments> TBool_evertrue() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",true),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", true),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", true)
        );
    }


    static Stream<Arguments> TBool_everfalse() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", true),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",true),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", true),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", true)
        );
    }


    static Stream<Arguments> TBool_nevertrue() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", false),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",false),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", false),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", false)
        );
    }


    static Stream<Arguments> TBool_neverfalse() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBoolInst("True@2019-09-01"), "TBoolInst", false),
                Arguments.of(new TBoolSeq("{True@2019-09-01, False@2019-09-02}"), "TBoolSeq",false),
                Arguments.of(new TBoolSeq("[True@2019-09-01, False@2019-09-02]"), "TBoolSeq", false),
                Arguments.of(new TBoolSeqSet("{[True@2019-09-01, False@2019-09-02],[True@2019-09-03, True@2019-09-05]}"), "TBoolSeqSet", false)
        );
    }





    /* --------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------- */
    /* --------------------------------------------------------------------------------- */



    @ParameterizedTest(name = "Test from string constructor.")
    @MethodSource("TBool_string_constructor")
    public void testFromStringConstructor(String value, String type, TInterpolation interp, String repr) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            TBoolInst tb = new TBoolInst(value);
            assertTrue(tb instanceof TBoolInst);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TBoolSeq") {
            TBoolSeq tb = new TBoolSeq(value);
            assertTrue(tb instanceof TBoolSeq);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TBoolSeqSet") {
            TBoolSeqSet tb = new TBoolSeqSet(value);
            assertTrue(tb instanceof TBoolSeqSet);
            assertEquals(tb.to_string(), repr);
            assertTrue(tb.interpolation() == interp);
        }
    }


    @ParameterizedTest(name = "Test from base time constructor.")
    @MethodSource("TBool_base_time_constructor")
    public void testFromBaseTimeConstructor(Time base, String type, TInterpolation interp) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            TBoolInst tb = (TBoolInst) TBool.from_base_time(true, base);
            assertTrue(tb instanceof TBoolInst);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TBoolSeq") {
            TBoolSeq tb = (TBoolSeq) TBool.from_base_time(true, base);
            assertTrue(tb instanceof TBoolSeq);
            assertTrue(tb.interpolation() == interp);
        } else if (type == "TBoolSeqSet") {
            TBoolSeqSet tb = (TBoolSeqSet) TBool.from_base_time(true, base);
            assertTrue(tb instanceof TBoolSeqSet);
            assertTrue(tb.interpolation() == interp);
        }
    }

    @ParameterizedTest(name = "Test from copy constructor.")
    @MethodSource("TBool_copy_constructor")
    public void testCopyConstructor(Temporal base, String type) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            TBoolInst tb = (TBoolInst) base.copy();
            assertEquals(tb.to_string(),(((TBoolInst) base).to_string()));
        } else if (type == "TBoolSeq") {
            TBoolSeq tb = (TBoolSeq) base.copy();
            assertEquals(tb.to_string(),(((TBoolSeq) base).to_string()));
        } else if (type == "TBoolSeqSet") {
            TBoolSeqSet tb = (TBoolSeqSet) base.copy();
            assertEquals(tb.to_string(),(((TBoolSeqSet) base).to_string()));
        }
    }



    @ParameterizedTest(name = "Test string method.")
    @MethodSource("TBool_string")
    public void testString(Temporal base, String type, String expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals(expected,(((TBoolInst) base).to_string()));
        } else if (type == "TBoolSeq") {
            assertEquals(expected,(((TBoolSeq) base).to_string()));
        } else if (type == "TBoolSeqSet") {
            assertEquals(expected,(((TBoolSeqSet) base).to_string()));
        }
    }


    @ParameterizedTest(name = "Test bounding box method.")
    @MethodSource("TBool_bounding")
    public void testBoundingBox(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.bounding_box().toString(),expected.toString());
    }



    @ParameterizedTest(name = "Test interpolation method.")
    @MethodSource("TBool_interp")
    public void testInterpolation(Temporal base, String type, TInterpolation expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.interpolation(),expected);
    }


    @ParameterizedTest(name = "Test start values method.")
    @MethodSource("TBool_start")
    public void testStartValues(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBool) base).start_value() ,expected);
    }


    @ParameterizedTest(name = "Test end values method.")
    @MethodSource("TBool_end")
    public void testEndValues(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBool) base).end_value() ,expected);
    }


    @ParameterizedTest(name = "Test time  method.")
    @MethodSource("TBool_time")
    public void testTime(Temporal base, String type, PeriodSet expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.time().toString() ,expected.toString());
    }


    @ParameterizedTest(name = "Test period method.")
    @MethodSource("TBool_bounding")
    public void testPeriod(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.period().toString() ,expected.toString());
    }


    @ParameterizedTest(name = "Test span method.")
    @MethodSource("TBool_bounding")
    public void testSpan(Temporal base, String type, Period expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.timespan().toString(),expected.toString());
    }


    @ParameterizedTest(name = "Test numinst method.")
    @MethodSource("TBool_numinst")
    public void testNumInst(Temporal base, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.num_instants(),expected);
    }


    @ParameterizedTest(name = "Test startinst method.")
    @MethodSource("TBool_startinst")
    public void testStartInst(Temporal base, String type, TBoolInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBoolInst)base.start_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test endinst method.")
    @MethodSource("TBool_endinst")
    public void testEndInst(Temporal base, String type, TBoolInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBoolInst)base.end_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test mininst method.")
    @MethodSource("TBool_mininst")
    public void testMinInst(Temporal base, String type, TBoolInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBoolInst)base.min_instant()).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test maxinst method.")
    @MethodSource("TBool_maxinst")
    public void testMaxInst(Temporal base, String type, TBoolInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBoolInst)base.max_instant()).to_string(),expected.to_string());
    }

    @ParameterizedTest(name = "Test instn method.")
    @MethodSource("TBool_instn")
    public void testInstN(Temporal base, String type, int n, TBoolInst expected) {
        functions.meos_initialize("UTC");
        assertEquals(((TBoolInst)base.instant_n(n)).to_string(),expected.to_string());
    }


    @ParameterizedTest(name = "Test num timestamp method.")
    @MethodSource("TBool_numinst")
    public void testNumtmstmp(Temporal base, String type, int expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.num_timestamps(),expected);
    }


    @ParameterizedTest(name = "Test start timestamp method.")
    @MethodSource("TBool_startmstp")
    public void testStarttmstmp(Temporal base, String type, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.start_timestamp(),expected);
    }


    @ParameterizedTest(name = "Test end timestamp method.")
    @MethodSource("TBool_endtmstp")
    public void testEndtmstmp(Temporal base, String type, LocalDateTime expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.end_timestamp(),expected);
    }


    @ParameterizedTest(name = "Test Hash method.")
    @MethodSource("TBool_hash")
    public void testHash(Temporal base, String type, long expected) {
        functions.meos_initialize("UTC");
        assertEquals(base.hash(),expected);
    }


    @ParameterizedTest(name = "Test to instant method.")
    @MethodSource("TBool_instant")
    public void testInstant(Temporal base, TBoolInst type) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_instant();
        assertTrue(tmp instanceof TBoolInst);
        assertEquals(((TBoolInst) tmp).to_string(), type.to_string());
    }


    @ParameterizedTest(name = "Test to sequence method.")
    @MethodSource("TBool_tosequence")
    public void testSequence(Temporal base, TInterpolation type, TBoolSeq tseq) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_sequence(type);
        assertTrue(tmp instanceof TBoolSeq);
        assertEquals(((TBoolSeq) tmp).to_string(), tseq.to_string());
    }


    @ParameterizedTest(name = "Test to sequenceset method.")
    @MethodSource("TBool_tosequenceset")
    public void testSequenceSet(Temporal base, TInterpolation type, TBoolSeqSet tseqset) {
        functions.meos_initialize("UTC");
        Temporal tmp = base.to_sequenceset(type);
        assertTrue(tmp instanceof TBoolSeqSet);
        assertEquals(((TBoolSeqSet) tmp).to_string(), tseqset.to_string());
    }


    @ParameterizedTest(name = "Test insert method.")
    @MethodSource("TBool_insert")
    public void testInsert(Temporal base, Temporal base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals(((TBoolInst)base.insert(base2)).to_string(), ((TBoolSeq) tseq).to_string());
        } else if (type == "TBoolSeq") {
            assertEquals(((TBoolSeq)base.insert(base2)).to_string(), ((TBoolSeq) tseq).to_string());
        } else if (type == "TBoolSeqSet") {
            assertEquals(((TBoolSeqSet)base.insert(base2)).to_string(), ((TBoolSeqSet) tseq).to_string());
        }
    }


    @ParameterizedTest(name = "Test update method.")
    @MethodSource("TBool_update")
    public void testUpdate(Temporal base, Temporal base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals(((TBoolInst)base.update(base2)).to_string(), ((TBoolInst) tseq).to_string());
        } else if (type == "TBoolSeq") {
            assertEquals(((TBoolSeq)base.update(base2)).to_string(), ((TBoolSeq) tseq).to_string());
        } else if (type == "TBoolSeqSet") {
            assertEquals(((TBoolSeqSet)base.update(base2)).to_string(), ((TBoolSeqSet) tseq).to_string());
        }
    }


    @ParameterizedTest(name = "Test append sequence method.")
    @MethodSource("TBool_appendseq")
    public void testAppendSeq(Temporal base, TSequence base2, Temporal tseq, String type) {
        functions.meos_initialize("UTC");
        if (type == "TBoolSeq") {
            assertEquals(((TBoolSeq)base.append_sequence(base2)).to_string(), ((TBoolSeq) tseq).to_string());
        } else if (type == "TBoolSeqSet") {
            assertEquals(((TBoolSeqSet)base.append_sequence(base2)).to_string(), ((TBoolSeqSet) tseq).to_string());
        }
    }



    @ParameterizedTest(name = "Test when true method.")
    @MethodSource("TBool_whentrue")
    public void testWhentrue(Temporal base, String type, PeriodSet pset) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).when_true().toString(), pset.toString());
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).when_true().toString(), pset.toString());
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).when_true().toString(), pset.toString());
        }
    }


    @ParameterizedTest(name = "Test when false method.")
    @MethodSource("TBool_whenfalse")
    public void testWhenfalse(Temporal base, String type, PeriodSet pset) {
        functions.meos_initialize("UTC");
        if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).when_false().toString(), pset.toString());
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).when_false().toString(), pset.toString());
        }
    }


    @ParameterizedTest(name = "Test to always true method.")
    @MethodSource("TBool_alwaystrue")
    public void testAlwaystrue(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).always_eq(true), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).always_eq(true), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).always_eq(true), expected);
        }
    }


    @ParameterizedTest(name = "Test to always false method.")
    @MethodSource("TBool_alwaysfalse")
    public void testAlwaysfalse(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).always_eq(false), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).always_eq(false), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).always_eq(false), expected);
        }
    }



    @ParameterizedTest(name = "Test to ever true method.")
    @MethodSource("TBool_evertrue")
    public void testEvertrue(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).ever_eq(true), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).ever_eq(true), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).ever_eq(true), expected);
        }
    }



    @ParameterizedTest(name = "Test to ever false method.")
    @MethodSource("TBool_everfalse")
    public void testEverfalse(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).ever_eq(false), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).ever_eq(false), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).ever_eq(false), expected);
        }
    }


    @ParameterizedTest(name = "Test to never true method.")
    @MethodSource("TBool_nevertrue")
    public void testNevertrue(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).never_eq(true), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).never_eq(true), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).never_eq(true), expected);
        }
    }



    @ParameterizedTest(name = "Test to never false method.")
    @MethodSource("TBool_neverfalse")
    public void testNeverfalse(Temporal base, String type, boolean expected) {
        functions.meos_initialize("UTC");
        if (type == "TBoolInst") {
            assertEquals( ((TBoolInst) base).never_eq(false), expected);
        } else if (type == "TBoolSeq") {
            assertEquals( ((TBoolSeq) base).never_eq(false), expected);
        } else if (type == "TBoolSeqSet") {
            assertEquals( ((TBoolSeqSet) base).never_eq(false), expected);
        }
    }











}