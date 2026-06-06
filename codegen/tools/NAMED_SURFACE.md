# Canonical named-operation surface (codegen layer above the C IDL)

`meos-idl.json` describes MEOS at the **C-FFI level** (`temporal_as_mfjson`, raw C
params). Bindings, however, mirror MobilityDB's **named** surface — `asMFJSON`,
with overloads and per-argument defaults — and need to know which MEOS C function
each named function calls. `extract_named_surface.py` produces that layer,
`meos-named-surface.json`, from two canonical sources already in the tree:

- **SQL catalog** (`mobilitydb/sql/**/*.in.sql`) — `CREATE FUNCTION` gives every
  named function, its overloads, argument types, and which arguments have
  `DEFAULT`s (so the valid call arities are known: `asMFJSON` → minArity 1,
  maxArity 4).
- **doxygen chain** — `@sqlfn name()` on the PG C wrapper and `@csqlfn #PgFunc()`
  on the MEOS C function link `SQL name → PG C function → MEOS C function`
  (`asMFJSON → Temporal_as_mfjson → temporal_as_mfjson`).

Run: `extract_named_surface.py <mobilitydb-root>` → `../input/meos-named-surface.json`.

## Why (North Star)

Every binding's named surface — and the Spark Connect registrar that injects it —
is then a deterministic emission from this one spec, never a hand-maintained
function list: the camelCase Spark idiom is a mechanical remap of the canonical
name, the UDF body calls the linked MEOS C function, and the overloads/defaults
fix the call-arity exactly (no runtime null-padding heuristic). A new function in
the SQL catalog flows into every binding with no per-binding edit.

## A2 — emitters that consume this spec (next)

- **MobilitySpark UDFs + Connect registrar:** for each function, remap the name to
  the Spark idiom, emit a UDF over the linked MEOS C function for each overload
  arity, and emit the matching `SparkSessionExtensions.injectFunction` entry.
- Same spec feeds the PG/DuckDB identity dialects, PyMEOS, and the Flink/Kafka
  facades.

## Refinements tracked

- Group each overload with its specific C function by first-argument type (the `c`
  list is currently per-name; type-dispatched families like `speed` carry several).
- Parse `@sqlop` (760 operator tags) for the operator surface.
- Raise C-linkage coverage past the current 434/1284 (alias and multi-`@csqlfn`
  resolution).
