#!/usr/bin/env python3
"""Refuse a generated Spark or Flink surface registering a name the engine owns.

Spark and Flink define or reserve names PostgreSQL gives MobilityDB functions (round, lower,
length, hash, unnest, ...), and the catalog states on each such SQL signature the name these
engines publish it under, its altSqlName (MEOS-API #173). A PostgreSQL name every signature of
which carries an altSqlName is one no Spark or Flink surface may register: on Spark it replaces
the engine's own function in the session, on Flink it is shadowed by the built-in or refused by
the parser. A name some signature states without one (scale over the number and time extents,
beside geoScale over the geometries) stays registrable.

The registrations are read from the Java the generators write, in the three forms
codegen_jvm.py emits: `udf().register("<name>"` on the UDF surface, `register(spark,
"<name>"` on the typed Spark SQL surface and `createTemporaryFunction("<name>"` on the Flink SQL
surface. A finding fails the run and is listed, as the ledger of unreached functions in #main
of codegen_spark_udfs.py fails the Spark gaps step on a function it does not list.

Usage: check_owned_names.py <catalog> <generated-dir> [<generated-dir> ...]
"""
import json
import re
import sys
from pathlib import Path

_REGISTER = re.compile(r'(?:udf\(\)\.register\(|register\(spark, |create\w*Function\()"(\w+)"')


def owned_names(catalog: dict) -> set[str]:
    """The PostgreSQL names every signature of which carries an altSqlName, lowercased, since
    both engines match names regardless of case."""
    with_alt, without = set(), set()
    for f in catalog.get("functions", []):
        for s in f.get("sqlSignatures") or ():
            name = (s.get("sqlName") or f.get("sqlfn") or "").lower()
            (with_alt if s.get("altSqlName") else without).add(name)
    return with_alt - without


def registered(root: Path) -> set[str]:
    """Every name the Java under `root` registers."""
    names = set()
    for java in root.rglob("*.java"):
        names.update(_REGISTER.findall(java.read_text(errors="ignore")))
    return names


def main(argv: list[str]) -> int:
    catalog = json.loads(Path(argv[1]).read_text())
    owned = owned_names(catalog)
    bad = 0
    for d in argv[2:]:
        names = registered(Path(d))
        hits = sorted(n for n in names if n.lower() in owned)
        print(f"{d}: {len(names)} names registered, {len(hits)} the engine owns")
        for n in hits:
            print(f"  ERROR: registers {n}, whose signatures all carry an altSqlName")
        bad += len(hits)
    return 1 if bad else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))
