package boxes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import functions.functions;
import java.sql.SQLException;
import java.util.stream.Stream;
import types.boxes.*;
import types.collections.base.Span;
import types.collections.number.FloatSpan;
import types.collections.number.IntSpan;
import types.collections.time.*;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;


import static org.junit.jupiter.api.Assertions.*;

class TBoxTest {

    static Stream<Arguments> TBox_sources() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1, 2])"),"TBox", "TBOXFLOAT X([1, 2])" ),
                Arguments.of(new TBox("TBOX T([2019-09-01, 2019-09-02])"), "TBox", "TBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])" ),
                Arguments.of(new TBox("TBOXFLOAT XT([1, 2],[2019-09-01, 2019-09-02])"),"TBox", "TBOXFLOAT XT([1, 2],[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])" )
        );
    }

    static Stream<Arguments> TBox_number() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(1, "TBOXINT X([1, 2))","TBox"),
                Arguments.of(1.5f, "TBOXFLOAT X([1.5, 1.5])", "TBox")
        );
    }

    static Stream<Arguments> TBox_span() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new IntSpan(1, 2, true, true),"TBox", "TBOXINT X([1, 3))" ),
                Arguments.of(new FloatSpan(1.5f, 2.5f, true, true),"TBox", "TBOXFLOAT X([1.5, 2.5])" )
        );
    }


    static Stream<Arguments> TBox_time() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TimestampSet("{2019-09-01, 2019-09-02}"),"TBox", "TBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])" ),
                Arguments.of(new Period("[2019-09-01, 2019-09-02]"),"TBox", "TBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])" ),
                Arguments.of(new PeriodSet("{[2019-09-01, 2019-09-02],[2019-09-03, 2019-09-05]}"), "TBox","TBOX T([2019-09-01 00:00:00+00, 2019-09-05 00:00:00+00])")
        );
    }


    static Stream<Arguments> TBox_basic() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1,2])"),"TBox", "TBOXFLOAT X([1, 2])" ),
                Arguments.of(new TBox("TBOX T([2019-09-01,2019-09-02])"),"TBox", "TBOX T([2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])" ),
                Arguments.of(new TBox("TBOXFLOAT XT([1,2],[2019-09-01,2019-09-02])"), "TBox","TBOXFLOAT XT([1, 2],[2019-09-01 00:00:00+00, 2019-09-02 00:00:00+00])")
        );
    }


    static Stream<Arguments> TBox_tofloatspan() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1,2])"),"TBox", new FloatSpan(1.0f, 2.0f, true, true) ),
                Arguments.of(new TBox("TBOXFLOAT XT([1,2],[2019-09-01,2019-09-02])"), "TBox",new FloatSpan(1.0f, 2.0f, true, true))
        );
    }

    static Stream<Arguments> TBox_toperiod() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1,2])"),"TBox", new Period("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]")),
                Arguments.of(new TBox("TBOXFLOAT XT([1,2],[2019-09-01,2019-09-02])"), "TBox", new Period("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]"))
        );
    }

    static Stream<Arguments> TBox_expandfloat() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1,2])"),"TBox", new TBox("TBOXFLOAT X([1, 2])")),
                Arguments.of(new TBox("TBOXFLOAT XT([1,2],[2019-09-01,2019-09-02])"), "TBox", new TBox("TBOXFLOAT XT([1,2],[2019-09-01, 2019-09-02])"))
        );
    }

    static Stream<Arguments> TBox_expandtime() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1,2])"),"TBox", new Period("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]")),
                Arguments.of(new TBox("TBOXFLOAT XT([1,2],[2019-09-01,2019-09-02])"), "TBox", new Period("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]"))
        );
    }



    static Stream<Arguments> TBox_round() throws SQLException {
        functions.meos_initialize("UTC");
        return Stream.of(
                Arguments.of(new TBox("TBOXFLOAT X([1.123456789,2.123456789])"),"TBox", new TBox("TBOXFLOAT X([1.12,2.12])")),
                Arguments.of(new TBox("TBOXFLOAT XT([1.123456789,2.123456789],[2019-09-01, 2019-09-03])"), "TBox", new TBox("TBOXFLOAT XT([1.12,2.12],[2019-09-01, 2019-09-03])"))
        );
    }






    /* ---------------------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------------------- */
    /* ---------------------------------------------------------------------------------------- */


    @ParameterizedTest(name = "Test from as constructor.")
    @MethodSource("TBox_sources")
    public void testStringConstructor(TBox box, String type, String expected) throws ParseException, SQLException {
        functions.meos_initialize("UTC");
        assertTrue(box instanceof TBox);
        assertEquals(box.toString(),expected);
    }


    @ParameterizedTest(name = "Test from value number constructor.")
    @MethodSource("TBox_number")
    public void testFromValueNConstructor(Number val, String box, String type) throws ParseException, SQLException {
        functions.meos_initialize("UTC");
        TBox new_tb = TBox.from_value_number(val);
        assertTrue(new_tb instanceof TBox);
        assertEquals(new_tb.toString(),box);
    }


    @ParameterizedTest(name = "Test from span constructor.")
    @MethodSource("TBox_span")
    public void testFromSpanConstructor(Span sp, String type, String expected) throws ParseException, SQLException {
        functions.meos_initialize("UTC");
        TBox new_tb = TBox.from_value_span(sp);
        assertTrue(new_tb instanceof TBox);
        assertEquals(new_tb.toString(),expected);
    }


    @ParameterizedTest(name = "Test from time constructor.")
    @MethodSource("TBox_time")
    public void testFromTimeConstructor(Time t, String type, String expected) throws Exception {
        functions.meos_initialize("UTC");
        TBox new_tb = TBox.from_time(t);
        assertTrue(new_tb instanceof TBox);
        assertEquals(new_tb.toString(),expected);
    }



    @ParameterizedTest(name = "Test from time constructor.")
    @MethodSource("TBox_time")
    public void testCopyConstructor(Time t, String type, String expected) throws Exception {
        functions.meos_initialize("UTC");
        TBox new_tb = TBox.from_time(t);
        assertTrue(new_tb instanceof TBox);
        assertEquals(new_tb.toString(),expected);
    }


    @ParameterizedTest(name = "Test copy constructor.")
    @MethodSource("TBox_basic")
    public void testCopyConstructor(TBox t, String type, String expected) throws Exception {
        functions.meos_initialize("UTC");
        TBox new_tb = t.copy();
        assertTrue(new_tb instanceof TBox);
        assertEquals(new_tb.toString(), t.toString());
    }


    @ParameterizedTest(name = "Test copy constructor.")
    @MethodSource("TBox_basic")
    public void testStrConstructor(TBox t, String type, String expected) throws Exception {
        functions.meos_initialize("UTC");
        assertTrue(t instanceof TBox);
        assertEquals(t.toString(), expected);
    }

    @ParameterizedTest(name = "Test floatspan method.")
    @MethodSource("TBox_tofloatspan")
    public void testStrConstructor(TBox t, String type, Span expected) throws Exception {
        functions.meos_initialize("UTC");
        FloatSpan z = t.to_floatspan();
        assertTrue(z instanceof FloatSpan);
        assertEquals(z.toString(15), ((FloatSpan)expected).toString(15));
    }




    @ParameterizedTest(name = "Test expand float method.")
    @MethodSource("TBox_expandfloat")
    public void testExpandFloat(TBox t, String type, TBox expected) throws Exception {
        functions.meos_initialize("UTC");
        TBox tb = t.expand(1.0f);
        assertTrue(tb instanceof TBox);
        assertEquals(t.toString(15),expected.toString(15));

    }


    @ParameterizedTest(name = "Test round float method.")
    @MethodSource("TBox_round")
    public void testRound(TBox t, String type, TBox expected) throws Exception {
        functions.meos_initialize("UTC");
        TBox new_tb = t.round(2);
        assertEquals(new_tb.toString(15), expected.toString(15));
    }

















}
