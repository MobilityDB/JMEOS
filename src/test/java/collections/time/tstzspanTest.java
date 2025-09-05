package collections.time;

import functions.functions;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import types.collections.base.Span;
import types.collections.time.tstzspan;
import types.collections.time.tstzspanset;
import types.collections.time.tstzset;

import types.TemporalObject;
import types.basic.tfloat.TFloatInst;
import types.basic.tfloat.TFloatSeq;
import types.basic.tfloat.TFloatSeqSet;
import types.boxes.STBox;
import types.boxes.TBox;
import types.collections.time.tstzspan;
import types.collections.time.tstzspanset;

import static org.junit.jupiter.api.Assertions.*;

import types.collections.time.Time;
import functions.*;
import javax.naming.OperationNotSupportedException;


class tstzspanTest {
	private tstzspan tstzspan = new tstzspan("(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)");
	private tstzspan tstzspan2 = new tstzspan("[2019-09-08 02:03:00+0, 2019-09-10 02:03:00+0]");
	private tstzspan p = new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)");

	static error_handler_fn errorHandler= new error_handler();

	tstzspanTest() throws SQLException {
	}


	static Stream<Arguments> tstzspan_constructor() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of("(2019-09-08 00:00:00+0, 2019-09-10 00:00:00+0)",LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), false,false),
				Arguments.of("[2019-09-08 00:00:00+0, 2019-09-10 00:00:00+0]", LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), true,true)
		);
	}

	static Stream<Arguments> tstzspan_constructor2() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0",LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
				);
	}

	static Stream<Arguments> tstzspan_constructor3() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0), LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
		);
	}

	static Stream<Arguments> tstzspan_constructor4() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of("2019-09-08 00:00:00+0", LocalDateTime.of(2019, 9, 10, 0, 0),LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0))
		);
	}


	static Stream<Arguments> tstzspan_constructor5() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(true,true),
				Arguments.of(true,false),
				Arguments.of(false,true),
				Arguments.of(false,false)
		);
	}

	private static Stream<Arguments> temporals_adjacent() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), false),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), true),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), true),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), true)
		);
	}


	private static Stream<Arguments> temporals_iscontained() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), true),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_contains() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), false),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), false),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), false),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), false),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_overlaps() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), true),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_same() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), false),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), false),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_before() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), false),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), false),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), false),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), false),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), false),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_after() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), false),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), false),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), true),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), false),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), false),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), false),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), true)
		);
	}


	private static Stream<Arguments> temporals_overbefore() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), false),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), true),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), false)
		);
	}


	private static Stream<Arguments> temporals_overafter() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true),
				Arguments.of(new TFloatInst("1.0@2020-01-01"), true),
				Arguments.of(new TFloatSeq("(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31]"), true),
				Arguments.of(new TFloatSeqSet("{(1.0@2020-01-01, 3.0@2020-01-10, 10.0@2020-01-20, 0.0@2020-01-31], (1.0@2021-01-01, 3.0@2021-01-10, 10.0@2021-01-20, 0.0@2021-01-31]}"), true),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), true),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), true)
		);
	}


	private static Stream<Arguments> temporals_distance() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), 0.0),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), 0.0),
				Arguments.of(new TBox("TBOXFLOAT XT([0, 10),[2020-01-01, 2020-01-31])"), 0.0),
				Arguments.of(new STBox("STBOX ZT(((1.0,2.0,3.0),(4.0,5.0,6.0)),[2001-01-01, 2001-01-02])"), 599443200)
		);
	}


	private static Stream<Arguments> intersection() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true),
				Arguments.of(new tstzspanset("{(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0), (2021-01-01 00:00:00+0, 2021-01-31 00:00:00+0)}"), true)
		);
	}


	private static Stream<Arguments> other() {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		return Stream.of(
				Arguments.of(new tstzspan("(2020-01-01 00:00:00+0, 2020-01-31 00:00:00+0)"), true)
		);
	}







	public void assert_tstzspan_equality(tstzspan intsp, LocalDateTime lower, LocalDateTime upper, Boolean lower_inc, Boolean upper_inc){
		if (lower != null) {
			assertEquals(intsp.lower(), lower);
		}
		if (upper != null) {
			assertEquals(intsp.upper(), upper);
		}
		if (lower_inc != null) {
			assertEquals(intsp.lower_inc(), lower_inc);
		}
		if (upper_inc != null) {
			assertEquals(intsp.upper_inc(), upper_inc);
		}
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("tstzspan_constructor")
	public void testtstzspanConstructor(String source, LocalDateTime lower, LocalDateTime upper, boolean lower_inc, boolean upper_inc) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan(source);
		assert_tstzspan_equality(p,lower,upper,lower_inc,upper_inc);
	}

	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("tstzspan_constructor2")
	public void testtstzspanConstructor2(String lower, String upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan(lower,upper);
		assert_tstzspan_equality(p,lowerv,upperv,true,false);
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("tstzspan_constructor3")
	public void testtstzspanConstructor3(LocalDateTime lower, LocalDateTime upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan(lower,upper);
		assert_tstzspan_equality(p,lowerv,upperv,true,false);
	}


	@ParameterizedTest(name = "Test Constructor method")
	@MethodSource("tstzspan_constructor4")
	public void testtstzspanConstructor4(String lower, LocalDateTime upper, LocalDateTime lowerv, LocalDateTime upperv) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan(lower,upper);
		assert_tstzspan_equality(p,lowerv,upperv,true,false);
	}

	@Test
	public void testtstzspanBounds() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0");
		assert_tstzspan_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),true,false);
	}


	@ParameterizedTest(name="Test Bound Inclusivity")
	@MethodSource("tstzspan_constructor5")
	public void testtstzspanIncluBounds(boolean lower,boolean upper) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan p = new tstzspan("2019-09-08 00:00:00+0", "2019-09-10 00:00:00+0",lower,upper);
		assert_tstzspan_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),lower,upper);
	}


	@Test
	public void testHexwkbConstructor() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
//		tstzspan p = types.collections.time.tstzspan.from_hexwkb("012100000040021FFE3402000000B15A26350200");
		String hexwkb_string= tstzspan.as_hexwkb();
//		System.out.println(hexwkb_string);
		tstzspan p = types.collections.time.tstzspan.from_hexwkb(hexwkb_string);
//		System.out.println(p.toString());
		assert_tstzspan_equality(p, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testFromAsConstructor() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertNotEquals(this.tstzspan,new tstzspan("(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)"));
	}

	@Test
	public void testCopyConstructor() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspan other = this.tstzspan.copy();
		assertNotEquals(this.tstzspan, other);
		assertEquals(other.toString(), this.tstzspan.toString());
	}

	@Test
	public void testtstzspanOut() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.tstzspan.toString(), "(2019-09-08 00:00:00+00, 2019-09-10 00:00:00+00)");
	}


	@Test
	public void testTotstzspanSet() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		tstzspanset pset = tstzspan.to_spanset();
		System.out.println(pset.toString());
		String spanset_string= pset.toString();
		assertTrue(pset != null);
		assertEquals(pset.num_periods(),1);
		assertEquals(pset.start_span().toString(),this.tstzspan.toString());
		assert_tstzspan_equality(tstzspan, LocalDateTime.of(2019, 9, 8, 0, 0), LocalDateTime.of(2019, 9, 10, 0, 0),false,false);
	}

	@Test
	public void testUpperAccessors() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.tstzspan.lower(), LocalDateTime.of(2019, 9, 8, 0, 0));
		assertEquals(this.tstzspan2.lower(), LocalDateTime.of(2019, 9, 8, 2, 3));
	}


	@Test
	public void testLowerAccessors() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.tstzspan.upper(), LocalDateTime.of(2019, 9, 10, 0, 0));
		assertEquals(this.tstzspan2.upper(), LocalDateTime.of(2019, 9, 10, 2, 3));
	}

	@Test
	public void testLowerIncAccessors() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertFalse(this.tstzspan.lower_inc());
		assertTrue(this.tstzspan2.lower_inc());
	}

	@Test
	public void testUpperIncAccessors() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertFalse(this.tstzspan.upper_inc());
		assertTrue(this.tstzspan2.upper_inc());
	}


	@Test
	public void testDurationInSeconds() throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
//		System.out.println(tstzspan.toString());
		types.collections.time.tstzspan tst= new tstzspan("(2019-09-08 00:00:00+00, 2022-10-25 00:05:00+00)");
//		System.out.println(tst.duration());
//		System.out.println(tst.duration_in_second());
//		System.out.println(tstzspan.duration_in_second());
//		System.out.println(this.tstzspan2.duration());
		assertEquals(this.tstzspan.duration_in_second(), 172800);
		assertEquals(this.tstzspan2.duration_in_second(), 172800);
	}


//	@Test
//	public void testHash() throws SQLException {
//		functions.meos_initialize_timezone("UTC");
//      functions.meos_initialize_error_handler(errorHandler);
//		assertEquals(this.tstzspan.hash(), 1164402929);
//	}


	@ParameterizedTest(name="Test Adjacency method")
	@MethodSource("temporals_adjacent")
	public void testAdjacency(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		STBox st= new STBox(functions.tstzspan_stbox(p.get_inner()));
		System.out.println(st.toString(15));
		assertEquals(this.p.is_adjacent(other), expected);
	}

	@ParameterizedTest(name="Test is contained in method")
	@MethodSource("temporals_iscontained")
	public void testIsContainedIn(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.is_contained_in(other), expected);

	}


	@ParameterizedTest(name="Test contains method")
	@MethodSource("temporals_contains")
    public void testContains(TemporalObject other, boolean expected) throws Exception {
        functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
        assertEquals(this.p.contains(other), expected);

    }


	@ParameterizedTest(name="Test overlaps method")
	@MethodSource("temporals_overlaps")
	public void testOverlaps(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.overlaps(other), expected);
	}


	@ParameterizedTest(name="Test is same method")
	@MethodSource("temporals_same")
	public void testIsSame(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.is_same(other), expected);
	}



	@ParameterizedTest(name="Test is before method")
	@MethodSource("temporals_before")
	public void testIsBefore(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.is_before(other), expected);
	}



	@ParameterizedTest(name="Test is after method")
	@MethodSource("temporals_after")
	public void testIsAfter(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.is_after(other), expected);
	}


	@ParameterizedTest(name="Test is over or before method")
	@MethodSource("temporals_overbefore")
	public void testIsOverOrBefore(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertEquals(this.p.is_over_or_before(other), expected);

	}


	@ParameterizedTest(name="Test is over or after method")
	@MethodSource("temporals_overafter")
	public void testIsOverOrAfter(TemporalObject other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		STBox st= new STBox(functions.tstzspan_stbox(p.get_inner()));
		System.out.println(st.toString(15));
		assertEquals(this.p.is_over_or_after(other), expected);

	}



	@ParameterizedTest(name="Test distance method")
	@MethodSource("temporals_distance")
	public void testDistance(TemporalObject other, double expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		STBox st= new STBox("STBOX ZT(((1.0, 2.0, 3.0),(4.0, 5.0, 6.0)),[2001-01-01, 2001-01-02])");
		double dist= p.distance(st);
		System.out.println(dist);
//		System.out.println(this.p.distance(other));
		assertEquals(this.p.distance(other), expected);
	}



	@ParameterizedTest(name="Test intersection method")
	@MethodSource("intersection")
	public void testIntersection(Time other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		this.p.intersection(other);
	}

	@ParameterizedTest(name="Test minus method")
	@MethodSource("intersection")
	public void testMinus(Time other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		this.p.minus(other);
	}

	@ParameterizedTest(name="Test union method")
	@MethodSource("intersection")
	public void testUnion(Time other, boolean expected) throws Exception {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		this.p.union(other);
	}



	@ParameterizedTest(name="Test equal method")
	@MethodSource("other")
	public void testEqual(Time t) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertFalse(this.tstzspan.equals(t));
	}


	@ParameterizedTest(name="Test ne method")
	@MethodSource("other")
	public void testNotEqual(Time t) throws SQLException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertTrue(this.tstzspan.notEquals(t));
	}

	@ParameterizedTest(name="Test lt method")
	@MethodSource("other")
	public void testLessThan(Time t) throws SQLException, OperationNotSupportedException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertTrue(this.tstzspan.lessThan(t));
	}

	@ParameterizedTest(name="Test le method")
	@MethodSource("other")
	public void testLessThanOrEqual(Time t) throws SQLException, OperationNotSupportedException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertTrue(this.tstzspan.lessThanOrEqual(t));
	}

	@ParameterizedTest(name="Test gt method")
	@MethodSource("other")
	public void testGreaterThan(Time t) throws SQLException, OperationNotSupportedException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertTrue(this.tstzspan.greaterThan(t));
	}

	@ParameterizedTest(name="Test ge method")
	@MethodSource("other")
	public void testGreaterThanOrEqual(Time t) throws SQLException, OperationNotSupportedException {
		functions.meos_initialize_timezone("UTC");
functions.meos_initialize_error_handler(errorHandler);
		assertFalse(this.tstzspan.greaterThanOrEqual(t));
	}

}
