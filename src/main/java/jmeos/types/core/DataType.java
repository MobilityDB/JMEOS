package jmeos.types.core;

import org.postgresql.util.PGobject;

import java.sql.SQLException;

/**
 * Base class for all MobilityDB types that will be registered to Postgres connection.
 */
public abstract class DataType extends PGobject {
	/**
	 * Default constructor that will be set the type based on the class annotation TypeName
	 */
	protected DataType() {
		super();
		
		// register type
		Class<?> clazz = this.getClass();
		
		if (clazz.isAnnotationPresent(TypeName.class)) {
			setType(clazz.getAnnotation(TypeName.class).name());
		}
	}
	
	/**
	 * Parses the object to string representation in the form required by org.postgresql.
	 *
	 * @return the value in string representation of this data type
	 */
	@Override
	public abstract String getValue();
	
	/**
	 * Sets the value from the string representation
	 *
	 * @param value a string representation of the value of the data type
	 * @throws SQLException thrown when the value cannot be parsed or if it is invalid
	 */
	@Override
	public abstract void setValue(final String value) throws SQLException;
}
