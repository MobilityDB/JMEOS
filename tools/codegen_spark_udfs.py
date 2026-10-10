#!/usr/bin/env python3
"""Generate the MobilitySpark UDF surface from the MEOS-API catalog.

North Star (meos-api-codegen-regularity): the Spark UDF surface is GENERATED
from the single MEOS-API source of truth, never hand-written. This drives the
engine over the WHOLE catalog (every @sqlfn name + every contract operator bare
name from portableAliases) — no hardcoded scope, no skip-hacks. Functions whose
types are genuinely internal (Datum/SkipList/function-pointers/out-param arrays)
are the ONLY exclusions, and they are reported, never silently skipped.

Two emission modes:
  - SINGLE: a name with one backing -> a 1:1 UDF.
  - DISPATCH: a name/operator with N typed backings (Spark cannot overload by
    name) -> ONE UDF that classifies each String arg by its MEOS type and routes
    to the catalog-determined backing.

Wire format: temporals, spans, span sets and sets travel as their WKB bytes (a
BinaryType column), and an argument of those kinds is also read from hex-WKB text;
boxes, cbuffer/npoint/pose and geometries travel as TEXT; scalars are typed Spark
columns (Integer/Double/Boolean/Long/Timestamp).

Usage: python3 tools/codegen_spark_udfs.py [--catalog PATH] [--out DIR] [--report]
"""
import argparse, json, os, sys, collections, glob, re


def norm(c):
    return c.replace("const ", "").replace("struct ", "").strip()


# ── Pointer-typed args: canonical base -> (GeneratedFunctions parser, KIND) ──
# ── Pointer-typed args and returns, filled from the catalog by derive_codecs ──
# PARSE: canonical base -> (reader expression over the Object Spark hands the UDF, KIND); the
# reader returns a jnr Pointer (freed after the call), or null when the value is not of its
# kind. SERIAL: canonical base -> (Spark type, writer expression).
PARSE = {}
SERIAL = {}
# The WKB variant the byte writers take: the extended form, which keeps the SRID.
WKB_VARIANT = 4
# The types whose WKB states its MeosType after the byte-order byte, each with the typed reader
# UdfMarshal holds for it. A *_from_wkb / *_from_hexwkb parser CRASHES (SIGSEGV) on a
# well-formed buffer of the wrong TYPE (a span fed to temporal_from_wkb); a typed reader reads
# the type first and calls the C parser only when it matches, else returns null, so a foreign
# value is refused and the arg-kind dispatch can safely try every candidate. The other byte
# codecs (boxes, circular buffers, network points, poses) state no type in their WKB.
TYPED_READER = {
    "Temporal": ("UdfMarshal.tFrom(%s)",       "K_TEMPORAL"),
    "Span":     ("UdfMarshal.spanFrom(%s)",    "K_SPAN"),
    "SpanSet":  ("UdfMarshal.spansetFrom(%s)", "K_SPANSET"),
    "Set":      ("UdfMarshal.setFrom(%s)",     "K_SET"),
}
# The readers that check the kind of what they read, so an overload using them can be told
# apart from its siblings at run time. The one test #_safe_dispatch and #_dispatchable both ask.
TYPE_CHECKED = ("UdfMarshal.tFrom", "UdfMarshal.spanFrom", "UdfMarshal.spansetFrom",
                "UdfMarshal.setFrom", "UdfMarshal.geoFromText")


def type_checked(parse):
    return parse.startswith(TYPE_CHECKED)


def _fn_ref(name, aux):
    """A java.util.function.Function<String, Pointer> calling `name` on the text, the catalog's
    default passed for each trailing formatting argument, as #_value_class_src passes them. The
    lambda takes the generator's own underscore-led name, which no UDF parameter (a C parameter
    name) carries, so it never shadows the parameter of the UDF it sits in."""
    if not aux:
        return "GeneratedFunctions::%s" % name
    return "_txt -> GeneratedFunctions.%s(_txt, %s)" % (
        name, ", ".join(str(a["default"]) for a in aux))


# The types whose functions share a SQL name with those of a type already marshalled, where no
# reader tells the two apart: hasZ/xMin.. of TPCBox and STBox, point of PoseChain and Cbuffer,
# bandPixelType/height.. of Raster and Raquet. Marshalling them would leave each such name with
# overloads no dispatcher can choose between (#_dispatchable), and the name would go; they wait
# for the class-prefixed names that separate them. Read by #derive_codecs beside INTERNAL.
DEFERRED_TYPES = {"TPCBox", "PoseChain", "Raster"}


def derive_codecs(cat, have):
    """Fill PARSE and SERIAL from the catalog's typeEncodings, the codec each value travels in:
    the Spark twin of #_codecs in codegen_jvm.py, which chooses the Flink codec the same way.

    A type whose byte codec the catalog states travels as its WKB bytes, a BinaryType column,
    and is read from those bytes, from its hex-WKB text, and from its text form when its text
    reader is the generic <type>_in; any other type travels in its text form. The subtypes
    the object model's subtype axis lists (TInstant, TSequence, TSequenceSet) take the codec of
    Temporal. A geometry reads through UdfMarshal.geoFromText, which accepts the EWKT the
    geometry writer geo_as_ewkt answers, where the catalog's text reader geo_from_text takes
    WKT alone."""
    # A class whose reader returns the value itself, not a pointer to it, is a scalar the jar
    # passes by value (TimestampTz, DateADT, Timestamp, TimeADT): arg_kind and ret_emit carry
    # it as one, so it takes no pointer codec here.
    ret_of = {f["name"]: f["returnType"]["canonical"] for f in cat.get("functions") or []}
    for base, e in (cat.get("typeEncodings") or {}).items():
        if base in DEFERRED_TYPES or base in INTERNAL:
            continue
        if e.get("in") in ret_of and "*" not in ret_of[e["in"]]:
            continue
        dec = e.get("decoders") or {}
        byt = e.get("bytes") or {}
        kind = "K_" + base.upper()
        if byt and have(byt.get("decoder")) and have(byt.get("encoder")):
            SERIAL[base] = ("BinaryType", "GeneratedFunctions.%s(%%s, (byte) %d)"
                            % (byt["encoder"], WKB_VARIANT))
            if base in TYPED_READER:
                PARSE[base] = TYPED_READER[base]
                continue
            hexr = dec.get("wkb") if have(dec.get("wkb")) else None
            text = e.get("in") if (e.get("in") == base.lower() + "_in" and have(e.get("in"))
                                   and not e.get("in_aux")) else None
            PARSE[base] = ("UdfMarshal.read(%%s, null, GeneratedFunctions::%s, %s, %s)"
                           % (byt["decoder"],
                              "GeneratedFunctions::%s" % hexr if hexr else "null",
                              "GeneratedFunctions::%s" % text if text else "null"), kind)
        elif have(e.get("in")) and have(e.get("out")):
            out_aux = "".join(", %s" % a["default"] for a in e.get("out_aux") or [])
            SERIAL[base] = ("StringType", "GeneratedFunctions.%s(%%s%s)" % (e["out"], out_aux))
            PARSE[base] = ("UdfMarshal.textIn(%%s, %s)" % _fn_ref(e["in"], e.get("in_aux")),
                           kind)
    if "GSERIALIZED" in PARSE:
        PARSE["GSERIALIZED"] = ("UdfMarshal.geoFromText(%s)", "K_GSERIALIZED")
    axis = ((cat.get("objectModel") or {}).get("axes") or {}).get("subtype") or {}
    for v in axis.get("values") or []:
        sub = v.get("class")
        if sub and sub != "Temporal" and "Temporal" in PARSE:
            PARSE[sub] = PARSE["Temporal"]
            SERIAL[sub] = SERIAL["Temporal"]
# The catalog spells a 32-bit integer by its definition: `int` where the C source writes int,
# `int32_t` where it writes int32 or int32_t. JMEOS passes both as a Java int.
INT32 = ("int", "int32_t")
# ── Scalar args: canonical -> (Spark DataType, Java boxed type, "parse expr") ──
SCALAR_ARG = {
    "int":         ("IntegerType", "Integer", "%s"),
    "int32_t":     ("IntegerType", "Integer", "%s"),
    "bool":        ("BooleanType", "Boolean", "%s"),
    "double":      ("DoubleType",  "Double",  "%s"),
    "int64_t":     ("LongType",    "Long",    "%s"),
    "uint64_t":    ("LongType",    "Long",    "%s"),   # 64-bit (H3Index/hash) <-> jnr long
    "DateADT":     ("IntegerType", "Integer", "%s"),   # JMEOS maps DateADT -> int
    "uint8_t":     ("ByteType",    "Byte",    "%s"),   # the WKB `variant` flag <-> jnr byte
}
# ── Scalar returns: canonical -> (Spark DataType, Java box, "serialize expr") ──
SCALAR_RET = {
    "bool":     ("BooleanType", "%s"),
    "double":   ("DoubleType",  "%s"),
    "int64_t":  ("LongType",    "%s"),
    "uint64_t": ("LongType",    "%s"),
    "DateADT":  ("IntegerType", "%s"),
    "char *":   ("StringType",  "%s"),     # cstring already a Java String via jnr
}
# A text result is a MEOS allocation the jar returns as a Pointer: text_out copies it into the
# String Spark answers and the pointer path of #emit_single frees it after, as a value it
# serializes (#ret_emit).
TEXT_RET = ("StringType", "GeneratedFunctions.text_out(%s)")
# operators whose int (1/0/-1) result is a tri-state predicate -> BooleanType ==1
PRED_OPS = {"?=", "?<>", "?<", "?<=", "?>", "?>=",
            "%=", "%<>", "%<", "%<=", "%>", "%>=",
            "#=", "#<>", "#<", "#<=", "#>", "#>="}
# genuinely-internal / non-user-facing base types -> legitimately OUT OF SCOPE.
INTERNAL = {"Datum", "SkipList", "GBOX", "BOX3D", "void", "meosType", "MeosType",
            "uint8_t", "LWGEOM", "GEOSGeometry", "RTree", "interpType", "json_object",
            "size_t", "Match", "unsigned int",
            "uint32", "text", "char"}
# (text/char are counted as not-yet-emitted, NOT as a permanent exclusion. An Interval
#  travels in the text form its catalog encoding states, like every other type derive_codecs
#  reads; DateADT (->int) and TimestampTz (->OffsetDateTime) are handled.)


# JMEOS actual signatures (name -> (javaRet, nArgs)), parsed from the jar in main().
# The jar is the ground truth: it catches catalog/typerecover disagreements (uint64
# collapsed to int, collapsed-jsonb int*, opaque PJ pointers) before they miscompile.
JSIG = {}
JPRIM = {"long": "LongType", "int": "IntegerType", "double": "DoubleType",
         "boolean": "BooleanType", "float": "DoubleType"}


def base(canon):
    t = norm(canon)
    if "(*" in t or "()" in t or t.endswith("**"):
        return "__INTERNAL__"
    b = t.replace("*", "").strip()
    return b


# pointer-to-primitive out-params: JMEOS drops the param, allocs a buffer, and
# returns a Pointer to it (the bool/void return is discarded). canonical -> deref.
OUTPRIM = {
    "double *":   ("DoubleType",  "%s.getDouble(0)"),
    "int *":      ("IntegerType", "%s.getInt(0)"),
    "int64_t *":  ("LongType",    "%s.getLongLong(0)"),
    "bool *":     ("BooleanType", "(%s.getByte(0) != 0)"),
}


def arg_kind(canon):
    """('ptr', parse, KIND) | ('scalar', DataType, Box, expr) | ('ts',) | None."""
    nc = norm(canon)
    b = base(canon)
    if b == "TimestampTz" and nc == "TimestampTz":
        return ("ts",)
    if b in PARSE:
        return ("ptr",) + PARSE[b]
    # An enum the catalog reads from its name: the SQL text is parsed into the int JMEOS takes.
    if b in ENUM_PARSER and "*" not in nc:
        return ("scalar", "StringType", "String", "GeneratedFunctions.%s(%%s)" % ENUM_PARSER[b])
    # scalar ONLY when not a pointer: int* / DateADT* are arrays/out-params, not ints.
    if b in SCALAR_ARG and "*" not in nc:
        return ("scalar",) + SCALAR_ARG[b]
    # C string: a single `const char *` is a Java String (jnr marshals it), passed
    # straight to JMEOS — this is how the *_in(text) parsers take their WKT literal.
    # (char** / multi-pointer stay unmapped; the jar arity cross-check guards mismatches.)
    if b == "char" and nc.count("*") == 1:
        return ("scalar", "StringType", "String", "%s")
    return None


def classify(f):
    """Split params into (in_params, out). Out-parameters are the params the catalog
    flags in shape.outParams — the source's Doxygen @param[out], cross-checked against
    the C signature in MEOS-API, so the same signal drives the JMEOS jar's folding and
    ours. A size_t* out-param is the buffer-length JMEOS swallows and drops (the
    *_as_wkb / *_as_hexwkb family). A single non-size pointer out-param on a bool/void
    function folds to a returned Pointer, dereferenced (OUTPRIM) or serialized (SERIAL);
    out is (DataType, expr) or None. Functions with two or more value out-params do not
    fold — their out-params stay visible."""
    params = f["params"]
    outset = set(f.get("shape", {}).get("outParams", []))
    # size_t* out-params are the byte-count buffers of the *_as_wkb/_as_hexwkb family:
    # JMEOS swallows them and returns the buffer directly, so they are never visible.
    vis = [p for p in params
           if not (p["name"] in outset and norm(p["canonical"]) == "size_t *")]
    rt = norm(f["returnType"]["canonical"])
    results = [p for p in vis if p["name"] in outset
               and "const" not in p["canonical"]
               and norm(p["canonical"]).endswith("*")]
    if rt in ("bool", "void") and len(results) == 1:
        p = results[0]
        lastc = p["canonical"]
        lastn = norm(lastc)
        in_params = [q for q in vis if q is not p]
        if lastn in OUTPRIM:
            return in_params, OUTPRIM[lastn]
        if base(lastc) in SERIAL:
            return in_params, SERIAL[base(lastc)]
    return vis, None


def ret_emit(canon, sqlop):
    """('ptr', DataType, serialize) | ('scalar', DataType, serialize) | None."""
    t = norm(canon)
    b = base(t)
    if b in SERIAL and t.endswith("*"):
        return ("ptr",) + SERIAL[b]
    if t == "text *":
        return ("ptr",) + TEXT_RET
    if b == "TimestampTz":            # JMEOS maps TimestampTz -> OffsetDateTime
        return ("dt", "StringType", "UdfMarshal.tsOut(%s)")
    if t in INT32 or b in INT32:
        if sqlop in PRED_OPS:
            return ("scalar", "BooleanType", "%s == 1")
        return ("scalar", "IntegerType", "%s")
    if t in SCALAR_RET:
        return ("scalar",) + SCALAR_RET[t]
    if b in SCALAR_RET:
        return ("scalar",) + SCALAR_RET[b]
    return None


def supported(f):
    """Reason string if NOT emittable, else None."""
    # A binding calls the public API alone. The catalog's `api` states it, public for a
    # function whose @ingroup is a public group; a function stating no group reads as
    # internal there, so its name is no test of it.
    if f.get("api") != "public":
        return "internal"
    in_params, out = classify(f)
    if out is None:
        r = ret_emit(f["returnType"]["canonical"], f.get("sqlop"))
        if r is None:
            b = base(f["returnType"]["canonical"])
            return ("internal" if b in INTERNAL or b == "__INTERNAL__" else "ret:"+norm(f["returnType"]["canonical"]))
    # An array the catalog names in shape.inputArrays is no single value, though a contiguous
    # array of structs (spanset_make reads Span *spans) has the C type of one: a UDF decoding
    # one value would hand MEOS `count` elements to read past it. The typed SQL surfaces carry
    # such an array (#_array_arg of codegen_jvm.py); here it is refused.
    arrays = {a["param"] for a in (f.get("shape") or {}).get("inputArrays") or ()}
    for p in in_params:
        if p["name"] in arrays and (arg_kind(p["canonical"]) or ("",))[0] == "ptr":
            return "array:" + norm(p["canonical"])
    for p in in_params:
        if arg_kind(p["canonical"]) is None:
            b = base(p["canonical"])
            return ("internal" if b in INTERNAL or b == "__INTERNAL__" else "arg:"+norm(p["canonical"]))
    # cross-check against the jar: my call arity must match JMEOS's. A mismatch means
    # the catalog type disagrees with JMEOS (collapsed-jsonb int*, opaque PJ pointer,
    # multi-out-param) — exclude rather than emit a call that won't bind.
    if JSIG and f["name"] in JSIG and JSIG[f["name"]][1] != len(in_params):
        return "arity:jmeos-mismatch"
    # return-kind cross-check: if JMEOS returns a Pointer but my catalog-inferred
    # return is a scalar (or vice versa), the catalog collapsed a type (LWGEOM/Jsonb
    # -> int) — exclude rather than miscompile.
    if out is None and JSIG and f["name"] in JSIG:
        r = ret_emit(f["returnType"]["canonical"], f.get("sqlop"))
        if (JSIG[f["name"]][0] == "jnr.ffi.Pointer") != (r[0] == "ptr"):
            return "ret:jmeos-kind-mismatch"
    return None


_JAVA_KEYWORDS = {
    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
    "class", "const", "continue", "default", "do", "double", "else", "enum",
    "extends", "final", "finally", "float", "for", "goto", "if", "implements",
    "import", "instanceof", "int", "interface", "long", "native", "new", "package",
    "private", "protected", "public", "return", "short", "static", "strictfp",
    "super", "switch", "synchronized", "this", "throw", "throws", "transient",
    "try", "void", "volatile", "while", "true", "false", "null", "var",
}


def _javaid(n):
    """A MEOS param name may collide with a Java keyword (e.g. synchronized)."""
    return n + "_" if n in _JAVA_KEYWORDS else n


def class_for(group):
    """Java class name for a doxygen @ingroup group. The group string is kept
    literally (meos_ prefix stripped) so the same function lands in the same-named
    class across tools. Functions with no @ingroup go to GeneratedUdfs_ungrouped."""
    g = group or "ungrouped"
    if g.startswith("meos_"):
        g = g[len("meos_"):]
    return "GeneratedUdfs_" + g


# Default literals for SQL-OPTIONAL trailing args (params in [sqlArity, sqlArityMax))
# that a generated UDF supplies so it can expose the SQL-required arity instead of the
# wider C one — e.g. asHexWKB(temporal) calls temporal_as_hexwkb(p, (byte) 4),
# trajectory(temporal) calls tpoint_trajectory(p, false). Only scalar flags are
# defaultable; if a hidden arg isn't here the UDF keeps the full C arity.
HIDE_DEFAULT = {"bool": "false", "uint8_t": "(byte) 4", "int": "0", "int32_t": "0",
                "double": "0.0", "int64_t": "0L", "uint64_t": "0L"}


def _java_bound(v, ctype):
    """Translate a C `boundArgs` literal or SQL default to a Java literal for `ctype`, or
    None if it has no direct Java form (NULL, or a name the catalog gives no value). A
    macro or enum member name stands for the value the catalog states for it."""
    v = CONST.get(v, v)
    v = ("true" if v else "false") if isinstance(v, bool) else str(v)
    # SQL spells a boolean literal in any case (a DEFAULT TRUE reaches the catalog as it is
    # written), Java in lower case
    if v.lower() in ("true", "false"):
        v = v.lower()
    if ctype == "bool":
        if re.fullmatch(r"-?\d+", v):
            return "true" if int(v) else "false"
        return v if v in ("true", "false") else None
    if v in ("true", "false"):
        return v
    if re.fullmatch(r"-?\d+", v):
        if ctype == "uint8_t":
            return "(byte) %s" % v
        if ctype in ("int64_t", "uint64_t"):
            return "%sL" % v
        return v
    if re.fullmatch(r"-?\d+\.\d+", v):
        return v
    return None


def _omitted(f, p, vis, name):
    """The values MobilityDB gives parameter `p` of `f` when a call of the SQL name `name`
    states `vis` arguments: the literal the wrapper passes for a signature of that arity
    (per-signature `boundArgs`), and the SQL DEFAULT of a signature whose arguments from `vis`
    on all have one. Only the signatures of `name` count, since one C function backs several
    names binding different literals (`shift`, `scale`, `shiftScale`). A signature's SQL
    arguments pair in order with the visible C parameters it does not bind."""
    fbound = f.get("shape", {}).get("boundArgs", {})
    params = classify(f)[0]
    vals = []
    for s in f.get("sqlSignatures") or []:
        if (s.get("sqlName") or f.get("sqlfn")) != name:
            continue
        sb = {**fbound, **(s.get("boundArgs") or {})}
        args = s.get("args") or []
        if len(args) == vis and p["name"] in sb:
            vals.append(sb[p["name"]])
            continue
        free = [q["name"] for q in params if q["name"] not in sb]
        if len(args) > vis and p["name"] in free:
            i = free.index(p["name"])
            dflt = (list(s.get("argDefaults") or []) + [None] * len(args))[:len(args)]
            if vis <= i < len(args) and all(d is not None for d in dflt[vis:]):
                # an argument left to a NULL default passes what the wrapper passes for it,
                # the catalog's nullDefaultBinds, as the typed surfaces pass it
                # (#_null_default_signatures of codegen_jvm.py)
                nd = (s.get("nullDefaultBinds") or {}).get(str(i)) or {}
                null = dflt[i].strip().upper() == "NULL"
                vals.append(nd.get(p["name"], dflt[i]) if null else dflt[i])
    return vals


def hidden_arg(f, p, vis=None, name=None):
    """The literal a generated UDF supplies for a SQL-hidden trailing param. When the
    catalog records the value the MobilityDB wrapper binds (`shape.boundArgs`, e.g.
    valueAtTimestamp binds strict=true), emit THAT; for the UDF `name` exposing `vis`
    arguments, the one value every signature of that name gives the parameter at that arity
    (`_omitted`); otherwise fall back to the generic type default. Without boundArgs the
    generic default silently diverged from MobilityDB (valueAtTimestamp returned the value
    at an exclusive bound instead of NULL, asEWKT passed 0 decimal digits, which MEOS
    refuses)."""
    ct = base(p["canonical"])
    bound = f.get("shape", {}).get("boundArgs", {}).get(p["name"])
    if bound is not None:
        lit = _java_bound(bound, ct)
        if lit is not None:
            return lit
    if vis is not None:
        lits = {_java_bound(v, ct) for v in _omitted(f, p, vis, name)}
        if len(lits) == 1 and None not in lits:
            return lits.pop()
    return HIDE_DEFAULT[ct]


def emit_single(name, f, vis_arity=None):
    params, out = classify(f)
    # SQL-faithful arity: expose only the first vis_arity params, supplying HIDE_DEFAULT
    # literals for the optional trailing flags. Fall back to the full C arity if any
    # hidden arg has no known default.
    hidden = []
    if vis_arity is not None and 0 <= vis_arity < len(params):
        tail = params[vis_arity:]
        if all(base(p["canonical"]) in HIDE_DEFAULT and "*" not in norm(p["canonical"]) for p in tail):
            hidden = tail
            params = params[:vis_arity]
    if out is None:
        r = ret_emit(f["returnType"]["canonical"], f.get("sqlop"))
        ret_dt, ret_ser, ret_ptr, ret_out = r[1], r[2], (r[0] == "ptr"), False
        # trust the jar for plain numeric returns: the catalog collapses uint64->int,
        # but JMEOS exposes the true width (e.g. *_hash_extended returns long).
        if r[0] == "scalar" and ret_ser == "%s" and name in JSIG and JSIG[name][0] in JPRIM:
            ret_dt = JPRIM[JSIG[name][0]]
    else:
        ret_dt, ret_ser, ret_ptr, ret_out = out[0], out[1], False, True
    box = _RETBOX[ret_dt]
    argnames = [_javaid(p["name"] or ("a%d" % i)) for i, p in enumerate(params)]
    argboxes, kinds = [], []
    for p in params:
        k = arg_kind(p["canonical"])
        kinds.append(k)
        argboxes.append({"ptr": "Object", "ts": "Object", "scalar": k[2] if k[0] == "scalar" else "String"}[k[0]])
    iface = "UDF%d<%s, %s>" % (len(params), ", ".join(argboxes), box) if params else "UDF0<%s>" % box
    # Emit as an inline lambda inside a register() call (NOT a static field): 2349
    # static-field initializers overrun the 64 KB <clinit> bytecode limit. Inline
    # lambdas compile to separate synthetic methods, keeping the chunk method small.
    L = [f'        spark.udf().register("{name}", ({iface}) (' + ", ".join(argnames) + ") -> {"
         if params else f'        spark.udf().register("{name}", ({iface}) () -> {{']
    if argnames:
        L.append("        if (" + " || ".join(f"{a} == null" for a in argnames) + ") return null;")
    callargs, frees = [], []
    for a, p, k in zip(argnames, params, kinds):
        if k[0] == "ptr":
            parse = k[1]
            L.append(f"        jnr.ffi.Pointer p_{a} = {parse % a};")
            L.append(f"        if (p_{a} == null) return null;")
            callargs.append(f"p_{a}"); frees.append(f"p_{a}")
        elif k[0] == "ts":
            L.append(f"        java.time.OffsetDateTime dt_{a} = UdfMarshal.tsOdt({a});")
            callargs.append(f"dt_{a}")
        else:
            callargs.append(k[3] % a)
    # supply the wrapper-bound literal (shape.boundArgs) — or the generic type default —
    # for the SQL-hidden trailing flags (sqlArity..C-arity)
    for p in hidden:
        callargs.append(hidden_arg(f, p, len(params), name))
    call = f"GeneratedFunctions.{f['name']}(" + ", ".join(callargs) + ")"
    L.append("        try {")
    if ret_out:
        # JMEOS returns a jnr-allocated buffer (GC-managed) — deref, never free it.
        L.append(f"            jnr.ffi.Pointer _r = {call};")
        L.append("            if (_r == null) return null;")
        L.append(f"            return {ret_ser % '_r'};")
    elif ret_ptr:
        L.append(f"            jnr.ffi.Pointer _r = {call};")
        L.append("            if (_r == null) return null;")
        L.append(f"            try {{ return {ret_ser % '_r'}; }} finally {{ MeosMemory.free(_r); }}")
    else:
        L.append(f"            return {ret_ser % call};")
    L.append("        } finally {")
    for fr in frees:
        L.append(f"            MeosMemory.free({fr});")
    L.append("        }")
    L.append(f"        }}, DataTypes.{ret_dt});")
    return "\n".join(L)


_RETBOX = {"IntegerType": "Integer", "DoubleType": "Double", "BooleanType": "Boolean",
           "LongType": "Long", "StringType": "String", "ByteType": "Byte",
           "BinaryType": "byte[]"}


def _sig(f):
    """The Spark-marshalled SIGNATURE of an emittable function: (arg-slot tuple,
    return-shape). Two functions with the SAME _sig present an identical Java UDF
    interface, so several C overloads of one @sqlfn name that share a _sig can be
    dispatched by ONE Spark UDF. Slot = the Java box for a scalar arg, "P" for any
    pointer arg (always an Object), "T" for a timestamp. Returns None if unemittable."""
    params, out = classify(f)
    slots = []
    for p in params:
        k = arg_kind(p["canonical"])
        if k is None:
            return None
        slots.append("P" if k[0] == "ptr" else ("T" if k[0] == "ts" else k[2]))
    if out is not None:
        ret = ("out", out[0], out[1])
    else:
        r = ret_emit(f["returnType"]["canonical"], f.get("sqlop"))
        if r is None:
            return None
        ret = (("ptr" if r[0] == "ptr" else "scalar"), r[1], r[2])
    return (tuple(slots), ret)


def _visparams(f):
    """The parameters a generated UDF exposes: the SQL-required ones, when the trailing C
    parameters the SQL surface hides are all defaultable flags. This is the shape the UDF
    presents and the shape emit_dispatch emits, so it is the shape overloads are grouped
    and told apart by — grouping on the wider C shape puts an overload whose flag is
    SQL-optional in a group of its own, where a larger group of siblings outvotes it and
    the overload the SQL name means is dropped before any preference is consulted."""
    params = classify(f)[0]
    va = f.get("sqlArity")
    if va is not None and 0 <= va < len(params):
        tail = params[va:]
        if all(base(q["canonical"]) in HIDE_DEFAULT and "*" not in norm(q["canonical"])
               for q in tail):
            return params[:va]
    return params


def _sqlsig(f):
    """_sig over the SQL-visible parameters — the Java UDF interface the name presents."""
    sig = _sig(f)
    if sig is None:
        return None
    return (tuple(sig[0][:len(_visparams(f))]), sig[1])


def _merges(sig, rep):
    """Whether the overloads of _sqlsig `sig` join those of `rep` in one UDF, the shape test
    beside #_dispatchable, which then admits the overloads of the merged group.

    Spark keeps one registration per name and a Java UDF one interface, but a position may
    take Object: a pointer value is told apart by its type-checked reader, which answers null
    for a value of another class (UdfMarshal.read), and a scalar by its Java class, so two
    overloads of one arity and one result whose classes differ at a position meet in one UDF
    over Object there (th3index over a temporal point and an integer, and over a cell and a
    time value). A timestamp position is read by UdfMarshal.tsOdt, which tells no class apart,
    so a position where one of them takes a timestamp keeps the shapes apart."""
    return (len(sig[0]) == len(rep[0]) and sig[1] == rep[1]
            and all(a == b or "T" not in (a, b) for a, b in zip(sig[0], rep[0])))


def _safe_dispatch(f):
    """A function is safely arg-kind-dispatchable only if every pointer arg parses via a
    type-safe WKB reader (UdfMarshal.tFrom and its siblings, which check the WKB type and
    return null on a foreign family) or the validating WKT/EWKT parser
    (UdfMarshal.geoFromText, which delegates to geo_from_text and returns null on a
    foreign string). The text *_in parsers (stbox_in / tbox_in / cbuffer_in / npoint_in /
    pose_in / jsonb_in) are NOT: fed a hex-WKB or WKT string they may mis-parse, so an
    overload using one cannot be told apart at runtime and must not enter a dispatcher."""
    for p in classify(f)[0]:
        k = arg_kind(p["canonical"])
        if k and k[0] == "ptr" and not type_checked(k[1]):
            return False
    return True


def _dispatchable(group):
    """The overloads of one @sqlfn signature group a parse-based dispatcher can hold.

    A text *_in parser cannot be what tells two overloads apart (see _safe_dispatch), but at a
    position where every overload of the group takes the same kind no overload is chosen:
    each reads the value with the same parser, and the dispatcher routes on the other
    positions. So an overload is left out only when it takes a text-parsed argument at a
    position where the group's overloads differ. atStbox's overloads differ only by the
    temporal they restrict, and all of them read the box with stbox_in."""
    kinds = [_argkinds(f) for f in group]

    # the test #_safe_dispatch asks, through #type_checked
    def text_parsed(k):
        return k and k[0] == "ptr" and not type_checked(k[1])

    return [f for f, ks in zip(group, kinds)
            if not any(text_parsed(k) and any(i >= len(o) or o[i] != k for o in kinds)
                       for i, k in enumerate(ks))]


def _parsetuple(f):
    """The arg KINDS that a runtime parse can actually DISTINGUISH, over the SQL-visible
    parameters: K_TEMPORAL / K_GSERIALIZED / K_SPAN ... for pointer args, a constant marker for
    scalars and timestamps, and K_TEMPORAL:<type> where the overload names a concrete
    temporal type the WKB byte identifies. Two overloads with the SAME _parsetuple cannot
    be told apart by parsing, so only one of them may go into a parse-based dispatcher;
    ones that carry different concrete types can, and both are kept."""
    out = []
    for k in _argkinds(f):
        out.append(k[2] if k[0] == "ptr" else ("TS" if k[0] == "ts" else "S"))
    return tuple(out)


# Among parse-indistinguishable overloads, prefer the geometry family the canonical
# BerlinMOD suite (and most users) call: tgeo / geo. Lower rank = more preferred.
_FAMTOK = ["_tgeo_", "tgeo_", "_geo_", "_geo", "geo_", "temporal_", "_tspatial_", "tpoint", "tnumber"]
def _famrank(f):
    n = f["name"]
    return next((i for i, t in enumerate(_FAMTOK) if t in n), len(_FAMTOK))


# MeosType name -> WKB type byte, for the temporal types only, filled from the catalog's
# MeosType enum in main(). A C overload whose name begins with one of these names takes
# that concrete temporal type, so the dispatcher can tell it apart from a sibling overload
# by the WKB type byte instead of guessing.
TEMPTYPE_CODE = {}
# For each C enum, the public catalog function reading it from its name, so an enum argument
# travels as the text its SQL function takes. The rule is the Flink arm's, SqlModel._enum_parsers
# in codegen_jvm.py, read from the public functions alone, as #supported admits only those.
ENUM_PARSER = {}
# The value of each macro and enum member the catalog states, which is what a bound
# literal or SQL default naming one passes. Filled from the catalog before any emit pass.
CONST = {}


def _expected_temptype(f):
    """The concrete temporal type a C overload takes, from its name, or None.

    MobilityDB names a typed entry point `<type>_<operation>`, and the catalog's MeosType
    enum is the list of type names, so the longest MeosType temporal name the function name
    starts with is the type of its receiver. A name built on a type CLASS rather than a type
    (tpoint_trajectory, eintersects_tgeo_tgeo) matches nothing and stays generic."""
    n = f["name"]
    cands = [t for t in TEMPTYPE_CODE if n.startswith(t + "_")]
    return max(cands, key=len) if cands else None


def _argkinds(f):
    """The arg kinds of f's SQL-visible parameters, with the receiver refined to the
    concrete temporal type the overload takes.

    arg_kind resolves a `Temporal *` to one generic parser, so two overloads that differ
    only by temporal type (tgeompoint_to_th3index / tgeogpoint_to_th3index) look identical
    to a parse-based dispatcher and one of them is answered by the other's C function. The
    WKB type byte distinguishes them, so the receiver of a typed overload parses through
    the type-checked parser and carries that type in its kind."""
    ks = [arg_kind(p["canonical"]) for p in _visparams(f)]
    t = _expected_temptype(f)
    if t is not None:
        for i, k in enumerate(ks):
            if k and k[0] == "ptr" and k[2] == "K_TEMPORAL":
                ks[i] = ("ptr", "UdfMarshal.tFromOf(%%s, %d)" % TEMPTYPE_CODE[t],
                         "K_TEMPORAL:" + t)
                break
    return ks


def emit_dispatch(name, cands, vis_arity=None):
    """Emit ONE Spark UDF for an @sqlfn name backed by SEVERAL C overloads that share
    a _sig (e.g. eIntersects <- eintersects_tgeo_tgeo / _tgeo_geo / _geo_tgeo). Spark
    cannot overload a UDF name, so the single lambda parses each pointer arg with each
    candidate's parsers in turn: the FIRST candidate whose every pointer arg parses is
    the matching overload (the WKB / WKT / span parsers are mutually discriminating,
    so exactly one matches). Parse-all-then-check keeps it leak-free on every path.
    vis_arity (SQL-required arity) exposes only the first N args, supplying HIDE_DEFAULT
    literals for the optional trailing flags (shared across the overloads via _sig)."""
    rep = cands[0]
    slots, ret = _sig(rep)
    n = len(slots)
    vis = n
    rep_params = classify(rep)[0]
    if vis_arity is not None and 0 <= vis_arity < n:
        tail = rep_params[vis_arity:]
        if all(base(p["canonical"]) in HIDE_DEFAULT and "*" not in norm(p["canonical"]) for p in tail):
            vis = vis_arity
            slots = slots[:vis]
            n = vis
    argnames = ["a%d" % i for i in range(n)]
    # A position whose class the overloads disagree on takes Object, and each overload
    # reads a scalar there only from a value of its own class (#_merges).
    shapes = [_sig(f)[0][:n] for f in cands]
    mixed = {i for i in range(n) if len({s[i] for s in shapes}) > 1}
    argboxes = [("Object" if s in ("P", "T") or i in mixed else s) for i, s in enumerate(slots)]
    ret_kind, ret_dt, ret_ser = ret
    box = _RETBOX[ret_dt]
    iface = "UDF%d<%s, %s>" % (n, ", ".join(argboxes), box) if n else "UDF0<%s>" % box
    L = ['        spark.udf().register("%s", (%s) (%s) -> {' % (name, iface, ", ".join(argnames))]
    if argnames:
        L.append("        if (" + " || ".join("%s == null" % a for a in argnames) + ") return null;")
    # order the PERMISSIVE parsers LAST so the strict ones get first refusal: a GEO/WKT
    # parser accepts what a WKB parser refuses, and an overload named after a type CLASS
    # (tpoint_trajectory) accepts every temporal, so it must not answer for a sibling
    # named after a concrete type (tpose_trajectory) that the WKB byte would have matched.
    def geocount(f):
        return sum(1 for p in classify(f)[0] if base(p["canonical"]) == "GSERIALIZED")
    def permissiveness(f):
        return (geocount(f), 0 if _expected_temptype(f) else 1)
    for f in sorted(cands, key=permissiveness):
        cps = classify(f)[0]
        ks = _argkinds(f)
        callargs, ptrs, classes = [], [], []
        L.append("        {")
        for i, (a, k) in enumerate(zip(argnames, ks)):
            if k[0] == "ptr":
                pv = "P_%s" % a
                L.append("          jnr.ffi.Pointer %s = %s;" % (pv, k[1] % a))
                ptrs.append(pv); callargs.append(pv)
            elif k[0] == "ts":
                L.append("          java.time.OffsetDateTime D_%s = UdfMarshal.tsOdt(%s);" % (a, a))
                callargs.append("D_%s" % a)
            elif i in mixed:
                classes.append("%s instanceof %s" % (a, k[2]))
                callargs.append(k[3] % ("((%s) %s)" % (k[2], a)))
            else:
                callargs.append(k[3] % a)
        # SQL-hidden trailing flags get the wrapper-bound literal (shape.boundArgs) or the
        # generic default (zip above paired only the first `vis` exposed args; the
        # candidate's remaining params are the flags).
        for p in cps[vis:]:
            callargs.append(hidden_arg(f, p, vis, name))
        cond = " && ".join(classes + ["%s != null" % p for p in ptrs]) or "true"
        free = " ".join("MeosMemory.free(%s);" % p for p in ptrs)
        call = "GeneratedFunctions.%s(%s)" % (f["name"], ", ".join(callargs))
        L.append("          if (%s) {" % cond)
        if ret_kind == "out":
            L.append("            jnr.ffi.Pointer _r = %s;" % call)
            L.append("            try { return _r == null ? null : %s; } finally { %s }" % (ret_ser % "_r", free))
        elif ret_kind == "ptr":
            L.append("            jnr.ffi.Pointer _r = %s;" % call)
            L.append("            try { return _r == null ? null : %s; } finally { MeosMemory.free(_r); %s }" % (ret_ser % "_r", free))
        else:
            L.append("            try { return %s; } finally { %s }" % (ret_ser % call, free))
        L.append("          }")
        for p in ptrs:
            L.append("          if (%s != null) MeosMemory.free(%s);" % (p, p))
        L.append("        }")
    # No overload takes these arguments: refused, as TemporalAggregate.pick refuses a temporal
    # type its steps do not take, never answered as NULL. The UDF answers the overloads sharing
    # its one Spark result type; the typed SQL surface (MobilitySparkSql) resolves every one.
    L.append('        throw new IllegalArgumentException("%s takes the arguments of %s; '
             'the typed SQL surface (MobilitySparkSql) resolves every overload");'
             % (name, ", ".join(f["name"] for f in cands)))
    L.append("        }, DataTypes.%s);" % ret_dt)
    return "\n".join(L)


def emit_timearg(name, op):
    """Emit the time-restrict polymorphic UDF (atTime / minusTime): the time arg is
    classified at runtime (timestamp / period / set / span set) and routed to the
    matching temporal_<op>_{timestamptz,tstzspan,tstzset,tstzspanset} backing — Spark
    cannot overload a UDF name by the time arg's type."""
    refs = ", ".join("GeneratedFunctions::temporal_%s_%s" % (op, k)
                     for k in ("timestamptz", "tstzspan", "tstzset", "tstzspanset"))
    return ('        spark.udf().register("%s", (UDF2<Object, Object, byte[]>) (a, b) -> {\n'
            '        if (a == null || b == null) return null;\n'
            '        jnr.ffi.Pointer t = UdfMarshal.tFrom(a);\n'
            '        if (t == null) return null;\n'
            '        try { return UdfMarshal.restrictTime(t, b, %s); }\n'
            '        finally { MeosMemory.free(t); }\n'
            '        }, DataTypes.BinaryType);' % (name, refs))


def tgeoarr_shape(f):
    """Recognize a MEOS NxN array kernel — params are one or more (Temporal **, int)
    array pairs, an optional `double` (distance), an optional `bool` (the model of the earth
    a geography is measured on, `spheroid`), and optional non-const out-params
    (int *count, SpanSet ***periods). Returns a shape dict or None.
    ret: 'double' (scalar, e.g. minDistance) | 'pairs' (int* of 2*count [i,j], e.g. the
    *Pairs functions). periods=True iff a SpanSet*** out-param is present (tDwithin)."""
    ps = f["params"]
    n = len(ps)
    i = arrays = 0
    dist = has_count = periods = False
    flag = None
    while i < n:
        c = norm(ps[i]["canonical"]); isconst = "const" in ps[i]["canonical"]
        bare = c.replace("*", "").strip()   # base() maps ** -> __INTERNAL__, so strip here
        if bare == "Temporal" and c.endswith("**"):
            if i + 1 < n and norm(ps[i + 1]["canonical"]) == "int":
                arrays += 1; i += 2; continue
            return None
        if c == "double":
            dist = True; i += 1; continue
        if c == "bool" and flag is None:
            flag = ps[i]["name"]; i += 1; continue
        if c == "int *" and not isconst:
            has_count = True; i += 1; continue
        if bare == "SpanSet" and c.count("*") == 3 and not isconst:
            periods = True; i += 1; continue
        return None
    if arrays < 1:
        return None
    rt = norm(f["returnType"]["canonical"])
    ret = "double" if rt == "double" else ("pairs" if rt == "int *" else None)
    if ret is None or (ret == "pairs" and not has_count):
        return None
    return {"arrays": arrays, "dist": dist, "flag": flag, "periods": periods, "ret": ret}


def emit_tgeoarr(name, f, shape):
    """Emit a Spark UDF for an NxN array kernel. Each (Temporal**, int) pair becomes one
    Spark array arg of temporals (the count is the array length); an optional `double` distance
    is a Double arg; out-params (count / periods) are auto-allocated. Scalar-return kernels
    yield a Double UDF; pairs-return kernels yield array<struct<i,j[,periods]>> (consumed via
    LATERAL explode), with MEOS 1-based indices mapped to 0-based."""
    nA = shape["arrays"]
    # the flag a SQL signature omits (its SQL-required arity stops before it) is the value
    # MobilityDB gives it, as #emit_single supplies a hidden flag
    flag, hidden = shape["flag"], None
    vis = nA + (1 if shape["dist"] else 0)
    if flag and f.get("sqlArity") == vis:
        p = next(q for q in f["params"] if q["name"] == flag)
        flag, hidden = None, hidden_arg(f, p, vis, name)
    argnames = ["a%d" % i for i in range(nA)] + (["dist"] if shape["dist"] else []) \
        + ([flag] if flag else [])
    # dist arrives as Object: a bare SQL literal like 10.0 is decimal (BigDecimal), not
    # double, so take it as Object and coerce via Number rather than failing the cast.
    boxes = ["Object"] * nA + (["Object"] if shape["dist"] else []) \
        + (["Boolean"] if flag else [])
    if shape["ret"] == "double":
        retbox, ret_dt = "Double", "DataTypes.DoubleType"
    else:
        fields = ['DataTypes.createStructField("i", DataTypes.IntegerType, false)',
                  'DataTypes.createStructField("j", DataTypes.IntegerType, false)']
        if shape["periods"]:
            fields.append('DataTypes.createStructField("periods", DataTypes.BinaryType, true)')
        struct = "DataTypes.createStructType(new org.apache.spark.sql.types.StructField[]{%s})" % ", ".join(fields)
        retbox, ret_dt = "java.util.List<org.apache.spark.sql.Row>", "DataTypes.createArrayType(%s)" % struct
    iface = "UDF%d<%s, %s>" % (len(argnames), ", ".join(boxes), retbox)
    L = ['        spark.udf().register("%s", (%s) (%s) -> {' % (name, iface, ", ".join(argnames))]
    L.append("        if (" + " || ".join(["a%d == null" % i for i in range(nA)]
                                   + ([flag + " == null"] if flag else []))
             + ") return null;")
    for i in range(nA):
        L.append("        Object[] s%d = UdfMarshal.asArray(a%d);" % (i, i))
    L.append("        if (" + " || ".join("s%d == null" % i for i in range(nA)) + ") return null;")
    L.append("        jnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();")
    for i in range(nA):
        L.append("        jnr.ffi.Pointer[] e%d = new jnr.ffi.Pointer[s%d.length];" % (i, i))
        L.append("        jnr.ffi.Pointer arr%d = UdfMarshal.tArrNative(s%d, e%d, _rt);" % (i, i, i))
    callargs = []
    for i in range(nA):
        callargs += ["arr%d" % i, "s%d.length" % i]
    if shape["dist"]:
        callargs.append("dist == null ? 0.0 : ((Number) dist).doubleValue()")
    if flag:
        callargs.append(flag)
    elif hidden:
        callargs.append(hidden)
    if shape["ret"] == "pairs":
        L.append("        jnr.ffi.Pointer _cnt = jnr.ffi.Memory.allocateDirect(_rt, 4);")
        callargs.append("_cnt")
    if shape["periods"]:
        L.append("        jnr.ffi.Pointer _per = jnr.ffi.Memory.allocateDirect(_rt, 8);")
        callargs.append("_per")
    call = "GeneratedFunctions.%s(%s)" % (f["name"], ", ".join(callargs))
    L.append("        try {")
    if shape["ret"] == "double":
        L.append("            return %s;" % call)
    else:
        L.append("            jnr.ffi.Pointer _res = %s;" % call)
        L.append("            int _c = _cnt.getInt(0L);")
        if shape["periods"]:
            L.append("            return UdfMarshal.readPairsPeriods(_res, _c, _per.getPointer(0L));")
        else:
            L.append("            return UdfMarshal.readPairs(_res, _c);")
    L.append("        } finally {")
    for i in range(nA):
        # the native array is read by MEOS during the call and by nothing after it (codegen_jvm.py)
        L.append("            java.lang.ref.Reference.reachabilityFence(arr%d);" % i)
        L.append("            UdfMarshal.freeArr(e%d);" % i)
    L.append("        }")
    L.append("        }, %s);" % ret_dt)
    return "\n".join(L)


# Return-element base -> (Spark element type, UdfMarshal reader). ONLY fixed-width scalar
# elements whose C array has a known stride; struct elements (Span*/STBox*/TBox*) are NOT
# here (their per-element size is opaque to the binding), nor TimestampTz*/DateADT* (which
# need MEOS-owned rendering, not raw longs).
SCALAR_ELEM = {
    "int":      ("IntegerType", "readIntArr"),
    "int64_t":  ("LongType",    "readLongArr"),
    "uint64_t": ("LongType",    "readLongArr"),
    "double":   ("DoubleType",  "readDoubleArr"),
}


def scalar_values_shape(f):
    """Recognize an array-returning accessor `(inputs..., int *count) -> <scalar>*`: the
    trailing non-const `int *` is the element-count out-param and the return is a
    contiguous C array of a fixed-width scalar (int/int64/uint64/double). JMEOS returns the
    array Pointer and the caller allocates the count. Returns a shape dict or None."""
    params, out = classify(f)
    if out is not None or not params:
        return None
    last = params[-1]
    if "const" in last["canonical"] or norm(last["canonical"]) != "int *":
        return None
    rt = norm(f["returnType"]["canonical"])
    rb = base(f["returnType"]["canonical"])
    if not rt.endswith("*") or rt.count("*") != 1 or rb not in SCALAR_ELEM:
        return None
    ins = params[:-1]
    # every non-count input must marshal (a stray `int *`/pointer-out among the inputs is
    # arg_kind None -> excluded, so array-in kernels like intset_make never match here).
    for p in ins:
        if arg_kind(p["canonical"]) is None:
            return None
    return {"elem": SCALAR_ELEM[rb], "ins": ins}


def emit_scalar_values(name, f, shape):
    """Emit a Spark UDF for a scalar array-returning accessor: marshal the inputs (mirroring
    emit_single), allocate the `int *count` out-param, call, then read `count` fixed-width
    elements from the returned array (freeing it) into a Spark array<int|long|double>."""
    ins = shape["ins"]
    ret_dt, reader = shape["elem"]
    argnames = [_javaid(p["name"] or ("a%d" % i)) for i, p in enumerate(ins)]
    argboxes, kinds = [], []
    for p in ins:
        k = arg_kind(p["canonical"]); kinds.append(k)
        argboxes.append({"ptr": "Object", "ts": "Object",
                         "scalar": k[2] if k[0] == "scalar" else "String"}[k[0]])
    retbox = "java.util.List<%s>" % {"IntegerType": "Integer", "LongType": "Long",
                                     "DoubleType": "Double"}[ret_dt]
    iface = ("UDF%d<%s, %s>" % (len(ins), ", ".join(argboxes), retbox)) if ins else "UDF0<%s>" % retbox
    L = ['        spark.udf().register("%s", (%s) (%s) -> {' % (name, iface, ", ".join(argnames))]
    if argnames:
        L.append("        if (" + " || ".join("%s == null" % a for a in argnames) + ") return null;")
    callargs, frees = [], []
    for a, p, k in zip(argnames, ins, kinds):
        if k[0] == "ptr":
            L.append("        jnr.ffi.Pointer p_%s = %s;" % (a, k[1] % a))
            L.append("        if (p_%s == null) return null;" % a)
            callargs.append("p_%s" % a); frees.append("p_%s" % a)
        elif k[0] == "ts":
            L.append("        java.time.OffsetDateTime dt_%s = UdfMarshal.tsOdt(%s);" % (a, a))
            callargs.append("dt_%s" % a)
        else:
            callargs.append(k[3] % a)
    L.append("        jnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();")
    L.append("        jnr.ffi.Pointer _cnt = jnr.ffi.Memory.allocateDirect(_rt, 4);")
    callargs.append("_cnt")
    call = "GeneratedFunctions.%s(%s)" % (f["name"], ", ".join(callargs))
    L.append("        try {")
    L.append("            jnr.ffi.Pointer _res = %s;" % call)
    L.append("            return UdfMarshal.%s(_res, _cnt.getInt(0L));" % reader)
    L.append("        } finally {")
    for fr in frees:
        L.append("            MeosMemory.free(%s);" % fr)
    L.append("        }")
    L.append("        }, DataTypes.createArrayType(DataTypes.%s));" % ret_dt)
    return "\n".join(L)


# ── set-returning signatures: one Spark row per SQL row, the rows as an array ──
# A SQL function returning a set of rows (unnest, valueSplit, spaceTiles, dynTimeWarpPath ...)
# is a MEOS C function returning parallel arrays: its result and its out-parameters, the length
# in its `int *count` out-parameter. The catalog names the C value behind each column of the
# row (sqlSignatures[].columns): `from` is "return", an out-parameter, or "ordinal" (the row's
# 1-based ordinality); `field` reads a member of a struct element. The UDF returns
# array<struct<columns>>, which `LATERAL VIEW inline` unfolds into the rows, or array<value>
# for a single unnamed column, which `explode` unfolds. The struct layouts are the catalog's
# (STRUCTS, filled in main), sized as ObjectLayerGenerator#structSize sizes them.
STRUCTS = {}

SCALAR_BYTES = {"bool": 1, "char": 1, "int8": 1, "int8_t": 1, "uint8": 1, "uint8_t": 1,
                "short": 2, "int16": 2, "int16_t": 2, "uint16": 2, "uint16_t": 2,
                "int": 4, "int32": 4, "int32_t": 4, "uint32": 4, "uint32_t": 4, "float": 4,
                "Oid": 4, "DateADT": 4,
                "long": 8, "int64": 8, "int64_t": 8, "uint64": 8, "uint64_t": 8, "double": 8,
                "float8": 8, "Datum": 8, "Timestamp": 8, "TimestampTz": 8, "TimeADT": 8,
                "size_t": 8, "uintptr_t": 8}


def _size_align(ctype):
    """(size, alignment) in bytes of a struct field's C type, or None when the catalog does
    not state it: a scalar, a pointer, a fixed-size array or a nested catalog struct."""
    c = norm(ctype)
    if c.endswith("*"):
        return 8, 8
    if "[" in c:
        b = _size_align(c[:c.index("[")])
        return None if b is None else (b[0] * int(c[c.index("[") + 1:c.index("]")]), b[1])
    if c in STRUCTS:
        lay = struct_layout(c)
        return None if lay is None else (lay[0], lay[1])
    n = SCALAR_BYTES.get(c)
    return None if n is None else (n, n)


def struct_layout(name):
    """(size, alignment, {field: (offset, ctype)}) of a catalog struct under the C layout rules
    of the 64-bit targets the binding ships for, or None when a field's size is unknown."""
    offset, widest, fields = 0, 1, {}
    for f in STRUCTS[name].get("fields") or []:
        sa = _size_align(f["cType"])
        if sa is None:
            return None
        widest = max(widest, sa[1])
        offset = (offset + sa[1] - 1) // sa[1] * sa[1]
        fields[f["name"]] = (offset, norm(f["cType"]))
        offset += sa[0]
    return (offset + widest - 1) // widest * widest, widest, fields


# Contiguous scalar elements: C type -> (Spark type, Java read of element `i` of array `a`).
# A TimestampTz renders as MEOS writes it, as every TimestampTz result does (ret_emit);
# a DateADT stays the day count, as a DateADT result does.
ROW_SCALAR = {
    "int":         ("IntegerType", "%s.getInt((long) %s * 4L)"),
    "int32_t":     ("IntegerType", "%s.getInt((long) %s * 4L)"),
    "int64_t":     ("LongType",    "%s.getLongLong((long) %s * 8L)"),
    "uint64_t":    ("LongType",    "%s.getLongLong((long) %s * 8L)"),
    "double":      ("DoubleType",  "%s.getDouble((long) %s * 8L)"),
    "bool":        ("BooleanType", "(%s.getByte((long) %s) != 0)"),
    "DateADT":     ("IntegerType", "%s.getInt((long) %s * 4L)"),
    "TimestampTz": ("StringType",  "UdfMarshal.tsOut(utils.TimestampTzConverter.toOffsetDateTime("
                                   "%s.getLongLong((long) %s * 8L)))"),
}


def row_column(f, col):
    """How to read one column of a returned row: a dict with the Spark type, the Java read of
    row `_i` from the array the column names (one %s), and whether each element is a MEOS
    allocation to free; None when the column cannot be read."""
    src = col["from"]
    if src == "ordinal":
        return {"src": "ordinal", "dt": "IntegerType", "read": "(_i + 1)", "free": False}
    if "element" in col:            # the index pairs of the *Pairs kernels: tgeoarr's shape
        return None
    if src == "return":
        arr = norm(f["returnType"]["canonical"])
    else:
        p = next((p for p in f["params"] if p["name"] == src), None)
        if p is None or not norm(p["canonical"]).endswith("**"):
            return None
        arr = norm(p["canonical"])[:-1].strip()       # the array the out-parameter points to
    if not arr.endswith("*"):
        return None
    elem = arr[:-1].strip()
    if "field" in col:
        lay = struct_layout(elem) if elem in STRUCTS else None
        if lay is None or col["field"] not in lay[2]:
            return None
        off, ctype = lay[2][col["field"]]
        if ctype not in ROW_SCALAR:
            return None
        at = "%%s.slice((long) _i * %dL + %dL)" % (lay[0], off)
        return {"src": src, "dt": ROW_SCALAR[ctype][0], "free": False,
                "read": ROW_SCALAR[ctype][1] % (at, "0")}
    if elem.endswith("*"):                            # an array of pointers
        b = elem[:-1].strip()
        if b in SERIAL:
            return {"src": src, "dt": SERIAL[b][0], "free": True,
                    "read": SERIAL[b][1] % "%s.getPointer((long) _i * 8L)"}
        if b == "text":
            return {"src": src, "dt": "StringType", "free": True,
                    "read": "GeneratedFunctions.text_out(%s.getPointer((long) _i * 8L))"}
        return None
    if elem in ROW_SCALAR:
        return {"src": src, "dt": ROW_SCALAR[elem][0], "free": False,
                "read": ROW_SCALAR[elem][1] % ("%s", "_i")}
    if elem in SERIAL and elem in STRUCTS:            # contiguous structs: STBox, TBox
        lay = struct_layout(elem)
        if lay is None:
            return None
        return {"src": src, "dt": SERIAL[elem][0], "free": False,
                "read": SERIAL[elem][1] % ("%%s.slice((long) _i * %dL)" % lay[0])}
    return None


def setret_shape(f, sig, vis=None):
    """The rows a set-returning signature of `f` returns, or None when they cannot be read: the
    inputs (every parameter outside shape.outParams, each marshallable), the `int *count`
    out-parameter, the out-parameters holding arrays, and one reader per column. With `vis`, the
    SQL-required arity of a SQL name, the inputs from `vis` on are hidden when each is a
    defaultable flag preceding every out-parameter, as #emit_single hides them."""
    outs = f.get("shape", {}).get("outParams", [])
    params = f["params"]
    ins = [p for p in params if p["name"] not in outs]
    hidden = []
    if vis is not None and 0 < vis < len(ins):
        tail = ins[vis:]
        first_out = min([i for i, q in enumerate(params) if q["name"] in outs] or [len(params)])
        if all(base(q["canonical"]) in HIDE_DEFAULT and "*" not in norm(q["canonical"])
               and params.index(q) < first_out for q in tail):
            hidden, ins = tail, ins[:vis]
    counts = [p for p in params if p["name"] in outs and norm(p["canonical"]) == "int *"
              and "const" not in p["canonical"]]
    if len(counts) != 1 or not ins or any(arg_kind(p["canonical"]) is None for p in ins):
        return None
    cols = sig.get("columns") or [{"name": None, "from": "return"}]
    readers = []
    for col in cols:
        r = row_column(f, col)
        if r is None:
            return None
        readers.append(dict(r, name=col["name"]))
    arrays = [p["name"] for p in params if p["name"] in outs and p is not counts[0]]
    if any(r["src"] not in ("return", "ordinal") and r["src"] not in arrays for r in readers):
        return None
    return {"ins": ins, "hidden": hidden, "count": counts[0]["name"], "arrays": arrays,
            "cols": readers}


def _row_dt(shape):
    """The Spark type of one returned row: the value of a single unnamed column, else a struct
    of the named columns."""
    cols = shape["cols"]
    if len(cols) == 1 and cols[0]["name"] is None:
        return "DataTypes.%s" % cols[0]["dt"]
    fields = ", ".join('DataTypes.createStructField("%s", DataTypes.%s, true)'
                       % (c["name"], c["dt"]) for c in cols)
    return ("DataTypes.createStructType(new org.apache.spark.sql.types.StructField[]{%s})"
            % fields)


def _setret_key(shape):
    """What two overloads must share to answer one Spark UDF, as #_sig is for the @sqlfn pass:
    the kinds of their inputs and the Spark types of their columns (the column names follow the
    group, see #emit_setret)."""
    return (tuple(arg_kind(p["canonical"]) for p in shape["ins"]),
            tuple(c["dt"] for c in shape["cols"]),
            len(shape["cols"]) == 1 and shape["cols"][0]["name"] is None)


def emit_setret(name, cands, colnames):
    """Emit a Spark UDF returning the rows of a set-returning signature as an array, its columns
    named `colnames`. `cands` are the (C function, shape) overloads sharing one #_setret_key,
    tried in turn as #emit_dispatch tries its overloads: a typed overload first, its receiver
    read through the reader that checks its WKB type byte (#_argkinds), and the first whose
    every input reads is called. The inputs marshal as in #emit_scalar_values; then one cell
    per array out-parameter and the count are allocated, `count` rows are read column by
    column, and each element MEOS allocated, each array, and the inputs are freed."""
    rep = cands[0][1]
    argnames = [_javaid(p["name"] or ("a%d" % i)) for i, p in enumerate(rep["ins"])]
    argboxes = []
    for p in rep["ins"]:
        k = arg_kind(p["canonical"])
        argboxes.append({"ptr": "Object", "ts": "Object",
                         "scalar": k[2] if k[0] == "scalar" else "String"}[k[0]])
    single = len(rep["cols"]) == 1 and rep["cols"][0]["name"] is None
    elem = "Object" if single else "org.apache.spark.sql.Row"
    iface = "UDF%d<%s, java.util.List<%s>>" % (len(argnames), ", ".join(argboxes), elem)
    L = ['        spark.udf().register("%s", (%s) (%s) -> {' % (name, iface, ", ".join(argnames))]
    L.append("        if (" + " || ".join("%s == null" % a for a in argnames) + ") return null;")
    order = sorted(cands, key=lambda fs: (0 if _expected_temptype(fs[0]) else 1, fs[0]["name"]))
    for f, shape in order:
        t = _expected_temptype(f) if len(cands) > 1 else None
        L.append("        {")
        callargs, ptrs = [], []
        for a, p in zip(argnames, shape["ins"]):
            k = arg_kind(p["canonical"])
            if k[0] == "ptr" and t is not None and k[2] == "K_TEMPORAL" and not ptrs:
                k = ("ptr", "UdfMarshal.tFromOf(%%s, %d)" % TEMPTYPE_CODE[t], k[2])
            if k[0] == "ptr":
                L.append("        jnr.ffi.Pointer p_%s = %s;" % (a, k[1] % a))
                callargs.append("p_%s" % a); ptrs.append("p_%s" % a)
            elif k[0] == "ts":
                callargs.append("UdfMarshal.tsOdt(%s)" % a)
            else:
                callargs.append(k[3] % a)
        for p in shape.get("hidden", []):
            callargs.append(hidden_arg(f, p, len(shape["ins"]), name))
        L.append("        if (%s) {" % (" && ".join("%s != null" % p for p in ptrs) or "true"))
        L.append("        jnr.ffi.Runtime _rt = jnr.ffi.Runtime.getSystemRuntime();")
        cells = {}
        for p in f["params"]:
            if p["name"] in shape["arrays"]:
                cells[p["name"]] = "_o_%s" % _javaid(p["name"])
                L.append("        jnr.ffi.Pointer %s = jnr.ffi.Memory.allocateDirect(_rt, 8, true);"
                         % cells[p["name"]])
                callargs.append(cells[p["name"]])
            elif p["name"] == shape["count"]:
                L.append("        jnr.ffi.Pointer _cnt = "
                         "jnr.ffi.Memory.allocateDirect(_rt, 4, true);")
                callargs.append("_cnt")
        L.append("        jnr.ffi.Pointer _ret = null;")
        L.append("        int _c = 0;")
        L.append("        try {")
        L.append("            _ret = GeneratedFunctions.%s(%s);" % (f["name"], ", ".join(callargs)))
        L.append("            if (_ret == null) return null;")
        L.append("            _c = _cnt.getInt(0L);")
        arrvar = {"return": "_ret"}
        for n, cell in cells.items():
            arrvar[n] = "%s.getPointer(0L)" % cell
        reads = [c["read"] if c["src"] == "ordinal" else c["read"] % arrvar[c["src"]]
                 for c in shape["cols"]]
        L.append("            java.util.List<%s> _rows = new java.util.ArrayList<>(_c);" % elem)
        L.append("            for (int _i = 0; _i < _c; _i++)")
        L.append("                _rows.add(%s);" % (reads[0] if single else
                 "org.apache.spark.sql.RowFactory.create(%s)" % ", ".join(reads)))
        L.append("            return _rows;")
        L.append("        } finally {")
        for c in shape["cols"]:
            if c["free"]:
                v = arrvar[c["src"]]
                L.append("            if (%s != null) for (int _i = 0; _i < _c; _i++) "
                         "MeosMemory.free(%s.getPointer((long) _i * 8L));" % (v, v))
        for cell in cells.values():
            L.append("            MeosMemory.free(%s.getPointer(0L));" % cell)
        L.append("            MeosMemory.free(_ret);")
        for p in ptrs:
            L.append("            MeosMemory.free(%s);" % p)
        L.append("        }")
        L.append("        }")
        for p in ptrs:
            L.append("        MeosMemory.free(%s);" % p)
        L.append("        }")
    L.append("        return null;")
    named = [dict(c, name=None if n in (None, "None") else n)
             for c, n in zip(rep["cols"], colnames)]
    L.append("        }, DataTypes.createArrayType(%s));" % _row_dt(dict(rep, cols=named)))
    return "\n".join(L)


GEN_NOTE = "// GENERATED by tools/codegen_spark_udfs.py from the MEOS-API catalog. DO NOT EDIT.\n"
IMPORTS = """\
package org.mobilitydb.spark.generated;

import functions.GeneratedFunctions;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.*;
import org.apache.spark.sql.types.DataTypes;
import org.mobilitydb.spark.MeosMemory;
"""

# Shared marshalling helpers live in their own class: the 2300+ UDFs are partitioned
# across many Part classes (a single class overruns the 64 KB method / constant-pool
# limits — exactly why JMEOS splits GeneratedFunctions into MeosLibraryPartA/PartB).
MARSHAL = GEN_NOTE + """\
package org.mobilitydb.spark.generated;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import functions.GeneratedFunctions;
import org.mobilitydb.spark.MeosMemory;

final class UdfMarshal {
    private UdfMarshal() {}
    private static final DateTimeFormatter PG_TZ = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXX");
    // JMEOS marshals TimestampTz as java.time.OffsetDateTime.
    static java.time.OffsetDateTime tsOdt(Object a) {
        if (a instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) a).toInstant().atOffset(ZoneOffset.UTC);
        if (a instanceof java.time.OffsetDateTime) return (java.time.OffsetDateTime) a;
        if (a instanceof java.time.Instant) return ((java.time.Instant) a).atOffset(ZoneOffset.UTC);
        // A String timestamp: MEOS owns TZ resolution (ecosystem-uniform, like SRID via
        // geo_from_text) — parse via timestamptz_in, NEVER a Java/Spark offset fixup.
        return GeneratedFunctions.timestamptz_in(a.toString().trim(), -1);
    }
    static String tsOut(java.time.OffsetDateTime t) {
        return t == null ? null : t.format(PG_TZ);
    }

    // A hex-WKB string is an even-length run of hex digits. The WKB readers check this
    // BEFORE handing a String to a *_from_hexwkb parser: MEOS's hex decoder crashes (not
    // returns null) on non-hex bytes, so trying a temporal parser on a WKT geometry literal
    // would segfault. WKT fails isHex (it has letters like 'L','I','N','(' that are not hex
    // digits), so the dispatch falls through to the geo_from_text branch instead.
    static boolean isHex(String s) {
        int n = s.length();
        if (n == 0 || (n & 1) != 0) return false;
        for (int i = 0; i < n; i++)
            if (Character.digit(s.charAt(i), 16) < 0) return false;
        return true;
    }

    // Geometry/geography arg parse. The canonical wire form is EWKT
    // ("SRID=4326;POLYGON(...)") so the SRID survives to MEOS — but geo_from_text() is
    // WKT-only and returns null on the "SRID=" prefix, dropping the SRID (which H3 /
    // geo_to_h3index_set REQUIRE via ensure_srid_is_latlong). Split the prefix and pass
    // its value as geo_from_text's srid argument; plain WKT (no prefix) parses at SRID 0.
    // CRITICAL for overload dispatch: reject a pure-hex string up front. geo_from_text()
    // also accepts hex-(E)WKB, so a temporal/set hex-WKB arg (a tgeompoint trip) would be
    // MIS-READ as a geometry (garbage npoints / free() crash) when a (geo, tgeo) candidate
    // is tried before the (tgeo, geo) one. A real WKT/EWKT geometry ALWAYS contains
    // non-hex chars (P O L Y G N '(' '=' ';' ...), so isHex(s) is false for it; only a
    // foreign WKB hex is pure-hex — exactly what must fall through to the next candidate.
    static Pointer geoFromText(Object o) {
        if (!(o instanceof String) || isHex((String) o)) return null;
        String s = (String) o;
        int srid = 0; String wkt = s;
        if (s.length() > 5 && s.regionMatches(true, 0, "SRID=", 0, 5)) {
            int semi = s.indexOf(';');
            if (semi > 5) {
                try { srid = Integer.parseInt(s.substring(5, semi).trim()); wkt = s.substring(semi + 1); }
                catch (NumberFormatException e) { srid = 0; wkt = s; }
            }
        }
        return GeneratedFunctions.geo_from_text(wkt, srid);
    }

    // A text reader (stbox_in, tbox_in, cbuffer_in ...) takes a String and nothing else: a
    // WKB value handed to it is not of its kind, the null every reader answers with.
    static Pointer textIn(Object o, java.util.function.Function<String, Pointer> in) {
        return o instanceof String ? in.apply((String) o) : null;
    }

    // ── NxN array (tgeoarr) marshalling ──────────────────────────────────────────
    // The MEOS *_tgeoarr_tgeoarr kernels take (Temporal **arr, int n) array pairs and
    // run the whole NxN inside C. Spark passes an array column as a scala Seq /
    // WrappedArray (or a java List) whose elements are WKB bytes or hex-WKB text, so
    // normalize to Object[] first.
    static Object[] asArray(Object o) {
        if (o == null) return null;
        if (o instanceof scala.collection.Seq) {
            scala.collection.Seq<?> q = (scala.collection.Seq<?>) o;
            Object[] r = new Object[q.length()];
            for (int i = 0; i < r.length; i++) r[i] = q.apply(i);
            return r;
        }
        if (o instanceof java.util.List) return ((java.util.List<?>) o).toArray();
        if (o instanceof Object[]) return (Object[]) o;
        return null;
    }
    // Parse a temporal array into a native Temporal** buffer; the parsed element pointers
    // are stored in `elems` (parallel) so the caller frees them after the call.
    static Pointer tArrNative(Object[] vals, Pointer[] elems, Runtime rt) {
        Pointer buf = Memory.allocateDirect(rt, Math.max(1, vals.length) * 8);
        for (int i = 0; i < vals.length; i++) {
            Pointer p = tFrom(vals[i]);
            elems[i] = p;
            buf.putPointer((long) i * 8L, p);
        }
        return buf;
    }
    static void freeArr(Pointer[] elems) { for (Pointer p : elems) MeosMemory.free(p); }
    // A *_tgeoarr_tgeoarr kernel returns a flat int* of 2*count [i,j] index pairs (caller
    // frees). The C kernel uses 0-based C indices (the PG SETOF wrapper is what makes them
    // 1-based) — and the Spark UDF calls the kernel directly via JMEOS, so the indices are
    // already 0-based array offsets; emit them as-is (matching MeosSetSetJoin).
    static java.util.List<Row> readPairs(Pointer res, int cnt) {
        java.util.ArrayList<Row> out = new java.util.ArrayList<>();
        if (res == null || cnt <= 0) return out;
        for (int k = 0; k < cnt; k++)
            out.add(RowFactory.create(res.getInt((long) (2 * k) * 4L),
                                      res.getInt((long) (2 * k + 1) * 4L)));
        MeosMemory.free(res);
        return out;
    }
    // tDwithin also returns a parallel SpanSet** of the per-pair intersection periods
    // (via a SpanSet ***out-param); render each as WKB. Frees res, the periods array,
    // and each period span set.
    static java.util.List<Row> readPairsPeriods(Pointer res, int cnt, Pointer ssArr) {
        java.util.ArrayList<Row> out = new java.util.ArrayList<>();
        if (res == null || cnt <= 0) { MeosMemory.free(res); return out; }
        for (int k = 0; k < cnt; k++) {
            Pointer ss = ssArr == null ? null : ssArr.getPointer((long) k * 8L);
            byte[] periods = ss == null ? null : GeneratedFunctions.spanset_as_wkb(ss, (byte) 4);
            out.add(RowFactory.create(res.getInt((long) (2 * k) * 4L),
                                      res.getInt((long) (2 * k + 1) * 4L), periods));
            MeosMemory.free(ss);
        }
        MeosMemory.free(ssArr);
        MeosMemory.free(res);
        return out;
    }

    // Scalar array-return accessors (*_values, pose_orientation, quadbin_k_ring, ...) return
    // a contiguous native array of `cnt` fixed-width elements (cnt from the int* out-param);
    // read each at its known stride and free the array (malloc-backed, like readPairs).
    static java.util.List<Integer> readIntArr(Pointer p, int cnt) {
        java.util.ArrayList<Integer> o = new java.util.ArrayList<>();
        if (p == null) return o;
        for (int i = 0; i < cnt; i++) o.add(p.getInt((long) i * 4L));
        MeosMemory.free(p);
        return o;
    }
    static java.util.List<Long> readLongArr(Pointer p, int cnt) {
        java.util.ArrayList<Long> o = new java.util.ArrayList<>();
        if (p == null) return o;
        for (int i = 0; i < cnt; i++) o.add(p.getLongLong((long) i * 8L));
        MeosMemory.free(p);
        return o;
    }
    static java.util.List<Double> readDoubleArr(Pointer p, int cnt) {
        java.util.ArrayList<Double> o = new java.util.ArrayList<>();
        if (p == null) return o;
        for (int i = 0; i < cnt; i++) o.add(p.getDouble((long) i * 8L));
        MeosMemory.free(p);
        return o;
    }

    // ── Type-SAFE WKB reading ────────────────────────────────────────────────────
    // A *_from_wkb / *_from_hexwkb parser reads the MEOS WKB structure for ONE type family
    // and SEGV-crashes on a well-formed buffer of a different family (a tstzspan fed to
    // temporal_from_wkb). The WKB opens with its byte-order byte and then the 16-bit
    // MeosType of the value, so read the type first and only call the C parser when the
    // family matches. These sets are generated from the catalog's MeosType enum
    // (data-driven, no hardcoding).
__WKB_KINDS__
    // A value of these kinds arrives as its WKB bytes, the form every generated function
    // returns, or as hex-WKB text. Hex text a BinaryType input received through Spark's
    // string-to-binary cast arrives as the ASCII bytes of that text, which open with the
    // character '0' where WKB opens with its byte-order byte 0 or 1: read those as the text.
    static Object wire(Object o) {
        if (o instanceof byte[]) {
            byte[] b = (byte[]) o;
            if (b.length > 0 && b[0] == '0')
                return new String(b, java.nio.charset.StandardCharsets.US_ASCII);
        }
        return o;
    }
    // The MeosType of a WKB value, read in the byte order its first byte states (1 little
    // endian, 0 big endian); -1 when the value is neither WKB bytes nor hex-WKB text.
    static int wkbType(Object o) {
        int b0, b1, b2;
        if (o instanceof byte[]) {
            byte[] b = (byte[]) o;
            if (b.length < 3) return -1;
            b0 = b[0] & 0xFF; b1 = b[1] & 0xFF; b2 = b[2] & 0xFF;
        } else if (o instanceof String) {
            String s = (String) o;
            if (s.length() < 6 || !isHex(s)) return -1;
            b0 = hexByte(s, 0); b1 = hexByte(s, 1); b2 = hexByte(s, 2);
        } else {
            return -1;
        }
        if (b0 == 1) return b1 | (b2 << 8);
        if (b0 == 0) return (b1 << 8) | b2;
        return -1;
    }
    private static int hexByte(String s, int i) {
        return Character.digit(s.charAt(2 * i), 16) * 16 + Character.digit(s.charAt(2 * i + 1), 16);
    }
    // The reader of a type with a byte codec: `bytes` reads its WKB bytes, `hex` its hex-WKB
    // text and `text` its text form, a null function standing for a form the type has no
    // reader of. `family` is the set of MeosTypes its WKB states, or null for a WKB stating
    // none, whose reader then checks nothing and so tells no overload apart.
    static Pointer read(Object o, java.util.Set<Integer> family,
            java.util.function.Function<byte[], Pointer> bytes,
            java.util.function.Function<String, Pointer> hex,
            java.util.function.Function<String, Pointer> text) {
        o = wire(o);
        if (o instanceof byte[])
            return family == null || family.contains(wkbType(o)) ? bytes.apply((byte[]) o) : null;
        if (!(o instanceof String)) return null;
        String s = (String) o;
        if (isHex(s))
            return hex != null && (family == null || family.contains(wkbType(s)))
                ? hex.apply(s) : null;
        return text != null ? text.apply(s) : null;
    }
    static Pointer tFrom(Object o) {
        return read(o, TEMPORAL_WKB, GeneratedFunctions::temporal_from_wkb,
            GeneratedFunctions::temporal_from_hexwkb, null);
    }
    // The same parse restricted to ONE temporal type, for an overload named after a
    // concrete type: `code` is that type's MeosType value, which is the WKB type, so
    // a value of a sibling type is refused here and answered by its own overload instead
    // of reaching a C function that rejects it as the wrong type at run time.
    static Pointer tFromOf(Object o, int code) {
        return read(o, java.util.Set.of(code), GeneratedFunctions::temporal_from_wkb,
            GeneratedFunctions::temporal_from_hexwkb, null);
    }
    static Pointer spanFrom(Object o) {
        return read(o, SPAN_WKB, GeneratedFunctions::span_from_wkb,
            GeneratedFunctions::span_from_hexwkb, null);
    }
    static Pointer spansetFrom(Object o) {
        return read(o, SPANSET_WKB, GeneratedFunctions::spanset_from_wkb,
            GeneratedFunctions::spanset_from_hexwkb, null);
    }
    static Pointer setFrom(Object o) {
        return read(o, SET_WKB, GeneratedFunctions::set_from_wkb,
            GeneratedFunctions::set_from_hexwkb, null);
    }

    // Time-restrict polymorphism (atTime / minusTime): MobilityDB resolves the time arg
    // by type (timestamptz / tstzspan / tstzset / tstzspanset), but Spark cannot overload
    // a UDF name. A span, set or span set arrives as WKB bytes or hex-WKB text, the forms
    // the generated functions return one in (tstzspan_make, span ...), read by the
    // type-checked readers, which refuse any other value; else a literal, classified by its
    // first char (a period "[..]", a set "{..}", a span set "{[..],..}", else a bare
    // timestamp). It routes to the matching MEOS overload, returning the restricted temporal
    // as WKB.
    static byte[] restrictTime(Pointer t, Object arg,
            BiFunction<Pointer, java.time.OffsetDateTime, Pointer> byTs,
            BiFunction<Pointer, Pointer, Pointer> bySpan,
            BiFunction<Pointer, Pointer, Pointer> bySet,
            BiFunction<Pointer, Pointer, Pointer> bySpanset) {
        Object a = arg instanceof String ? ((String) arg).trim() : arg;
        Pointer r;
        Pointer h;
        if ((h = spanFrom(a)) != null) {
            try { r = bySpan.apply(t, h); } finally { MeosMemory.free(h); }
        } else if ((h = setFrom(a)) != null) {
            try { r = bySet.apply(t, h); } finally { MeosMemory.free(h); }
        } else if ((h = spansetFrom(a)) != null) {
            try { r = bySpanset.apply(t, h); } finally { MeosMemory.free(h); }
        } else if (!(a instanceof String)) {
            return null;
        } else {
            String s = (String) a;
            if (s.startsWith("{")) {
                boolean spanset = s.indexOf('[') >= 0 || s.indexOf('(') >= 0;
                Pointer p = spanset ? GeneratedFunctions.tstzspanset_in(s) : GeneratedFunctions.tstzset_in(s);
                if (p == null) return null;
                try { r = (spanset ? bySpanset : bySet).apply(t, p); } finally { MeosMemory.free(p); }
            } else if (s.startsWith("[") || s.startsWith("(")) {
                Pointer p = GeneratedFunctions.tstzspan_in(s);
                if (p == null) return null;
                try { r = bySpan.apply(t, p); } finally { MeosMemory.free(p); }
            } else {
                r = byTs.apply(t, tsOdt(s));
            }
        }
        if (r == null) return null;
        try { return GeneratedFunctions.temporal_as_wkb(r, (byte) 4); } finally { MeosMemory.free(r); }
    }
}
"""

# A temporal aggregate of MEOS as a Spark Aggregator over WKB temporals, a generated
# helper written beside UdfMarshal like the MARSHAL template above. The roles the catalog
# states for the aggregate are handed in per temporal type, and the buffer holds the native
# state of the partition. When Spark moves a buffer between executors it writes the state as
# the bytes the serialize function writes and reads it back through the deserialize function,
# and the state a combine function does not answer is released through the final function,
# which consumes it.
AGGREGATE = GEN_NOTE + """\
package org.mobilitydb.spark.generated;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import jnr.ffi.Pointer;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;
import functions.GeneratedFunctions;
import org.mobilitydb.spark.MeosMemory;

public final class TemporalAggregate extends Aggregator<byte[], TemporalAggregate.Buffer, byte[]> {
    /** A transition or a combine function: (state, value) or (state, state). */
    public interface Step extends Serializable {
        Pointer apply(Pointer a, Pointer b);
    }

    /** A final function, which consumes the state. */
    public interface Final extends Serializable {
        Pointer apply(Pointer state);
    }

    /** A serialize function: the bytes of a state, which is kept. */
    public interface Write extends Serializable {
        byte[] apply(Pointer state);
    }

    /** A deserialize function: the state the bytes hold. */
    public interface Read extends Serializable {
        Pointer apply(byte[] form);
    }

    /** The state of a partition, the index of the temporal type it aggregates, and the
     * functions writing the state as bytes and reading it back when it leaves the executor. */
    public static final class Buffer implements Serializable {
        transient Pointer state;
        int step = -1;
        Write write;
        Read read;

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeObject(state == null ? null : write.apply(state));
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            byte[] form = (byte[]) in.readObject();
            state = form == null ? null : read.apply(form);
        }
    }

    private final int[] codes;
    private final Step[] trans;
    private final Step[] combs;
    private final Final[] fins;
    private final Write[] writes;
    private final Read[] reads;

    /** codes[i] is the WKB type byte of the values the roles of index i take. */
    public TemporalAggregate(int[] codes, Step[] trans, Step[] combs, Final[] fins, Write[] writes,
            Read[] reads) {
        this.codes = codes;
        this.trans = trans;
        this.combs = combs;
        this.fins = fins;
        this.writes = writes;
        this.reads = reads;
    }

    private int pick(Object value) {
        int type = UdfMarshal.wkbType(value);
        for (int i = 0; i < codes.length; i++)
            if (codes[i] == type)
                return i;
        throw new IllegalArgumentException("the aggregate takes no temporal value of type " + type);
    }

    /** Release a state by releasing what its final function answers. */
    private void release(int i, Pointer state) {
        if (state != null)
            MeosMemory.free(fins[i].apply(state));
    }

    @Override public Buffer zero() { return new Buffer(); }

    @Override public Buffer reduce(Buffer b, byte[] wkb) {
        if (wkb == null)
            return b;
        Object value = UdfMarshal.wire(wkb);
        int i = pick(value);
        if (b.step >= 0 && b.step != i)
            throw new IllegalArgumentException("the aggregate takes values of one temporal type");
        Pointer t = UdfMarshal.tFrom(value);
        if (t == null)
            return b;
        try {
            b.state = trans[i].apply(b.state, t);
            b.step = i;
            b.write = writes[i];
            b.read = reads[i];
        } finally {
            MeosMemory.free(t);
        }
        return b;
    }

    /** The two partial states joined. A combine answers in the place of its first state, except
     * that a skip-list combine answers its second state when the first is empty: the state it
     * does not answer is released. */
    @Override public Buffer merge(Buffer b1, Buffer b2) {
        if (b2.state == null)
            return b1;
        if (b1.state == null)
            return b2;
        if (b1.step != b2.step)
            throw new IllegalArgumentException("the aggregate takes values of one temporal type");
        Pointer r = combs[b1.step].apply(b1.state, b2.state);
        release(b1.step, r != null && r.address() == b2.state.address() ? b1.state : b2.state);
        b1.state = r;
        b2.state = null;
        return b1;
    }

    @Override public byte[] finish(Buffer b) {
        if (b.state == null)
            return null;
        Pointer t = fins[b.step].apply(b.state);
        b.state = null;
        if (t == null)
            return null;
        try {
            return GeneratedFunctions.temporal_as_wkb(t, (byte) 4);
        } finally {
            MeosMemory.free(t);
        }
    }

    @Override public Encoder<Buffer> bufferEncoder() { return Encoders.javaSerialization(Buffer.class); }
    @Override public Encoder<byte[]> outputEncoder() { return Encoders.BINARY(); }
}
"""


def main():
    ap = argparse.ArgumentParser()
    here = os.path.dirname(os.path.abspath(__file__))
    ap.add_argument("--catalog", default=os.path.join(here, "..", "..", "MEOS-API", "output", "meos-idl.json"))
    # Default into the maven build dir (NOT a source root): build-time generation
    # owns target/; a bare run must never pollute src/ (which would double-compile).
    ap.add_argument("--out", default=os.path.join(
        here, "..", "target", "generated-sources", "spark",
        "org", "mobilitydb", "spark", "generated"))
    ap.add_argument("--jar", default=os.path.join(here, "..", "libs", "JMEOS-1.4.jar"),
                    help="JMEOS jar; only functions JMEOS actually exposes are emitted "
                         "(catalog is a superset incl. internal _addmat/above8D/GEOS macros)")
    ap.add_argument("--report", action="store_true")
    ap.add_argument("--gaps", help="the ledger of the public functions the surface does not reach; "
                    "a function missing from it fails the run")
    ap.add_argument("--rebaseline", action="store_true",
                    help="rewrite the --gaps ledger to the functions the surface does not reach")
    args = ap.parse_args()

    cat = json.load(open(args.catalog))
    # The temporal MeosType names and their WKB type bytes, from the catalog's own enum:
    # what lets the dispatcher tell two overloads of one SQL name apart by the type of the
    # value handed to it. Filled before any emit pass reads it.
    _mt = next((e for e in cat.get("enums", []) if e["name"] in ("MeosType", "meosType")), None)
    for _v in (_mt.get("values") or _mt.get("members") or []) if _mt else []:
        _nm = _v["name"] if isinstance(_v, dict) else _v
        _val = _v.get("value") if isinstance(_v, dict) else None
        if _val is not None and _nm and _nm.startswith("T_T") and "BOX" not in _nm:
            TEMPTYPE_CODE[_nm[2:].lower()] = _val
    # The value of each macro and enum member a wrapper binds or a SQL default names.
    CONST.update({x["name"]: x["value"] for x in cat.get("macros", [])
                  if isinstance(x.get("value"), (int, float))})
    CONST.update({v["name"]: v["value"] for e in cat.get("enums", [])
                  for v in e.get("values") or []
                  if isinstance(v, dict) and isinstance(v.get("value"), int)})
    fns = cat["functions"]

    # The catalog is the raw extern parse of the MobilityDB headers; JMEOS curates
    # its public surface (e.g. drops matrix internals _addmat/_choldc1, RTree node
    # predicates above8D/adjacent2D, GEOS conv macros MEOS_GEOS2POSTGIS). Emit ONLY
    # what JMEOS exposes, so a generated UDF can never call an absent jar symbol.
    jar_syms = None
    if args.jar and os.path.exists(args.jar):
        import subprocess
        jv = subprocess.run(["javap", "-p", "-cp", args.jar, "functions.GeneratedFunctions"],
                            capture_output=True, text=True).stdout
        jar_syms = set(re.findall(r"\b([a-z][A-Za-z0-9_]+)\(", jv))
        # JSIG: name -> (javaReturnType, nArgs) — the jar's actual signatures.
        for m in re.finditer(r"public static (\S+) ([a-z][A-Za-z0-9_]+)\(([^)]*)\)", jv):
            jret, jname, jargs = m.group(1), m.group(2), m.group(3).strip()
            JSIG[jname] = (jret if "." in jret else jret.lower(),
                           0 if not jargs else len(jargs.split(",")))
    # The codec each value travels in, from the catalog, before any emit pass reads it.
    derive_codecs(cat, lambda n: bool(n) and (jar_syms is None or n in jar_syms))
    # The public parser of each enum the catalog states, before any emit pass reads it.
    enums = {e["name"] for e in cat.get("enums", [])}
    for f in fns:
        ps = f["params"]
        rt = norm(f["returnType"]["canonical"])
        if (rt in enums and len(ps) == 1 and norm(ps[0]["canonical"]) == "char *"
                and f.get("api") == "public"
                and (jar_syms is None or f["name"] in jar_syms)):
            ENUM_PARSER.setdefault(rt, f["name"])

    # GOAL: reach the WHOLE JMEOS surface. Every MEOS C function (unique by its C
    # name) becomes a 1:1 UDF named by that C symbol — that is how the ~2254
    # functions with no @sqlfn are reached. The portable dialect (@sqlfn / operator
    # bare names from the contract) is layered on top in a later dispatch pass.
    grouped, names, cov = {}, set(), 0
    skips = collections.Counter()
    internal = total = not_in_jar = 0

    for f in fns:
        name = f["name"]
        if jar_syms is not None and name not in jar_syms:
            not_in_jar += 1          # internal helper JMEOS doesn't expose — skip
            continue
        total += 1
        if name in names:
            continue
        why = supported(f)
        if why:
            if why == "internal":
                internal += 1
            else:
                skips[why] += 1
            continue
        grouped.setdefault(class_for(f.get("group")), []).append(emit_single(name, f))
        names.add(name)
        cov += 1

    # ── DISPATCH PASS: portable bare names from the contract families ──
    # Spark cannot overload by name, but MEOS exposes SUPERCLASS entrypoints that
    # dispatch every concrete temporal type internally from the type-erased WKB.
    # So a portable comparison name (eEqual, tLessThan, aGreaterEqual) wraps the
    # superclass *_temporal_temporal function whose @sqlfn it is (ever_eq_, tlt_,
    # always_ge_temporal_temporal) for ALL temporal subtypes. But some eEqual/eNotEqual
    # overloads have a NON-temporal first arg — eEqual(geo, tgeo), and the th3 cell-set
    # prefilter eEqual(h3indexset, th3index) whose first arg is a Set* — which the Temporal*
    # superclass cannot reach. Gather those (same 2-pointer signature, distinct
    # parse-tuple, dispatch-safe) and emit ONE parse-dispatching UDF so they resolve too.
    by_name = {f["name"]: f for f in fns}
    fams = (cat.get("portableAliases") or {}).get("families", {})
    ndisp = 0
    for fam in ("everComparison", "alwaysComparison", "temporalComparison"):
        for e in fams.get(fam, []):
            bare = e["bareName"]
            backing = next((f for f in fns if f.get("sqlfn") == bare
                            and f["name"].endswith("_temporal_temporal")), None)
            if not (backing and supported(backing) is None):
                continue
            # Non-temporal overloads under the SAME @sqlfn bare name (geo/set first arg),
            # sharing the superclass's 2-pointer signature, each parse-distinct.
            rep_sig = _sig(backing)
            cands, seen = [backing], {_parsetuple(backing)}
            extras = [f for f in fns
                      if f.get("sqlfn") == bare and f["name"] != backing["name"]
                      and supported(f) is None and _safe_dispatch(f)
                      and _sig(f) == rep_sig]
            for f in sorted(extras, key=_famrank):
                pt = _parsetuple(f)
                if pt not in seen:
                    seen.add(pt)
                    cands.append(f)
            emit = emit_single(bare, backing) if len(cands) == 1 else emit_dispatch(bare, cands)
            grouped.setdefault("GeneratedUdfs_portable_comparison", []).append(emit)
            ndisp += 1
            cov += 1
    print("  dispatch bare names (comparison)  : %d" % ndisp, file=sys.stderr)

    # ── bare-name-IS-prefix families: same / topology ──
    # Each contract bareName IS the MEOS C operator prefix, and the superclass
    # entrypoint *_temporal_temporal dispatches every concrete subtype internally from
    # the type-erased WKB — so one emit covers all six type families. Same emit
    # machinery as the comparison dispatch; only the backing-name pattern differs.
    # The position operators have no bare name: they take their names by class
    # (stboxLeft, tboxBefore, spanOverleft), which the @sqlfn pass below registers.
    PREFIX = [("same", "%s_temporal_temporal")]
    nbare = 0
    for fam, pat in PREFIX:
        for e in fams.get(fam, []):
            bare = e["bareName"]
            backing = by_name.get(pat % bare)
            if backing and supported(backing) is None:
                grouped.setdefault("GeneratedUdfs_portable_operator", []).append(
                    emit_single(bare, backing))
                nbare += 1
                cov += 1
    # topology (&&, @>, <@, -|-) is polymorphic over SPANS and TEMPORALS: overlaps(
    # tstzspan, tstzspan) = overlaps_span_span, overlaps(tgeompoint, ...) =
    # overlaps_temporal_temporal. Dispatch across both — the type-safe WKB parsers route a
    # span (e.g. from timeSpan()) to the span backing instead of crashing the temporal one.
    for e in fams.get("topology", []):
        bare = e["bareName"]
        cands = [by_name.get(bare + "_span_span"), by_name.get(bare + "_temporal_temporal")]
        cands = [f for f in cands if f and supported(f) is None]
        if not cands:
            continue
        code = emit_single(bare, cands[0]) if len(cands) == 1 else emit_dispatch(bare, cands)
        grouped.setdefault("GeneratedUdfs_portable_operator", []).append(code)
        nbare += 1
        cov += 1
    print("  dispatch bare names (operator)    : %d" % nbare, file=sys.stderr)

    # (distance — tdistance / nearestApproachDistance — is NOT registered here: those
    # carry @sqlfn tags (tDistance / nearestApproachDistance) with several typed C
    # overloads, so the @sqlfn pass below emits them with full arg-kind dispatch — a
    # single tgeo_geo backing here would wrongly null a trip-vs-trip call.)

    # ── NxN array (tgeoarr) pass: array-in + SETOF/array-of-struct UDFs ──
    # The *_tgeoarr_tgeoarr kernels take Temporal** array args, so the 1:1 and @sqlfn
    # passes exclude them (** -> internal). Emit each under its @sqlfn name via the array
    # template: minDistance (array,array -> double) and the *Pairs (array,array[,dist] ->
    # array<struct<i,j[,periods]>>, consumed by LATERAL explode; MEOS 1-based -> 0-based).
    narr = 0
    for f in fns:
        s = f.get("sqlfn")
        if not s or s in names:
            continue
        if jar_syms is not None and f["name"] not in jar_syms:
            continue
        shape = tgeoarr_shape(f)
        if shape is None:
            continue
        grouped.setdefault(class_for(f.get("group")), []).append(emit_tgeoarr(s, f, shape))
        names.add(s)
        cov += 1
        narr += 1
    print("  NxN array (tgeoarr) UDFs          : %d" % narr, file=sys.stderr)

    # ── scalar-value array pass: (inputs..., int *count) -> <scalar>* accessors ──
    # The value-array accessors (tint_values / tfloat_values / *set_values / th3index_values
    # / tquadbin_values / pose_orientation / quadbin_k_ring) carry an `int *count` out-param,
    # so the 1:1 and @sqlfn passes exclude them (arg:int *). Emit each under its C name: the
    # return is a contiguous fixed-width scalar array -> a Spark array<int|long|double>.
    nval = 0
    for f in fns:
        nm = f["name"]
        if nm in names:
            continue
        if jar_syms is not None and nm not in jar_syms:
            continue
        shape = scalar_values_shape(f)
        if shape is None:
            continue
        grouped.setdefault(class_for(f.get("group")), []).append(emit_scalar_values(nm, f, shape))
        names.add(nm)
        cov += 1
        nval += 1
    print("  scalar-value array UDFs           : %d" % nval, file=sys.stderr)

    # ── set-returning pass: each SQL signature returning a set of rows, as an array ──
    # A function is reached under its C name, its columns named as most of its signatures name
    # them. A SQL name is registered when its overloads share one input and row signature
    # (_setret_key), since a Spark UDF has one signature: the UDF tries each overload, a typed
    # one first. A SQL name whose overloads need different signatures (valueSplit's int,
    # double and bigint bins) stays unregistered, each overload reachable under its C name.
    STRUCTS.update({s["name"]: s for s in cat.get("structs") or []})
    setret, setret_sql, nset_c, nset_sql = {}, {}, 0, 0
    for f in fns:
        nm = f["name"]
        if jar_syms is not None and nm not in jar_syms:
            continue
        if (f.get("group") or "").startswith("meos_internal"):
            continue
        if nm in JSIG and JSIG[nm][1] != len(f["params"]):
            continue
        for sig in f.get("sqlSignatures") or []:
            if not sig.get("retSet"):
                continue
            shape = setret_shape(f, sig)
            if shape is not None:
                setret.setdefault(sig.get("sqlName") or f.get("sqlfn"), []).append((f, shape))
                setret_sql.setdefault(sig.get("sqlName") or f.get("sqlfn"), []).append(
                    (f, setret_shape(f, sig, f.get("sqlArity"))))
    by_fn = {}
    for sname, cands in setret.items():
        for f, shape in cands:
            by_fn.setdefault(f["name"], []).append((f, shape))
    for nm in sorted(by_fn):
        if nm in names:
            continue
        cols = collections.Counter(tuple(str(c["name"]) for c in s["cols"]) for _, s in by_fn[nm])
        f, shape = by_fn[nm][0]
        grouped.setdefault(class_for(f.get("group")), []).append(
            emit_setret(nm, [(f, shape)], min(cols, key=lambda k: (-cols[k], k))))
        names.add(nm)
        cov += 1
        nset_c += 1
    set_left = []
    for sname in sorted(n for n in setret_sql if n):
        if sname in names:
            continue
        if len({_setret_key(s) for _, s in setret_sql[sname]}) != 1:
            set_left.append(sname)
            continue
        one = {}
        for f, shape in setret_sql[sname]:
            one.setdefault(f["name"], (f, shape))
        cols = collections.Counter(tuple(str(c["name"]) for c in s["cols"])
                                   for _, s in setret_sql[sname])
        grouped.setdefault("GeneratedUdfs_sqlfn", []).append(
            emit_setret(sname, list(one.values()), min(cols, key=lambda k: (-cols[k], k))))
        names.add(sname)
        nset_sql += 1
    print("  set-returning UDFs                : %d C names, %d SQL names "
          "(left to C names, several signatures: %s)"
          % (nset_c, nset_sql, ", ".join(set_left) or "none"), file=sys.stderr)

    # ── @sqlfn CANONICAL-NAME pass: emit the MobilityDB SQL surface ──
    # Every catalog function carries the canonical MobilityDB SQL spelling in its
    # @sqlfn tag (numInstants, eIntersects, atTime, asHexWKB ...). That is the name a
    # user — and the portable BerlinMOD suite — actually calls, so emit each UDF under
    # its @sqlfn name with the C symbol as backing. One @sqlfn often maps SEVERAL C
    # overloads differing only by argument KIND (eIntersects <- eintersects_tgeo_tgeo /
    # _tgeo_geo / _geo_tgeo); since Spark cannot overload a UDF name, those that share a
    # marshalled _sig() are emitted as ONE arg-kind-dispatching UDF (emit_dispatch).
    # Registered AFTER the contract bare names (class name sorts later) so an @sqlfn
    # with full overload dispatch supersedes a single-backing portable registration.
    # skip @sqlfn names already owned by the contract bare-name passes (operator
    # superclass registrations). EXCLUDE the distance family: its names (tDistance /
    # nearestApproachDistance) are better served by the @sqlfn arg-kind dispatch.
    portable_names = {e["bareName"] for fn, fam in fams.items() if fn != "distance" for e in fam}
    sqlgroups = {}
    for f in fns:
        # A backing-only @sqlfn (the shared bbox-topological tag same_bbox/contains_bbox/…,
        # classified in the catalog by MEOS-API) is NOT a deployed SQL name — MobilityDB
        # exposes only the operator's bare portable alias (registered by the topology pass).
        # Never register the `_bbox` backing tag as a UDF. (catalog SoT: sqlfnBackingOnly.)
        if not f.get("sqlfn") or f.get("sqlfnBackingOnly") or supported(f) is not None:
            continue
        # A C function backing several SQL names (temporal_from_hexwkb behind tintFromHexWKB,
        # tbigintFromHexWKB …, temporal_as_tinstant behind tintInst, tfloatInst …) states each
        # name on its signatures, sqlfn being the representative: the function joins the
        # group of every name its signatures carry, as _omitted reads them.
        for s in {sig.get("sqlName") or f["sqlfn"] for sig in f.get("sqlSignatures") or []} \
                or {f["sqlfn"]}:
            if s not in names and s not in portable_names:
                sqlgroups.setdefault(s, []).append(f)
    nsql = nsqldisp = ndropped = nskip = 0
    sqlfn_emitted = set()
    for sname in sorted(sqlgroups):
        # time-restrict polymorphism (atTime / minusTime): the time arg type
        # (timestamptz / tstzspan / tstzset / spanset) can't be ONE Spark UDF signature,
        # so emit a String-classifying dispatch instead of the normal arg-kind one.
        gnames = {f["name"] for f in sqlgroups[sname]}
        trop = next((m for m in ("at", "minus")
                     if ("temporal_%s_timestamptz" % m) in gnames
                     and ("temporal_%s_tstzspan" % m) in gnames), None)
        if trop:
            grouped.setdefault("GeneratedUdfs_sqlfn", []).append(emit_timearg(sname, trop))
            sqlfn_emitted.add(sname)
            nsql += 1
            cov += 1
            continue
        # subgroup the overloads by marshalled signature; emit the largest consistent
        # group (a name whose overloads disagree on arity/scalar-shape can't be one
        # Spark UDF — take the dominant shape, the rest stay reachable via their C name).
        bysig = {}
        for f in sqlgroups[sname]:
            sig = _sqlsig(f)
            if sig is not None:
                bysig.setdefault(sig, []).append(f)
        if not bysig:
            continue
        # The name's own functions, those whose sqlfn it is, choose the shape: the one holding
        # most of them, the first of them on a tie. A function stating the name on one of its
        # signatures joins a shape and never chooses it; the largest shape answers a name
        # that only signatures state. A key on the overloads, as _famrank is below.
        pos = {id(f): i for i, f in enumerate(sqlgroups[sname])}
        own = lambda g: [pos[id(f)] for f in g if f.get("sqlfn") == sname]
        group = max(bysig.values(),
                    key=lambda g: (len(own(g)), -min(own(g))) if own(g) else (0, len(g)))
        # The shapes the chosen one meets in one UDF answer under the name too (#_merges).
        rsig = _sqlsig(group[0])
        group = group + [f for sig, g in bysig.items() if g is not group and _merges(sig, rsig)
                         for f in g]
        # A multi-overload @sqlfn needs a runtime parse dispatcher, which is only sound
        # when the overloads discriminate via WKB / WKT — drop an overload whose text-*_in
        # argument (stbox/tbox/cbuffer/npoint/pose) sits where the overloads differ, so e.g.
        # nearestApproachDistance keeps just its tgeo_tgeo / tgeo_geo overloads while atStbox
        # keeps every temporal it restricts. A name left with no safe overload is skipped
        # (still reachable under its C names), never emitted as a fragile guess.
        if len(group) > 1:
            group = _dispatchable(group)
        if not group:
            nskip += 1
            continue
        # Keep only parse-DISTINGUISHABLE overloads: one per _parsetuple, preferring the
        # tgeo/geo family. Overloads differing only by temporal subtype can't be routed
        # by parsing, so they're left to their C name (not silently mis-dispatched). Among
        # overloads of one parse shape, the function whose own sqlfn is the name comes
        # first: spatialset_as_text answers asText of a set before bigintset_out, which
        # states that name on one of its signatures.
        best = {}
        rank = lambda f: (f.get("sqlfn") != sname, _famrank(f))
        for f in group:
            t = _parsetuple(f)
            if t not in best or rank(f) < rank(best[t]):
                best[t] = f
        disp = sorted(best.values(), key=lambda f: f["name"])
        ndropped += len(group) - len(disp)
        # ever/always boolean predicates (eIntersects, aDisjoint, eDwithin ...) follow
        # the MobilityDB @sqlfn convention <e|a><Verb> and return int in C (1/0, -1 on
        # error) but boolean in SQL. Tag them with a predicate sqlop so ret_emit yields
        # BooleanType (== 1). Guarded on an int C-return, so atTime/asHexWKB (also a*) —
        # which return a temporal / string — are untouched.
        if re.match(r"[ea][A-Z]", sname):
            disp = [dict(f, sqlop="?=") if norm(f["returnType"]["canonical"]) in INT32 else f
                    for f in disp]
        # expose the SQL-required arity (sqlArity) — default the optional trailing flags
        # so e.g. asHexWKB(temporal) / trajectory(temporal) are 1-arg, matching SQL.
        va = disp[0].get("sqlArity")
        code = (emit_single(sname, disp[0], vis_arity=va) if len(disp) == 1
                else emit_dispatch(sname, disp, vis_arity=va))
        if len(disp) > 1:
            nsqldisp += 1
        grouped.setdefault("GeneratedUdfs_sqlfn", []).append(code)
        sqlfn_emitted.add(sname)
        nsql += 1
        cov += 1
    print("  @sqlfn canonical names      : %d  (%d arg-kind-dispatched, %d subtype-siblings + %d unsafe-overload names to C-name)" %
          (nsql, nsqldisp, ndropped, nskip), file=sys.stderr)

    # ── temporal aggregate pass: the catalog's aggregates over temporal values as Spark aggregators ──
    # An aggregate of the catalog's `aggregates` section states the public MEOS function of each
    # role it defines. It is emitted when it takes one temporal value and answers one, states a
    # combine, and keeps an internal state its serialize and deserialize functions write and
    # read: a partition keeps the state between two values, and a partial aggregate travels
    # between executors as the bytes the serialize function writes (TemporalAggregate.java). The
    # overloads of one name are chosen by the WKB type byte of the value, like the typed
    # overloads of a dispatcher. Spark resolves function names regardless of case and holds one
    # function per name, so an aggregate whose name a scalar of the catalog or of this surface
    # carries takes the `Agg` suffix of its canonical name: the catalog's own `Agg` name where it
    # states one beside the bare one (mergeAgg, tMinAgg), else the bare name with the suffix
    # (tAndAgg), and the scalar keeps the bare name.
    registered = {m.lower() for part in grouped.values() for code in part
                  for m in re.findall(r'register\("([^"]+)"', code)}
    registered |= {f["sqlfn"].lower() for f in fns
                   if isinstance(f.get("sqlfn"), str) and not f.get("sqlAgg")}
    have = lambda n: n in by_name and (jar_syms is None or n in jar_syms)
    stated = {a["sqlName"].lower() for a in cat.get("aggregates") or []}
    temporal = set(cat.get("temporalTypes") or {})
    aggs, agg_left = {}, set()
    for a in cat.get("aggregates") or []:
        role = lambda k: (a.get(k) or {}).get("meos")
        name = a["sqlName"]
        if name.lower() in registered:
            if (name + "Agg").lower() in stated:
                continue
            name += "Agg"
        takes = a.get("args") or []
        roles = [role(k) for k in ("transition", "combine", "final", "serialize", "deserialize")]
        if (len(takes) != 1 or takes[0] not in temporal or a.get("ret") not in temporal
                or a.get("stype") != "internal" or not all(roles) or not all(map(have, roles))):
            agg_left.add(name)
            continue
        steps = aggs.setdefault(name, {})
        steps.setdefault(TEMPTYPE_CODE[takes[0]], roles)
    nagg = 0
    for sname in sorted(aggs):
        steps = sorted(aggs[sname].items())
        role = lambda i: ", ".join("GeneratedFunctions::%s" % r[i] for _, r in steps)
        grouped.setdefault("GeneratedUdfs_aggregate", []).append(
            '        spark.udf().register("%s", org.apache.spark.sql.functions.udaf(\n'
            '            new TemporalAggregate(new int[] {%s},\n'
            '                new TemporalAggregate.Step[] {%s},\n'
            '                new TemporalAggregate.Step[] {%s},\n'
            '                new TemporalAggregate.Final[] {%s},\n'
            '                new TemporalAggregate.Write[] {%s},\n'
            '                new TemporalAggregate.Read[] {%s}),\n'
            '            org.apache.spark.sql.Encoders.BINARY()));'
            % (sname, ", ".join(str(c) for c, _ in steps), role(0), role(1), role(2), role(3),
               role(4)))
        registered.add(sname.lower())
        nagg += 1
        cov += 1
    print("  temporal aggregates               : %d  (left out: %s)"
          % (nagg, ", ".join(sorted(agg_left - set(aggs))) or "none"), file=sys.stderr)

    # Organize by doxygen module group (@ingroup), one class per group — the SAME
    # structure as the MEOS reference manual / XML docs, so a function is found in the
    # same place across tools. This also keeps every class small, dodging the per-class
    # constant-pool / BootstrapMethods limits a single 2300-lambda class would hit.
    # Within a class, register() statements are chunked to stay under the 64 KB method
    # bytecode limit.
    os.makedirs(args.out, exist_ok=True)
    # Clean stale generated files first: this tool fully OWNS args.out, so a prior
    # run's classes (a function later excluded by the jar arity/kind cross-check, or a
    # now-empty/renamed group) must not linger — they would silently break the build.
    for _old in glob.glob(os.path.join(args.out, "*.java")):
        os.remove(_old)
    CHUNK = 40        # register() statements per method (64 KB method-bytecode safety)
    MAXCLASS = 120    # UDFs per class (constant-pool / BootstrapMethods safety)
    written = []
    for grp in sorted(grouped):
        part = grouped[grp]
        subs = [part[i:i + MAXCLASS] for i in range(0, len(part), MAXCLASS)]
        for si, sub in enumerate(subs):
            cls = grp if len(subs) == 1 else "%s_%d" % (grp, si)
            chunks = [sub[i:i + CHUNK] for i in range(0, len(sub), CHUNK)]
            body = GEN_NOTE + IMPORTS + "\nfinal class %s {\n    private %s() {}\n" % (cls, cls)
            for i, ch in enumerate(chunks):
                body += "\n    private static void reg%d(SparkSession spark) {\n" % i + "\n".join(ch) + "\n    }\n"
            body += "\n    static void register(SparkSession spark) {\n"
            body += "\n".join("        reg%d(spark);" % i for i in range(len(chunks)))
            body += "\n    }\n}\n"
            with open(os.path.join(args.out, cls + ".java"), "w") as fh:
                fh.write(body)
            written.append(cls)

    # Build the WKB-kind byte sets from the catalog's MeosType enum (the WKB type byte =
    # the enum value): categorise each T_* by name suffix so the safe WKB readers know
    # which MeosType bytes belong to temporals vs spans vs spansets vs sets.
    mt = next((e for e in cat.get("enums", []) if e["name"] in ("MeosType", "meosType")), None)
    kinds = {"TEMPORAL_WKB": [], "SPAN_WKB": [], "SPANSET_WKB": [], "SET_WKB": []}
    for v in (mt.get("values") or mt.get("members") or []) if mt else []:
        nm = v["name"] if isinstance(v, dict) else v
        val = v.get("value") if isinstance(v, dict) else None
        if val is None or not nm:
            continue
        if nm.endswith("SPANSET"):
            kinds["SPANSET_WKB"].append(val)
        elif nm.endswith("SPAN"):
            kinds["SPAN_WKB"].append(val)
        elif nm.endswith("SET"):
            kinds["SET_WKB"].append(val)
        elif nm.startswith("T_T") and "BOX" not in nm:
            kinds["TEMPORAL_WKB"].append(val)
    wkb_lines = "\n".join(
        "    private static final java.util.Set<Integer> %s = java.util.Set.of(%s);"
        % (k, ", ".join(str(x) for x in sorted(set(v)))) for k, v in kinds.items())
    with open(os.path.join(args.out, "UdfMarshal.java"), "w") as fh:
        fh.write(MARSHAL.replace("__WKB_KINDS__", wkb_lines))
    with open(os.path.join(args.out, "TemporalAggregate.java"), "w") as fh:
        fh.write(AGGREGATE)

    main_cls = GEN_NOTE + IMPORTS + "\npublic final class GeneratedSpatioTemporalUDFs {\n"
    main_cls += "    private GeneratedSpatioTemporalUDFs() {}\n"
    main_cls += "\n    public static void registerAll(SparkSession spark) {\n"
    main_cls += "\n".join("        %s.register(spark);" % c for c in written)
    main_cls += "\n    }\n}\n"
    with open(os.path.join(args.out, "GeneratedSpatioTemporalUDFs.java"), "w") as fh:
        fh.write(main_cls)

    print("wrote %d group classes + UdfMarshal + GeneratedSpatioTemporalUDFs in %s" % (len(written), args.out), file=sys.stderr)
    print("  JMEOS functions in catalog : %d" % total, file=sys.stderr)
    print("  1:1 UDFs emitted (reached)  : %d  (%.0f%%)" % (cov, 100.0*cov/total), file=sys.stderr)
    print("  internal (excluded)         : %d" % internal, file=sys.stderr)
    print("  deferred type gaps (top):", file=sys.stderr)
    for k, c in skips.most_common(18):
        print("     %4d  %s" % (c, k), file=sys.stderr)

    # ── the ledger of unreached functions ──
    # A function the jar exposes and no emitted UDF calls is unreached. It is out of the surface
    # by the catalog's own statement when the catalog says it is not public, or that its
    # category (lifecycle, index) is not a value-to-value function (the `network` reasons
    # MEOS-API's parser/enrich.py fills). Every other unreached function is a gap: --gaps names
    # the ledger of the known ones, and a gap missing from it fails the run, so a function
    # dropped by a catalog change is refused instead of disappearing. A ledger entry the
    # surface now reaches is a notice: the ledger shrinks with --rebaseline.
    if args.gaps and jar_syms is not None:
        emitted = "".join(c for part in grouped.values() for c in part) + MARSHAL + AGGREGATE
        reached = set(re.findall(r"GeneratedFunctions(?:\.|::)([a-z][A-Za-z0-9_]+)", emitted))
        gaps = {}
        for f in fns:
            nm = f["name"]
            if nm not in jar_syms or nm in reached:
                continue
            why = [r.strip() for r in ((f.get("network") or {}).get("reason") or "").split(";")
                   if r.strip()]
            if f.get("api") != "public" or any(r in ("internal", "lifecycle", "index")
                                               for r in why):
                continue
            gaps[nm] = "; ".join(why) or supported(f) or "not emitted"
        if args.rebaseline:
            with open(args.gaps, "w") as fh:
                fh.write("# The public JMEOS functions the generated Spark surface does not reach,\n"
                         "# one per line with the reason the catalog states. Written by\n"
                         "# codegen_spark_udfs.py --rebaseline; a function missing from it fails\n"
                         "# the run. The ledger only shrinks, as types get their mappings.\n")
                for nm in sorted(gaps):
                    fh.write("%s\t%s\n" % (nm, gaps[nm]))
            print("  gaps ledger rewritten: %d functions" % len(gaps), file=sys.stderr)
            return
        known = set()
        if os.path.exists(args.gaps):
            for line in open(args.gaps):
                if line.strip() and not line.startswith("#"):
                    known.add(line.split("\t")[0].strip())
        new = sorted(set(gaps) - known)
        stale = sorted(known - set(gaps))
        if stale:
            print("  NOTICE: %d ledger functions are reached or gone, shrink it with --rebaseline: %s"
                  % (len(stale), ", ".join(stale[:20])), file=sys.stderr)
        if new:
            print("  ERROR: %d public functions the surface does not reach and the ledger does not "
                  "list; map their types or record them with --rebaseline:" % len(new),
                  file=sys.stderr)
            for nm in new:
                print("     %s\t%s" % (nm, gaps[nm]), file=sys.stderr)
            sys.exit(1)
        print("  gaps ledger: %d functions, none new" % len(gaps), file=sys.stderr)


if __name__ == "__main__":
    main()
