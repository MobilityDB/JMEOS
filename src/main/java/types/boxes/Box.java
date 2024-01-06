package types.boxes;

import types.TemporalObject;
import types.collections.time.Period;


/**
 * Interface to define a box from which STbox and TBox are inherited.
 *
 * @author Nidhal Mareghni
 * @since 10/07/2023
 */
public interface Box extends TemporalObject{
    Period to_period();

}
