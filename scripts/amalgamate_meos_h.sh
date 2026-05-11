#!/usr/bin/env bash
# Build the single-file MEOS header that the JMEOS FunctionsExtractor
# expects at src/main/java/builder/resources/meos.h.
#
# JMEOS's extractor reads exactly one file. MEOS 1.4 split its public
# surface across many headers, so we concatenate them into one. We also
# append a few extern declarations for symbols that MEOS exports from
# libmeos.so but does not declare in any public header (or declares only
# in private headers that we deliberately do not include because their
# Datum / MeosType density would balloon the binding surface and pull in
# half-stable internals).
#
# Usage:
#   scripts/amalgamate_meos_h.sh /path/to/MobilityDB
# (writes src/main/java/builder/resources/meos.h)

set -euo pipefail

if [[ $# -lt 1 || ! -d "$1/meos/include" ]]; then
    echo "usage: $0 <path-to-MobilityDB-checkout>" >&2
    echo "  expected: <path>/meos/include/meos.h to exist" >&2
    exit 2
fi

MEOS_INCLUDE="$1/meos/include"
OUT="$(dirname "$0")/../src/main/java/builder/resources/meos.h"
mkdir -p "$(dirname "$OUT")"

# Order matters: postgres_ext_defs.in.h carries the typedefs (Datum,
# TimestampTz, int64, …) that the rest reference; the per-type headers
# follow, and meos.h itself ends up after the postgres preamble so the
# public surface is parseable.
cat \
    "$MEOS_INCLUDE/postgres_ext_defs.in.h" \
    "$MEOS_INCLUDE/postgres_int_defs.h" \
    "$MEOS_INCLUDE/meos.h" \
    "$MEOS_INCLUDE/meos_geo.h" \
    "$MEOS_INCLUDE/meos_cbuffer.h" \
    "$MEOS_INCLUDE/meos_npoint.h" \
    "$MEOS_INCLUDE/meos_pose.h" \
    "$MEOS_INCLUDE/meos_rgeo.h" \
    > "$OUT"

# Appended decls — these symbols are exported from libmeos.so but live
# either with no prototype at all (acovers_tgeo_*) or in MEOS private
# headers we exclude on purpose (meos_internal*.h, temporal/temporal.h,
# temporal/meos_catalog.h). Without these lines the JMEOS regen would
# omit them and downstream consumers would have to re-bind via JNR-FFI.
cat >> "$OUT" <<'EOF'
extern int acovers_geo_tgeo(const GSERIALIZED *gs, const Temporal *temp);
extern int acovers_tgeo_geo(const Temporal *temp, const GSERIALIZED *gs);
extern int acovers_tgeo_tgeo(const Temporal *temp1, const Temporal *temp2);
extern char *mobilitydb_version(void);
extern char *mobilitydb_full_version(void);
extern int temporal_mem_size(const Temporal *temp);
extern MeosType temptype_basetype(MeosType type);
extern Datum *temporal_values_p(const Temporal *temp, int *count);
extern Set *set_make_free(Datum *values, int count, MeosType basetype, bool order);
extern Temporal **tnumber_value_split(const Temporal *temp, Datum vsize, Datum vorigin, Datum **bins, int *count);
extern Temporal **tnumber_value_time_split(const Temporal *temp, Datum size, const Interval *duration, Datum vorigin, TimestampTz torigin, Datum **value_bins, TimestampTz **time_bins, int *count);
extern TBox *tnumber_value_time_boxes(const Temporal *temp, Datum vsize, const Interval *duration, Datum vorigin, TimestampTz torigin, int *count);
extern TBox *tbox_get_value_time_tile(Datum value, TimestampTz t, Datum vsize, const Interval *duration, Datum vorigin, TimestampTz torigin, MeosType basetype, MeosType spantype);
EOF

extern_count=$(grep -c '^extern' "$OUT")
echo "wrote $OUT ($(wc -l < "$OUT") lines, ${extern_count} extern decls)"
