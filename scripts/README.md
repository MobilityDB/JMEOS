# JMEOS regeneration pipeline

These scripts let a maintainer rebuild `src/main/java/functions/functions.java`
from scratch against a MobilityDB checkout. Most JMEOS users never run them;
they consume the pre-built `jar/JMEOS.jar`. Run this when:

- Bumping JMEOS to a newer MEOS release.
- Adding a new public MEOS function and wanting it bound automatically.
- Investigating a wrapper bug surfaced in a downstream consumer
  (MobilitySpark, future Java consumers).

## One-liner

```bash
scripts/regenerate.sh /path/to/MobilityDB
mvn package -Dmaven.test.skip=true
```

That re-derives the bindings end-to-end and writes a fresh `jar/JMEOS.jar`.

## What each script does

### `amalgamate_meos_h.sh`

The JMEOS extractor reads exactly **one** file at
`src/main/java/builder/resources/meos.h`. MEOS 1.4 split its public surface
across many headers, so the script concatenates them into one in this order:

```
postgres_ext_defs.in.h   (typedefs: Datum, TimestampTz, int64, …)
postgres_int_defs.h
meos.h                   (core temporal API)
meos_geo.h               (spatial + tspatial)
meos_cbuffer.h
meos_npoint.h
meos_pose.h
meos_rgeo.h
```

Then it appends extern decls for symbols that are exported from libmeos.so but
live only in private headers the amalgam excludes on purpose
(`meos_internal*.h`, `temporal/temporal.h`, `temporal/meos_catalog.h`):

| Symbol | Why it's appended |
|---|---|
| `acovers_geo_tgeo`, `acovers_tgeo_geo`, `acovers_tgeo_tgeo` | `acovers_*` family lives only in `meos/src/geo/tgeo_spatialrels.c` |
| `mobilitydb_version`, `mobilitydb_full_version` | `temporal/temporal.h` (private) |
| `temporal_mem_size`, `temptype_basetype` | `temporal/temporal.h`, `temporal/meos_catalog.h` |
| `temporal_values_p`, `set_make_free` | `meos_internal.h` — Datum-typed |
| `tnumber_value_split`, `tnumber_value_time_split`, `tnumber_value_time_boxes`, `tbox_get_value_time_tile` | `meos_internal.h` — Datum + MeosType-typed |

The `Datum → long` and `MeosType → int` lowering live in
`builder/FunctionsGenerator.java` (the `equivalentTypes` map).

### `post_regen_patch.py`

Idempotent post-process for the auto-generated `functions.java`. Fixes two
things the generator gets wrong:

1. **`rtree_search` / `rtree_search_temporal`** — the C signature is
   `int foo(in, in, void *query, MeosArray *result)` but the generator's
   bool-out heuristic mis-compiles them. Rewritten to a straight delegation
   that takes a caller-supplied `Pointer result`.

2. **`bool foo(args, T *result)` vs `bool foo(args, T **result)`** —
   the generator emits the same wrapper for both, with a spurious
   `getPointer(0)` indirection. For `T **result` (pointer-out, INDIR, 10
   cases) this is correct. For `T *result` (value-out, DIRECT, 18 cases)
   it turns the value buffer into garbage — a caller's `getDouble(0)`
   reads the buffer's address as IEEE bits and crashes
   `Unsafe_GetDouble` with SIGSEGV.

   The DIRECT/INDIR classification is hand-derived from MEOS C signatures
   (see the `DIRECT = {…}` set near the top of the script). Names that
   match the broken pattern but are not in `DIRECT` are left untouched
   (i.e. correctly INDIR-shaped). The script reports the count of each
   class on every run; if a future MEOS bump adds a new bool-out function,
   it'll show up in the INDIR count — review it and add to `DIRECT` if
   the C signature is `T *result`.

### `regenerate.sh`

End-to-end orchestrator. Invokes, in order:

1. `amalgamate_meos_h.sh <MobilityDB-path>`
2. `mvn compile -q` (so the extractor + generator can run from `target/classes`)
3. `java -cp target/classes builder.FunctionsExtractor`
4. `java -cp target/classes builder.FunctionsGenerator`
5. `python3 scripts/post_regen_patch.py src/main/java/functions/functions.java`

After it finishes, run `mvn package -Dmaven.test.skip=true` to produce the jar.

## Why not wire this into `mvn package` directly

Most JMEOS consumers don't have a MobilityDB source checkout sitting next to
JMEOS — they grab the published artefact and use it. Folding the regen
pipeline into the default Maven lifecycle would force every consumer to
clone MobilityDB, install Python 3, and run a sed-style patcher just to
build a jar that already exists in `jar/JMEOS.jar`. Maintainers who actually
need to regen run `regenerate.sh` explicitly; everyone else's `mvn package`
stays pure-Maven.

## Smoke test

`src/test/java/regen/RegenWrapperSanityTest.java` exercises one DIRECT
(`stbox_xmin → 1.5`) and one INDIR (`ttext_value_n → text_out → "hello"`)
wrapper end-to-end. If the post-regen classifier ever misclassifies a future
MEOS function, one of these two cases fails immediately instead of silently
shipping a wrapper that crashes the JVM at the call site.

The test is skipped by default (matches the existing JMEOS test-suite
convention which keeps surefire skipped because the tests need a runtime
libmeos.so). To run after a regen:

```bash
# Temporarily flip <skipTests>true</skipTests> in pom.xml's surefire config
mvn test -Dtest='regen.RegenWrapperSanityTest'
```

## Adding a new MEOS function — checklist

1. The function exists in a public MEOS header (`meos.h`, `meos_geo.h`, …)
   and is exported from libmeos.so. Verify the second part with
   `nm -D /usr/local/lib/libmeos.so | grep ' T <name>$'`.
   - If the symbol is missing because the C definition uses `inline TYPE`
     (without `static`), the C99 linker may have skipped emission. The
     fix is on the MEOS side: drop `inline`. See MobilityDB PR #939.
2. Run `scripts/regenerate.sh <MobilityDB-path>`.
3. If the new function has a `T *result` out-param wrapper that the
   patcher classified as INDIR, add it to `DIRECT` in
   `post_regen_patch.py` and re-run the patcher.
4. `mvn package -Dmaven.test.skip=true`.
5. Add a smoke-test case to `RegenWrapperSanityTest.java` if the function
   is consumer-critical.
