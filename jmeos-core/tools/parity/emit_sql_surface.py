#!/usr/bin/env python3
"""Emit forwarding facade methods for MEOS functions that the MobilityDB SQL
layer exposes as user functions but whose implementation lives in the internal
headers (`meos_internal*.h`). JMEOS binds them; exposing them here makes the
Flink facade match the SQL user surface as well as the public MEOS API.

Each method forwards to its `functions.GeneratedFunctions` export using the
exact JMEOS signature, so the output compiles by construction.

Run from :
    javap -classpath jar/JMEOS.jar -p functions.GeneratedFunctions > /tmp/gen_sigs.txt
    python3 tools/parity/emit_sql_surface.py /tmp/gen_sigs.txt
"""

# Canonical MobilityDB PostgreSQL-License header, emitted on every generated file.
MEOS_LICENSE_BANNER = (
    '/*****************************************************************************\n'
    ' *\n'
    ' * This MobilityDB code is provided under The PostgreSQL License.\n'
    ' * Copyright (c) 2020-2026, Université libre de Bruxelles and MobilityDB\n'
    ' * contributors\n'
    ' *\n'
    ' * Permission to use, copy, modify, and distribute this software and its\n'
    ' * documentation for any purpose, without fee, and without a written\n'
    ' * agreement is hereby granted, provided that the above copyright notice and\n'
    ' * this paragraph and the following two paragraphs appear in all copies.\n'
    ' *\n'
    ' * IN NO EVENT SHALL UNIVERSITE LIBRE DE BRUXELLES BE LIABLE TO ANY PARTY FOR\n'
    ' * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING\n'
    ' * LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,\n'
    ' * EVEN IF UNIVERSITE LIBRE DE BRUXELLES HAS BEEN ADVISED OF THE POSSIBILITY\n'
    ' * OF SUCH DAMAGE.\n'
    ' *\n'
    ' * UNIVERSITE LIBRE DE BRUXELLES SPECIFICALLY DISCLAIMS ANY WARRANTIES,\n'
    ' * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY\n'
    ' * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON\n'
    ' * AN "AS IS" BASIS, AND UNIVERSITE LIBRE DE BRUXELLES HAS NO OBLIGATIONS TO\n'
    ' * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.\n'
    ' *\n'
    ' *****************************************************************************/'
)
import re, os, sys, glob

HERE = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
INC = os.environ.get("MEOS_INCLUDE", "/home/esteban/src/MobilityDB/meos/include")
MDB_SQL = os.environ.get("MDB_SQL", "/home/esteban/src/MobilityDB/mobilitydb/sql")
FACADE = os.path.join(HERE, "src/main/java/org/mobilitydb/meos")
OUT = os.path.join(FACADE, "MeosOpsSqlSurface.java")

_PUBSTATIC = re.compile(r'public static [A-Za-z0-9_.<>\[\]]+ ([a-z0-9_]+)\(')
_SIG = re.compile(r'public static (\S+) (\w+)\(([^)]*)\);')
_CREATE = re.compile(r'CREATE\s+(?:OR\s+REPLACE\s+)?FUNCTION\s+([A-Za-z0-9_]+)\s*\(', re.I)
_CSYM = re.compile(r"AS\s+'[^']*'\s*,\s*'([A-Za-z0-9_]+)'", re.I)

OOS_SECTIONS = {
    "temporal/011_span_indexes.in.sql", "temporal/012_spanset_indexes.in.sql",
    "temporal/013_set_indexes.in.sql", "temporal/019_geo_constructors.in.sql",
    "temporal/043_temporal_gist.in.sql", "temporal/044_temporal_spgist.in.sql",
    "temporal/999_oid_cache.in.sql", "geo/073_tgeo_gist.in.sql",
    "geo/073_tpoint_gist.in.sql", "geo/074_tgeo_spgist.in.sql",
    "geo/074_tpoint_spgist.in.sql", "cbuffer/166_tcbuffer_indexes.in.sql",
    "npoint/092_tnpoint_gin.in.sql", "npoint/098_tnpoint_indexes.in.sql",
    "pose/114_tpose_indexes.in.sql", "rgeo/134_trgeo_indexes.in.sql",
}
OOS_SUFFIXES = ("_in", "_out", "_recv", "_send", "_typmod_in", "_typmod_out",
    "_transfn", "_combinefn", "_finalfn", "_serialize", "_deserialize",
    "_sel", "_joinsel", "_supportfn", "_analyze",
    "_cmp", "_eq", "_ne", "_lt", "_le", "_gt", "_ge", "_hash", "_hash_extended")
OOS_NAMES = {"range", "multirange", "create_trip", "transform_gk"}


def header_names(*headers):
    names = set()
    for h in headers:
        names |= set(re.findall(r'^\s*extern\s+.+?\b([a-z][A-Za-z0-9_]*)\s*\(', open(os.path.join(INC, h)).read(), re.M))
    return names


def sql_addressable():
    addr = set()
    for path in glob.glob(os.path.join(MDB_SQL, "**", "*.in.sql"), recursive=True):
        section = os.path.relpath(path, MDB_SQL).replace(os.sep, "/")
        txt = open(path, encoding="utf-8", errors="replace").read()
        for m in _CREATE.finditer(txt):
            name = m.group(1); tail = txt[m.end():m.end() + 1200].split(';')[0]
            cm = _CSYM.search(tail); sym = cm.group(1) if cm else None
            key = (sym or name).lower()
            if section in OOS_SECTIONS or key in OOS_NAMES or key.endswith(OOS_SUFFIXES):
                continue
            if sym is not None:
                addr.add(key)
    return addr


def main():
    sigfile = sys.argv[1] if len(sys.argv) > 1 else "/tmp/parity/gen_sigs.txt"
    sigs = {}
    for line in open(sigfile):
        m = _SIG.search(line)
        if m:
            sigs.setdefault(m.group(2), []).append((m.group(1), m.group(3).strip()))
    facade = set()
    for f in glob.glob(os.path.join(FACADE, "MeosOps*.java")):
        facade |= set(_PUBSTATIC.findall(open(f).read()))
    pub = header_names("meos.h", "meos_geo.h", "meos_cbuffer.h", "meos_npoint.h", "meos_pose.h", "meos_rgeo.h")
    addr = sql_addressable()
    # SQL-addressable, bound by JMEOS, not in the public-surface facade, not already exposed
    targets = sorted((addr & set(sigs)) - facade - pub)

    L = [MEOS_LICENSE_BANNER, "", "package org.mobilitydb.meos;", "",
         "/**", " * Forwarding facade methods for MEOS functions that the MobilityDB SQL layer",
         " * exposes as user functions but whose implementation lives in the internal headers",
         " * ({@code meos_internal*.h}). JMEOS binds them; they are exposed here so the facade",
         " * matches the SQL user surface as well as the public MEOS API. Each method delegates",
         " * to its {@code functions.GeneratedFunctions} export under the",
         " * {@link MeosOpsRuntime#MEOS_AVAILABLE} guard.",
         " */",
         "public final class MeosOpsSqlSurface {", "",
         "    private MeosOpsSqlSurface() { /* utility */ }", ""]
    count = 0
    for name in targets:
        for ret, args in sigs[name]:
            params = [a.strip() for a in args.split(",")] if args else []
            decl = ", ".join(f"{t} arg{i}" for i, t in enumerate(params))
            call = ", ".join(f"arg{i}" for i in range(len(params)))
            ret_kw = "" if ret == "void" else "return "
            L += [f"    /** MEOS {{@code {name}}} — SQL-surface function (meos_internal). */",
                  f"    public static {ret} {name}({decl}) {{",
                  f"        if (!MeosOpsRuntime.MEOS_AVAILABLE)",
                  f'            throw new UnsupportedOperationException("{name} requires libmeos'
                  f' — set -Dmeos.enabled=true");',
                  f"        {ret_kw}functions.GeneratedFunctions.{name}({call});",
                  "    }", ""]
            count += 1
    L.append("}")
    open(OUT, "w").write("\n".join(L) + "\n")
    print(f"SQL-surface (internal-backed) functions to expose: {len(targets)}")
    print(f"forwarding methods emitted:                        {count}")
    print(f"wrote {OUT}")


if __name__ == "__main__":
    main()
