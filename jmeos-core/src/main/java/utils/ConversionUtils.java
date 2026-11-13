/*****************************************************************************
 *
 * This MobilityDB code is provided under The PostgreSQL License.
 * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB
 * contributors
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without a written
 * agreement is hereby granted, provided that the above copyright notice and
 * this paragraph and the following two paragraphs appear in all copies.
 *
 * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING
 * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 *
 * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON
 * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *****************************************************************************/

package utils;

import com.google.common.collect.BoundType;
import functions.error_handler;
import functions.GeneratedFunctions;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import com.google.common.collect.Range;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
//import types.temporal.TInstant;
//import types.temporal.TSequence;
//import types.temporal.TSequenceSet;
//import types.temporal.Temporal;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;


/**
 * Class based on the manually-defined functions from PyMeos.
 * This class contains conversions functions for MEOS/JMEOS types.
 *
 * @author Killian Monnier and Nidhal Mareghni
 * @see <a href="https://github.com/MobilityDB/PyMEOS/blob/9d84632df5a4b060d43421248c142af337ec4ddd/pymeos_cffi/pymeos_cffi/builder/build_pymeos_functions.py">...</a>
 * @since 09/08/2023
 */
public class ConversionUtils {
	
	/*
	 * The casts of the time types convert through MEOS and are exact or an error (MobilityDB
	 * doc/specs/meos-bindings-algebra.md, section 5): each is a generated wrapper of a MEOS
	 * conversion and a constructor of the host type. A value MEOS cannot state raises the MEOS
	 * error of its function, one the host type cannot state the exception of its constructor, and
	 * a value with a fraction of a microsecond, which no MEOS value holds, an
	 * IllegalArgumentException.
	 */

	private static final long NANOS_PER_MICRO = 1_000L;

	/** The whole microseconds of a count of nanoseconds, which must hold no fraction of one. */
	private static long wholeMicros(long nanos, Object value) {
		if (nanos % NANOS_PER_MICRO != 0) {
			throw new IllegalArgumentException("A MEOS value holds whole microseconds: " + value);
		}
		return nanos / NANOS_PER_MICRO;
	}

	/**
	 * Casts a {@link LocalDateTime}, taken in UTC, to the {@link OffsetDateTime} of the MEOS
	 * {@code TimestampTz} it states.
	 */
	public static OffsetDateTime datetimeToTimestampTz(LocalDateTime dt) {
		return TimestampTzConverter.toOffsetDateTime(
				TimestampTzConverter.toTimestampTz(dt.atOffset(ZoneOffset.UTC)));
	}

	/**
	 * Casts an {@link OffsetDateTime} to the MEOS {@code TimestampTz} it states and answers that
	 * instant as a {@link LocalDateTime} in UTC.
	 */
	public static LocalDateTime timestamptz_to_datetime(OffsetDateTime ts) {
		return TimestampTzConverter.toOffsetDateTime(TimestampTzConverter.toTimestampTz(ts))
				.toLocalDateTime();
	}

	/**
	 * Casts a {@link Duration} to a MEOS {@code Interval} through {@code interval_from_parts}: no
	 * months, its whole days as days and the rest of it as microseconds, both of its sign.
	 */
	public static Pointer timedelta_to_interval(Duration td){
		long days = td.toDays();
		return GeneratedFunctions.interval_from_parts(0, Math.toIntExact(days),
				wholeMicros(td.minusDays(days).toNanos(), td));
	}

	/**
	 * Casts a MEOS {@code Interval} to a {@link Duration} through {@code interval_to_parts}, a day
	 * being 24 hours. A month has no fixed length, so an interval holding one has no Duration.
	 */
	public static Duration interval_to_timedelta(Pointer p){
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer months = Memory.allocate(runtime, Integer.BYTES);
		Pointer days = Memory.allocate(runtime, Integer.BYTES);
		Pointer us = Memory.allocate(runtime, Long.BYTES);
		GeneratedFunctions.interval_to_parts(p, months, days, us);
		if (months.getInt(0) != 0) {
			throw new IllegalArgumentException("an interval of " + months.getInt(0)
				+ " month(s) has no fixed duration");
		}
		return Duration.ofDays(days.getInt(0))
				.plus(Duration.of(us.getLongLong(0), java.time.temporal.ChronoUnit.MICROS));
	}

	/** Casts a {@link LocalDate} to a MEOS {@code DateADT} through {@code unix_days_to_date}. */
	public static int date_to_date_adt(LocalDate date) {
		return GeneratedFunctions.unix_days_to_date(Math.toIntExact(date.toEpochDay()));
	}

	/** Casts a MEOS {@code DateADT} to a {@link LocalDate} through {@code date_to_unix_days}. */
	public static LocalDate date_adt_to_date(int date) {
		return LocalDate.ofEpochDay(GeneratedFunctions.date_to_unix_days(date));
	}

	/** Casts a {@link LocalTime} to a MEOS {@code TimeADT} through {@code time_from_parts}. */
	public static long time_to_time_adt(LocalTime time) {
		return GeneratedFunctions.time_from_parts(wholeMicros(time.toNanoOfDay(), time));
	}

	/**
	 * Casts a MEOS {@code TimeADT} to a {@link LocalTime} through {@code time_to_parts}. A
	 * {@link LocalTime} ends its day before {@code 24:00:00}, which it cannot state.
	 */
	public static LocalTime time_adt_to_time(long time) {
		return LocalTime.ofNanoOfDay(
				GeneratedFunctions.time_to_parts(time).getLongLong(0) * NANOS_PER_MICRO);
	}

	/** Casts an {@link OffsetTime} to a MEOS {@code TimeTzADT} through {@code timetz_from_parts}. */
	public static Pointer timetz_to_timetz_adt(OffsetTime timetz) {
		return GeneratedFunctions.timetz_from_parts(
				wholeMicros(timetz.toLocalTime().toNanoOfDay(), timetz),
				timetz.getOffset().getTotalSeconds());
	}

	/**
	 * Casts a MEOS {@code TimeTzADT} to an {@link OffsetTime} through {@code timetz_to_parts}. An
	 * {@link OffsetTime} ends its day before {@code 24:00:00}, which it cannot state.
	 */
	public static OffsetTime timetz_adt_to_timetz(Pointer timetz) {
		Runtime runtime = Runtime.getSystemRuntime();
		Pointer us = Memory.allocate(runtime, Long.BYTES);
		Pointer offset = Memory.allocate(runtime, Integer.BYTES);
		GeneratedFunctions.timetz_to_parts(timetz, us, offset);
		return OffsetTime.of(LocalTime.ofNanoOfDay(us.getLongLong(0) * NANOS_PER_MICRO),
				ZoneOffset.ofTotalSeconds(offset.getInt(0)));
	}

	public static Pointer intrange_to_intspan(Range<Integer> intrange) throws SQLException {
		boolean lower_inc = intrange.lowerBoundType() == BoundType.CLOSED;
		boolean upper_inc = intrange.upperBoundType() == BoundType.CLOSED;
		return GeneratedFunctions.intspan_make(intrange.lowerEndpoint(),intrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range intspan_to_intrange(Pointer span){
		BoundType lower_inc = GeneratedFunctions.span_lower_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = GeneratedFunctions.span_upper_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(GeneratedFunctions.intspan_lower(span), lower_inc, GeneratedFunctions.intspan_upper(span), upper_inc);
	}

	public static Pointer floatrange_to_floatspan(Range<Float> floatrange){
		boolean lower_inc = floatrange.lowerBoundType() == BoundType.CLOSED;
		boolean upper_inc = floatrange.upperBoundType() == BoundType.CLOSED;
		return GeneratedFunctions.floatspan_make(floatrange.lowerEndpoint(),floatrange.upperEndpoint(),lower_inc, upper_inc);
	}

	public static Range floatspan_to_floatrange(Pointer span){
		BoundType lower_inc = GeneratedFunctions.span_lower_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		BoundType upper_inc = GeneratedFunctions.span_upper_inc(span) ? BoundType.CLOSED : BoundType.OPEN;
		return Range.range(GeneratedFunctions.floatspan_lower(span), lower_inc, GeneratedFunctions.floatspan_upper(span), upper_inc);
	}



//	public static TInstant asTInstant(Temporal temporal) {
//			return (TInstant) temporal;
//		}
//
//	public static TSequence asTSequence(Temporal temporal) {
//			return (TSequence) temporal;
//		}
//
//	public static TSequenceSet asTSequenceSet(Temporal temporal) {
//			return (TSequenceSet) temporal;
//		}


	public static Pointer geo_to_gserialized(Geometry geom, boolean geodetic){
		if (geodetic){
			return geography_to_gserialized(geom);
		}
		else{
			return geometry_to_gserialized(geom);
		}
	}


	public static Pointer geometry_to_gserialized(Geometry geom){
		WKTWriter wktWriter = new WKTWriter();
		String text = wktWriter.write(geom);
		if (geom.getSRID() > 0){
			text = "SRID="+geom.getSRID()+";"+text;
		}
		Pointer ptr = GeneratedFunctions.geom_in(text,-1);
		return ptr;
	}


	public static Pointer geography_to_gserialized(Geometry geom){
		WKTWriter wktWriter = new WKTWriter();
		String text = wktWriter.write(geom);
		if (geom.getSRID() > 0){
			text = "SRID="+geom.getSRID()+";"+text;
		}
		Pointer ptr = GeneratedFunctions.geog_in(text,-1);
		return ptr;
	}

	public static Point gserialized_to_shapely_point(Pointer geom, int precision) throws ParseException, ParseException {
        Geometry geometry = gserialized_to_shapely_geometry(geom, precision);
		return (Point) geometry;
	}

	public static Geometry gserialized_to_shapely_geometry(Pointer geom, int precision) throws ParseException, ParseException {
		String text = GeneratedFunctions.geo_as_text(geom,precision);
		WKTReader wktReader = new WKTReader();
		Geometry geometry = wktReader.read(text);
		int srid = GeneratedFunctions.geo_srid(geom);
		if (srid > 0){
			geometry.setSRID(srid);
		}
		return geometry;
	}

	public static String hexWKBToWKB(String hexWKB) {
		if (hexWKB.length() % 2 != 0) {
			System.err.println("Invalid HexWKB format.");
			return null;
		}

		StringBuilder wkbStringBuilder = new StringBuilder();
		for (int i = 0; i < hexWKB.length(); i += 2) {
			String hexPair = hexWKB.substring(i, i + 2);
			int hexValue = Integer.parseInt(hexPair, 16);
			wkbStringBuilder.append((char) hexValue);
		}

		return wkbStringBuilder.toString();
	}


	public static LocalDateTime string_to_LocalDateTime(String value){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(value, formatter);
	}
}
