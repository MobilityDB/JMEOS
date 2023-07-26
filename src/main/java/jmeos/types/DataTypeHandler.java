package jmeos.types;

import jmeos.types.basic.tbool.TBool;
import jmeos.types.basic.tfloat.TFloat;
import jmeos.types.basic.tint.TInt;
import jmeos.types.basic.tpoint.tgeog.TGeogPoint;
import jmeos.types.basic.tpoint.tgeom.TGeomPoint;
import jmeos.types.basic.ttext.TText;
import jmeos.types.boxes.STBox;
import jmeos.types.boxes.TBox;
import jmeos.types.core.DataType;
import jmeos.types.core.TypeName;
import jmeos.types.time.Period;
import jmeos.types.time.PeriodSet;
import jmeos.types.time.TimestampSet;
import org.postgresql.PGConnection;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Singleton helper used to register all the MobilityDB data types to the connection.
 */
public enum DataTypeHandler {
	INSTANCE;
	
	private final ArrayList<Class<? extends DataType>> types;
	
	DataTypeHandler() {
		types = new ArrayList<>();
		types.add(Period.class);
		types.add(PeriodSet.class);
		types.add(TimestampSet.class);
		types.add(TBox.class);
		types.add(STBox.class);
		types.add(TInt.class);
		types.add(TBool.class);
		types.add(TFloat.class);
		types.add(TText.class);
		types.add(TGeomPoint.class);
		types.add(TGeogPoint.class);
	}
	
	/**
	 * Registers the type based on the TypeName annotation of the class
	 *
	 * @param connection - the PGConnection
	 * @throws SQLException if any of the classes does not implement PGobject
	 */
	public void registerTypes(PGConnection connection) throws SQLException {
		for (Class<? extends DataType> clazz : types) {
			if (clazz.isAnnotationPresent(TypeName.class)) {
				connection.addDataType(clazz.getAnnotation(TypeName.class).name(), clazz);
			}
		}
	}
}
