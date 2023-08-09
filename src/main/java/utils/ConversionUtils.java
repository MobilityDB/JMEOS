package utils;

//import org.postgresql.geometric.PGgeometry;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.io.ParseException;
//import org.locationtech.jts.io.WKTReader;

import functions.functions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class based on the manually-defined functions from PyMeos.
 *
 * @author Killian Monnier
 * @see <a href="https://github.com/MobilityDB/PyMEOS/blob/9d84632df5a4b060d43421248c142af337ec4ddd/pymeos_cffi/pymeos_cffi/builder/build_pymeos_functions.py">...</a>
 * @since 09/08/2023
 */
public class ConversionUtils {
	
	public static long datetimeToTimestampTz(LocalDateTime dt) {
		String formattedDt = dt.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"));
		return functions.pg_timestamptz_in(formattedDt, -1);
	}
	
	public static LocalDateTime timestampTzToDatetime(int ts) {
		return ZonedDateTime.parse(functions.pg_timestamptz_out(ts)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}
	
	//	public static Pointer timedeltaToInterval(Duration td) {
	//		Interval interval = new Interval();
	//		interval.time = (td.toMillis() * 1000);
	//		interval.day = (int) td.toDays();
	//		interval.month = 0;
	//		return interval.getPointer();
	//	}
	
	//	public static Duration intervalToTimedelta(Pointer interval) {
	//		return Duration.ofDays(interval.getInt(Interval.dayOffset()))
	//				.plusMillis(interval.getLong(Interval.timeOffset()) / 1000);
	//	}
	
	//	public static Pointer geometryToGserialized(Geometry geom, Boolean geodetic) {
	//		String text;
	//		if (geom instanceof PGgeometry) {
	//			text = ((PGgeometry) geom).getValue();
	//			if (geom.getSRID() > 0) {
	//				text = "SRID=" + geom.getSRID() + ";" + text;
	//			}
	//		} else if (geom instanceof Geometry) {
	//			text = geom.toText();
	//			if (geom.getSRID() > 0) {
	//				text = "SRID=" + geom.getSRID() + ";" + text;
	//			}
	//		} else {
	//			throw new IllegalArgumentException("Parameter geom must be either a PostGIS Geometry or a JTS Geometry");
	//		}
	//		GSERIALIZED gs = gserialized_in(text.getBytes(), -1);
	//		if (geodetic != null) {
	//			int geodeticFlag = geodetic ? 0x08 : 0x00;
	//			gs.gflags(gs.gflags() | geodeticFlag);
	//		}
	//		return gs;
	//	}
	
	//	public static Point gserializedToShapelyPoint(Pointer geom, int precision) throws ParseException {
	//		String wktText = gserialized_as_text(geom, precision);
	//		WKTReader wktReader = new WKTReader();
	//		return (Point) wktReader.read(wktText);
	//	}
	
	//	public static Geometry gserializedToShapelyGeometry(Pointer geom, int precision) throws ParseException {
	//		String wktText = gserialized_as_text(geom, precision);
	//		WKTReader wktReader = new WKTReader();
	//		return wktReader.read(wktText);
	//	}
	
	//	public static Pointer intrangeToIntspan(Range irange) {
	//		return intspan_make(irange.lower, irange.upper, irange.lower_inc, irange.upper_inc).getPointer();
	//	}
	
	//	public static intrange intspanToIntrange(Pointer ispan) {
	//		Span span = new Span(ispan);
	//		return new intrange(intspan_lower(span), intspan_upper(span), span.lower_inc(), span.upper_inc());
	//	}
	
	//	public static Pointer floatrangeToFloatspan(floatrange frange) {
	//		return floatspan_make(frange.lower, frange.upper, frange.lower_inc, frange.upper_inc).getPointer();
	//	}
	
	//	public static floatrange floatspanToFloatrange(Pointer fspan) {
	//		Span span = new Span(fspan);
	//		return new floatrange(floatspan_lower(span), floatspan_upper(span), span.lower_inc(), span.upper_inc());
	//	}
	
	//	public static TInstant asTInstant(Temporal temporal) {
	//		return temporal.castTo(TInstant.class);
	//	}
	
	//	public static TSequence asTSequence(Temporal temporal) {
	//		return temporal.castTo(TSequence.class);
	//	}
	
	//	public static TSequenceSet asTSequenceSet(Temporal temporal) {
	//		return temporal.castTo(TSequenceSet.class);
	//	}
}
