package types.collections.time;

import types.collections.base.Base;
import types.collections.base.Collection;

import java.time.OffsetDateTime;

public interface TimeCollection extends Collection<DateTime> {

    default boolean is_before(Object other) throws Exception {
        return this.is_left((Base)other);
    }

    default boolean is_over_or_before(Object other) throws Exception {
        return this.is_over_or_left((Base)other);
    }

    default boolean is_over_or_after(Object other) throws Exception {
        return this.is_over_or_right((Base)other);
    }

    default boolean is_after(Object other) throws Exception {
        return this.is_right((Base)other);
    }

}