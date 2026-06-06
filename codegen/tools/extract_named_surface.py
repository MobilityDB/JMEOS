#!/usr/bin/env python3
"""Extract the canonical named-operation surface of MobilityDB.

The surface that bindings (MobilitySpark UDFs, the Spark Connect registrar,
PyMEOS, ...) must mirror is the MobilityDB SQL function catalog: the named
functions with their overloads and per-argument defaults. The SQL <-> C linkage
is already canonical in the source as a doxygen tag chain:

    SQL name  <-(@sqlfn name())-  PG C function  <-(@csqlfn #PgFunc())-  MEOS C function

This tool reads the MobilityDB tree and emits one spec, meos-named-surface.json,
that the codegen consumes to generate each binding's named surface and its
Spark Connect registrar from a single source -- no hand-maintained function list.

Usage:
    extract_named_surface.py <mobilitydb-root> [out.json]
"""
import json, re, sys, os, glob

# ---- 1. SQL catalog: names, overloads, per-arg type + default ---------------
CREATE_RE = re.compile(r'CREATE\s+(?:OR\s+REPLACE\s+)?FUNCTION\s+(\w+)\s*\((.*?)\)\s*RETURNS\s+([A-Za-z0-9_\[\]]+)',
                       re.IGNORECASE | re.DOTALL)

def split_top(args):
    """Split an arg list on top-level commas (ignoring commas inside parens)."""
    out, depth, cur = [], 0, ''
    for ch in args:
        if ch == '(': depth += 1; cur += ch
        elif ch == ')': depth -= 1; cur += ch
        elif ch == ',' and depth == 0: out.append(cur); cur = ''
        else: cur += ch
    if cur.strip(): out.append(cur)
    return [a.strip() for a in out if a.strip()]

def parse_arg(a):
    """One arg -> (type, has_default, default). Forms: 'type' | 'name type' |
    '[name] type DEFAULT v'. The default literal (after DEFAULT) lets the registrar
    fill an omitted optional argument with the canonical value, not a null pad."""
    parts = re.split(r'\bDEFAULT\b', a, flags=re.IGNORECASE)
    has_def = len(parts) > 1
    head = parts[0].strip()
    toks = head.split()
    typ = toks[-1] if toks else head           # type is the last token before DEFAULT
    default = parts[1].strip().rstrip(',') if has_def else None
    return typ, has_def, default

def parse_sql(root):
    funcs = {}
    for f in glob.glob(os.path.join(root, 'mobilitydb', 'sql', '**', '*.in.sql'), recursive=True):
        text = open(f, encoding='utf-8', errors='ignore').read()
        for m in CREATE_RE.finditer(text):
            name, arglist, ret = m.group(1), m.group(2), m.group(3)
            args = [dict(zip(('type', 'hasDefault', 'default'), parse_arg(a))) for a in split_top(arglist)]
            req = sum(1 for a in args if not a['hasDefault'])
            funcs.setdefault(name, []).append(
                {'args': args, 'returns': ret, 'minArity': req, 'maxArity': len(args)})
    return funcs

# ---- 2. doxygen chain: SQL name <- PG C func <- MEOS C func ------------------
SQLFN_RE  = re.compile(r'@sqlfn\s+(\w+)\s*\(')          # on the PG C wrapper comment
CSQLFN_RE = re.compile(r'@csqlfn\s+#(\w+)\s*\(')        # on the MEOS C function comment
PGDEF_RE  = re.compile(r'^(\w+)\s*\(PG_FUNCTION_ARGS\)', re.MULTILINE)
CDEF_RE   = re.compile(r'^(\w+)\s*\(', re.MULTILINE)    # a C function definition name at col 0

def _next_name(text, pos, pat):
    m = pat.search(text, pos)
    return m.group(1) if m else None

def parse_doxygen(root):
    pg2sql, meos2pg = {}, {}
    for sub, sqlmap, defpat, tagpat, key in (
            ('mobilitydb', pg2sql, PGDEF_RE, SQLFN_RE, 'sql'),
            ('meos', meos2pg, CDEF_RE, CSQLFN_RE, 'csql')):
        for f in glob.glob(os.path.join(root, sub, 'src', '**', '*.c'), recursive=True):
            text = open(f, encoding='utf-8', errors='ignore').read()
            for m in tagpat.finditer(text):
                target = m.group(1)                    # sql name, or referenced PG func
                cfunc = _next_name(text, m.end(), defpat)  # the C function this comment documents
                if cfunc:
                    sqlmap[cfunc] = target
    return pg2sql, meos2pg

def main():
    root = sys.argv[1] if len(sys.argv) > 1 else '.'
    out = sys.argv[2] if len(sys.argv) > 2 else os.path.join(
        os.path.dirname(__file__), '..', 'input', 'meos-named-surface.json')
    sqlfuncs = parse_sql(root)
    pg2sql, meos2pg = parse_doxygen(root)
    sql2meos = {}
    for meos, pg in meos2pg.items():
        sqlname = pg2sql.get(pg)
        if sqlname:
            sql2meos.setdefault(sqlname, []).append({'pg': pg, 'meos': meos})
    surface = []
    for name in sorted(sqlfuncs):
        surface.append({'name': name, 'overloads': sqlfuncs[name], 'c': sql2meos.get(name, [])})
    spec = {'source': 'MobilityDB SQL catalog + doxygen @sqlfn/@csqlfn chain',
            'functionCount': len(surface), 'functions': surface}
    os.makedirs(os.path.dirname(out), exist_ok=True)
    json.dump(spec, open(out, 'w'), indent=1)
    linked = sum(1 for s in surface if s['c'])
    print(f'named functions: {len(surface)}  with C linkage: {linked}  -> {out}')

if __name__ == '__main__':
    main()
