#!/usr/bin/env bash
# regen-from-catalog.sh — regenerate JMEOS (the JVM FFI binding) from the MEOS catalog and
# build the jar the JVM consumers (Spark/Flink/Kafka) bind (per codegen/GENERATION.md).
#
# Usage:  tools/regen-from-catalog.sh
#   env:  CATALOG = path to meos-idl.json produced by MEOS-API run.py over MobilityDB master
#                   (required)
#         LIBMEOS = path to the all-families libmeos.so built from master (for tests; optional)
#
# Invoked standalone, or by MEOS-API tools/ecosystem-generate.sh (phase 1, before the JVM
# consumers, which regenerate their own projections from the same catalog).
set -euo pipefail
CATALOG="${CATALOG:?set CATALOG to the meos-idl.json from MEOS-API run.py over MobilityDB master}"
HERE="$(cd "$(dirname "$0")/.." && pwd)"

# 1. vendor the catalog (codegen/input/meos-idl.json is the generator's committed input)
cp "$CATALOG" "$HERE/codegen/input/meos-idl.json"

# 2. run FunctionsGenerator with EXPLICIT in/out paths (the default base doubles to codegen/codegen/)
( cd "$HERE" && mvn -q -pl codegen -am compile \
    && mvn -q -pl codegen exec:java -Dexec.mainClass=FunctionsGenerator \
         -Dexec.args="codegen/input/meos-idl.json jmeos-core/src/main/java/functions/GeneratedFunctions.java" )

# 3. build the jar the JVM consumers bind
if [ -n "${LIBMEOS:-}" ]; then cp "$LIBMEOS" "$HERE/jmeos-core/src/libmeos.so" 2>/dev/null || true; fi
( cd "$HERE" && mvn -q -pl jmeos-core -am -DskipTests package )
echo "[jmeos] regenerated from catalog -> $HERE/jar/JMEOS.jar"
