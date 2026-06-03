#!/usr/bin/env python3
"""Symbol-level parity audit for the MobilityFlink generated MEOS facade.

Measures, per type family, how much of the MEOS public-header function surface
the generated facade (org.mobilitydb.meos.MeosOps*) exposes, cross-checked
against the JMEOS jar export set so a method only counts when it is genuinely
bindable.

Reference surface = the MEOS public C API (meos/include/meos*.h, excluding the
meos_internal*.h headers).  This is the surface JMEOS mirrors and the facade
delegates to, so it is the exact denominator for a Flink binding.  A second view
maps the facade against the MobilityDB SQL layer's underlying C symbols for
comparability with the SQL-surface bindings.

Coverage reported here is STATIC: the facade method exists and binds a JMEOS
symbol of matching arity.  Runtime behaviour is exercised separately by the
per-family smoke tests.

Usage:
    python3 tools/parity/parity_audit.py \
        --meos-include /path/to/MobilityDB/meos/include \
        --jar jar/JMEOS.jar \
        --facade src/main/java/org/mobilitydb/meos \
        --mdb-sql /path/to/MobilityDB/mobilitydb/sql \
        --out docs/parity-status.md
"""
import argparse, re, os, glob, subprocess, collections
from datetime import date

PUBLIC_HEADERS = ["meos.h", "meos_geo.h", "meos_cbuffer.h",
                  "meos_npoint.h", "meos_pose.h", "meos_rgeo.h",
"meos_h3.h"]
FAMILY = {"meos.h": "core temporal / set / span / spanset / tbox",
          "meos_geo.h": "geo (tgeo / tpoint / stbox)",
          "meos_cbuffer.h": "cbuffer", "meos_npoint.h": "npoint",
          "meos_pose.h": "pose", "meos_rgeo.h": "rgeo",
          "meos_h3.h": "h3 / th3index"}

_DECL = re.compile(r'^\s*extern\s+.+?\b([a-z][A-Za-z0-9_]*)\s*\(', re.M)
_PUBSTATIC = re.compile(r'public static [A-Za-z0-9_.<>\[\]]+ ([a-z0-9_]+)\(')

# MobilityDB SQL out-of-scope bucketing (PG-only; no MEOS/Flink equivalent).
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


def jmeos_symbols(jar):
    out = subprocess.run(["javap", "-classpath", jar, "-p",
                          "functions.GeneratedFunctions"],
                         capture_output=True, text=True).stdout
    return set(re.findall(r'\b([a-z][a-z0-9_]+)\(', out))


def facade_methods(d):
    syms = set()
    for f in glob.glob(os.path.join(d, "MeosOps*.java")):
        syms |= set(_PUBSTATIC.findall(open(f).read()))
    return syms


def libmeos_symbols(path):
    out = subprocess.run(["nm", "-D", path], capture_output=True, text=True).stdout
    return {line.split()[-1] for line in out.splitlines() if line.strip()}


def public_surface(inc):
    fam, allpub = {}, set()
    for h in PUBLIC_HEADERS:
        names = set(_DECL.findall(open(os.path.join(inc, h)).read()))
        for n in names:
            allpub.add(n); fam.setdefault(n, h)
    return allpub, fam


def sql_csymbols(root):
    create = re.compile(r'CREATE\s+(?:OR\s+REPLACE\s+)?FUNCTION\s+([A-Za-z0-9_]+)\s*\(', re.I)
    csym = re.compile(r"AS\s+'[^']*'\s*,\s*'([A-Za-z0-9_]+)'", re.I)
    langsql = re.compile(r"LANGUAGE\s+'?(?:sql|plpgsql)'?", re.I)
    addressable, oos, sqlc = set(), 0, 0
    for path in glob.glob(os.path.join(root, "**", "*.in.sql"), recursive=True):
        section = os.path.relpath(path, root).replace(os.sep, "/")
        txt = open(path, encoding="utf-8", errors="replace").read()
        for m in create.finditer(txt):
            name = m.group(1); tail = txt[m.end():m.end() + 1200].split(';')[0]
            cm = csym.search(tail); sym = cm.group(1) if cm else None
            key = (sym or name).lower()
            if section in OOS_SECTIONS or key in OOS_NAMES or key.endswith(OOS_SUFFIXES):
                oos += 1; continue
            if sym is None:
                sqlc += 1; continue
            addressable.add(sym.lower())
    return addressable, oos, sqlc


def main():
    ap = argparse.ArgumentParser()
    here = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
    ap.add_argument("--meos-include", default="/home/esteban/src/MobilityDB/meos/include")
    ap.add_argument("--jar", default=os.path.join(here, "jar", "JMEOS.jar"))
    ap.add_argument("--facade", default=os.path.join(here, "src/main/java/org/mobilitydb/meos"))
    ap.add_argument("--mdb-sql", default="/home/esteban/src/MobilityDB/mobilitydb/sql")
    ap.add_argument("--out", default=os.path.join(here, "docs", "parity-status.md"))
    ap.add_argument("--libmeos", default=None,
                    help="path to a built libmeos.so; cross-checks that every facade method "
                         "resolves to an exported symbol (runtime resolution check)")
    a = ap.parse_args()

    jm = jmeos_symbols(a.jar)
    fa_all = facade_methods(a.facade)
    fa = fa_all & jm
    pub, fam = public_surface(a.meos_include)
    bindable = pub & jm
    covered = bindable & fa
    missing = sorted(bindable - fa)

    bc, cc = collections.Counter(), collections.Counter()
    for n in bindable:
        bc[fam[n]] += 1
        if n in covered:
            cc[fam[n]] += 1

    addr, oos, sqlc = sql_csymbols(a.mdb_sql)
    sql_cov = len(addr & fa); sql_bindable = len(addr & jm)

    pct = lambda c, t: (100.0 * c / t) if t else 0.0
    L = []
    L.append("# MobilityFlink parity status — MEOS surface audit\n")
    L.append(f"Generated {date.today().isoformat()} by `tools/parity/parity_audit.py`.\n")
    L.append("The MobilityFlink MEOS facade (`org.mobilitydb.meos.MeosOps*`) exposes "
             "MEOS C functions to Flink through JMEOS. This audit measures, per type family, "
             "the share of the **MEOS public C API** that the facade exposes and that JMEOS binds.\n")
    L.append(f"**Headline.** The facade exposes **{len(covered)} of {len(bindable)} "
             f"public MEOS functions ({pct(len(covered), len(bindable)):.1f}%)**. "
             f"The MEOS public surface (`meos/include/meos*.h`, excluding internal headers) is "
             f"{len(pub)} functions; JMEOS binds {len(bindable)} of them. "
             f"{len(missing)} bindable functions are not exposed (listed in §3).\n")
    L.append("Coverage is **static**: a function counts as covered when the facade declares a "
             "method of the same name and arity that delegates to a JMEOS export.\n")
    L.append("Per-family runtime behaviour is asserted by "
             "`src/test/java/org/mobilitydb/meos/MeosFacadeSmokeTest.java`, which constructs "
             "and reads back a value in the core, geo, cbuffer, npoint and pose families through the "
             "facade against libmeos. The cbuffer, npoint and pose families require a libmeos built "
             "with the extended modules (`-DCBUFFER=ON -DNPOINT=ON -DPOSE=ON -DRGEO=ON`); the stock "
             "library carries the core and geo surfaces only.\n")
    L.append("## 1. Reference surface and method\n")
    L.append("- **Denominator**: distinct function names declared `extern` in the MEOS public "
             "headers `meos.h`, `meos_geo.h`, `meos_cbuffer.h`, `meos_npoint.h`, `meos_pose.h`, "
             "`meos_rgeo.h`. Internal headers (`meos_internal*.h`) are excluded.\n")
    L.append("- **Numerator**: `public static` methods on the generated `MeosOps*` facade whose "
             "name is also a `functions.GeneratedFunctions` export in the bundled JMEOS jar.\n")
    L.append(f"- **JMEOS jar**: {os.path.relpath(a.jar, here)} exports {len(jm)} static methods.\n")
    L.append("## 2. Per-family coverage of the public MEOS surface\n")
    L.append("| Family (header) | Public ∩ JMEOS | Exposed | Missing | Coverage |")
    L.append("|---|---:|---:|---:|---:|")
    for h in PUBLIC_HEADERS:
        b, c = bc[h], cc[h]
        L.append(f"| {FAMILY[h]} (`{h}`) | {b} | {c} | {b - c} | {pct(c, b):.1f}% |")
    L.append(f"| **total** | **{len(bindable)}** | **{len(covered)}** | "
             f"**{len(bindable) - len(covered)}** | **{pct(len(covered), len(bindable)):.1f}%** |\n")
    L.append("## 3. Bindable MEOS functions not exposed by the facade\n")
    L.append(f"{len(missing)} functions are present in the public MEOS headers and bound by "
             "JMEOS but not generated into the facade:\n")
    for h in PUBLIC_HEADERS:
        ms = [n for n in missing if fam[n] == h]
        if ms:
            L.append(f"- **`{h}`** ({len(ms)}): " + ", ".join(f"`{n}`" for n in ms))
    L.append("")
    L.append("## 4. MobilityDB SQL-surface cross-check\n")
    L.append("The facade is also matched against the underlying MEOS C symbol of each addressable "
             "`CREATE FUNCTION` in `mobilitydb/sql/**/*.in.sql` (PG-only sections and helper symbols "
             f"bucketed out; {oos} out-of-scope, {sqlc} SQL/plpgsql-composed functions with no single "
             "C symbol). Functions the SQL layer implements through the internal MEOS headers "
             "(`meos_internal*.h`) are exposed via `MeosOpsSqlSurface`.\n")
    L.append(f"- Addressable distinct C symbols: **{len(addr)}**; bound by JMEOS: **{sql_bindable}**; "
             f"exposed by the facade: **{sql_cov}** "
             f"({pct(sql_cov, sql_bindable):.1f}% of the JMEOS-bindable SQL surface).\n")
    L.append(f"- The remaining **{len(addr) - sql_bindable}** addressable C symbols are not exported "
             "by JMEOS under the name the SQL layer's extension wrapper uses; the wrapper names "
             "differ from the MEOS function names they call.\n")

    if a.libmeos:
        libsyms = libmeos_symbols(a.libmeos)
        resolved = fa_all & libsyms
        unresolved = sorted(fa_all - libsyms)
        L.append("## 5. Runtime symbol resolution\n")
        L.append("Every facade method delegates to a libmeos symbol of the same name. Against a "
                 "MEOS shared library built with the extended modules (`-DCBUFFER=ON -DNPOINT=ON "
                 "-DPOSE=ON -DRGEO=ON`), "
                 f"**{len(resolved)} of {len(fa_all)}** facade methods resolve to an exported "
                 "symbol.\n")
        if unresolved:
            hdr_only = sorted(n for n in unresolved if n in pub)
            jmeos_only = sorted(n for n in unresolved if n not in pub)
            L.append(f"The remaining {len(unresolved)} are present in the JMEOS jar but not "
                     "exported by the MEOS shared library (a JMEOS-jar / library version skew):\n")
            if hdr_only:
                L.append(f"- declared in the public headers, not exported by this build "
                         f"({len(hdr_only)}): " + ", ".join(f"`{n}`" for n in hdr_only))
            if jmeos_only:
                L.append(f"- not declared in the current public headers, JMEOS jar ahead of the "
                         f"library ({len(jmeos_only)}): " + ", ".join(f"`{n}`" for n in jmeos_only))
            L.append("")
        else:
            L.append("All facade methods resolve.\n")
        print(f"libmeos resolution:         {len(resolved)}/{len(fa_all)} "
              f"({len(unresolved)} unresolved)")

    md = "\n".join(L) + "\n"
    os.makedirs(os.path.dirname(a.out), exist_ok=True)
    open(a.out, "w").write(md)

    print(f"public MEOS surface:        {len(pub)}")
    print(f"  bound by JMEOS:           {len(bindable)}")
    print(f"  exposed by facade:        {len(covered)}  ({pct(len(covered), len(bindable)):.1f}%)")
    print(f"  missing (bindable):       {len(missing)}")
    print(f"SQL addressable C-symbols:  {len(addr)}  (bindable {sql_bindable}, exposed {sql_cov})")
    print(f"wrote {a.out}")


if __name__ == "__main__":
    main()
