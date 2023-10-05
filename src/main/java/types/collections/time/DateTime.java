package types.collections.time;

import types.TemporalObject;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Class that represent a DateTime.
 * @since 09/08/2023
 * @author Killian Monnier
 */
public class DateTime extends TemporalObject<LocalDateTime> {
	
	/**
	 * The default constructor.
	 */
	public DateTime() {
		super();
	}
	
	public DateTime(LocalDateTime dateTime) {
		this._inner = dateTime;
	}
	
	@Override
	public String getValue() {
		return null;
	}
	
	@Override
	public void setValue(String value) throws SQLException {
	
	}
}