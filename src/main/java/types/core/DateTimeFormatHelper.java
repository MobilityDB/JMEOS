package types.core;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper to format dateTime values
 */
public final class DateTimeFormatHelper {
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ssX";
	
	private DateTimeFormatHelper() {
	}
	
	/**
	 * Formats a string into an OffsetDateTime
	 *
	 * @param value String
	 * @return OffsetDateTime
	 */
	public static OffsetDateTime getDateTimeFormat(String value) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT);
		return OffsetDateTime.parse(value.trim(), format);
	}
	
	/**
	 * Formats an OffsetDateTime into a string
	 *
	 * @param value OffsetDateTime
	 * @return String
	 */
	public static String getStringFormat(OffsetDateTime value) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT);
		return format.format(value);
	}
}
