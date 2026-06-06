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
# arg0's runtime MEOS type tag (meos_typeof_hexwkb) to the type-specific impl.
idl = json.load(open(os.path.join(HERE, '..', 'input', 'meos-idl.json')))
prim2ctype0 = {}
for fdef in idl['functions']:
    ps = fdef.get('params') or []
    if ps:
        prim2ctype0[fdef['name']] = ps[0].get('cType', '')

def category(ctype):
    if 'STBox' in ctype: return 'stbox'
    if 'TBox' in ctype: return 'tbox'
    if 'GSERIALIZED' in ctype: return 'geo'
    if 'Temporal' in ctype: return 'temporal'
    if 'Span' in ctype: return 'span'
    if 'Set' in ctype: return 'set'
    return 'other'

# meostype_name strings each impl's arg0 category answers to (geo serves both
# geometry/geography; temporal is the catch-all default; span/set take the
# concrete source type from the *_to_* primaryMeos name)
CAT_TNAMES = {'stbox': ['stbox'], 'tbox': ['tbox'],
              'geo': ['geometry', 'geography', 'geometryset', 'geographyset']}

def impl_routes(spark):
    """(tnames, isDefault) for an impl, by its primaryMeos arg0 category."""
    d = impls[spark]
    cat = category(prim2ctype0.get(d['primaryMeos'], ''))
    if cat == 'temporal':
        return [], True
    if cat in CAT_TNAMES:
        return CAT_TNAMES[cat], False
    if cat in ('span', 'set'):
        pm = d['primaryMeos']
        src = pm.split('_to_')[0] if '_to_' in pm else None
        return ([src] if src else []), False
    return [], False

single, multi, dispatch = [], [], []
for fn in named['functions']:
    names = sorted({sn for c in fn['c'] for sn in meos2spark.get(c['meos'], [])})
    if len(names) == 1:
        single.append((fn['name'], names[0]))
    elif len(names) > 1:
        cats = {category(prim2ctype0.get(impls[s]['primaryMeos'], '')) for s in names}
        # arg0 distinguishes only when impls differ on the FIRST-arg category and
        # at least one is a concrete WKB-peekable receiver (box/geo/span/set)
        peekable = cats & {'stbox', 'tbox', 'geo', 'span', 'set'}
        if len(cats) > 1 and peekable:
            routes = {}      # tname -> spark impl
            default = None
            for s in names:
                tns, isdef = impl_routes(s)
                for tn in tns:
                    routes.setdefault(tn, s)
                if isdef and default is None:
                    default = s
            if routes:
                dispatch.append((fn['name'], names, routes, default))
            else:
                multi.append((fn['name'], names))
        else:
            multi.append((fn['name'], names))

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
                 f'"{d["retType"]}", {fqn})')

dlines = []
for canon, names, routes, default in dispatch:
    if canon in seen:
        continue
    seen.add(canon)
    d = impls[names[0]]    # arity/types shared across the type-specific impls
    argts = ', '.join(f'"{t}"' for t in d['argTypes'])
    def fqn(s):
        x = impls[s]
        return f"{x['pkg']}.{x['class']}.{x['field']}"
    rmap = ', '.join(f'"{tn}" -> ({fqn(s)}: AnyRef)' for tn, s in sorted(routes.items()))
    dflt = f"({fqn(default)}: AnyRef)" if default else "null"
    dlines.append(f'    injMulti(ext, "{canon}", {d["arity"]}, Array[String]({argts}), '
                  f'"{d["retType"]}", Map[String, AnyRef]({rmap}), {dflt})')

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
                  argTs: Array[String], retT: String, udf: AnyRef): Unit = {
    val builder = (children: Seq[Expression]) => {
      val n = math.min(children.size, arity)
      ScalaUDF(MobilitySparkConnectExtensionsGen.fn(n, udf, arity), dt(retT), children,
        argTs.take(n).map(t => Some(enc(t))).toSeq, Some(enc(retT)), Some(name))
    }
    try ext.injectFunction((FunctionIdentifier(name), new ExpressionInfo(name, name), builder))
    catch { case _: Throwable => }
  }
  // One SQL name over several type-specific impls: dispatch per row on arg0's
  // runtime MEOS type tag to the impl whose receiver type matches.
  private def injMulti(ext: SparkSessionExtensions, name: String, arity: Int,
                       argTs: Array[String], retT: String,
                       routes: Map[String, AnyRef], dflt: AnyRef): Unit = {
    val builder = (children: Seq[Expression]) => {
      val n = math.min(children.size, arity)
      ScalaUDF(MobilitySparkConnectExtensionsGen.disp(n, routes, dflt, arity), dt(retT), children,
        argTs.take(n).map(t => Some(enc(t))).toSeq, Some(enc(retT)), Some(name))
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
  private def call(u: AnyRef, m: Int, a: Seq[Any]): Any = {
    def g(i: Int): AnyRef = if (i < a.length) a(i).asInstanceOf[AnyRef] else null
    m match {
      case 1 => u.asInstanceOf[UDF1[Any, Any]].call(g(0))
      case 2 => u.asInstanceOf[UDF2[Any, Any, Any]].call(g(0), g(1))
      case 3 => u.asInstanceOf[UDF3[Any, Any, Any, Any]].call(g(0), g(1), g(2))
      case _ => u.asInstanceOf[UDF4[Any, Any, Any, Any, Any]].call(g(0), g(1), g(2), g(3))
    }
  }
  def fn(n: Int, u: AnyRef, m: Int): AnyRef = n match {
    case 1 => (a: Any) => call(u, m, Seq(a))
    case 2 => (a: Any, b: Any) => call(u, m, Seq(a, b))
    case 3 => (a: Any, b: Any, c: Any) => call(u, m, Seq(a, b, c))
    case _ => (a: Any, b: Any, c: Any, d: Any) => call(u, m, Seq(a, b, c, d))
  }
  // route by arg0's MEOS type-tag name to the matching type-specific impl
  private def pick(a0: Any, routes: Map[String, AnyRef], dflt: AnyRef): AnyRef = a0 match {
    case s: String =>
      val tn = try GeneratedFunctions.meostype_name(GeneratedFunctions.meos_typeof_hexwkb(s))
               catch { case _: Throwable => null }
      routes.getOrElse(tn, dflt)
    case _ => dflt
  }
  def disp(n: Int, routes: Map[String, AnyRef], dflt: AnyRef, m: Int): AnyRef = n match {
    case 1 => (a: Any) => { val u = pick(a, routes, dflt); if (u == null) null else call(u, m, Seq(a)) }
    case 2 => (a: Any, b: Any) => { val u = pick(a, routes, dflt); if (u == null) null else call(u, m, Seq(a, b)) }
    case 3 => (a: Any, b: Any, c: Any) => { val u = pick(a, routes, dflt); if (u == null) null else call(u, m, Seq(a, b, c)) }
    case _ => (a: Any, b: Any, c: Any, d: Any) => { val u = pick(a, routes, dflt); if (u == null) null else call(u, m, Seq(a, b, c, d)) }
  }
}
''')
print(f"single-impl injected: {len(lines)}   multi-impl (v2 dispatch): {len(multi)}   -> {out}")
