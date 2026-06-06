#!/usr/bin/env python3
"""Generate the MobilitySpark Spark Connect registrar from the canonical specs.

Inputs (both produced by the sibling extractors, derived from canonical sources):
  - meos-named-surface.json   canonical SQL names + overloads (defaults -> arities)
  - spark-impls.json          each Spark UDF: holder pkg/class/field, arity, types,
                              and the MEOS C functions its body calls

Joining them on the SQL->MEOS C doxygen chain yields canonical name -> Spark impl.
This tool emits MobilitySparkConnectExtensionsGen.scala: a SparkSessionExtensions
that injects each canonical function under its IDENTITY name (no camelCase remap,
no hand-written table). Single-impl functions bind directly to the one impl; the
builder adapts to the call-site arity by null-padding the impl's optional args (so
e.g. asMFJSON(g) reaches the impl's default precision). Multi-impl functions (one
SQL name over several type-specific impls) whose first argument differs in MEOS
type are emitted as a per-row dispatch ScalaUDF that peeks the arg0 WKB type tag
(meos_typeof_hexwkb) and routes to the type-matching impl. Multi-impl functions
that differ only on a later argument (e.g. atTime on its time arg) are listed for
the arg-N dispatch extension.

Usage: generate_spark_registrar.py [named-surface.json] [spark-impls.json] [out.scala]
"""
import json, os, sys

HERE = os.path.dirname(__file__)
named = json.load(open(sys.argv[1] if len(sys.argv) > 1 else os.path.join(HERE, '..', 'input', 'meos-named-surface.json')))
impls = json.load(open(sys.argv[2] if len(sys.argv) > 2 else os.path.join(HERE, '..', 'input', 'spark-impls.json')))['impls']
out = sys.argv[3] if len(sys.argv) > 3 else os.path.join(HERE, '..', 'output', 'MobilitySparkConnectExtensionsGen.scala')

meos2spark = {}
for sn, d in impls.items():
    if d.get('primaryMeos'):
        meos2spark.setdefault(d['primaryMeos'], []).append(sn)

# C-FFI param types -> receiver category, so a multi-impl SQL name can dispatch on
# a runtime MEOS type tag (meos_typeof_hexwkb of the differentiating argument) to
# the type-specific impl.
idl = json.load(open(os.path.join(HERE, '..', 'input', 'meos-idl.json')))
prim2params = {f['name']: [p.get('cType', '') for p in (f.get('params') or [])]
               for f in idl['functions']}
# every meostype_name string, longest first for greedy matching of the concrete
# type embedded in a primaryMeos name (e.g. temporal_at_tstzspanset -> tstzspanset)
TNAMES = json.load(open('/tmp/tnames.json')) if os.path.exists('/tmp/tnames.json') else []

def category(ctype):
    if 'STBox' in ctype: return 'stbox'
    if 'TBox' in ctype: return 'tbox'
    if 'GSERIALIZED' in ctype: return 'geo'
    if 'Temporal' in ctype: return 'temporal'
    if 'Span' in ctype or 'Set' in ctype: return 'spanset'
    return 'other'

CAT_TNAMES = {'stbox': ['stbox'], 'tbox': ['tbox'],
              'geo': ['geometry', 'geography', 'geomset', 'geogset']}

def ctype_at(spark, pos):
    ps = prim2params.get(impls[spark]['primaryMeos'], [])
    return ps[pos] if pos < len(ps) else ''

def impl_tnames(spark, pos):
    """(tnames, isDefault) the impl answers to at arg position `pos`. Concrete
    receivers map to fixed tags; a Temporal receiver is the catch-all default; a
    generic Span/Set takes the concrete tag embedded in its primaryMeos name;
    a non-WKB receiver (timestamptz/numeric) yields nothing (cannot be peeked)."""
    cat = category(ctype_at(spark, pos))
    if cat == 'temporal':
        return [], True
    if cat in CAT_TNAMES:
        return CAT_TNAMES[cat], False
    if cat == 'spanset':
        pm = impls[spark]['primaryMeos']
        hit = next((t for t in TNAMES if t in pm), None)
        return ([hit] if hit else []), False
    return [], False

def dispatch_for(names):
    """Pick the first arg position at which the impls differ and yield a usable
    routes map, then return (pos, routes, default) or None."""
    arity = min(impls[s]['arity'] for s in names)
    for pos in range(arity):
        routes, default = {}, None
        for s in names:
            tns, isdef = impl_tnames(s, pos)
            for tn in tns:
                routes.setdefault(tn, s)
            if isdef and default is None:
                default = s
        # this position differentiates if it routes to >=2 distinct impls (or one
        # concrete route plus a temporal default)
        targets = set(routes.values()) | ({default} if default else set())
        if routes and len(targets) > 1:
            return pos, routes, default
    return None

single, multi, dispatch = [], [], []
for fn in named['functions']:
    names = sorted({sn for c in fn['c'] for sn in meos2spark.get(c['meos'], [])})
    if len(names) == 1:
        single.append((fn['name'], names[0]))
    elif len(names) > 1 and len({impls[s]['primaryMeos'] for s in names}) == 1:
        # several Spark registrations (bare + camelCase) of one MEOS op: single op,
        # bind the identity name to the impl named for it (else the shortest name)
        pick = next((s for s in names if s.lower() == fn['name'].lower()), None) \
            or min(names, key=len)
        single.append((fn['name'], pick))
    elif len(names) > 1:
        d = dispatch_for(names)
        if d:
            pos, routes, default = d
            dispatch.append((fn['name'], names, pos, routes, default))
        else:
            multi.append((fn['name'], names))

import re as _re
named_by_name = {fn['name']: fn for fn in named['functions']}

def scala_default(v):
    if v is None:
        return 'null'
    if _re.fullmatch(r'-?\d+', v):
        return f'(Integer.valueOf({v}): AnyRef)'
    if v.strip().upper() in ('TRUE', 'FALSE'):
        return f'(java.lang.Boolean.{v.strip().upper()}: AnyRef)'
    return 'null'   # unsupported default literal: fall back to a null pad

def defaults_arr(canon, arity):
    """Fill an omitted optional arg with the SQL default ONLY when the impl exposes
    exactly one overload's worth of args (impl arity == that overload's maxArity, so
    positions align); otherwise null-pad and let the impl's own default hold (the
    impl may expose a non-leading subset of the canonical args)."""
    fn = named_by_name.get(canon)
    ov = next((o for o in fn['overloads'] if o['maxArity'] == arity), None) if fn else None
    if not ov:
        items = ', '.join('null' for _ in range(arity))
    else:
        items = ', '.join(
            scala_default(ov['args'][i].get('default') if i < len(ov['args']) else None)
            for i in range(arity))
    return f'Array[AnyRef]({items})'

lines = []
seen = set()
for canon, spark in single:
    if canon in seen:
        continue
    d = impls[spark]
    seen.add(canon)
    fqn = f"{d['pkg']}.{d['class']}.{d['field']}"
    argts = ', '.join(f'"{t}"' for t in d['argTypes'])
    lines.append(f'    inj(ext, "{canon}", {d["arity"]}, Array[String]({argts}), '
                 f'"{d["retType"]}", {fqn}, {defaults_arr(canon, d["arity"])})')

dlines = []
def fqn(s):
    x = impls[s]
    return f"{x['pkg']}.{x['class']}.{x['field']}"

def route_target(canon, s):
    # (impl, impl arity, impl SQL-default fills) so the dispatch can pad an omitted
    # optional argument of the chosen impl exactly as the single-impl path does
    d = impls[s]
    return f"({fqn(s)}: AnyRef, {d['arity']}, {defaults_arr(canon, d['arity'])})"

for canon, names, pos, routes, default in dispatch:
    if canon in seen:
        continue
    seen.add(canon)
    d = impls[names[0]]
    retT = d['retType']
    rmap = ', '.join(f'"{tn}" -> {route_target(canon, s)}' for tn, s in sorted(routes.items()))
    dflt = route_target(canon, default) if default else "null"
    dlines.append(f'    injMulti(ext, "{canon}", {pos}, "{retT}", '
                  f'Map[String, (AnyRef, Int, Array[AnyRef])]({rmap}), {dflt})')

HEADER = '''/* GENERATED by codegen/tools/generate_spark_registrar.py from the canonical
 * named-operation surface (meos-named-surface.json) joined with the MobilitySpark
 * impl scan (spark-impls.json). Do not edit by hand. Each canonical function is
 * injected under its identity name (no camelCase remap, no hand-written table).
 * Single-impl functions bind to the one impl; multi-impl functions that differ on
 * arg0's MEOS type dispatch per row on the arg0 WKB type tag. Functions that
 * differ only on a later argument are listed in the trailing comment. */
package org.mobilitydb.spark.connect

import org.apache.spark.sql.SparkSessionExtensions
import org.apache.spark.sql.catalyst.FunctionIdentifier
import org.apache.spark.sql.catalyst.expressions.{Expression, ExpressionInfo, ScalaUDF}
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.api.java.{UDF1, UDF2, UDF3, UDF4}
import functions.GeneratedFunctions

class MobilitySparkConnectExtensionsGen extends (SparkSessionExtensions => Unit) {
  private lazy val encs: Map[String, ExpressionEncoder[_]] = Map(
    "String" -> ExpressionEncoder[String](), "Integer" -> ExpressionEncoder[java.lang.Integer](),
    "Long" -> ExpressionEncoder[java.lang.Long](), "Double" -> ExpressionEncoder[java.lang.Double](),
    "Number" -> ExpressionEncoder[java.lang.Double](), "Float" -> ExpressionEncoder[java.lang.Float](),
    "Boolean" -> ExpressionEncoder[java.lang.Boolean](), "Timestamp" -> ExpressionEncoder[java.sql.Timestamp]())
  private def enc(t: String) = encs.getOrElse(t, encs("String"))
  private def dt(t: String): DataType = t match {
    case "Integer" => IntegerType; case "Long" => LongType; case "Double" | "Number" => DoubleType
    case "Float" => FloatType; case "Boolean" => BooleanType; case "Timestamp" => TimestampType
    case _ => StringType
  }
  private def inj(ext: SparkSessionExtensions, name: String, arity: Int,
                  argTs: Array[String], retT: String, udf: AnyRef, defaults: Array[AnyRef]): Unit = {
    val builder = (children: Seq[Expression]) => {
      val n = math.min(children.size, arity)
      ScalaUDF(MobilitySparkConnectExtensionsGen.fn(n, udf, arity, defaults), dt(retT), children,
        argTs.take(n).map(t => Some(enc(t))).toSeq, Some(enc(retT)), Some(name))
    }
    try ext.injectFunction((FunctionIdentifier(name), new ExpressionInfo(name, name), builder))
    catch { case _: Throwable => }
  }
  // One SQL name over several type-specific impls: dispatch per row on the
  // differentiating argument's MEOS type tag to the impl whose receiver matches.
  // Each route carries (impl, impl arity, impl SQL-default fills).
  private def injMulti(ext: SparkSessionExtensions, name: String, dpos: Int, retT: String,
                       routes: Map[String, (AnyRef, Int, Array[AnyRef])],
                       dflt: (AnyRef, Int, Array[AnyRef])): Unit = {
    val builder = (children: Seq[Expression]) => {
      val n = children.size
      ScalaUDF(MobilitySparkConnectExtensionsGen.disp(n, dpos, routes, dflt), dt(retT), children,
        children.map(_ => Some(enc("String"))), Some(enc(retT)), Some(name))
    }
    try ext.injectFunction((FunctionIdentifier(name), new ExpressionInfo(name, name), builder))
    catch { case _: Throwable => }
  }

  def apply(ext: SparkSessionExtensions): Unit = {
'''

os.makedirs(os.path.dirname(out), exist_ok=True)
with open(out, 'w') as f:
    f.write(HEADER)
    f.write('\n'.join(lines))
    f.write('\n\n    // multi-impl: per-row arg0 MEOS-type-tag dispatch\n')
    f.write('\n'.join(dlines))
    f.write('\n  }\n')
    f.write('  // multi-impl not arg0-dispatchable (all impls share arg0 type; need\n')
    f.write('  // the differentiating arg, e.g. atTime on the time argument):\n')
    for canon, names in multi:
        f.write(f'  //   {canon} -> {names}\n')
    f.write('}\n\n')
    # Companion object: the shipped ScalaUDF closures live here so they capture
    # only the serializable UDF object, never the (non-serializable) extension.
    f.write('''object MobilitySparkConnectExtensionsGen {
  private def call(u: AnyRef, m: Int, a: Seq[Any], defaults: Array[AnyRef]): Any = {
    // fill an omitted optional arg with the canonical SQL default, not a null pad
    def g(i: Int): AnyRef =
      if (i < a.length) a(i).asInstanceOf[AnyRef]
      else if (defaults != null && i < defaults.length) defaults(i) else null
    m match {
      case 1 => u.asInstanceOf[UDF1[Any, Any]].call(g(0))
      case 2 => u.asInstanceOf[UDF2[Any, Any, Any]].call(g(0), g(1))
      case 3 => u.asInstanceOf[UDF3[Any, Any, Any, Any]].call(g(0), g(1), g(2))
      case _ => u.asInstanceOf[UDF4[Any, Any, Any, Any, Any]].call(g(0), g(1), g(2), g(3))
    }
  }
  def fn(n: Int, u: AnyRef, m: Int, defaults: Array[AnyRef]): AnyRef = n match {
    case 1 => (a: Any) => call(u, m, Seq(a), defaults)
    case 2 => (a: Any, b: Any) => call(u, m, Seq(a, b), defaults)
    case 3 => (a: Any, b: Any, c: Any) => call(u, m, Seq(a, b, c), defaults)
    case _ => (a: Any, b: Any, c: Any, d: Any) => call(u, m, Seq(a, b, c, d), defaults)
  }
  // route by the differentiating argument's MEOS type-tag name to the matching
  // (impl, impl arity, defaults); the default route handles temporal/unknown tags
  private def pick(args: Seq[Any], dpos: Int, routes: Map[String, (AnyRef, Int, Array[AnyRef])],
                   dflt: (AnyRef, Int, Array[AnyRef])): (AnyRef, Int, Array[AnyRef]) =
    (if (dpos < args.length) args(dpos) else null) match {
      case s: String =>
        val tn = try GeneratedFunctions.meostype_name(GeneratedFunctions.meos_typeof_hexwkb(s))
                 catch { case _: Throwable => null }
        routes.getOrElse(tn, dflt)
      case _ => dflt
    }
  private def run(t: (AnyRef, Int, Array[AnyRef]), a: Seq[Any]): Any =
    if (t == null || t._1 == null) null else call(t._1, t._2, a, t._3)
  def disp(n: Int, dpos: Int, routes: Map[String, (AnyRef, Int, Array[AnyRef])],
           dflt: (AnyRef, Int, Array[AnyRef])): AnyRef = n match {
    case 1 => (a: Any) => run(pick(Seq(a), dpos, routes, dflt), Seq(a))
    case 2 => (a: Any, b: Any) => run(pick(Seq(a, b), dpos, routes, dflt), Seq(a, b))
    case 3 => (a: Any, b: Any, c: Any) => run(pick(Seq(a, b, c), dpos, routes, dflt), Seq(a, b, c))
    case _ => (a: Any, b: Any, c: Any, d: Any) => run(pick(Seq(a, b, c, d), dpos, routes, dflt), Seq(a, b, c, d))
  }
}
''')
print(f"single-impl injected: {len(lines)}   multi-impl (v2 dispatch): {len(multi)}   -> {out}")
