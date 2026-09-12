#!/usr/bin/env python3
"""Unified MEOS-surface code generator for the MobilityDB JVM bindings.

ONE generator, three engines, selected by ``--engine {spark|flink|kafka}``. Every
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

Shared front-end (facade back-end only): load the catalog, list the jar symbols,
and derive each function's object-model class / role / header directly from the
catalog's ``objectModel``. The spark back-end owns its own catalog+jar front-end
(it needs jar arities the facade parse does not), so nothing about it changes.

Usage:
  codegen_jvm.py --engine spark --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine flink --catalog meos-idl.json --jar JMEOS.jar --out DIR
  codegen_jvm.py --engine kafka --catalog meos-idl.json --jar JMEOS.jar --out DIR
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

def run_spark(args):
    """Delegate to the sibling codegen_spark_udfs.py so output is byte-identical.

    The reference generator owns the whole catalog+jar front-end and the SQL-UDF
    emit; running its own code (rather than a re-implementation) is what makes the
    output provably identical to today's. Every binding vendors codegen_spark_udfs.py
    next to this file, so the import target is always the sibling."""
    spark_path = Path(__file__).resolve().parent / 'codegen_spark_udfs.py'
    spec = importlib.util.spec_from_file_location('codegen_spark_udfs', spark_path)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    argv = ['codegen_spark_udfs',
            '--catalog', args.catalog,
            '--out', args.out,
            '--jar', args.jar]
    if args.report:
        argv.append('--report')
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
    empty = 'new PairsAndPeriods(new int[0][], new String[0])' if fold['periods'] \
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
    '        public final String[] periodsHexwkb;',
    '',
    '        PairsAndPeriods(int[][] pairs, String[] periodsHexwkb) {',
    '            this.pairs = pairs;',
    '            this.periodsHexwkb = periodsHexwkb;',
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
     * explicitly.  Unsafe.freeMemory calls that system free; loading libc through
     * LibraryLoader instead hits classloader-boundary trouble inside the engines. */
    private static final sun.misc.Unsafe UNSAFE;

    static {{
        try {{
            java.lang.reflect.Field f =
                    sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) f.get(null);
        }} catch (ReflectiveOperationException e) {{
            throw new ExceptionInInitializerError(e);
        }}
    }}

    /** Free a native pointer owned by the caller.  Null-safe. */
    public static void free(jnr.ffi.Pointer p) {{
        if (p != null) {{
            UNSAFE.freeMemory(p.address());
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
     * the times when each resulting pair holds; each is rendered as hex-WKB.  Frees the
     * pairs, the span-set array and every span set in it. */
    public static String[] readPeriods(jnr.ffi.Pointer ssArr, int count) {{
        String[] out = new String[Math.max(0, count)];
        for (int k = 0; k < out.length; k++) {{
            jnr.ffi.Pointer ss = ssArr == null
                    ? null : ssArr.getPointer((long) k * 8L);
            out[k] = ss == null
                    ? null : GeneratedFunctions.spanset_as_hexwkb(ss, (byte) 0);
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

# The WKB variant the hex encoders write: the extended form, which keeps the SRID.
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

    def __init__(self, cat, jmeos):
        self.cat = cat
        self.jmeos = jmeos
        self.fns = cat['functions']
        self.by_name = {f['name']: f for f in self.fns}
        self.enc = cat.get('typeEncodings', {})
        self.enums = {e['name'] for e in cat.get('enums', [])}
        self._align()
        self._codecs()
        self._enum_parsers()

    def visible(self, f):
        outs = set((f.get('shape') or {}).get('outParams') or [])
        return [p for p in f['params'] if p['name'] not in outs], \
               [p for p in f['params'] if p['name'] in outs]

    def signatures(self, f):
        """(sqlName, args, ret, argDefaults) for every SQL signature of f."""
        for s in f.get('sqlSignatures') or []:
            yield (s.get('sqlName') or f['sqlfn'], s.get('args') or [], s.get('ret'),
                   s.get('argDefaults') or [])

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
            for _, args, ret, _ in self.signatures(f):
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

        WKB where the catalog states a WKB decoder and an asHexWKB encoder for the C
        type: the WKB carries the value's own type, so one codec serves every SQL type
        the C type stands for.  Text otherwise, and only for a C type a single SQL type
        stands for, since a text decoder cannot tell those SQL types apart."""
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
            if dec.get('wkb') in self.jmeos and cb in hexwkb:
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
        """For each C enum, the catalog function reading it from its name."""
        self.enum_parser = {}
        for f in self.fns:
            rt = _norm(f['returnType']['canonical'])
            ps = f['params']
            if rt in self.enums and len(ps) == 1 and _norm(ps[0]['canonical']) == 'char *' \
                    and f['name'] in self.jmeos:
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
        return (f'{SQL_PKG}.types.{vc}',
                ['if (_r == null) return null;',
                 f'try {{ return {SQL_PKG}.types.{vc}.encode(_r); }}',
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
        return f'{SQL_PKG}.types.{m.value_class[sql]}'
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
        return f'{SQL_PKG}.types.{m.value_class[elem]}[]', t
    hit = SQL_ARRAY_SCALAR.get((elem, _base(el['c']))) \
        or SQL_ARRAY_SCALAR.get((elem, _base(el['canonical'])))
    if hit and c.count('*') == 1:
        temps.append((t, f'MeosSqlRuntime.{hit[1]}({name})', 'buffer'))
        return f'{hit[0]}[]', t
    return None


def _overload(m, f, args, ret, jsig):
    """The eval method for one signature, or (None, reason).

    The SQL arguments pair with the visible C parameters in order, except that a SQL array
    stands for the C array shape.inputArrays names together with the count its lengthFrom
    names, which the eval passes as the length of the Flink array."""
    vis, outs = m.visible(f)
    outs = [p for p in outs if _norm(p['canonical']) != 'size_t *']
    if len(jsig['arg_types']) != len(vis):
        return None, 'arity:jmeos'
    arrays = {a['param']: a for a in (f.get('shape') or {}).get('inputArrays') or []}
    counts = {}
    for a in arrays.values():
        lf = a.get('lengthFrom') or {}
        if lf.get('kind') != 'param' or lf.get('name') not in {p['name'] for p in vis}:
            return None, 'array:length'
        counts[lf['name']] = a['param']
    walk = [i for i, p in enumerate(vis) if p['name'] not in counts]
    if len(args) != len(walk):
        return None, 'arity:sql'
    params, temps, passed = [], [], {}
    call = [None] * len(vis)
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
    r = _ret(m, ret, f, jsig['ret'], outs)
    if r is None:
        return None, f'ret:{ret}/{_norm(f["returnType"]["canonical"])}'
    return (params, call, temps, r, f['name'], jsig['ret']), None


def _emit_eval(ov, defaults=None):
    params, call, temps, (rcls, rstmts), cname, jret = ov
    shown = params if defaults is None else params[:len(params) - len(defaults)]
    L = [f'    public {rcls} eval({", ".join(f"{t} {n}" for t, n in shown)}) {{']
    if shown:
        L.append(f'        if ({" || ".join(f"{n} == null" for _, n in shown)}) return null;')
    # An argument left to its SQL default is that default, converted like any other.
    for (t, n), lit in zip(params[len(shown):], defaults or []):
        L.append(f'        {t} {n} = {lit};')
    jr = _jshort(jret)
    body = [f'{jr} _r = GeneratedFunctions.{cname}({", ".join(call)});'] + rstmts
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


def _value_class_src(m, sql):
    cls = m.value_class[sql]
    kind, dec, enc, in_aux, out_aux = m.codec[sql]
    daux = ''.join(', ' + str(a.get('default', 0)) for a in in_aux)
    eaux = f', (byte) {WKB_VARIANT}' if kind == 'wkb' \
        else ''.join(', ' + str(a.get('default', 0)) for a in out_aux)
    return f'''package {SQL_PKG}.types;

import functions.GeneratedFunctions;
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

    public {cls}(String form) {{
        super(form);
    }}

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public Pointer decode() {{
        return GeneratedFunctions.{dec}(form{daux});
    }}

    /** The value MEOS holds at p, in the form this type carries. */
    public static {cls} encode(Pointer p) {{
        String s = GeneratedFunctions.{enc}(p{eaux});
        return s == null ? null : new {cls}(s);
    }}

    public static final class Serializer extends MeosValueSerializer<{cls}> {{
        @Override
        protected {cls} make(String form) {{
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


SQL_SUPPORT = {
    'MeosValue': f'''package {SQL_PKG};

import jnr.ffi.Pointer;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * A MEOS value as Flink holds it: the serialized form its catalog codec writes. */
public abstract class MeosValue {{

    public final String form;

    protected MeosValue(String form) {{
        this.form = form;
    }}

    /** The MEOS value this one carries, allocated by MEOS for the caller to free. */
    public abstract Pointer decode();

    @Override
    public boolean equals(Object o) {{
        return o != null && o.getClass() == getClass() && form.equals(((MeosValue) o).form);
    }}

    @Override
    public int hashCode() {{
        return form.hashCode();
    }}

    @Override
    public String toString() {{
        return form;
    }}
}}
''',
    'MeosValueSerializer': f'''package {SQL_PKG};

import java.io.IOException;
import org.apache.flink.api.common.typeutils.base.TypeSerializerSingleton;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Writes a MEOS value as its serialized form; the value is immutable. */
public abstract class MeosValueSerializer<T extends MeosValue> extends TypeSerializerSingleton<T> {{

    protected abstract T make(String form);

    @Override public boolean isImmutableType() {{ return true; }}
    @Override public T createInstance() {{ return make(""); }}
    @Override public T copy(T from) {{ return from; }}
    @Override public T copy(T from, T reuse) {{ return from; }}
    @Override public int getLength() {{ return -1; }}

    @Override
    public void serialize(T value, DataOutputView out) throws IOException {{
        out.writeUTF(value.form);
    }}

    @Override
    public T deserialize(DataInputView in) throws IOException {{
        return make(in.readUTF());
    }}

    @Override
    public T deserialize(T reuse, DataInputView in) throws IOException {{
        return deserialize(in);
    }}

    @Override
    public void copy(DataInputView in, DataOutputView out) throws IOException {{
        out.writeUTF(in.readUTF());
    }}
}}
''',
    'MeosSqlRuntime': f'''package {SQL_PKG};

import functions.GeneratedFunctions;
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

/* AUTO-GENERATED by tools/codegen_jvm.py — do not edit by hand.
 * Conversions between Flink's SQL values and the ones MEOS takes, and the release of
 * what MEOS allocates.  MEOS counts days and microseconds from the PostgreSQL epoch. */
public final class MeosSqlRuntime {{

    private static final long PG_EPOCH_DAY = 10957L;
    private static final long PG_EPOCH_SECOND = 946684800L;

    private static final jnr.ffi.Runtime RUNTIME = jnr.ffi.Runtime.getSystemRuntime();
    private static final String NULL_ELEMENT = "null array element not allowed in this context";

    private static final sun.misc.Unsafe UNSAFE;

    static {{
        try {{
            java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) f.get(null);
        }} catch (ReflectiveOperationException e) {{
            throw new ExceptionInInitializerError(e);
        }}
    }}

    private static final ThreadLocal<Boolean> ISO_INTERVALS = ThreadLocal.withInitial(() -> {{
        GeneratedFunctions.meos_set_intervalstyle("iso_8601", 0);
        return Boolean.TRUE;
    }});

    private MeosSqlRuntime() {{ }}

    /** The overloads of one function as a single flat alternative of argument types, each
     * mapped to its result type, so planning a call is linear in the number of overloads. */
    public static TypeInference inference(DataType[][] args, DataType[] rets) {{
        InputTypeStrategy[] seqs = new InputTypeStrategy[args.length];
        Map<InputTypeStrategy, TypeStrategy> results = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {{
            ArgumentTypeStrategy[] a = new ArgumentTypeStrategy[args[i].length];
            for (int j = 0; j < a.length; j++) {{
                a[j] = InputTypeStrategies.explicit(args[i][j]);
            }}
            seqs[i] = InputTypeStrategies.sequence(a);
            results.put(seqs[i], TypeStrategies.explicit(rets[i]));
        }}
        return TypeInference.newBuilder()
                .inputTypeStrategy(seqs.length == 1 ? seqs[0] : InputTypeStrategies.or(seqs))
                .outputTypeStrategy(TypeStrategies.mapping(results))
                .build();
    }}

    /** Free each value MEOS allocated.  Null-safe. */
    public static void free(Pointer[] ps) {{
        for (Pointer p : ps) {{
            if (p != null) {{
                UNSAFE.freeMemory(p.address());
            }}
        }}
    }}

    /** Free a result unless it is one of the inputs, which the caller frees. */
    public static void freeResult(Pointer r, Pointer[] in) {{
        for (Pointer p : in) {{
            if (p != null && p.address() == r.address()) {{
                return;
            }}
        }}
        UNSAFE.freeMemory(r.address());
    }}

    /** Free a result unless it is one of the inputs, which the caller frees. */
    public static void freeResult(Pointer r, Inputs in) {{
        if (!in.holds(r)) {{
            UNSAFE.freeMemory(r.address());
        }}
    }}

    /** What an eval taking an array hands MEOS, gathered as it is built: the values MEOS
     * allocated, released by {{@link #free}}, and the C arrays, kept reachable until then. */
    public static final class Inputs {{

        private Pointer[] owned = new Pointer[8];
        private int n;
        private final java.util.ArrayList<Pointer> held = new java.util.ArrayList<>();

        /** Keep p for release and pass it on. */
        public Pointer value(Pointer p) {{
            if (n == owned.length) {{
                owned = java.util.Arrays.copyOf(owned, 2 * n);
            }}
            owned[n++] = p;
            return p;
        }}

        /** Keep the C array b reachable until the call returns and pass it on. */
        public Pointer hold(Pointer b) {{
            held.add(b);
            return b;
        }}

        /** The values decoded one by one into the C array of pointers MEOS reads.  A null
         * element raises, as it does in PostgreSQL. */
        public Pointer values(MeosValue[] vs) {{
            int w = RUNTIME.addressSize();
            Pointer b = hold(buffer(vs.length, w));
            for (int i = 0; i < vs.length; i++) {{
                Pointer p = value(element(vs, i).decode());
                if (p == null) {{
                    throw new IllegalArgumentException("an array element does not decode");
                }}
                b.putPointer((long) i * w, p);
            }}
            return b;
        }}

        boolean holds(Pointer r) {{
            for (int i = 0; i < n; i++) {{
                if (owned[i] != null && owned[i].address() == r.address()) {{
                    return true;
                }}
            }}
            return false;
        }}

        /** Free each value kept. */
        public void free() {{
            for (int i = 0; i < n; i++) {{
                if (owned[i] != null) {{
                    UNSAFE.freeMemory(owned[i].address());
                }}
            }}
            n = 0;
            held.clear();
        }}
    }}

    private static <T> T element(T[] xs, int i) {{
        if (xs[i] == null) {{
            throw new IllegalArgumentException(NULL_ELEMENT);
        }}
        return xs[i];
    }}

    /** Memory for n elements of the given width, which the garbage collector releases. */
    private static Pointer buffer(int n, int width) {{
        return Memory.allocateDirect(RUNTIME, Math.max(1, n) * width);
    }}

    public static Pointer ints(Integer[] xs) {{
        Pointer b = buffer(xs.length, 4);
        for (int i = 0; i < xs.length; i++) {{
            b.putInt(4L * i, element(xs, i));
        }}
        return b;
    }}

    public static Pointer longs(Long[] xs) {{
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {{
            b.putLongLong(8L * i, element(xs, i));
        }}
        return b;
    }}

    public static Pointer doubles(Double[] xs) {{
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {{
            b.putDouble(8L * i, element(xs, i));
        }}
        return b;
    }}

    public static Pointer dates(java.time.LocalDate[] xs) {{
        Pointer b = buffer(xs.length, 4);
        for (int i = 0; i < xs.length; i++) {{
            b.putInt(4L * i, dateAdt(element(xs, i)));
        }}
        return b;
    }}

    public static Pointer timestamps(java.time.Instant[] xs) {{
        Pointer b = buffer(xs.length, 8);
        for (int i = 0; i < xs.length; i++) {{
            b.putLongLong(8L * i, micros(element(xs, i)));
        }}
        return b;
    }}

    /** Microseconds from the PostgreSQL epoch, the TimestampTz MEOS reads. */
    public static long micros(java.time.Instant t) {{
        return Math.addExact(Math.multiplyExact(t.getEpochSecond() - PG_EPOCH_SECOND, 1000000L),
                t.getNano() / 1000);
    }}

    public static int dateAdt(java.time.LocalDate d) {{
        return (int) (d.toEpochDay() - PG_EPOCH_DAY);
    }}

    public static java.time.LocalDate date(int d) {{
        return java.time.LocalDate.ofEpochDay(d + PG_EPOCH_DAY);
    }}

    public static java.time.Instant timestamptz(long micros) {{
        return java.time.Instant.ofEpochSecond(PG_EPOCH_SECOND + Math.floorDiv(micros, 1000000L),
                Math.floorMod(micros, 1000000L) * 1000L);
    }}

    public static Pointer interval(java.time.Duration d) {{
        return GeneratedFunctions.interval_in(d.toString(), -1);
    }}

    public static java.time.Duration duration(Pointer p) {{
        ISO_INTERVALS.get();
        return java.time.Duration.parse(GeneratedFunctions.interval_out(p));
    }}
}}
''',
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

    names = defaultdict(list)          # SQL name -> eval methods
    seen = defaultdict(set)
    skipped = defaultdict(int)
    for f in m.fns:
        if not f.get('sqlfn') or f.get('sqlfnBackingOnly') or f.get('api') == 'internal':
            continue
        jsig = jmeos.get(f['name'])
        if jsig is None:
            skipped['not in jar'] += 1
            continue
        for sqlname, sargs, sret, sdef in m.signatures(f):
            ov, why = _overload(m, f, sargs, sret, jsig)
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
                names[sqlname].append((_emit_eval(ov, dv), list(key), ov[3][0]))

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
        body += [f'            {_datatype(r)},' for _, _, r in sigs]
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
    print(f'flink-sql: {len(classes)} SQL functions ({n_ov} overloads), '
          f'{len(m.value_class)} value types into {root}')
    for why, n in sorted(skipped.items(), key=lambda x: -x[1]):
        print(f'  skipped {n:5d}  {why}')


# ───────────────────────── entry point ─────────────────────────

def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument('--engine', required=True, choices=['spark', 'flink', 'kafka', 'flink-sql'])
    ap.add_argument('--catalog', required=True, help='MEOS-API meos-idl.json')
    ap.add_argument('--jar', required=True,
                    help='JMEOS jar with functions.GeneratedFunctions')
    ap.add_argument('--out', required=True, help='output directory')
    ap.add_argument('--package', default='org.mobilitydb.meos',
                    help='facade package (flink/kafka only)')
    ap.add_argument('--report', action='store_true', help='spark only')
    args = ap.parse_args()

    if args.engine == 'spark':
        run_spark(args)
    elif args.engine == 'flink-sql':
        run_flink_sql(args)
    else:
        run_facades(args)


if __name__ == '__main__':
    main()
