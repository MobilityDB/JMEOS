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

def scan_impls(root):
    impls = {}      # sparkName -> dict
    fields = {}     # field -> (arity, argTypes, bodyMeosFns)
    for f in glob.glob(os.path.join(root, 'src', 'main', 'java', '**', '*UDFs.java'), recursive=True):
        text = open(f, encoding='utf-8', errors='ignore').read()
        cls = os.path.basename(f)[:-5]
        # field declarations + the body that follows (until the next "public static" or EOF)
        decls = list(FIELD_RE.finditer(text))
        for i, m in enumerate(decls):
            arity = int(m.group(1))
            targs = [t.strip() for t in m.group(2).split(',')]
            field = m.group(3)
            body = text[m.end(): decls[i + 1].start() if i + 1 < len(decls) else len(text)]
            meos = list(dict.fromkeys(GENFN_RE.findall(body)))   # ordered-unique
            fields[field] = {'class': cls, 'arity': arity,
                             'argTypes': targs[:-1], 'retType': targs[-1], 'meosFns': meos}
        for m in REG_RE.finditer(text):
            sparkName, field, ret = m.group(1), m.group(2), m.group(3)
            fd = fields.get(field)
            if fd:
                impls[sparkName] = {'field': field, 'class': fd['class'], 'retDataType': ret,
                                    'arity': fd['arity'], 'argTypes': fd['argTypes'],
                                    'retType': fd['retType'], 'meosFns': fd['meosFns']}
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
        for mf in d['meosFns']:
            meos2spark.setdefault(mf, []).append(sn)
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
