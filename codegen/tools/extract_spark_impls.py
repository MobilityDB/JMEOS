#!/usr/bin/env python3
"""Derive the canonical-function -> MobilitySpark-impl mapping (no hand list).

The MobilitySpark UDFs are the Spark implementations. Each is registered as
`register("<sparkName>", <field>, DataTypes.<RET>)` and its lambda body calls one
or more `GeneratedFunctions.<meos_fn>` (the MEOS C function it implements). This
tool scans the MobilitySpark sources to produce, per registered UDF:
    sparkName, holderClass, field, retType, arity, argTypes, meosFns[]

Joined against meos-named-surface.json (canonical SQL name -> MEOS C function, via
the doxygen chain), it yields canonical SQL name -> Spark impl — the mapping the
Connect-registrar emitter needs, derived rather than hand-maintained. It also
shows that the camelCase remaps (asMFJSON->temporalAsMfjson, Xmin->stboxXmin, ...)
are mechanically recoverable, so they need not be a hand-written dialect table.

Usage:
    extract_spark_impls.py <mobilityspark-root> [named-surface.json]
"""
import json, re, sys, os, glob

REG_RE = re.compile(r'register\(\s*"([^"]+)"\s*,\s*(\w+)\s*,\s*DataTypes\.(\w+)\s*\)')
# a public static UDFn field declaration: capture name + generic type args
FIELD_RE = re.compile(r'public\s+static\s+final\s+UDF(\d)<([^>]*)>\s+(\w+)\s*=', re.MULTILINE)
GENFN_RE = re.compile(r'GeneratedFunctions\.(\w+)\s*\(')
# I/O helpers a UDF body calls for marshaling, not the semantic operation
HELPER_RE = re.compile(r'(_from_hexwkb|_from_binary|_from_wkb|_from_hexewkb|_in|_out|'
                       r'_as_hexwkb|_as_wkb|_as_text|_from_mfjson|_from_text|geo_from_text|'
                       r'pg_timestamptz_in|free)$')

def primary_meos(meos):
    """The semantic MEOS function of a UDF body: the last non-marshaling call
    (the result-producing op, after any input parse / bbox-prefilter helpers)."""
    for m in reversed(meos):
        if not HELPER_RE.search(m):
            return m
    return meos[-1] if meos else None

def scan_impls(root):
    impls = {}      # sparkName -> dict
    fields = {}     # field -> (arity, argTypes, bodyMeosFns)
    for f in glob.glob(os.path.join(root, 'src', 'main', 'java', '**', '*UDFs.java'), recursive=True):
        text = open(f, encoding='utf-8', errors='ignore').read()
        cls = os.path.basename(f)[:-5]
        pkgm = re.search(r'^\s*package\s+([\w.]+)\s*;', text, re.MULTILINE)
        pkg = pkgm.group(1) if pkgm else ''
        # field declarations + the body that follows (until the next "public static" or EOF)
        decls = list(FIELD_RE.finditer(text))
        for i, m in enumerate(decls):
            arity = int(m.group(1))
            targs = [t.strip() for t in m.group(2).split(',')]
            field = m.group(3)
            body = text[m.end(): decls[i + 1].start() if i + 1 < len(decls) else len(text)]
            meos = list(dict.fromkeys(GENFN_RE.findall(body)))   # ordered-unique
            fields[field] = {'class': cls, 'pkg': pkg, 'arity': arity,
                             'argTypes': targs[:-1], 'retType': targs[-1], 'meosFns': meos,
                             'primaryMeos': primary_meos(meos)}
        for m in REG_RE.finditer(text):
            sparkName, field, ret = m.group(1), m.group(2), m.group(3)
            fd = fields.get(field)
            if fd:
                impls[sparkName] = {'field': field, 'class': fd['class'], 'pkg': fd['pkg'],
                                    'retDataType': ret, 'arity': fd['arity'], 'argTypes': fd['argTypes'],
                                    'retType': fd['retType'], 'meosFns': fd['meosFns'],
                                    'primaryMeos': fd['primaryMeos']}
    return impls

def main():
    root = sys.argv[1] if len(sys.argv) > 1 else '.'
    spec_path = sys.argv[2] if len(sys.argv) > 2 else os.path.join(
        os.path.dirname(__file__), '..', 'input', 'meos-named-surface.json')
    impls = scan_impls(root)
    out = os.path.join(os.path.dirname(__file__), '..', 'input', 'spark-impls.json')
    json.dump({'implCount': len(impls), 'impls': impls}, open(out, 'w'), indent=1)

    # join with the canonical named surface to recover canonical -> spark impl
    surface = json.load(open(spec_path))['functions']
    # MEOS C fn -> spark impl(s)
    meos2spark = {}
    for sn, d in impls.items():
        if d.get('primaryMeos'):
            meos2spark.setdefault(d['primaryMeos'], []).append(sn)
    derived = {}
    for fn in surface:
        for c in fn['c']:
            for sn in meos2spark.get(c['meos'], []):
                derived.setdefault(fn['name'], set()).add(sn)
    print(f"spark impls: {len(impls)}  -> {out}")
    print("derived canonical -> spark impl (the remap, recovered mechanically):")
    for canon in ('asMFJSON', 'Xmin', 'Tmin', 'azimuth', 'speed', 'atTime', 'sequenceN', 'numSequences'):
        got = sorted(derived.get(canon, []))
        print(f"  {canon:14} -> {got}")

if __name__ == '__main__':
    main()
