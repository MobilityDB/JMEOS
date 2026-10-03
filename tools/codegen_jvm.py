#!/usr/bin/env python3
"""Unified MEOS-surface code generator for the MobilityDB JVM bindings.

ONE generator, its engines selected by ``--engine``. Every
JVM binding (MobilitySpark, MobilityFlink, MobilityKafka) vendors this identical
file plus its ``codegen_spark_udfs.py`` sibling, so the generated surface can never
drift between engines — the North Star that MEOS is the single source of truth and
all bindings are GENERATED from it.

  * ``spark``        -> the MobilitySpark SQL-UDF surface. This path delegates
                        VERBATIM to the sibling ``codegen_spark_udfs.py``: the same
                        code runs, so the emitted files are byte-identical to what
                        that generator produces on its own.
  * ``flink|kafka``  -> the thin ``MeosOps*`` Java static-forwarder facades.
                        Covers the FULL jar surface (every
                        functions.GeneratedFunctions symbol), grouped by the
                        MEOS-API catalog object model: one class per object-model
                        class plus one per source header for the free functions,
                        plus the shared MeosOpsRuntime. flink and kafka differ ONLY
                        by the ``-Dmobility<engine>.meos.enabled`` toggle string.
  * ``flink-sql``    -> the Flink SQL surface: one Flink function per MobilityDB
                        SQL name the catalog states, each overload an eval method,
                        and one RAW type per MEOS value type the signatures use,
                        carrying the serialized form the catalog codec writes.
  * ``spark-sql``    -> the typed Spark SQL surface, from the same overloads as
                        flink-sql: one Spark UserDefinedType per MEOS value type over
                        the same serialized form, and one registration per SQL name
                        whose builder chooses the overload and its result type from
                        the argument types while Spark plans the call.

Shared front-end (facade back-end only): load the catalog, list the jar symbols,
and derive each function's object-model class / role / header directly from the
catalog's ``objectModel``. The spark back-end owns its own catalog+jar front-end
(it needs jar arities the facade parse does not), so nothing about it changes.

Usage:
  codegen_jvm.py --engine spark --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine flink --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine kafka --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine flink-sql --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine spark-sql --catalog meos-idl.json --jar JMEOS.jar --out DIR
"""
import argparse
import importlib.util
import json
import re
import subprocess
import sys
from collections import defaultdict
from pathlib import Path


# ───────────────────────── shared front-end ─────────────────────────

def load_catalog(path):
    with open(path) as f:
        return json.load(f)


SIG_RE = re.compile(
    r'^\s*public\s+static\s+(?P<ret>[\w\.<>\[\]]+)\s+(?P<name>\w+)\((?P<args>[^)]*)\)')


def parse_jmeos_signatures(jar):
    """javap functions.GeneratedFunctions -> {name: {ret, arg_types}} (jar SoT).

    The jar is the ground truth of what the bundled JMEOS actually exposes, so the
    facade surface is exactly the jar surface."""
    out = subprocess.run(
        ['javap', '-cp', str(jar), 'functions.GeneratedFunctions'],
        check=True, capture_output=True, text=True).stdout
    jmeos = {}
    for line in out.splitlines():
        m = SIG_RE.match(line.rstrip(';'))
        if m:
            raw = m.group('args').strip()
            jmeos[m.group('name')] = {
                'ret': m.group('ret'),
                'arg_types': [a.strip() for a in raw.split(',')] if raw else [],
            }
    return jmeos


_SEQ_RE = re.compile(r'\bTSequence\b')


def seq_typed(canonical):
    """Sequence-typed return: materializes a whole TSequence / *SeqSet, so the
    function is inherently non-streamable (drives the sequence-only guard). Purely
    catalog-derived from returnType.canonical — no name heuristics."""
    s = canonical or ''
    return bool(_SEQ_RE.search(s)) or 'SeqSet' in s


def object_model_index(cat):
    """Catalog-derived class / role / header / sequence index for the facades.

    Returns (fn_class, fn_role, fn_header, fn_seq):
      fn_class[name]  -> the object-model class name (e.g. 'TGeo', 'FloatSpan')
                         or None if the function is a free/plumbing function.
      fn_role[name]   -> the object-model role (constructor/accessor/... ) or None.
      fn_header[name] -> the source header the catalog attributes the function to.
      fn_seq[name]    -> True iff its return type is sequence-typed.

    class/role come from objectModel.classes[*].methods[*] (each function appears
    under exactly ONE class, agreeing with objectModel.functionToClass). The header
    is the per-function 'file' field. The spark back-end does not use this — it
    groups by doxygen @ingroup."""
    om = cat['objectModel']
    fn_class, fn_role, fn_header, fn_seq = {}, {}, {}, {}
    for cls_name, cls in om['classes'].items():
        for m in cls.get('methods', []):
            fn = m['function']
            fn_class.setdefault(fn, cls_name)
            fn_role.setdefault(fn, m.get('role'))
    for f in cat['functions']:
        n = f['name']
        fn_header[n] = f['file']
        fn_seq[n] = seq_typed(f['returnType'].get('canonical',
                                                   f['returnType'].get('c', '')))
    return fn_class, fn_role, fn_header, fn_seq


# ───────────────────────── spark back-end ─────────────────────────

def _spark_module():
    """The sibling codegen_spark_udfs.py as a module, loaded as #run_spark loaded it. Every
    binding vendors it next to this file, so the import target is always the sibling."""
    spark_path = Path(__file__).resolve().parent / 'codegen_spark_udfs.py'
    spec = importlib.util.spec_from_file_location('codegen_spark_udfs', spark_path)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod


def run_spark(args):
    """Delegate to the sibling codegen_spark_udfs.py so output is byte-identical.

    The reference generator owns the whole catalog+jar front-end and the SQL-UDF
    emit; running its own code (rather than a re-implementation) is what makes the
    output provably identical to today's. The module comes from #_spark_module."""
    mod = _spark_module()
    argv = ['codegen_spark_udfs',
            '--catalog', args.catalog,
            '--out', args.out,
            '--jar', args.jar]
    if args.report:
        argv.append('--report')
    if args.gaps:
        argv += ['--gaps', args.gaps]
    if args.rebaseline:
        argv.append('--rebaseline')
    saved = sys.argv
    try:
        sys.argv = argv
        mod.main()
    finally:
        sys.argv = saved


# ───────────────────────── facade back-end ─────────────────────────

def short_type(t):
    if t.startswith('java.lang.'):
        return t[len('java.lang.'):]
    return t.split('.')[-1] if '.' in t else t


def header_to_class(h):
    """Free-function class name from the source header, split on '_' AND '.' so a
    dotted internal header (e.g. postgres_ext_defs.in.h) yields a valid class."""
    base = h.replace('.h', '').replace('meos_', '').replace('meos', 'core')
    if base in ('', 'core'):
        return 'MeosOpsFreeCore'
    return 'MeosOpsFree' + ''.join(p.capitalize()
                                   for p in re.split(r'[_.]', base) if p)


def collect_imports(rows):
    imports = {'functions.GeneratedFunctions'}
    for r in rows:
        for t in [r['java_ret']] + [a[0] for a in r['java_params']]:
            if '.' in t and not t.startswith('java.lang.'):
                imports.add(t.replace('[]', ''))
    return sorted(i for i in imports if '.' in i)


def folded_nxn(r):
    """The folded form of an NxN kernel, or None.

    The catalog says everything needed: ``arrayReturn.groupSize`` marks the flattened
    index-pair return, ``outParams`` names the count (and, for the temporal
    relationships, the parallel span-set array), and the ``(TYPE **, int)`` argument
    pairs are the arrays.  Nothing here re-derives what the catalog already states.
    """
    f = r.get('cat')
    if not f:
        return None
    ar = (f.get('shape') or {}).get('arrayReturn') or {}
    if ar.get('groupSize') != 2:
        return None
    outs = set((f.get('shape') or {}).get('outParams') or [])
    params, arrays, scalars, count, periods = f.get('params', []), [], [], None, None
    i = 0
    while i < len(params):
        c = (params[i].get('cType') or '').replace(' ', '')
        nm = params[i].get('name')
        if c.endswith('**') and c.count('*') == 2 and i + 1 < len(params) \
                and (params[i + 1].get('cType') or '').replace(' ', '') == 'int':
            arrays.append(nm); i += 2; continue
        if nm in outs:
            if c == 'int*':
                count = nm
            else:
                periods = nm
            i += 1; continue
        if c in ('double', 'int'):
            scalars.append((nm, 'double' if c == 'double' else 'int')); i += 1; continue
        return None
    if not arrays or count is None:
        return None
    # The folded form is the canonical dialect of this kernel, so it is named by the
    # catalog's @sqlfn (eDwithinPairs, aDisjointPairs, ...) — the name the SQL surface
    # answers to — never the C symbol.  No @sqlfn, no folded form: the name is the
    # catalog's to state, not this generator's to invent.
    if not f.get('sqlfn'):
        return None
    return {'arrays': arrays, 'scalars': scalars, 'count': count,
            'periods': periods, 'group': ar['groupSize'], 'sqlfn': f['sqlfn']}


def emit_folded(r, fold, prop):
    """Emit the folded overload beside the 1:1 forwarder — the canonical dialect keeps
    its C shape, this is the kept idiomatic sugar, never a replacement."""
    fname = fold['sqlfn']
    args = ', '.join(['Pointer[] %s' % a for a in fold['arrays']]
                     + ['%s %s' % (t, n) for (n, t) in fold['scalars']])
    ret = 'PairsAndPeriods' if fold['periods'] else 'int[][]'
    L = ['    /**',
         f'     * MEOS {{@code {r["name"]}}} under its canonical name, over Java arrays.',
         '     * <p>The count and the written-back out-parameters are supplied and read',
         '     * here; the answer is the index pairs into the argument arrays.</p>',
         '     */',
         f'    public static {ret} {fname}({args}) {{',
         '        if (!MEOS_AVAILABLE) {',
         '            throw new UnsupportedOperationException(',
         f'                "{fname} requires libmeos — set -D{prop}=true");',
         '        }']
    guard = ' || '.join('%s == null' % a for a in fold['arrays'])
    empty = 'new PairsAndPeriods(new int[0][], new byte[0][])' if fold['periods'] \
        else 'new int[0][]'
    L += [f'        if ({guard}) {{',
          f'            return {empty};',
          '        }',
          '        jnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();']
    call = []
    for a in fold['arrays']:
        L.append(f'        Pointer _n{a} = MeosOpsRuntime.nativeArray({a}, _rt);')
        call += [f'_n{a}', f'{a}.length']
    for (n, _t) in fold['scalars']:
        call.append(n)
    L.append('        Pointer _count = jnr.ffi.Memory.allocateDirect(_rt, 4);')
    call.append('_count')
    if fold['periods']:
        L.append('        Pointer _periods = jnr.ffi.Memory.allocateDirect(_rt, 8);')
        call.append('_periods')
    L.append('        try {')
    L.append('            Pointer _res = GeneratedFunctions.%s(%s);'
             % (r['name'], ', '.join(call)))
    L.append('            int _c = _count.getInt(0L);')
    if fold['periods']:
        L.append('            return new PairsAndPeriods(')
        L.append('                    MeosOpsRuntime.readGroups(_res, _c, %d),'
                 % fold['group'])
        L.append('                    MeosOpsRuntime.readPeriods(_periods.getPointer(0L), _c));')
    else:
        L.append('            return MeosOpsRuntime.readGroups(_res, _c, %d);'
                 % fold['group'])
    L.append('        } finally {')
    for a in fold['arrays']:
        # the native buffer must outlive the call (feedback_jnr_array_reachability_fence)
        L.append(f'            java.lang.ref.Reference.reachabilityFence(_n{a});')
    L += ['        }', '    }', '']
    return L


PAIRS_HOLDER = [
    '    /** Index pairs and, for the temporal relationships, when each pair holds. */',
    '    public static final class PairsAndPeriods {',
    '        public final int[][] pairs;',
    '        public final byte[][] periodsWkb;',
    '',
    '        PairsAndPeriods(int[][] pairs, byte[][] periodsWkb) {',
    '            this.pairs = pairs;',
    '            this.periodsWkb = periodsWkb;',
    '        }',
    '    }',
    '',
]


def emit_method(r, prop):
    fname = r['name']
    args = ', '.join(f'{short_type(t)} {n}' for (t, n) in r['java_params'])
    call_args = ', '.join(n for (t, n) in r['java_params'])
    ret = short_type(r['java_ret'])
    L = ['    /**',
         f'     * MEOS {{@code {fname}}}.']
    if r.get('oo_class'):
        L.append(f'     * <p>Object-model class: {{@code {r["oo_class"]}}}'
                 + (f', role {{@code {r["role"]}}}' if r.get('role') else '')
                 + '.</p>')
    elif r.get('role'):
        L.append(f'     * <p>Object-model role: {{@code {r["role"]}}}.</p>')
    if r['seq']:
        L.append('     * <p>Sequence-only: builds a whole sequence — '
                 'not supported in a streaming context.</p>')
    L.append('     */')
    if r['seq']:
        # sequence-only guard
        L += [f'    public static {ret} {fname}({args}) {{',
              '        throw new UnsupportedOperationException(',
              f'            "{fname} is sequence-only — not supported in a '
              'streaming context");',
              '    }', '']
    else:
        # runtime MEOS_AVAILABLE guard
        ret_stmt = '' if ret == 'void' else 'return '
        L += [f'    public static {ret} {fname}({args}) {{',
              '        if (!MEOS_AVAILABLE) {',
              '            throw new UnsupportedOperationException(',
              f'                "{fname} requires libmeos — set -D{prop}=true");',
              '        }',
              f'        {ret_stmt}GeneratedFunctions.{fname}({call_args});',
              '    }', '']
    fold = folded_nxn(r)
    if fold:
        L += emit_folded(r, fold, prop)
    return L


def emit_class(cls, rows, pkg, prop, banner):
    seq_cnt = sum(1 for r in rows if r['seq'])
    L = [f'package {pkg};', '',
         '/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.',
         f' * {banner}',
         f' * Methods emitted: {len(rows)} (full jar surface'
         + (f'; {seq_cnt} sequence-only)' if seq_cnt else ')'),
         ' * Source: the bundled JMEOS functions.GeneratedFunctions surface,',
         ' *         grouped by the MEOS-API catalog object model.',
         ' */', '']
    L += [f'import {i};' for i in collect_imports(rows)]
    L += ['',
          f'public final class {cls} {{', '',
          '    public static final boolean MEOS_AVAILABLE = '
          'MeosOpsRuntime.MEOS_AVAILABLE;',
          '',
          f'    private {cls}() {{ /* utility */ }}', '']
    if any((folded_nxn(r) or {}).get('periods') for r in rows):
        L += PAIRS_HOLDER
    for r in sorted(rows, key=lambda r: r['name']):
        L += emit_method(r, prop)
    L.append('}')
    return '\n'.join(L) + '\n'


def runtime_src(pkg, prop):
    """Shared MEOS_AVAILABLE probe."""
    return f'''package {pkg};

import functions.GeneratedFunctions;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Shared runtime helper: owns the single MEOS_AVAILABLE static-init across all
 * generated MeosOps* facades, so libmeos is probed exactly once per JVM. */
public final class MeosOpsRuntime {{

    public static final boolean MEOS_AVAILABLE;

    static {{
        boolean enabled = Boolean.parseBoolean(
                System.getProperty("{prop}", "true"));
        boolean ok = false;
        if (enabled) {{
            try {{
                GeneratedFunctions.meos_initialize();
                ok = true;
            }} catch (Throwable t) {{
                ok = false;
            }}
        }}
        MEOS_AVAILABLE = ok;
    }}

    /* ── native memory ─────────────────────────────────────────────────────────
     * MEOS standalone allocates through the hook installed by
     * meos_initialize_allocator, whose default is libc malloc, so a returned
     * pointer is a libc-heap pointer that the system free accepts.  A JNR Pointer
     * is a raw address the Java GC does not track, so every owned return is freed
     * explicitly.  jffi's MemoryIO.freeMemory calls that system free; jffi is the
     * native layer jnr-ffi runs on, so it shares the classloader of every MEOS call
     * inside the engines and needs no internal JDK API. */
    private static final com.kenai.jffi.MemoryIO IO = com.kenai.jffi.MemoryIO.getInstance();

    /** Free a native pointer owned by the caller.  Null-safe. */
    public static void free(jnr.ffi.Pointer p) {{
        if (p != null) {{
            IO.freeMemory(p.address());
        }}
    }}

    /* ── NxN array marshalling ─────────────────────────────────────────────────
     * The NxN kernels take (TYPE **arr, int n) argument pairs and run the whole
     * cross product inside C.  The elements are already parsed, so an argument
     * array is copied into a native buffer of pointers; the buffer must stay
     * reachable across the call, which reachabilityFence at the call site does. */
    public static jnr.ffi.Pointer nativeArray(jnr.ffi.Pointer[] xs,
            jnr.ffi.Runtime rt) {{
        jnr.ffi.Pointer buf = jnr.ffi.Memory.allocateDirect(
                rt, Math.max(1, xs.length) * 8);
        for (int i = 0; i < xs.length; i++) {{
            buf.putPointer((long) i * 8L, xs[i]);
        }}
        return buf;
    }}

    /* A pairs-returning kernel answers a flat int array of groupSize * count ints,
     * `[i0, j0, i1, j1, ...]`, which the caller frees.  The indices are the 0-based
     * C offsets into the argument arrays — it is the PostgreSQL SETOF wrapper, not
     * the kernel, that renders them 1-based — so they are read as they stand. */
    public static int[][] readGroups(jnr.ffi.Pointer res, int count, int group) {{
        if (res == null || count <= 0) {{
            return new int[0][];
        }}
        int[][] out = new int[count][group];
        for (int k = 0; k < count; k++) {{
            for (int g = 0; g < group; g++) {{
                out[k][g] = res.getInt((long) (group * k + g) * 4L);
            }}
        }}
        free(res);
        return out;
    }}

    /* The temporal relationships also answer, through a parallel SpanSet ** out-array,
     * the times when each resulting pair holds; each is rendered as its WKB.  Frees the
     * pairs, the span-set array and every span set in it. */
    public static byte[][] readPeriods(jnr.ffi.Pointer ssArr, int count) {{
        byte[][] out = new byte[Math.max(0, count)][];
        for (int k = 0; k < out.length; k++) {{
            jnr.ffi.Pointer ss = ssArr == null
                    ? null : ssArr.getPointer((long) k * 8L);
            out[k] = ss == null
                    ? null : GeneratedFunctions.spanset_as_wkb(ss, (byte) {WKB_VARIANT});
            free(ss);
        }}
        free(ssArr);
        return out;
    }}

    private MeosOpsRuntime() {{ /* utility */ }}
}}
'''


def run_facades(args):
    prop = f'mobility{args.engine}.meos.enabled'
    out_dir = Path(args.out) / 'src/main/java' / args.package.replace('.', '/')
    out_dir.mkdir(parents=True, exist_ok=True)

    cat = load_catalog(args.catalog)
    jmeos = parse_jmeos_signatures(args.jar)
    fn_class, fn_role, fn_header, fn_seq = object_model_index(cat)
    fn_cat = {f['name']: f for f in cat.get('functions', [])}

    # FULL surface: one row per jar symbol (the jar is the ground truth of what the
    # bundled JMEOS actually exposes). A jar symbol absent from the catalog has no
    # class/header, so it falls back to the core free class and the return-type
    # sequence check.
    rows = []
    for name, sig in jmeos.items():
        rows.append({
            'name': name,
            'java_ret': sig['ret'],
            'java_params': [(t, f'arg{i}') for i, t in enumerate(sig['arg_types'])],
            'oo_class': fn_class.get(name),
            'role': fn_role.get(name),
            'header': fn_header.get(name, 'meos.h'),
            'seq': fn_seq.get(name, seq_typed(sig['ret'])),
            'cat': fn_cat.get(name),
        })

    # Regenerate from scratch: drop stale MeosOps*.java.  The NxN kernels that used
    # to need a hand-written caller are emitted folded beside their forwarder, so this
    # package holds generated code only.
    for f in out_dir.glob('MeosOps*.java'):
        f.unlink()
    (out_dir / 'MeosOpsRuntime.java').write_text(runtime_src(args.package, prop))

    # Group by the FINAL class name, not the raw grouping key: several distinct
    # headers collapse to the same free class (e.g. cbuffer.h and meos_cbuffer.h
    # both -> MeosOpsFreeCbuffer), so key on the class name and MERGE their rows —
    # else the second write_text would clobber the first and silently drop methods.
    by_class = defaultdict(list)      # class name -> rows
    class_headers = defaultdict(set)  # free class name -> contributing headers
    for r in rows:
        if r['oo_class']:
            by_class[f'MeosOps{r["oo_class"]}'].append(r)
        else:
            cls = header_to_class(r['header'])
            by_class[cls].append(r)
            class_headers[cls].add(r['header'])

    for cls, crows in sorted(by_class.items()):
        if cls in class_headers:
            banner = 'Free functions from ' + ', '.join(sorted(class_headers[cls]))
        else:
            banner = 'MEOS object-model class: ' + cls[len('MeosOps'):]
        (out_dir / f'{cls}.java').write_text(
            emit_class(cls, crows, args.package, prop, banner))

    n_seq = sum(1 for r in rows if r['seq'])
    n_oo = sum(1 for c in by_class if c not in class_headers)
    n_free = len(class_headers)
    print(f'{args.engine}: emitted {1 + len(by_class)} facade classes into {out_dir} '
          f'({n_oo} object-model + {n_free} free + MeosOpsRuntime), '
          f'{len(rows)} methods ({n_seq} sequence-only guarded)')


# ───────────────────────── flink-sql back-end ─────────────────────────
#
# A MEOS value crosses Flink as the serialized form its catalog codec writes, never
# as a native pointer: Flink copies, serializes, checkpoints and groups values, and a
# pointer is an address in one process.  Each eval decodes its MEOS arguments, calls
# the GeneratedFunctions wrapper, encodes the result and frees what MEOS allocated.

SQL_PKG = 'org.mobilitydb.flink.sql'

# The Flink-side Java class of each SQL scalar type the signatures use.
SQL_SCALAR = {
    'boolean': 'Boolean', 'integer': 'Integer', 'smallint': 'Short', 'bigint': 'Long',
    'float': 'Double', 'double precision': 'Double', 'text': 'String', 'cstring': 'String',
    'timestamptz': 'java.time.Instant', 'date': 'java.time.LocalDate',
    'interval': 'java.time.Duration',
}

# The Flink SQL type of each Flink-side scalar class; a MEOS value class carries its own TYPE.
SQL_DATATYPE = {
    'Boolean': 'DataTypes.BOOLEAN()', 'Integer': 'DataTypes.INT()',
    'Short': 'DataTypes.SMALLINT()', 'Long': 'DataTypes.BIGINT()',
    'Double': 'DataTypes.DOUBLE()', 'String': 'DataTypes.STRING()',
    'java.time.Instant': 'DataTypes.TIMESTAMP_LTZ(6)', 'java.time.LocalDate': 'DataTypes.DATE()',
    'java.time.Duration': 'DataTypes.INTERVAL(DataTypes.SECOND(3))',
}

# The WKB variant the WKB writers take: the extended form, which keeps the SRID.
WKB_VARIANT = 4

# A SQL array MEOS reads as a contiguous C array of scalars, keyed by the SQL element type
# and the C element type together: the Flink-side element class and the MeosSqlRuntime
# helper writing the C array.
SQL_ARRAY_SCALAR = {
    ('integer', 'int'): ('Integer', 'ints'),
    ('bigint', 'int64'): ('Long', 'longs'), ('bigint', 'int64_t'): ('Long', 'longs'),
    ('float', 'double'): ('Double', 'doubles'),
    ('double precision', 'double'): ('Double', 'doubles'),
    ('date', 'DateADT'): ('java.time.LocalDate', 'dates'),
    ('timestamptz', 'TimestampTz'): ('java.time.Instant', 'timestamps'),
}


def _datatype(cls):
    if cls.endswith('[]'):
        return f'DataTypes.ARRAY({_datatype(cls[:-2])})'
    return SQL_DATATYPE.get(cls, f'{cls}.TYPE')


def _norm(t):
    return (t or '').replace('const ', '').replace('struct ', '').strip()


def _base(t):
    return _norm(t).replace('*', '').strip()


def _single_pointer(t):
    n = _norm(t)
    return n.endswith('*') and n.count('*') == 1


def _jshort(t):
    return {'jnr.ffi.Pointer': 'Pointer', 'java.lang.String': 'String',
            'java.time.OffsetDateTime': 'OffsetDateTime'}.get(t, t)


def _javaid(name):
    ident = re.sub(r'\W', '_', name)
    return ident[0].upper() + ident[1:]


class SqlModel:
    """What the catalog and the jar state about the SQL surface, indexed once."""

    def __init__(self, cat, jmeos, pkg=SQL_PKG, engine='flink'):
        self.cat = cat
        # The Java package of the generated surface, where its value types live, and the engine
        # whose spellings of a SQL type, an array type and a row the overloads take.
        self.pkg = pkg
        self.engine = engine
        self.jmeos = jmeos
        self.fns = cat['functions']
        self.by_name = {f['name']: f for f in self.fns}
        self.enc = cat.get('typeEncodings', {})
        self.enums = {e['name'] for e in cat.get('enums', [])}
        # The value of each macro and enum member a wrapper binds by name.
        self.consts = {x['name']: x['value'] for x in cat.get('macros', [])
                       if isinstance(x.get('value'), (int, float))}
        self.consts.update({v['name']: v['value'] for e in cat.get('enums', [])
                            for v in e.get('values') or []
                            if isinstance(v, dict) and isinstance(v.get('value'), int)})
        self._align()
        self._codecs()
        self._enum_parsers()

    def datatype(self, cls):
        """The engine's SQL type expression for the Java class `cls`."""
        return _spark_datatype(self, cls) if self.engine == 'spark' else _datatype(cls)

    def array_type(self, elem):
        """The engine's SQL type expression for an array of the type expression `elem`."""
        return (f'DataTypes.createArrayType({elem})' if self.engine == 'spark'
                else f'DataTypes.ARRAY({elem})')

    def row_type(self, fields):
        """The engine's SQL type expression for a row of the named type expressions."""
        if self.engine == 'spark':
            return ('DataTypes.createStructType(new org.apache.spark.sql.types.StructField[] {'
                    + ', '.join(f'DataTypes.createStructField("{n}", {t}, true)' for n, t in fields)
                    + '})')
        return 'DataTypes.ROW(' + ', '.join(f'DataTypes.FIELD("{n}", {t})' for n, t in fields) + ')'

    @property
    def row_class(self):
        """The engine's Java class of a row."""
        return 'org.apache.spark.sql.Row' if self.engine == 'spark' else 'org.apache.flink.types.Row'

    @property
    def row_of(self):
        """The engine's Java factory of a row from its fields."""
        return ('org.apache.spark.sql.RowFactory.create' if self.engine == 'spark'
                else 'org.apache.flink.types.Row.of')

    def visible(self, f):
        outs = set((f.get('shape') or {}).get('outParams') or [])
        return [p for p in f['params'] if p['name'] not in outs], \
               [p for p in f['params'] if p['name'] in outs]

    def signatures(self, f):
        """(sqlName, args, ret, argDefaults, boundArgs) for every SQL signature of f, where
        boundArgs are the literals the wrapper passes for the C parameters the signature
        does not state: those of the whole function and those of the signature."""
        fbound = (f.get('shape') or {}).get('boundArgs') or {}
        for s in f.get('sqlSignatures') or []:
            yield (s.get('sqlName') or f['sqlfn'], s.get('args') or [], s.get('ret'),
                   s.get('argDefaults') or [], {**fbound, **(s.get('boundArgs') or {})})

    def _align(self):
        """The C base type each SQL type stands for, read off the signatures that
        pair it with a single C value: a parameter that is one pointer, or the return
        of a function with no out-parameters.  A pointer to pointers, or a return that
        an out-parameter counts, is an array, and the SQL type opposite it is an array
        or a record, not a value."""
        seen = defaultdict(lambda: defaultdict(int))
        for f in self.fns:
            if not f.get('sqlfn'):
                continue
            vis, outs = self.visible(f)
            for _, args, ret, _, _ in self.signatures(f):
                if len(args) == len(vis):
                    for a, p in zip(args, vis):
                        if _single_pointer(p['canonical']):
                            seen[a][_base(p['canonical'])] += 1
                rc = f['returnType']['canonical']
                if ret and not outs and _single_pointer(rc) and _norm(rc) != 'char *':
                    seen[ret][_base(rc)] += 1
        self.sql_cbase = {s: max(c, key=c.get) for s, c in seen.items()
                          if re.fullmatch(r'\w+', s)}

    def _codecs(self):
        """Value types and the codec each crosses Flink with.

        The WKB bytes where the catalog states the byte codec of the C type, and its
        hex WKB where it states a WKB decoder and an asHexWKB encoder: a C type has one
        WKB reader, so one codec serves every SQL type the C type stands for.  Text
        otherwise, and only for a C type a single SQL type stands for, since a text
        decoder cannot tell those SQL types apart.  The Spark generator chooses by the
        same rule (#derive_codecs in codegen_spark_udfs.py)."""
        hexwkb = {}
        for f in self.fns:
            if f.get('sqlfn') == 'asHexWKB' and f['params'] and f['name'] in self.jmeos:
                hexwkb.setdefault(_base(f['params'][0]['canonical']), f['name'])
        by_cbase = defaultdict(set)
        for s, cb in self.sql_cbase.items():
            by_cbase[cb].add(s)
        self.codec = {}
        for sql, cb in self.sql_cbase.items():
            if sql in SQL_SCALAR or sql == 'internal':
                continue
            e = self.enc.get(cb) or {}
            dec = (e.get('decoders') or {})
            encd = (e.get('encoders') or {})
            byt = (e.get('bytes') or {})
            if byt.get('decoder') in self.jmeos and byt.get('encoder') in self.jmeos:
                self.codec[sql] = ('bytes', byt['decoder'], byt['encoder'], [], [])
            elif dec.get('wkb') in self.jmeos and cb in hexwkb:
                self.codec[sql] = ('wkb', dec['wkb'], hexwkb[cb], [], [])
            elif len(by_cbase[cb]) == 1 and dec.get('text') in self.jmeos \
                    and encd.get('text') in self.jmeos:
                self.codec[sql] = ('text', dec['text'], encd['text'],
                                   e.get('in_aux') or [], e.get('out_aux') or [])
        om = {k.lower(): k for k in (self.cat.get('objectModel') or {}).get('classes', {})}
        self.value_class = {}
        taken = set()
        for sql in sorted(self.codec):
            cls = om.get(sql, _javaid(sql))
            while cls in taken:
                cls += '_'
            taken.add(cls)
            self.value_class[sql] = cls

    def _enum_parsers(self):
        """For each C enum, the public catalog function reading it from its name, as a binding
        calls the public API alone; the Spark arm reads the same (ENUM_PARSER in
        codegen_spark_udfs.py)."""
        self.enum_parser = {}
        for f in self.fns:
            rt = _norm(f['returnType']['canonical'])
            ps = f['params']
            if rt in self.enums and len(ps) == 1 and _norm(ps[0]['canonical']) == 'char *' \
                    and f.get('api') == 'public' and f['name'] in self.jmeos:
                self.enum_parser.setdefault(rt, f['name'])


def _default_literal(sql, lit):
    """The Java literal for a SQL default, or None when it is not a plain constant."""
    if lit is None:
        return None
    v = lit.strip()
    if v.startswith("'") and v.endswith("'"):
        v = v[1:-1]
        if sql in ('text', 'cstring'):
            return json.dumps(v)
    try:
        if sql in ('integer', 'smallint'):
            return str(int(v))
        if sql == 'bigint':
            return str(int(v)) + 'L'
        if sql in ('float', 'double precision'):
            return repr(float(v))
    except ValueError:
        return None
    if sql == 'boolean' and v.lower() in ('true', 'false'):
        return v.lower()
    return None


def _arg(m, sql, p, jt, name, temps):
    """Java expression passing Flink argument `name` to a jar parameter, or None."""
    c = _norm(p['canonical'])
    b = _base(p['canonical'])
    if sql in m.codec and jt == 'jnr.ffi.Pointer':
        t = f'_p{len(temps)}'
        temps.append((t, f'{name}.decode()', 'value'))
        return t
    if sql == 'interval' and jt == 'jnr.ffi.Pointer' and b == 'Interval':
        t = f'_p{len(temps)}'
        temps.append((t, f'MeosSqlRuntime.interval({name})', 'value'))
        return t
    if sql == 'boolean' and jt == 'boolean':
        return name
    if sql in ('integer', 'smallint') and jt in ('int', 'short', 'byte'):
        return name if jt == 'int' else f'({jt}) (int) {name}'
    if sql in ('integer', 'bigint') and jt == 'long':
        return f'(long) {name}'
    if sql in ('float', 'double precision') and jt == 'double':
        return name
    if sql in ('text', 'cstring') and jt == 'java.lang.String' and c == 'char *':
        return name
    if sql in ('text', 'cstring') and jt == 'int' and c in m.enum_parser:
        return f'GeneratedFunctions.{m.enum_parser[c]}({name})'
    if sql == 'timestamptz' and jt == 'java.time.OffsetDateTime':
        return f'{name}.atOffset(java.time.ZoneOffset.UTC)'
    if sql == 'date' and jt == 'int' and c == 'DateADT':
        return f'MeosSqlRuntime.dateAdt({name})'
    return None


def _ret(m, sql, f, jt, outs):
    """(Flink return class, statements turning `_r` into the result), or None."""
    rc = _norm(f['returnType']['canonical'])
    vc = m.value_class.get(sql)
    if outs:
        # A bool+result wrapper hands back the buffer the out-parameter was written to,
        # or null when MEOS wrote nothing.
        if len(outs) != 1 or rc != 'bool' or jt != 'jnr.ffi.Pointer':
            return None
        oc = _norm(outs[0]['canonical'])
        read = {('integer', 'int *'): ('Integer', '_r.getInt(0)'),
                ('float', 'double *'): ('Double', '_r.getDouble(0)'),
                ('bigint', 'int64 *'): ('Long', '_r.getLongLong(0)'),
                ('bigint', 'int64_t *'): ('Long', '_r.getLongLong(0)'),
                ('boolean', 'bool *'): ('Boolean', '_r.getByte(0) != 0'),
                ('timestamptz', 'TimestampTz *'):
                    ('java.time.Instant', 'MeosSqlRuntime.timestamptz(_r.getLongLong(0))'),
                ('date', 'DateADT *'): ('java.time.LocalDate', 'MeosSqlRuntime.date(_r.getInt(0))')}
        r = read.get((sql, oc))
        if not r:
            return None
        return r[0], [f'return _r == null ? null : {r[1]};']
    if vc and jt == 'jnr.ffi.Pointer':
        return (f'{m.pkg}.types.{vc}',
                ['if (_r == null) return null;',
                 f'try {{ return {m.pkg}.types.{vc}.encode(_r); }}',
                 'finally { MeosSqlRuntime.freeResult(_r, _in); }'])
    if sql in ('text', 'cstring') and jt == 'jnr.ffi.Pointer' and rc == 'text *' \
            and 'text_out' in m.jmeos:
        # A text MEOS allocates, read into a String through text_out and freed after, as the
        # Spark arm reads it (#ret_emit in codegen_spark_udfs.py).
        return ('String',
                ['if (_r == null) return null;',
                 'try { return GeneratedFunctions.text_out(_r); }',
                 'finally { MeosSqlRuntime.freeResult(_r, _in); }'])
    if sql == 'interval' and jt == 'jnr.ffi.Pointer':
        return ('java.time.Duration',
                ['if (_r == null) return null;',
                 'try { return MeosSqlRuntime.duration(_r); }',
                 'finally { MeosSqlRuntime.freeResult(_r, _in); }'])
    if sql == 'boolean' and jt == 'boolean':
        return 'Boolean', ['return _r;']
    if sql == 'boolean' and jt == 'int':
        # Three-valued: negative is undefined and reads as NULL.
        return 'Boolean', ['return _r < 0 ? null : _r != 0;']
    if sql in ('integer', 'smallint') and jt in ('int', 'short'):
        return 'Integer', ['return (int) _r;']
    if sql == 'bigint' and jt in ('long', 'int'):
        return 'Long', ['return (long) _r;']
    if sql in ('float', 'double precision') and jt == 'double':
        return 'Double', ['return _r;']
    if sql in ('text', 'cstring') and jt == 'java.lang.String':
        return 'String', ['return _r;']
    if sql == 'timestamptz' and jt == 'java.time.OffsetDateTime':
        return 'java.time.Instant', ['return _r == null ? null : _r.toInstant();']
    if sql == 'date' and jt == 'int' and rc == 'DateADT':
        return 'java.time.LocalDate', ['return MeosSqlRuntime.date(_r);']
    return None


def _flink_class(m, sql):
    if sql in m.value_class:
        return f'{m.pkg}.types.{m.value_class[sql]}'
    return SQL_SCALAR.get(sql)


def _array_arg(m, elem, a, p, jt, name, temps):
    """(Flink class, Java expression) passing the SQL array `name` of `elem` to the jar
    parameter shape.inputArrays pairs it with (entry `a`), or None.

    An array of MEOS values reaches MEOS as a C array of the pointers its elements decode
    to, released after the call as MobilityDuck's ListToTemporalArr and FreeTemporalArr
    release them: MEOS copies what it keeps of an array.  An array of scalars reaches it as
    the contiguous C array it reads."""
    if jt != 'jnr.ffi.Pointer':
        return None
    c = _norm(p['canonical'])
    el = a['element']
    t = f'_p{len(temps)}'
    if elem in m.value_class and c.count('*') == 2 \
            and _base(el['canonical']) == m.sql_cbase.get(elem):
        temps.append((t, name, 'values'))
        return f'{m.pkg}.types.{m.value_class[elem]}[]', t
    hit = SQL_ARRAY_SCALAR.get((elem, _base(el['c']))) \
        or SQL_ARRAY_SCALAR.get((elem, _base(el['canonical'])))
    if hit and c.count('*') == 1:
        temps.append((t, f'MeosSqlRuntime.{hit[1]}({name})', 'buffer'))
        return f'{hit[0]}[]', t
    return None


def _bound_literal(m, v, jt):
    """The Java literal passing the value a wrapper binds (boundArgs) to a jar parameter of
    type `jt`, or None.  A macro or enum member name stands for its catalog value."""
    v = m.consts.get(v, v)
    if v in ('true', 'false'):
        return v if jt == 'boolean' else None
    if v == 'NULL':
        return 'null' if jt == 'jnr.ffi.Pointer' else None
    try:
        x = float(v)
    except (TypeError, ValueError):
        return None
    if jt == 'double':
        return repr(x)
    if x != int(x):
        return None
    if jt == 'int':
        return str(int(x))
    if jt == 'long':
        return f'{int(x)}L'
    if jt in ('short', 'byte'):
        return f'({jt}) {int(x)}'
    return None


def _overload(m, f, args, ret, jsig, bound=None):
    """The eval of one signature returning one value, or (None, reason): the arguments
    #_inputs passes and the result #_ret reads, as (params, temps, Flink return class, body,
    Flink return type)."""
    vis, outs = m.visible(f)
    outs = [p for p in outs if _norm(p['canonical']) != 'size_t *']
    if len(jsig['arg_types']) != len(vis):
        return None, 'arity:jmeos'
    got, why = _inputs(m, f, args, jsig['arg_types'], bound)
    if got is None:
        return None, why
    params, call, temps = got
    r = _ret(m, ret, f, jsig['ret'], outs)
    if r is None:
        return None, f'ret:{ret}/{_norm(f["returnType"]["canonical"])}'
    rcls, rstmts = r
    body = [f'{_jshort(jsig["ret"])} _r = GeneratedFunctions.{f["name"]}({", ".join(call)});']
    return (params, temps, rcls, body + rstmts, m.datatype(rcls)), None


def _inputs(m, f, args, jtypes, bound=None):
    """((params, call, temps), None) passing the SQL arguments `args` of a signature of `f` to
    its visible C parameters, whose jar types are `jtypes`, or (None, reason).

    The SQL arguments pair with the visible C parameters in order, except that a SQL array
    stands for the C array shape.inputArrays names together with the count its lengthFrom
    names, which the eval passes as the length of the Flink array, and that a parameter
    the signature's wrapper binds (`bound`, from boundArgs) takes that literal."""
    vis, _ = m.visible(f)
    jsig = {'arg_types': jtypes}
    arrays = {a['param']: a for a in (f.get('shape') or {}).get('inputArrays') or []}
    counts = {}
    for a in arrays.values():
        lf = a.get('lengthFrom') or {}
        if lf.get('kind') != 'param' or lf.get('name') not in {p['name'] for p in vis}:
            return None, 'array:length'
        counts[lf['name']] = a['param']
    bound = {k: v for k, v in (bound or {}).items()
             if k in {p['name'] for p in vis} and k not in counts}
    walk = [i for i, p in enumerate(vis) if p['name'] not in counts and p['name'] not in bound]
    if len(args) != len(walk):
        return None, 'arity:sql'
    params, temps, passed = [], [], {}
    call = [None] * len(vis)
    for i, p in enumerate(vis):
        if p['name'] in bound:
            call[i] = _bound_literal(m, bound[p['name']], jsig['arg_types'][i])
            if call[i] is None:
                return None, f'bound:{bound[p["name"]]}/{jsig["arg_types"][i]}'
    for k, (i, sql) in enumerate(zip(walk, args)):
        p, jt, name = vis[i], jsig['arg_types'][i], f'a{k}'
        if sql.endswith('[]') != (p['name'] in arrays):
            return None, f'array:{sql}/{_norm(p["canonical"])}'
        if p['name'] in arrays:
            r = _array_arg(m, sql[:-2], arrays[p['name']], p, jt, name, temps)
            if r is None:
                return None, f'arrayarg:{sql}/{_norm(p["canonical"])}'
            fc, e = r
        else:
            fc = _flink_class(m, sql)
            if fc is None:
                return None, f'sqltype:{sql}'
            e = _arg(m, sql, p, jt, name, temps)
            if e is None:
                return None, f'arg:{sql}/{_norm(p["canonical"])}'
        params.append((fc, name))
        call[i] = e
        passed[p['name']] = name
    for i, p in enumerate(vis):
        if p['name'] in counts:
            if jsig['arg_types'][i] not in ('int', 'long'):
                return None, f'arraycount:{jsig["arg_types"][i]}'
            call[i] = f'{passed[counts[p["name"]]]}.length'
    return (params, call, temps), None


def _emit_eval(ov, defaults=None):
    """The Java eval of an overload, #_overload's or #_setret_overload's: the inputs #_inputs
    decodes, released after the body runs, the body itself and the Flink return class."""
    params, temps, rcls, body, _ = ov
    shown = params if defaults is None else params[:len(params) - len(defaults)]
    L = [f'    public {rcls} eval({", ".join(f"{t} {n}" for t, n in shown)}) {{']
    if shown:
        L.append(f'        if ({" || ".join(f"{n} == null" for _, n in shown)}) return null;')
    # An argument left to its SQL default is that default, converted like any other.
    for (t, n), lit in zip(params[len(shown):], defaults or []):
        L.append(f'        {t} {n} = {lit};')
    if all(kind == 'value' for _, _, kind in temps):
        for t, e, _ in temps:
            L.append(f'        Pointer {t} = {e};')
        L.append(f'        Pointer[] _in = {{{", ".join(t for t, _, _ in temps)}}};')
        L.append('        try {')
        L += [f'            {s}' for s in body]
        L.append('        } finally {')
        L.append('            MeosSqlRuntime.free(_in);')
    else:
        # An array decodes element by element and a null element raises, so what the call
        # takes is gathered as it is built, inside the try that releases it.
        L.append('        MeosSqlRuntime.Inputs _in = new MeosSqlRuntime.Inputs();')
        L.append('        try {')
        for t, e, kind in temps:
            v = {'value': f'_in.value({e})', 'values': f'_in.values({e})',
                 'buffer': f'_in.hold({e})'}[kind]
            L.append(f'            Pointer {t} = {v};')
        L += [f'            {s}' for s in body]
        L.append('        } finally {')
        L.append('            _in.free();')
    L += ['        }', '    }', '']
    return L


# ── set-returning signatures: one Flink array element per SQL row ──
# A SQL function returning a set of rows is a MEOS C function returning parallel arrays: its
# result and its out-parameters, the row count in its `int *` count out-parameter. The catalog
# names the C value behind each column of the row (sqlSignatures[].columns): `from` is
# "return", an out-parameter or "ordinal" (the row's 1-based ordinality), `field` reads a
# member of a struct element, and `element` with `offset` reads one member of a group of
# shape.arrayReturn.groupSize values. The eval returns ARRAY<value> for a single unnamed
# column and ARRAY<ROW<columns>> otherwise, which CROSS JOIN UNNEST unfolds into the rows, as
# the Spark arm returns array<value> and array<struct> (#emit_setret in codegen_spark_udfs.py).

# Contiguous scalar elements, keyed by C element type and SQL column type: the Flink class and
# the Java read of element {i} of the array at {p}.
SETRET_SCALAR = {
    ('int', 'integer'): ('Integer', '{p}.getInt((long) {i} * 4L)'),
    ('int32_t', 'integer'): ('Integer', '{p}.getInt((long) {i} * 4L)'),
    ('int64', 'bigint'): ('Long', '{p}.getLongLong((long) {i} * 8L)'),
    ('int64_t', 'bigint'): ('Long', '{p}.getLongLong((long) {i} * 8L)'),
    ('double', 'float'): ('Double', '{p}.getDouble((long) {i} * 8L)'),
    ('double', 'double precision'): ('Double', '{p}.getDouble((long) {i} * 8L)'),
    ('bool', 'boolean'): ('Boolean', '({p}.getByte((long) {i}) != 0)'),
    ('DateADT', 'date'): ('java.time.LocalDate',
                          'MeosSqlRuntime.date({p}.getInt((long) {i} * 4L))'),
    ('TimestampTz', 'timestamptz'): ('java.time.Instant',
                                     'MeosSqlRuntime.timestamptz({p}.getLongLong((long) {i} * 8L))'),
}


def _setret_column(m, f, col, layout):
    """How to read one column of a returned row, as #row_column in codegen_spark_udfs.py reads
    it for Spark: (Flink class, source array, Java read of row `_i` from the array `{a}`,
    whether each element is a MEOS allocation to free), or None when it cannot be read."""
    sql, src = col.get('type'), col['from']
    if src == 'ordinal':
        return {'integer': ('Integer', None, '(_i + 1)', False),
                'bigint': ('Long', None, '((long) _i + 1L)', False)}.get(sql)
    if src == 'return':
        arr = _norm(f['returnType']['canonical'])
    else:
        p = next((p for p in f['params'] if p['name'] == src), None)
        if p is None or not _norm(p['canonical']).endswith('**'):
            return None
        arr = _norm(p['canonical'])[:-1].strip()
    if not arr.endswith('*'):
        return None
    elem = arr[:-1].strip()
    if 'element' in col:
        group = ((f.get('shape') or {}).get('arrayReturn') or {}).get('groupSize')
        hit = SETRET_SCALAR.get((elem, sql))
        if src != 'return' or not group or not hit:
            return None
        read = hit[1].format(p='{a}', i=f'((long) _i * {group} + {col["element"]})')
        off = col.get('offset') or 0
        return hit[0], src, f'({read} + {off})' if off else read, False
    if 'field' in col:
        lay = layout(elem)
        if lay is None or col['field'] not in lay[2]:
            return None
        off, ctype = lay[2][col['field']]
        hit = SETRET_SCALAR.get((ctype, sql))
        if not hit:
            return None
        return hit[0], src, hit[1].format(p=f'{{a}}.slice((long) _i * {lay[0]}L + {off}L)',
                                          i='0'), False
    vc = m.value_class.get(sql)
    if elem.endswith('*'):                            # an array of pointers
        b = elem[:-1].strip()
        if vc and m.sql_cbase.get(sql) == b:
            return (f'{m.pkg}.types.{vc}', src,
                    f'{m.pkg}.types.{vc}.encode(MeosSqlRuntime.at({{a}}, _i))', True)
        if b == 'text' and sql == 'text' and 'text_out' in m.jmeos:
            return ('String', src, 'GeneratedFunctions.text_out(MeosSqlRuntime.at({a}, _i))', True)
        return None
    hit = SETRET_SCALAR.get((elem, sql))
    if hit:
        return hit[0], src, hit[1].format(p='{a}', i='_i'), False
    lay = layout(elem) if vc and m.sql_cbase.get(sql) == elem else None
    if lay is not None:                               # contiguous structs: STBox, TBox
        return (f'{m.pkg}.types.{vc}', src,
                f'{m.pkg}.types.{vc}.encode({{a}}.slice((long) _i * {lay[0]}L))', False)
    return None


def _setret_overload(m, f, sig, jsig, bound, layout):
    """The eval of one set-returning signature, or (None, reason), as #_overload builds the eval
    of a signature returning one value: the arguments #_inputs passes, a zeroed cell for the row
    count and for each array out-parameter, and the rows read column by column
    (#_setret_column). The jar takes the out-parameters, which MEOS writes through."""
    vis, outs = m.visible(f)
    if len(jsig['arg_types']) != len(f['params']):
        return None, 'setret:arity'
    if jsig['ret'] != 'jnr.ffi.Pointer':
        return None, 'setret:ret'
    jt = {p['name']: t for p, t in zip(f['params'], jsig['arg_types'])}
    counts = [p for p in outs if _norm(p['canonical']) == 'int *'
              and 'const' not in p['canonical']]
    if len(counts) != 1:
        return None, 'setret:count'
    cols = sig.get('columns') or [{'name': None, 'from': 'return', 'type': sig.get('ret')}]
    readers = []
    for col in cols:
        r = _setret_column(m, f, col, layout)
        if r is None:
            return None, f'setret:column/{col["from"]}:{col.get("type")}'
        readers.append((col['name'], r))
    sources = {r[1] for _, r in readers} - {None, 'return'}
    if any(p is not counts[0] and p['name'] not in sources for p in outs):
        return None, 'setret:out'
    got, why = _inputs(m, f, sig.get('args') or [], [jt[p['name']] for p in vis], bound)
    if got is None:
        return None, why
    params, call_vis, temps = got
    vcall = dict(zip([p['name'] for p in vis], call_vis))
    cells = {p['name']: f'_o{k}' for k, p in enumerate(o for o in outs if o is not counts[0])}
    call = [vcall.get(p['name']) or cells.get(p['name']) or '_cnt' for p in f['params']]
    single = len(readers) == 1 and readers[0][0] is None
    if single:
        ecls = readers[0][1][0]
        etype = m.datatype(ecls)
    else:
        ecls = m.row_class
        etype = m.row_type([(n, m.datatype(r[0])) for n, r in readers])
    arr = {'return': '_r', **{n: f'_a{k}' for n, k in
                              ((n, cells[n][2:]) for n in cells)}}
    reads = [r[2] if r[1] is None else r[2].replace('{a}', arr[r[1]]) for _, r in readers]
    body = ['Pointer _cnt = MeosSqlRuntime.cell(4);']
    body += [f'Pointer {c} = MeosSqlRuntime.cell(8);' for c in cells.values()]
    body += ['Pointer _r = null;', 'int _c = 0;', 'try {',
             f'    _r = GeneratedFunctions.{f["name"]}({", ".join(call)});',
             '    if (_r == null) return null;',
             '    _c = _cnt.getInt(0L);']
    body += [f'    Pointer _a{c[2:]} = {c}.getPointer(0L);' for c in cells.values()]
    body += [f'    {ecls}[] _rows = new {ecls}[_c];',
             '    for (int _i = 0; _i < _c; _i++) {',
             '        _rows[_i] = ' + (reads[0] if single else
                                       f'{m.row_of}({", ".join(reads)})') + ';',
             '    }',
             '    return _rows;',
             '} finally {']
    for (_, r) in readers:
        if r[3]:
            src = '_r' if r[1] == 'return' else f'{cells[r[1]]}.getPointer(0L)'
            body.append(f'    MeosSqlRuntime.freeEach({src}, _c);')
    body += [f'    MeosSqlRuntime.free({c}.getPointer(0L));' for c in cells.values()]
    body += ['    MeosSqlRuntime.free(_r);', '}']
    return (params, temps, f'{ecls}[]', body, m.array_type(etype)), None


def _value_class_src(m, sql):
    cls = m.value_class[sql]
    kind, dec, enc, in_aux, out_aux = m.codec[sql]
    daux = ''.join(', ' + str(a.get('default', 0)) for a in in_aux)
    eaux = f', (byte) {WKB_VARIANT}' if kind in ('wkb', 'bytes') \
        else ''.join(', ' + str(a.get('default', 0)) for a in out_aux)
    # The form is bytes whatever the codec: the WKB itself, or the UTF-8 of a string form.
    if kind == 'bytes':
        decode = f'GeneratedFunctions.{dec}(form)'
        encode = (f'byte[] b = GeneratedFunctions.{enc}(p{eaux});\n'
                  f'        return b == null ? null : new {cls}(b);')
        render = 'MeosValue.hex(form)'
    else:
        decode = f'GeneratedFunctions.{dec}(new String(form, StandardCharsets.UTF_8){daux})'
        encode = (f'String s = GeneratedFunctions.{enc}(p{eaux});\n'
                  f'        return s == null ? null : new {cls}(s.getBytes(StandardCharsets.UTF_8));')
        render = 'new String(form, StandardCharsets.UTF_8)'
    return f'''package {SQL_PKG}.types;

import functions.GeneratedFunctions;
import java.nio.charset.StandardCharsets;
import jnr.ffi.Pointer;
import org.apache.flink.api.common.typeutils.SimpleTypeSerializerSnapshot;
import org.apache.flink.api.common.typeutils.TypeSerializerSnapshot;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.types.DataType;
import {SQL_PKG}.MeosValue;
import {SQL_PKG}.MeosValueSerializer;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * The SQL type {sql}, carried as its {kind} form ({dec} / {enc}). */
@DataTypeHint(value = "RAW", bridgedTo = {cls}.class, rawSerializer = {cls}.Serializer.class)
public final class {cls} extends MeosValue {{

    /** The Flink SQL type of this value. */
    public static final DataType TYPE = DataTypes.RAW({cls}.class, new Serializer());

    public {cls}(byte[] form) {{
        super(form);
    }}

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public Pointer decode() {{
        return {decode};
    }}

    /** The value MEOS holds at p, in the form this type carries. */
    public static {cls} encode(Pointer p) {{
        {encode}
    }}

    @Override
    public String toString() {{
        return {render};
    }}

    public static final class Serializer extends MeosValueSerializer<{cls}> {{
        @Override
        protected {cls} make(byte[] form) {{
            return new {cls}(form);
        }}

        @Override
        public TypeSerializerSnapshot<{cls}> snapshotConfiguration() {{
            return new Snapshot();
        }}
    }}

    public static final class Snapshot extends SimpleTypeSerializerSnapshot<{cls}> {{
        public Snapshot() {{
            super(Serializer::new);
        }}
    }}
}}
'''


# ── the run-time class both JVM SQL engines share ──
# A generated SQL surface carries a MeosSqlRuntime: its package, imports and heading comment, the
# engine's own planning hook (Flink's type inference, Spark's overload resolution) and the
# conversions between the engine's SQL values and MEOS's and the release of what MEOS allocates,
# which are the same for both engines.
_FLINK_RUNTIME_IMPORTS = '''import functions.GeneratedFunctions;
import java.util.LinkedHashMap;
import java.util.Map;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.inference.ArgumentTypeStrategy;
import org.apache.flink.table.types.inference.InputTypeStrategies;
import org.apache.flink.table.types.inference.InputTypeStrategy;
import org.apache.flink.table.types.inference.TypeInference;
import org.apache.flink.table.types.inference.TypeStrategies;
import org.apache.flink.table.types.inference.TypeStrategy;

'''
_FLINK_RUNTIME_DOC = '''/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Conversions between Flink's SQL values and the ones MEOS takes, and the release of
 * what MEOS allocates.  MEOS counts days and microseconds from the PostgreSQL epoch. */
'''
_RUNTIME_FIELDS = '''    private static final long PG_EPOCH_DAY = 10957L;
    private static final long PG_EPOCH_SECOND = 946684800L;

    private static final jnr.ffi.Runtime RUNTIME = jnr.ffi.Runtime.getSystemRuntime();
    private static final String NULL_ELEMENT = "null array element not allowed in this context";

    /** jffi's MemoryIO frees through the system free, which MEOS allocates with; jffi is the
     * native layer jnr-ffi runs on and needs no internal JDK API. */
    private static final com.kenai.jffi.MemoryIO IO = com.kenai.jffi.MemoryIO.getInstance();

    private static final ThreadLocal<Boolean> ISO_INTERVALS = ThreadLocal.withInitial(() -> {
        GeneratedFunctions.meos_set_intervalstyle("iso_8601", 0);
        return Boolean.TRUE;
    });

    private MeosSqlRuntime() { }

'''
_FLINK_INFERENCE = '''    /** The overloads of one function as a single flat alternative of argument types, each
     * mapped to its result type, so planning a call is linear in the number of overloads. */
    public static TypeInference inference(DataType[][] args, DataType[] rets) {
        InputTypeStrategy[] seqs = new InputTypeStrategy[args.length];
        Map<InputTypeStrategy, TypeStrategy> results = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            ArgumentTypeStrategy[] a = new ArgumentTypeStrategy[args[i].length];
            for (int j = 0; j < a.length; j++) {
                a[j] = InputTypeStrategies.explicit(args[i][j]);
            }
            seqs[i] = InputTypeStrategies.sequence(a);
            results.put(seqs[i], TypeStrategies.explicit(rets[i]));
        }
        return TypeInference.newBuilder()
                .inputTypeStrategy(seqs.length == 1 ? seqs[0] : InputTypeStrategies.or(seqs))
                .outputTypeStrategy(TypeStrategies.mapping(results))
                .build();
    }

'''
_RUNTIME_CORE = '''    /** Free each value MEOS allocated.  Null-safe. */
    public static void free(Pointer[] ps) {
        for (Pointer p : ps) {
            if (p != null) {
                IO.freeMemory(p.address());
            }
        }
    }

    /** Free a value MEOS allocated.  Null-safe. */
    public static void free(Pointer p) {
        if (p != null) {
            IO.freeMemory(p.address());
        }
    }

    /** Free each of the n values MEOS allocated whose pointers the C array a holds.  Null-safe. */
    public static void freeEach(Pointer a, int n) {
        if (a != null) {
            for (int i = 0; i < n; i++) {
                free(at(a, i));
            }
        }
    }

    /** Element i of the C array of pointers a. */
    public static Pointer at(Pointer a, int i) {
        return a.getPointer((long) i * RUNTIME.addressSize());
    }

    /** Zeroed memory for one value of the given width that MEOS writes as an out-parameter,
     * which the garbage collector releases. */
    public static Pointer cell(int width) {
        return Memory.allocateDirect(RUNTIME, width, true);
    }

    /** Free a result unless it is one of the inputs, which the caller frees. */
    public static void freeResult(Pointer r, Pointer[] in) {
        for (Pointer p : in) {
            if (p != null && p.address() == r.address()) {
                return;
            }
        }
        IO.freeMemory(r.address());
    }

    /** Free a result unless it is one of the inputs, which the caller frees. */
    public static void freeResult(Pointer r, Inputs in) {
        if (!in.holds(r)) {
            IO.freeMemory(r.address());
        }
    }

    /** What an eval taking an array hands MEOS, gathered as it is built: the values MEOS
     * allocated, released by {@link #free}, and the C arrays, kept reachable until then. */
    public static final class Inputs {

        private Pointer[] owned = new Pointer[8];
        private int n;
        private final java.util.ArrayList<Pointer> held = new java.util.ArrayList<>();

        /** Keep p for release and pass it on. */
        public Pointer value(Pointer p) {
            if (n == owned.length) {
                owned = java.util.Arrays.copyOf(owned, 2 * n);
            }
            owned[n++] = p;
            return p;
        }

        /** Keep the C array b reachable until the call returns and pass it on. */
        public Pointer hold(Pointer b) {
            held.add(b);
            return b;
        }

        /** The values decoded one by one into the C array of pointers MEOS reads.  A null
         * element raises, as it does in PostgreSQL. */
        public Pointer values(MeosValue[] vs) {
            int w = RUNTIME.addressSize();
            Pointer b = hold(buffer(vs.length, w));
            for (int i = 0; i < vs.length; i++) {
                Pointer p = value(element(vs, i).decode());
                if (p == null) {
                    throw new IllegalArgumentException("an array element does not decode");
                }
                b.putPointer((long) i * w, p);
            }
            return b;
        }

        boolean holds(Pointer r) {
            for (int i = 0; i < n; i++) {
                if (owned[i] != null && owned[i].address() == r.address()) {
                    return true;
                }
            }
            return false;
        }

        /** Free each value kept. */
        public void free() {
            for (int i = 0; i < n; i++) {
                if (owned[i] != null) {
                    IO.freeMemory(owned[i].address());
                }
            }
            n = 0;
            held.clear();
        }
    }

    private static <T> T element(T[] xs, int i) {
        if (xs[i] == null) {
            throw new IllegalArgumentException(NULL_ELEMENT);
        }
        return xs[i];
    }

    /** Memory for n elements of the given width, which the garbage collector releases. */
    private static Pointer buffer(int n, int width) {
        return Memory.allocateDirect(RUNTIME, Math.max(1, n) * width);
    }

    public static Pointer ints(Integer[] xs) {
        Pointer b = buffer(xs.length, 4);
        for (int i = 0; i < xs.length; i++) {
            b.putInt(4L * i, element(xs, i));
        }
        return b;
    }

    public static Pointer longs(Long[] xs) {
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {
            b.putLongLong(8L * i, element(xs, i));
        }
        return b;
    }

    public static Pointer doubles(Double[] xs) {
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {
            b.putDouble(8L * i, element(xs, i));
        }
        return b;
    }

    public static Pointer dates(java.time.LocalDate[] xs) {
        Pointer b = buffer(xs.length, 4);
        for (int i = 0; i < xs.length; i++) {
            b.putInt(4L * i, dateAdt(element(xs, i)));
        }
        return b;
    }

    public static Pointer timestamps(java.time.Instant[] xs) {
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {
            b.putLongLong(8L * i, micros(element(xs, i)));
        }
        return b;
    }

    /** Microseconds from the PostgreSQL epoch, the TimestampTz MEOS reads. */
    public static long micros(java.time.Instant t) {
        return Math.addExact(Math.multiplyExact(t.getEpochSecond() - PG_EPOCH_SECOND, 1000000L),
                t.getNano() / 1000);
    }

    public static int dateAdt(java.time.LocalDate d) {
        return (int) (d.toEpochDay() - PG_EPOCH_DAY);
    }

    public static java.time.LocalDate date(int d) {
        return java.time.LocalDate.ofEpochDay(d + PG_EPOCH_DAY);
    }

    public static java.time.Instant timestamptz(long micros) {
        return java.time.Instant.ofEpochSecond(PG_EPOCH_SECOND + Math.floorDiv(micros, 1000000L),
                Math.floorMod(micros, 1000000L) * 1000L);
    }

    public static Pointer interval(java.time.Duration d) {
        return GeneratedFunctions.interval_in(d.toString(), -1);
    }

    public static java.time.Duration duration(Pointer p) {
        ISO_INTERVALS.get();
        return java.time.Duration.parse(GeneratedFunctions.interval_out(p));
    }
}
'''


def _runtime_src(pkg, imports, doc, planning):
    """The MeosSqlRuntime class of the surface in `pkg`: its imports and heading comment, the
    engine's planning hook, and the conversions and releases both engines share."""
    return (f'package {pkg};\n\n' + imports + doc + 'public final class MeosSqlRuntime {\n\n'
            + _RUNTIME_FIELDS + planning + _RUNTIME_CORE)


SQL_SUPPORT = {
    'MeosValue': f'''package {SQL_PKG};

import java.util.Arrays;
import jnr.ffi.Pointer;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * A MEOS value as Flink holds it: the serialized form its catalog codec writes, as bytes —
 * the WKB itself, or the UTF-8 of a string form. */
public abstract class MeosValue {{

    public final byte[] form;

    protected MeosValue(byte[] form) {{
        this.form = form;
    }}

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public abstract Pointer decode();

    /** The hex text of bytes, the readable form of a WKB. */
    public static String hex(byte[] b) {{
        StringBuilder s = new StringBuilder(2 * b.length);
        for (byte x : b) {{
            s.append(Character.forDigit((x >> 4) & 0xF, 16)).append(Character.forDigit(x & 0xF, 16));
        }}
        return s.toString().toUpperCase();
    }}

    @Override
    public boolean equals(Object o) {{
        return o != null && o.getClass() == getClass() && Arrays.equals(form, ((MeosValue) o).form);
    }}

    @Override
    public int hashCode() {{
        return Arrays.hashCode(form);
    }}
}}
''',
    'MeosValueSerializer': f'''package {SQL_PKG};

import java.io.IOException;
import org.apache.flink.api.common.typeutils.base.TypeSerializerSingleton;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Writes a MEOS value as the length of its form and the form's bytes, so a value of any size
 * crosses; the value is immutable. */
public abstract class MeosValueSerializer<T extends MeosValue> extends TypeSerializerSingleton<T> {{

    protected abstract T make(byte[] form);

    @Override public boolean isImmutableType() {{ return true; }}
    @Override public T createInstance() {{ return make(new byte[0]); }}
    @Override public T copy(T from) {{ return from; }}
    @Override public T copy(T from, T reuse) {{ return from; }}
    @Override public int getLength() {{ return -1; }}

    @Override
    public void serialize(T value, DataOutputView out) throws IOException {{
        out.writeInt(value.form.length);
        out.write(value.form);
    }}

    @Override
    public T deserialize(DataInputView in) throws IOException {{
        byte[] b = new byte[in.readInt()];
        in.readFully(b);
        return make(b);
    }}

    @Override
    public T deserialize(T reuse, DataInputView in) throws IOException {{
        return deserialize(in);
    }}

    @Override
    public void copy(DataInputView in, DataOutputView out) throws IOException {{
        int n = in.readInt();
        out.writeInt(n);
        out.write(in, n);
    }}
}}
''',
    'MeosSqlRuntime': _runtime_src(SQL_PKG, _FLINK_RUNTIME_IMPORTS, _FLINK_RUNTIME_DOC,
                                   _FLINK_INFERENCE),
}


def run_flink_sql(args):
    cat = load_catalog(args.catalog)
    jmeos = parse_jmeos_signatures(args.jar)
    m = SqlModel(cat, jmeos)
    root = Path(args.out) / 'src/main/java' / SQL_PKG.replace('.', '/')
    for sub in ('', 'types', 'functions'):
        d = root / sub
        d.mkdir(parents=True, exist_ok=True)
        for old in d.glob('*.java'):
            old.unlink()
    for name, src in SQL_SUPPORT.items():
        (root / f'{name}.java').write_text(src)
    for sql in m.value_class:
        (root / 'types' / f'{m.value_class[sql]}.java').write_text(_value_class_src(m, sql))

    # The catalog struct layouts, under #struct_layout of codegen_spark_udfs.py, the rule the
    # Spark arm sizes them by.
    spark = _spark_module()
    spark.STRUCTS.update({s['name']: s for s in cat.get('structs') or []})
    layout = lambda name: spark.struct_layout(name) if name in spark.STRUCTS else None  # noqa: E731

    names = defaultdict(list)          # SQL name -> eval methods
    seen = defaultdict(set)
    skipped = defaultdict(int)
    nset = 0
    for f in m.fns:
        if not f.get('sqlfn') or f.get('sqlfnBackingOnly') or f.get('api') == 'internal':
            continue
        jsig = jmeos.get(f['name'])
        if jsig is None:
            skipped['not in jar'] += 1
            continue
        for (sqlname, sargs, sret, sdef, sbound), sig in zip(m.signatures(f),
                                                             f.get('sqlSignatures') or []):
            if sig.get('retSet'):
                ov, why = _setret_overload(m, f, sig, jsig, sbound, layout)
                nset += ov is not None
            else:
                ov, why = _overload(m, f, sargs, sret, jsig, sbound)
            if ov is None:
                skipped[why.split('/')[0]] += 1
                continue
            defaults = (list(sdef) + [None] * len(sargs))[:len(sargs)]
            variants = [None]
            for k in range(1, len(sargs) + 1):
                lits = [_default_literal(sargs[len(sargs) - k + j], defaults[len(sargs) - k + j])
                        for j in range(k)]
                if any(x is None for x in lits):
                    break
                variants.append(lits)
            for dv in variants:
                shown = ov[0] if dv is None else ov[0][:len(ov[0]) - len(dv)]
                key = tuple(t for t, _ in shown)
                if key in seen[sqlname]:
                    continue
                seen[sqlname].add(key)
                names[sqlname].append((_emit_eval(ov, dv), list(key), ov[4]))

    classes = {}
    taken = set()
    for sqlname in sorted(names):
        cls = _javaid(sqlname)
        while cls.lower() in taken:
            cls += '_'
        taken.add(cls.lower())
        classes[sqlname] = cls
        sigs = names[sqlname]
        body = [f'package {SQL_PKG}.functions;', '',
                'import functions.GeneratedFunctions;',
                'import java.time.OffsetDateTime;',
                'import jnr.ffi.Pointer;',
                'import org.apache.flink.table.api.DataTypes;',
                'import org.apache.flink.table.catalog.DataTypeFactory;',
                'import org.apache.flink.table.functions.ScalarFunction;',
                'import org.apache.flink.table.types.DataType;',
                'import org.apache.flink.table.types.inference.TypeInference;',
                f'import {SQL_PKG}.MeosSqlRuntime;', '',
                '/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.',
                f' * The MobilityDB SQL function {sqlname}: {len(sigs)} overload(s). */',
                f'public final class {cls} extends ScalarFunction {{', '']
        for ev, _, _ in sigs:
            body += ev
        body += ['    @Override',
                 '    public TypeInference getTypeInference(DataTypeFactory typeFactory) {',
                 '        return MeosSqlRuntime.inference(new DataType[][] {']
        body += ['            {' + ', '.join(_datatype(a) for a in args) + '},'
                 for _, args, _ in sigs]
        body += ['        }, new DataType[] {']
        body += [f'            {r},' for _, _, r in sigs]
        body += ['        });', '    }']
        body.append('}')
        (root / 'functions' / f'{cls}.java').write_text('\n'.join(body) + '\n')

    reg = [f'package {SQL_PKG};', '',
           'import org.apache.flink.table.api.TableEnvironment;', '',
           '/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.',
           ' * Registers every generated function under its MobilityDB SQL name as a catalog',
           ' * function, which never shadows a Flink built-in of the same name. */',
           'public final class MobilityFlinkSql {', '',
           '    private MobilityFlinkSql() { }', '']
    items = sorted(classes.items())
    chunks = [items[i:i + 200] for i in range(0, len(items), 200)]
    reg.append('    public static void registerAll(TableEnvironment tEnv) {')
    reg += [f'        register{i}(tEnv);' for i in range(len(chunks))]
    reg += ['    }', '']
    for i, chunk in enumerate(chunks):
        reg.append(f'    private static void register{i}(TableEnvironment tEnv) {{')
        reg += [f'        tEnv.createTemporaryFunction("{n}", {SQL_PKG}.functions.{c}.class);'
                for n, c in chunk]
        reg += ['    }', '']
    reg.append('}')
    (root / 'MobilityFlinkSql.java').write_text('\n'.join(reg) + '\n')

    n_ov = sum(len(v) for v in names.values())
    print(f'flink-sql: {len(classes)} SQL functions ({n_ov} overloads, {nset} of them '
          f'set-returning signatures), {len(m.value_class)} value types into {root}')
    for why, n in sorted(skipped.items(), key=lambda x: -x[1]):
        print(f'  skipped {n:5d}  {why}')


# ───────────────────────── spark-sql: the typed Spark SQL surface ─────────────────────────
# The Spark twin of the flink-sql engine, on the same SqlModel (rule 8 of the portable naming
# notes): a MEOS value carries its SQL type, one Spark UserDefinedType per SQL type over the
# form its catalog codec writes, as Flink holds one RAW type per SQL type (#_value_class_src);
# and each SQL name is one registration whose builder chooses the overload and its result type
# from the argument types while Spark plans the call, as Flink's type inference does
# (#_FLINK_INFERENCE). The overloads are the flink-sql engine's own (#_overload, #_emit_eval),
# so a capability one engine gains the other gains with it.

SPARK_SQL_PKG = 'org.mobilitydb.spark.sql'

# The Spark SQL type of each Java class an overload takes or returns.
SPARK_DATATYPE = {
    'Boolean': 'DataTypes.BooleanType', 'Integer': 'DataTypes.IntegerType',
    'Short': 'DataTypes.ShortType', 'Long': 'DataTypes.LongType',
    'Double': 'DataTypes.DoubleType', 'String': 'DataTypes.StringType',
    'java.time.Instant': 'DataTypes.TimestampType', 'java.time.LocalDate': 'DataTypes.DateType',
    'java.time.Duration': 'MeosSqlRuntime.DURATION',
}


def _spark_datatype(m, cls):
    """The Spark DataType expression of a Java class #_overload names, or None."""
    if cls.endswith('[]'):
        inner = _spark_datatype(m, cls[:-2])
        return inner and f'DataTypes.createArrayType({inner})'
    if cls.startswith(f'{m.pkg}.types.'):
        return f'{cls}.TYPE'
    return SPARK_DATATYPE.get(cls)


def _spark_arg(cls, i):
    """The Java expression reading argument i, as Spark hands it, into the class `cls`."""
    a = f'a[{i}]'
    if cls == 'java.time.Instant':
        return f'MeosSqlRuntime.instant({a})'
    if cls == 'java.time.LocalDate':
        return f'MeosSqlRuntime.localDate({a})'
    if cls == 'java.time.Instant[]':
        return f'MeosSqlRuntime.instants({a})'
    if cls == 'java.time.LocalDate[]':
        return f'MeosSqlRuntime.localDates({a})'
    if cls.endswith('[]'):
        return f'MeosSqlRuntime.array({a}, new {cls[:-2]}[0])'
    return f'({cls}) {a}'


_SPARK_RUNTIME_IMPORTS = '''import functions.GeneratedFunctions;
import java.util.ArrayList;
import java.util.List;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.FunctionIdentifier;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.expressions.Cast;
import org.apache.spark.sql.catalyst.expressions.EvalMode;
import org.apache.spark.sql.catalyst.expressions.Expression;
import org.apache.spark.sql.catalyst.expressions.ScalaUDF;
import org.apache.spark.sql.internal.SQLConf;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DayTimeIntervalType;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.NullType;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.Seq;
import scala.jdk.javaapi.CollectionConverters;
import scala.runtime.AbstractFunction1;

'''

_SPARK_RUNTIME_DOC = '''/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Conversions between Spark's SQL values and the ones MEOS takes, the release of what MEOS
 * allocates, and the registration of each SQL name with the builder that resolves its
 * overloads.  MEOS counts days and microseconds from the PostgreSQL epoch. */
'''


def _spark_planning(arity):
    """The Spark planning hook of MeosSqlRuntime, the twin of #_FLINK_INFERENCE: one registration
    per name whose builder resolves the overload from the argument types, and the function
    classes a resolved call runs, up to `arity` arguments."""
    fns = []
    for n in range(arity + 1):
        ps = ', '.join(f'Object a{i}' for i in range(n))
        gen = ', '.join(['Object'] * (n + 1))
        args = ', '.join(f'a{i}' for i in range(n))
        fns.append(f'''    private static final class F{n} extends scala.runtime.AbstractFunction{n}<{gen}>
            implements java.io.Serializable {{
        private final Body b;

        F{n}(Body b) {{
            this.b = b;
        }}

        @Override
        public Object apply({ps}) {{
            return b.apply(new Object[] {{{args}}});
        }}
    }}
''')
    cases = ''.join(f'            case {n}: return new F{n}(b);\n' for n in range(arity + 1))
    return '''    /** The Spark type of an interval, from days to seconds, the one java.time.Duration maps to. */
    public static final DataType DURATION = DataTypes.createDayTimeIntervalType();

    /** One overload of a SQL name: the Spark types of its arguments in order, its result type
     * and the body computing it. */
    public static final class Overload implements java.io.Serializable {

        final DataType[] args;
        final DataType ret;
        final Body body;

        public Overload(DataType[] args, DataType ret, Body body) {
            this.args = args;
            this.ret = ret;
            this.body = body;
        }
    }

    /** The body of an overload over the arguments Spark hands it. */
    public interface Body extends java.io.Serializable {
        Object apply(Object[] a);
    }

    /** Register name once: its builder chooses among the overloads from the argument types while
     * Spark plans the call, as PostgreSQL resolves an overloaded function, and hands a call no
     * overload takes to the function Spark held under that name before, so a Spark built-in of
     * the same name keeps answering its own arguments. */
    public static void register(SparkSession spark, String name, Overload... overloads) {
        Option<Function1<Seq<Expression>, Expression>> before =
            spark.sessionState().functionRegistry().lookupFunctionBuilder(FunctionIdentifier.apply(name));
        spark.sessionState().functionRegistry().createOrReplaceTempFunction(name,
            new AbstractFunction1<Seq<Expression>, Expression>() {
                @Override
                public Expression apply(Seq<Expression> args) {
                    return resolve(name, overloads, args, before);
                }
            }, "scala_udf");
    }

    /** The call of the overload whose argument types the arguments have, else of the first one
     * they reach by widening a number, as PostgreSQL's implicit casts reach it. A name Spark
     * already answers keeps a call no overload takes as it stands for that function: round(2.5)
     * stays Spark's own, as PostgreSQL resolves it to its numeric round, not to MEOS's. */
    static Expression resolve(String name, Overload[] overloads, Seq<Expression> args,
            Option<Function1<Seq<Expression>, Expression>> before) {
        List<Expression> given = CollectionConverters.asJava(args);
        for (boolean widen : before.isDefined() ? new boolean[] {false} : new boolean[] {false, true}) {
            for (Overload o : overloads) {
                List<Expression> in = fit(o, given, widen);
                if (in != null) {
                    return call(name, o, in);
                }
            }
        }
        if (before.isDefined()) {
            return before.get().apply(args);
        }
        StringBuilder types = new StringBuilder();
        for (Expression e : given) {
            types.append(types.length() == 0 ? "" : ", ").append(e.dataType().simpleString());
        }
        throw new IllegalArgumentException("function " + name + "(" + types + ") does not exist");
    }

    private static List<Expression> fit(Overload o, List<Expression> given, boolean widen) {
        if (o.args.length != given.size()) {
            return null;
        }
        List<Expression> in = new ArrayList<>();
        for (int i = 0; i < o.args.length; i++) {
            Expression e = given.get(i);
            DataType have = e.dataType();
            if (have.equals(o.args[i])) {
                in.add(e);
            } else if (have instanceof NullType || sameKind(have, o.args[i])
                    || (widen && widens(have, o.args[i]))) {
                in.add(new Cast(e, o.args[i], Option.<String>empty(), EvalMode.fromSQLConf(SQLConf.get())));
            } else {
                return null;
            }
        }
        return in;
    }

    /** Whether two types hold the same Java values and differ only in what Spark notes of them:
     * the fields an interval states (INTERVAL '1' DAY is an interval day, the overload's an
     * interval day to second, one java.time.Duration), or whether an array may hold a null
     * (array(3, 1, 2) cannot, an overload's array may, one Java array). */
    private static boolean sameKind(DataType have, DataType want) {
        if (have instanceof DayTimeIntervalType && want instanceof DayTimeIntervalType) {
            return true;
        }
        return have instanceof ArrayType && want instanceof ArrayType
            && ((ArrayType) have).elementType().equals(((ArrayType) want).elementType());
    }

    /** Whether a number of type have reaches type want without loss, as PostgreSQL's implicit
     * numeric casts do: a smaller integer to a larger one, any number to a double. */
    private static boolean widens(DataType have, DataType want) {
        boolean integral = have.equals(DataTypes.ByteType) || have.equals(DataTypes.ShortType)
            || have.equals(DataTypes.IntegerType);
        if (want.equals(DataTypes.LongType)) {
            return integral;
        }
        if (want.equals(DataTypes.IntegerType)) {
            return have.equals(DataTypes.ByteType) || have.equals(DataTypes.ShortType);
        }
        if (want.equals(DataTypes.DoubleType)) {
            return integral || have.equals(DataTypes.LongType) || have.equals(DataTypes.FloatType)
                || have instanceof DecimalType;
        }
        return false;
    }

    private static Expression call(String name, Overload o, List<Expression> in) {
        List<Option<ExpressionEncoder<?>>> enc = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            enc.add(Option.empty());
        }
        return new ScalaUDF(function(o.body, in.size()), o.ret, CollectionConverters.asScala(in).toList(),
            CollectionConverters.asScala(enc).toList(), Option.empty(), Option.apply(name), true, true);
    }

    private static Object function(Body b, int n) {
        switch (n) {
''' + cases + '''            default: throw new IllegalArgumentException("too many arguments: " + n);
        }
    }

''' + '\n'.join(fns) + '''
    /** A timestamp as Spark hands it, java.sql.Timestamp or java.time.Instant. */
    public static java.time.Instant instant(Object o) {
        return o instanceof java.sql.Timestamp ? ((java.sql.Timestamp) o).toInstant() : (java.time.Instant) o;
    }

    /** A date as Spark hands it, java.sql.Date or java.time.LocalDate. */
    public static java.time.LocalDate localDate(Object o) {
        return o instanceof java.sql.Date ? ((java.sql.Date) o).toLocalDate() : (java.time.LocalDate) o;
    }

    /** An array of timestamps as Spark hands it, each element read as #instant reads one. */
    public static java.time.Instant[] instants(Object o) {
        Object[] xs = array(o, new Object[0]);
        java.time.Instant[] r = new java.time.Instant[xs.length];
        for (int i = 0; i < xs.length; i++) {
            r[i] = xs[i] == null ? null : instant(xs[i]);
        }
        return r;
    }

    /** An array of dates as Spark hands it, each element read as #localDate reads one. */
    public static java.time.LocalDate[] localDates(Object o) {
        Object[] xs = array(o, new Object[0]);
        java.time.LocalDate[] r = new java.time.LocalDate[xs.length];
        for (int i = 0; i < xs.length; i++) {
            r[i] = xs[i] == null ? null : localDate(xs[i]);
        }
        return r;
    }

    /** An array as Spark hands it, a Scala sequence, as the Java array of its elements. */
    @SuppressWarnings("unchecked")
    public static <T> T[] array(Object o, T[] empty) {
        if (o instanceof scala.collection.Seq) {
            return CollectionConverters.asJava((scala.collection.Seq<T>) o).toArray(empty);
        }
        return java.util.Arrays.copyOf((Object[]) o, ((Object[]) o).length,
            (Class<T[]>) empty.getClass());
    }

'''


def _spark_value_class_src(m, sql):
    """The Spark value of the SQL type `sql`: the class holding the form its catalog codec writes,
    as #_value_class_src writes the Flink one, and the UserDefinedType Spark carries it as."""
    cls = m.value_class[sql]
    kind, dec, enc, in_aux, out_aux = m.codec[sql]
    daux = ''.join(', ' + str(a.get('default', 0)) for a in in_aux)
    eaux = f', (byte) {WKB_VARIANT}' if kind in ('wkb', 'bytes') \
        else ''.join(', ' + str(a.get('default', 0)) for a in out_aux)
    if kind == 'bytes':
        decode = f'GeneratedFunctions.{dec}(form)'
        encode = (f'byte[] b = GeneratedFunctions.{enc}(p{eaux});\n'
                  f'        return b == null ? null : new {cls}(b);')
        render = 'MeosValue.hex(form)'
    else:
        decode = f'GeneratedFunctions.{dec}(new String(form, StandardCharsets.UTF_8){daux})'
        encode = (f'String s = GeneratedFunctions.{enc}(p{eaux});\n'
                  f'        return s == null ? null : new {cls}(s.getBytes(StandardCharsets.UTF_8));')
        render = 'new String(form, StandardCharsets.UTF_8)'
    return f'''package {m.pkg}.types;

import functions.GeneratedFunctions;
import java.nio.charset.StandardCharsets;
import jnr.ffi.Pointer;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.SQLUserDefinedType;
import org.apache.spark.sql.types.UserDefinedType;
import {m.pkg}.MeosValue;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * The SQL type {sql}, carried as its {kind} form ({dec} / {enc}). */
@SQLUserDefinedType(udt = {cls}.UDT.class)
public final class {cls} extends MeosValue {{

    /** The Spark SQL type of this value. */
    public static final DataType TYPE = new UDT();

    public {cls}(byte[] form) {{
        super(form);
    }}

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public Pointer decode() {{
        return {decode};
    }}

    /** The value MEOS holds at p, in the form this type carries. */
    public static {cls} encode(Pointer p) {{
        {encode}
    }}

    @Override
    public String toString() {{
        return {render};
    }}

    /** The SQL type {sql} as Spark carries it: the bytes of the form. */
    public static final class UDT extends UserDefinedType<{cls}> {{
        @Override
        public DataType sqlType() {{
            return DataTypes.BinaryType;
        }}

        @Override
        public Object serialize({cls} v) {{
            return v.form;
        }}

        @Override
        public {cls} deserialize(Object d) {{
            return new {cls}((byte[]) d);
        }}

        @Override
        public Class<{cls}> userClass() {{
            return {cls}.class;
        }}

        @Override
        public String typeName() {{
            return "{sql}";
        }}
    }}
}}
'''


_SPARK_MEOS_VALUE = '''package {pkg};

import java.util.Arrays;
import jnr.ffi.Pointer;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * A MEOS value as Spark holds it: the serialized form its catalog codec writes, as bytes —
 * the WKB itself, or the UTF-8 of a string form. */
public abstract class MeosValue implements java.io.Serializable {

    public final byte[] form;

    protected MeosValue(byte[] form) {
        this.form = form;
    }

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public abstract Pointer decode();

    /** The hex text of bytes, the readable form of a WKB. */
    public static String hex(byte[] b) {
        StringBuilder s = new StringBuilder(2 * b.length);
        for (byte x : b) {
            s.append(Character.forDigit((x >> 4) & 0xF, 16)).append(Character.forDigit(x & 0xF, 16));
        }
        return s.toString().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.getClass() == getClass() && Arrays.equals(form, ((MeosValue) o).form);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(form);
    }
}
'''


def run_spark_sql(args):
    """The typed Spark SQL surface: the flink-sql engine's overloads, registered once per SQL name
    through #_spark_planning's builder, over a UserDefinedType per SQL value type."""
    cat = load_catalog(args.catalog)
    jmeos = parse_jmeos_signatures(args.jar)
    m = SqlModel(cat, jmeos, SPARK_SQL_PKG, 'spark')
    root = Path(args.out) / 'src/main/java' / SPARK_SQL_PKG.replace('.', '/')
    for sub in ('', 'types', 'functions'):
        d = root / sub
        d.mkdir(parents=True, exist_ok=True)
        for old in d.glob('*.java'):
            old.unlink()
    for sql in m.value_class:
        (root / 'types' / f'{m.value_class[sql]}.java').write_text(_spark_value_class_src(m, sql))

    # The catalog struct layouts, under #struct_layout of codegen_spark_udfs.py, as the flink-sql
    # engine reads them for a set-returning signature's rows.
    spark = _spark_module()
    spark.STRUCTS.update({s['name']: s for s in cat.get('structs') or []})
    layout = lambda name: spark.struct_layout(name) if name in spark.STRUCTS else None  # noqa: E731

    names = defaultdict(list)          # SQL name -> (eval lines, Spark arg types, ret, classes)
    seen = defaultdict(set)
    skipped = defaultdict(int)
    nset = 0
    for f in m.fns:
        if not f.get('sqlfn') or f.get('sqlfnBackingOnly') or f.get('api') != 'public':
            continue
        jsig = jmeos.get(f['name'])
        if jsig is None:
            skipped['not in jar'] += 1
            continue
        for (sqlname, sargs, sret, sdef, sbound), sig in zip(m.signatures(f),
                                                             f.get('sqlSignatures') or []):
            if sig.get('retSet'):
                ov, why = _setret_overload(m, f, sig, jsig, sbound, layout)
                nset += ov is not None
            else:
                ov, why = _overload(m, f, sargs, sret, jsig, sbound)
            if ov is None:
                skipped[why.split('/')[0]] += 1
                continue
            ret = ov[4]
            if ret is None:
                skipped['spark:ret'] += 1
                continue
            defaults = (list(sdef) + [None] * len(sargs))[:len(sargs)]
            variants = [None]
            for k in range(1, len(sargs) + 1):
                lits = [_default_literal(sargs[len(sargs) - k + j], defaults[len(sargs) - k + j])
                        for j in range(k)]
                if any(x is None for x in lits):
                    break
                variants.append(lits)
            for dv in variants:
                shown = ov[0] if dv is None else ov[0][:len(ov[0]) - len(dv)]
                key = tuple(t for t, _ in shown)
                if key in seen[sqlname]:
                    continue
                types = [_spark_datatype(m, t) for t in key]
                if None in types:
                    skipped['spark:arg'] += 1
                    continue
                seen[sqlname].add(key)
                names[sqlname].append((_emit_eval(ov, dv), types, ret, list(key)))

    arity = max((len(t) for sigs in names.values() for _, t, _, _ in sigs), default=0)
    (root / 'MeosValue.java').write_text(_SPARK_MEOS_VALUE.replace('{pkg}', SPARK_SQL_PKG))
    (root / 'MeosSqlRuntime.java').write_text(
        _runtime_src(SPARK_SQL_PKG, _SPARK_RUNTIME_IMPORTS, _SPARK_RUNTIME_DOC,
                     _spark_planning(arity)))

    classes = {}
    taken = set()
    for sqlname in sorted(names):
        cls = _javaid(sqlname)
        while cls.lower() in taken:
            cls += '_'
        taken.add(cls.lower())
        classes[sqlname] = cls
        sigs = names[sqlname]
        body = [f'package {SPARK_SQL_PKG}.functions;', '',
                'import functions.GeneratedFunctions;',
                'import java.time.OffsetDateTime;',
                'import jnr.ffi.Pointer;',
                'import org.apache.spark.sql.types.DataType;',
                'import org.apache.spark.sql.types.DataTypes;',
                f'import {SPARK_SQL_PKG}.MeosSqlRuntime;', '',
                '/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.',
                f' * The MobilityDB SQL function {sqlname}: {len(sigs)} overload(s). */',
                f'public final class {cls} {{', '',
                f'    private {cls}() {{ }}', '']
        for k, (ev, _, _, _) in enumerate(sigs):
            body += [ev[0].replace('    public ', '    static ', 1).replace(' eval(', f' e{k}(', 1)] \
                + ev[1:]
        body += ['    /** The overloads of this name, in the order the builder tries them. */',
                 '    public static MeosSqlRuntime.Overload[] overloads() {',
                 '        return new MeosSqlRuntime.Overload[] {']
        for k, (_, types, ret, jcls) in enumerate(sigs):
            call = ', '.join(_spark_arg(c, i) for i, c in enumerate(jcls))
            body.append(f'            new MeosSqlRuntime.Overload(new DataType[] {{{", ".join(types)}}}, '
                        f'{ret}, a -> e{k}({call})),')
        body += ['        };', '    }', '}']
        (root / 'functions' / f'{cls}.java').write_text('\n'.join(body) + '\n')

    reg = [f'package {SPARK_SQL_PKG};', '',
           'import org.apache.spark.sql.SparkSession;', '',
           '/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.',
           ' * Registers every generated function under its MobilityDB SQL name, each name once with',
           ' * the builder that resolves its overloads from the argument types. */',
           'public final class MobilitySparkSql {', '',
           '    private MobilitySparkSql() { }', '']
    items = sorted(classes.items())
    chunks = [items[i:i + 200] for i in range(0, len(items), 200)]
    reg.append('    public static void registerAll(SparkSession spark) {')
    reg += [f'        register{i}(spark);' for i in range(len(chunks))]
    reg += ['    }', '']
    for i, chunk in enumerate(chunks):
        reg.append(f'    private static void register{i}(SparkSession spark) {{')
        reg += [f'        MeosSqlRuntime.register(spark, "{n}", {SPARK_SQL_PKG}.functions.{c}.overloads());'
                for n, c in chunk]
        reg += ['    }', '']
    reg.append('}')
    (root / 'MobilitySparkSql.java').write_text('\n'.join(reg) + '\n')

    n_ov = sum(len(v) for v in names.values())
    print(f'spark-sql: {len(classes)} SQL functions ({n_ov} overloads, {nset} of them '
          f'set-returning signatures), {len(m.value_class)} value types into {root}')
    for why, n in sorted(skipped.items(), key=lambda x: -x[1]):
        print(f'  skipped {n:5d}  {why}')


# ───────────────────────── entry point ─────────────────────────

def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument('--engine', required=True, choices=['spark', 'flink', 'kafka', 'flink-sql', 'spark-sql'])
    ap.add_argument('--catalog', required=True, help='MEOS-API meos-idl.json')
    ap.add_argument('--jar', required=True,
                    help='JMEOS jar with functions.GeneratedFunctions')
    ap.add_argument('--out', required=True, help='output directory')
    ap.add_argument('--package', default='org.mobilitydb.meos',
                    help='facade package (flink/kafka only)')
    ap.add_argument('--report', action='store_true', help='spark only')
    ap.add_argument('--gaps', help='spark only: the ledger of unreached public functions')
    ap.add_argument('--rebaseline', action='store_true', help='spark only: rewrite --gaps')
    args = ap.parse_args()

    if args.engine == 'spark':
        run_spark(args)
    elif args.engine == 'flink-sql':
        run_flink_sql(args)
    elif args.engine == 'spark-sql':
        run_spark_sql(args)
    else:
        run_facades(args)


if __name__ == '__main__':
    main()
