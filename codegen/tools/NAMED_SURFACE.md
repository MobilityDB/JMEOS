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

## Canonical name -> Spark impl (derived, not hand-mapped)

`extract_spark_impls.py <mobilityspark-root>` scans the MobilitySpark `*UDFs`
classes for each `register("sparkName", field, DataTypes.RET)` and the field's
body `GeneratedFunctions.<meos_fn>` calls, then joins on the named surface's
`SQL -> MEOS C` linkage to recover **canonical name -> Spark impl** mechanically.
This is what proves the camelCase remap (`asMFJSON->temporalAsMfjson`,
`Xmin->stboxXmin`, ...) is derivable and need not be a hand-written dialect table.

The join (1308 Spark impls) classifies each canonical function for the emitter:

- **single impl** (`asMFJSON->temporalAsMfjson`, `azimuth->tpointAzimuth`,
  `sequenceN`, `numSequences`): emit the **canonical (identity) name** bound to
  the one impl; arity adapts by null-padding the impl's optional args.
- **multi impl** (`Xmin->{stboxXmin,tboxXmin,tboxfloatXmin,tboxintXmin}`,
  `Tmin->{stboxTmin,tboxTmin}`, `atTime->{atTime,temporalAtTstzspan,...}`): the
  receiver/arg type is opaque (every MEOS value is a hex String in Spark), so the
  identity-named builder must **dispatch on the WKB type tag** (`meos_typeof_hexwkb`
  of the relevant arg) to the impl whose MEOS function matches that type. This is
  the reason the type-prefixed names exist; the registrar removes the need for them.
- **join gaps** (`speed->[]`): the SQL function's `@csqlfn` chain resolved to a
  type variant (`trgeometry_speed`) different from the MEOS function the Spark UDF
  calls; close by capturing all per-type C variants per SQL name.

## A2 — the emitter (consumes the two derived specs)

- **MobilitySpark Connect registrar:** for each canonical function, emit an
  `injectFunction` under the **identity** name; single-impl -> direct ScalaUDF over
  the impl; multi-impl -> a WKB-type-tag dispatch builder selecting the impl.
  Retires both the hand-written registrar and `dialect_spark.go`'s `sparkNameMap`.
- Same two specs feed the PG/DuckDB identity dialects, PyMEOS, and Flink/Kafka.

## Refinements tracked

- Group each overload with its specific C function by first-argument type (the `c`
  list is currently per-name; type-dispatched families like `speed` carry several).
- Parse `@sqlop` (760 operator tags) for the operator surface.
- Raise C-linkage coverage past the current 434/1284 (alias and multi-`@csqlfn`
  resolution).
