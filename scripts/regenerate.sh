#!/usr/bin/env bash
# Regenerate JMEOS bindings end-to-end.
#
# Pipeline:
#   1. Build the amalgamated meos.h from a MobilityDB checkout
#      (scripts/amalgamate_meos_h.sh).
#   2. Compile the builder classes so the extractor + generator can run.
#   3. Run FunctionsExtractor (writes target/classes/builder/meos_*.h).
#   4. Run FunctionsGenerator (writes src/main/java/functions/functions.java).
#   5. Apply post-regen patches (scripts/post_regen_patch.py).
#
# After this script finishes, "mvn package -Dmaven.test.skip=true" produces
# a usable jar/JMEOS.jar. The pipeline is intentionally NOT wired into the
# default mvn lifecycle — most users consume JMEOS without regenerating, so
# folding the pipeline into "mvn package" would force every consumer to
# clone MobilityDB and run a perl/python script. Maintainers run this
# manually when bumping the MEOS API surface.
#
# Usage:
#   scripts/regenerate.sh /path/to/MobilityDB

set -euo pipefail

if [[ $# -lt 1 || ! -d "$1/meos/include" ]]; then
    echo "usage: $0 <path-to-MobilityDB-checkout>" >&2
    exit 2
fi

MOBILITYDB="$1"
HERE="$(cd "$(dirname "$0")" && pwd)"
ROOT="$(cd "$HERE/.." && pwd)"

cd "$ROOT"

echo "==> 1/5  Build amalgamated meos.h"
"$HERE/amalgamate_meos_h.sh" "$MOBILITYDB"

echo "==> 2/5  Compile builder classes"
mvn compile -q

echo "==> 3/5  Run FunctionsExtractor"
( cd src/main/java && java -cp "$ROOT/target/classes" builder.FunctionsExtractor )

echo "==> 4/5  Run FunctionsGenerator"
java -cp "$ROOT/target/classes" builder.FunctionsGenerator

echo "==> 5/5  Apply post-regen patches"
python3 "$HERE/post_regen_patch.py" src/main/java/functions/functions.java

echo
echo "JMEOS bindings regenerated. Run 'mvn package -Dmaven.test.skip=true' to build the jar."
