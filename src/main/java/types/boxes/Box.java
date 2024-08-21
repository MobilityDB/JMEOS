package types.boxes;

import types.TemporalObject;
import types.collections.base.Set;
import types.collections.time.tstzset;
import types.collections.time.tstzspan;


/**
 * Interface to define a box from which STbox and TBox are inherited.
 *
 * @author ARIJIT SAMAL
 */
public interface Box extends TemporalObject{
//    Set<Object> to_period();
    tstzspan to_period();
}
