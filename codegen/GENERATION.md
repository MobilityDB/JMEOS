# JMEOS generation — the canonical per-binding generator policy

JMEOS is a **generated** binding. This document is the contract for how it is generated,
under the ecosystem-wide per-binding generator policy.

## The policy (ecosystem-wide)

Every MobilityDB language binding is a **pure projection of the MEOS-API catalog**, and
**each binding owns its own generator, in its own repo**, in a canonical layout — not a
single central generator-repo. The single source of truth is the **catalog**
(`MEOS-API/output/meos-idl.json`, generated from the MobilityDB `master` MEOS headers),
not a generator location. This mirrors how MEOS itself is built: independent, plug-and-play,
CMake-gated families — a binding is likewise an independent module that owns its generation.

Each binding repo satisfies the same invariants:

1. **In-repo generator**, one clearly-designated location. For JMEOS that is the
   `codegen/` Maven module (`codegen/src/main/java/FunctionsGenerator.java`).
2. **Derived from `master`** — the catalog is regenerated from the current MobilityDB
   `master` surface (`tools/regen-from-catalog.sh`); there is no pin and no per-binding
   compose manifest. The composing work lands on `master`, and the binding tracks it.
3. **Vendored catalog**, read-only, regenerated not hand-edited: `codegen/input/meos-idl.json`.
4. **Thin language projection** — language-neutral decisions (grouping, skip/classify,
   portable names, shape) belong upstream in the catalog, so per-language generators do
   not re-implement and drift.
5. **Full automation (North Star):** generate-then-retire toward a **zero hand-written**
   surface; anything that seems irreducible is either emitted by the generator or fixed at
   source in MEOS (export the symbol) — never hand-patched in the binding.

## JMEOS scope: raw FFI ONLY

JMEOS owns the **raw FFI** projection: `FunctionsGenerator.java` →
`jmeos-core/.../functions/GeneratedFunctions.java` plus the OO type layer. It exposes no
hand-written facade — the FFI surface and the OO type layer are both projected from the
catalog.

The `MeosOps*` **facades** and the **Spark-Connect registrar** are **consumer** projections
— each JVM consumer generates its own, in its own repo and namespace, from the same catalog,
forwarding to JMEOS's `GeneratedFunctions`:

- **MobilityFlink** — `flink-processor/tools/codegen/` emits `org.mobilitydb.flink.meos.MeosOps*`,
  filtered to the streaming-relevant surface.
- **MobilitySpark** — generates the Spark-Connect registrar (`MobilitySparkConnectExtensionsGen`)
  and its facade.
- **MobilityKafka** — generates its Streams operator facade.

A binding that calls the C MEOS API natively (e.g. **MobilityDuck**, C++↔C) needs no FFI
substrate: it projects the catalog straight into its host registration, organized by family.
The JVM consumers share JMEOS as the FFI substrate and each own their host-facing projection.
Keeping the facades and registrar out of JMEOS is what prevents the FFI line and the facade
line from diverging: a hand-maintained shared facade drifts against the surface (renames,
out-param and signature changes), so no facade is hand-maintained anywhere — each is a thin,
regenerated projection of the catalog.

## Generate-then-retire — the green-CI version is the probe

Removing hand-written code happens **little by little, never wipe-first**:

1. build/align the generator to the canonical structure;
2. generate the full surface, build green;
3. **prove generated ⊇ hand** against the **last green-CI version** (the equivalence
   probe) — suite + parity, **family by family**;
4. retire the hand registrations for that family;
5. repeat. The green-CI baseline is what catches a generated gap before it ships.

## Catalog: derived from MobilityDB master

JMEOS's vendored `codegen/input/meos-idl.json` is produced by MEOS-API `run.py` over the
current MobilityDB `master` `meos/include`. `tools/regen-from-catalog.sh <catalog>` vendors
that catalog, runs `FunctionsGenerator`, and builds the jar the JVM consumers bind. The
consumers regenerate their own projections from the same catalog, so the whole JVM tier
tracks `master` with no hand-written surface.
