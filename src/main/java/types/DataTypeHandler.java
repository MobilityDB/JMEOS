package types;

import org.postgresql.PGConnection;
import types.basic.tbool.TBool;
import types.basic.tfloat.TFloat;
import types.basic.tint.TInt;
import types.basic.tpoint.tgeog.TGeogPoint;
import types.basic.tpoint.tgeom.TGeomPoint;
import types.basic.ttext.TText;
import types.boxes.STBox;
import types.boxes.TBox;
import types.core.DataType;
import types.core.TypeName;
import types.collections.time.Period;
import types.collections.time.PeriodSet;
import types.collections.time.TimestampSet;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Singleton helper used to register all the MobilityDB data types to the connection.
 */
public enum DataTypeHandler {
	INSTANCE;

	DataTypeHandler() {
	}
	
	/**
	 * Registers the type based on the TypeName annotation of the class
	 *
	 * @param connection - the PGConnection
	 * @throws SQLException if any of the classes does not implement PGobject
	 */
	/*
	public void registerTypes(PGConnection connection) throws SQLException {
		for (Class<? extends DataType> clazz : types) {
			if (clazz.isAnnotationPresent(TypeName.class)) {
				connection.addDataType(clazz.getAnnotation(TypeName.class).name(), clazz);
			}
		}
	}

	 */
}
