package types.boxes;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.core.DataType;
import types.time.Period;

import java.sql.SQLException;

/**
 * Abstract class to define a box from which STbox and TBox are inherited.
 */

public abstract class Box extends TemporalObject<Pointer> {

    abstract public boolean get_tmin_inc();
    abstract public boolean get_tmax_inc();
    abstract public Period to_period() throws SQLException;

}
