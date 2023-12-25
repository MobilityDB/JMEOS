package types.boxes;

import types.TemporalObject;
import types.collections.time.Period;


/**
 * Interface to define a box from which STbox and TBox are inherited.
 */

public interface Box extends TemporalObject{

    boolean get_tmin_inc();
    boolean get_tmax_inc();
    Period to_period();

}
