# MEOS-API → MobilityFlink codegen

`org.mobilitydb.flink.meos.MeosOps*` is a generated, tier-aware Java
facade over the MEOS public API. Two siblings of classes:

- `MeosOps<Class>` — one per MEOS object-model class (50 classes, 751
  methods). Source: `codegen-oo.py`.
- `MeosOpsFree<Header>` — one per public MEOS header for functions
  *not* assigned to any OO class (6 headers, 1,346 methods). Source:
  `codegen-free.py`.

Both generators share the same per-method discipline: read JMEOS' actual
exported surface via `javap -p functions.GeneratedFunctions`,
inner-join with the streaming-relevance baseline v4 (tier
classification of every MEOS public function), emit one static method
per matched function whose body forwards to the JMEOS native binding
after probing the shared `MeosOpsRuntime.MEOS_AVAILABLE` flag.

## What is generated

For every MEOS public function `f` that

1. lives in one of the five public-API streaming tiers
   (`stateless`, `bounded-state`, `windowed`, `cross-stream`,
   `io-meta`), and
2. is exposed by JMEOS as a static method on
   `functions.GeneratedFunctions`,

the generator emits `MeosOps<Class>.f(...)` or
`MeosOpsFree<Header>.f(...)` whose body delegates to
`GeneratedFunctions.f(...)` after probing
`MeosOpsRuntime.MEOS_AVAILABLE`. Each method carries a Javadoc tier
marker so consumers know the per-method wiring shape (scalar UDF for
`stateless`/`bounded-state`, windowed aggregate for `windowed`, etc.).

## Why generated rather than hand-written

- **One source of truth.** Method signatures, classification, and tier
  semantics come from the MEOS-API catalog + the streaming-relevance
  baseline; no hand-written facade can drift from MEOS' actual surface.
- **Audit by regeneration.** Reviewing the diff is reviewing the
  catalog change; each generator is ~250 lines, the runtime is ~30,
  the rest is mechanical.
- **Bump-safe.** When the JMEOS jar regenerates against a new MEOS
  release, re-running the generators absorbs the new surface with zero
  hand edits.

## How to regenerate

```bash
# 1. Regenerate the MEOS-API catalog from MobilityDB headers
git clone --branch feat/object-model https://github.com/MobilityDB/MEOS-API.git
cd MEOS-API && pip install -r requirements.txt
python run.py /path/to/MobilityDB/meos/include /path/to/MobilityDB/mobilitydb/src
# → output/meos-idl.json

# 2. Produce the streaming-relevance baseline (v4 classifier)
# → streaming-relevance-baseline.json

# 3. Extract JMEOS PR #19 method signatures
jar xf flink-processor/jar/JMEOS.jar functions/GeneratedFunctions.class
javap -p functions.GeneratedFunctions > jmeos_signatures.txt

# 4. Run both generators
python flink-processor/tools/codegen/codegen-oo.py     # → MeosOps<Class>.java
python flink-processor/tools/codegen/codegen-free.py   # → MeosOpsFree<Header>.java
```

## Manifests

`meos-ops-manifest.json` (OO-classified) and
`meos-ops-free-manifest.json` (free fns) record the regeneration
provenance: total JMEOS method count, baseline target count, emit
count, per-tier breakdown, per-class/per-header method count, sample
of functions absent from JMEOS.

## Tier vocabulary

| Tier | Meaning | Flink wiring shape |
|---|---|---|
| `stateless` | Pure per-event, no state | `ScalarFunction` (Table API) or direct call in `MapFunction` |
| `bounded-state` | Per-event with bounded per-key state | `ScalarFunction` (state lives in the MEOS handle) |
| `windowed` | Output cardinality changes; needs a window | `AggregateFunction` over `TUMBLE`/`HOP` |
| `cross-stream` | Pairwise across streams; needs interval-overlap join | `CoProcessFunction` or `IntervalJoin` |
| `io-meta` | I/O, catalog, lifecycle helpers | Helper / `format` clause |

The current emission excludes `sequence-only` (functions requiring the
full sequence offline; ~14 fns) and `ambiguous` corner cases (~59 fns
where the mechanical classifier couldn't decide). Both are surfaced
separately for design decisions before emit.

## Coexistence with `MEOSBridge.java`

`berlinmod.MEOSBridge` is the hand-written, BerlinMOD-scoped bridge
introduced on `feat/jmeos-bridge-swap`. The generated `MeosOps*`
facades are the catalog-driven equivalent for the *whole* MEOS
surface; they coexist without redesigning `MEOSBridge`:

- `MEOSBridge` keeps the per-BerlinMOD-query intent (Haversine
  fallback, `dwithinSegmentMetres`, etc.) — high-level, query-shaped.
- `MeosOps*` exposes the raw MEOS surface tier-by-tier — low-level,
  catalog-shaped.

Both share the same `MEOS_AVAILABLE` discipline (via
`MeosOpsRuntime`) and the same `functions.GeneratedFunctions`
delegation.
