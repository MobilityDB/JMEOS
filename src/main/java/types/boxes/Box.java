package types.boxes;

import jnr.ffi.Pointer;
import types.TemporalObject;
import types.collections.time.Period;


/**
 * Interface to define a box from which STbox and TBox are inherited.
 */

public interface Box extends TemporalObject{

    public boolean get_tmin_inc();
    public boolean get_tmax_inc();
    public Period to_period();

}
