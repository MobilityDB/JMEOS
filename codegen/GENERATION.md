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
`jmeos-core/.../functions/GeneratedFunctions.java`, emitted into `target/generated-sources/`
at `generate-sources` and not committed.

Two surfaces are hand-written, and are the standing work toward the zero-hand North Star above:

- `jmeos-core/src/main/java/functions/functions.java` — a second FFI facade that coexists with
  `GeneratedFunctions`. MobilityFlink calls it directly (`aisdata/Query8_V2_Main.java`,
  `sncbdata/Query8_V2_Main.java`), so it is retired once those call sites move to
  `GeneratedFunctions`.
- the 65 OO type classes under `jmeos-core/src/main/java/types/` — no generator emits them.

Both bind C symbols by name and jnr-ffi resolves a method on its first call, so a MEOS rename
reaches them as an `UnsatisfiedLinkError` at run time rather than a build failure.
`NativeSymbolParityTest` resolves every declared FFI method against the loaded libmeos, which
turns that into a named build failure instead.

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

JMEOS's `codegen/input/meos-idl.json` is produced by MEOS-API `run.py`. It is derived, not
committed, so a clean checkout has no `codegen/input/` directory —
`tools/regen-from-catalog.sh` creates it.

`tools/meos-source-commit.txt` holds the MobilityDB commit this binding is built and tested
against; `.github/workflows/maven.yml` reads it and feeds it to the shared
`MobilityDB/MEOS-API/.github/actions/provision-meos` action, which derives the catalog and
builds libmeos from that one commit. Bumping the surface is an edit to that file.

## Regenerating by hand

CI does the three steps below through `provision-meos`. To run them yourself, on Ubuntu with
a JDK, Maven, CMake and the MEOS build dependencies installed:

**1. Build and install libmeos from the MobilityDB commit you are targeting.** Install into a
private prefix rather than `/usr/local`, so the run is self-contained:

```bash
MDB=~/src/MobilityDB                       # a checkout at the target commit
cmake -S "$MDB" -B "$MDB/build" -DCMAKE_BUILD_TYPE=Release -DMEOS=ON -DALL=ON
cmake --build "$MDB/build" -j"$(nproc)"
cmake --install "$MDB/build" --prefix "$MDB/.prefix"
```

`-DALL=ON` enables every optional family, matching what CI builds. The install writes the
self-contained public headers (`meos.h` spliced from `meos_export.h`) that the parse needs.

**2. Produce the catalog with MEOS-API `run.py`**, pointing it at the *installed* headers and
at the source checkout, which supplies the Doxygen `@ingroup` and `@sqlfn` maps:

```bash
MEOSAPI=~/src/MEOS-API
pip install -r "$MEOSAPI/requirements.txt"          # libclang and friends
cd "$MEOSAPI" && MDB_SRC_ROOT="$MDB" python3 run.py "$MDB/.prefix/include"
# -> $MEOSAPI/output/meos-idl.json
```

**3. Regenerate JMEOS and build the jar the JVM consumers bind:**

```bash
cd ~/src/JMEOS
CATALOG="$MEOSAPI/output/meos-idl.json" LIBMEOS="$MDB/.prefix/lib/libmeos.so" \
  tools/regen-from-catalog.sh
# -> jar/JMEOS.jar
```

With `LIBMEOS` set, the script runs the FFI suite against that library; without it the jar is
built with tests skipped. To run the suite directly instead:

```bash
GITHUB_WORKFLOW=1 LD_LIBRARY_PATH="$MDB/.prefix/lib" mvn clean test
```

`GITHUB_WORKFLOW=1` enables the FFI tests, and `LD_LIBRARY_PATH` is how `libmeos.so` is found.

The JVM consumers (MobilitySpark, MobilityFlink, MobilityKafka) regenerate their own
projections from the same catalog and the jar produced here.
