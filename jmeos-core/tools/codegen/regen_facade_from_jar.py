#!/usr/bin/env python3
"""Re-derive the MeosOps* facade signatures from a JMEOS jar.

The tier-aware generators (codegen-oo.py / codegen-free.py) emit the facade
from the MEOS-API streaming-relevance baseline. When only the binding jar is
available (e.g. bumping to a newly generated functions.GeneratedFunctions whose
type lowering changed), this tool re-emits each existing facade method with the
jar's current signature — preserving the per-class tier classification and the
Javadoc, changing only the parameter / return types to match the jar. It is the
signature-migration complement to the baseline generators, not a replacement.

Run from :
    python3 tools/codegen/regen_facade_from_jar.py \\
        src/main/java/org/mobilitydb/meos jar/JMEOS.jar

Forwarder facades emitted by their own scripts are left untouched here
(MeosOpsParityGaps via emit_gap_methods.py, MeosOpsSqlSurface via
emit_sql_surface.py, MeosOpsRuntime hand-maintained); re-run those against the
same jar afterwards.
"""
import re, glob, os, sys, subprocess

EXCLUDE = {"MeosOpsParityGaps.java", "MeosOpsSqlSurface.java", "MeosOpsRuntime.java"}
_SIG = re.compile(r'public static (\S+) ([a-z_][A-Za-z0-9_]*)\(([^)]*)\)')
# method block: optional Javadoc + `public static <ret> <name>(...) {` ... 4-space `}`
_BLOCK = re.compile(
    r'(?P<jd>(?:[ ]{4}/\*\*.*?\*/\n)?)'
    r'[ ]{4}public static [^\n;{]*?\b(?P<name>[a-z_][A-Za-z0-9_]*)\([^)]*\)\s*\{'
    r'.*?\n[ ]{4}\}\n',
    re.S)


def jar_signatures(jar):
    out = subprocess.run(["javap", "-classpath", jar, "-p",
                          "functions.GeneratedFunctions"],
                         capture_output=True, text=True).stdout
    sigs = {}
    for line in out.splitlines():
        m = _SIG.search(line)
        if m:
            ret, name, args = m.group(1), m.group(2), m.group(3).strip()
            sigs[name] = (ret, [a.strip() for a in args.split(",")] if args else [])
    return sigs


def emit(jd, name, sigs):
    if name not in sigs:
        return None
    ret, types = sigs[name]
    decl = ", ".join(f"{t} arg{i}" for i, t in enumerate(types))
    call = ", ".join(f"arg{i}" for i in range(len(types)))
    retkw = "" if ret == "void" else "return "
    return (f'{jd}    public static {ret} {name}({decl}) {{\n'
            f'        if (!MEOS_AVAILABLE) {{\n'
            f'            throw new UnsupportedOperationException(\n'
            f'                "{name} requires libmeos — set -Dmeos.enabled=true");\n'
            f'        }}\n'
            f'        {retkw}GeneratedFunctions.{name}({call});\n'
            f'    }}\n')


def main():
    facade_dir, jar = sys.argv[1], sys.argv[2]
    sigs = jar_signatures(jar)
    changed = 0
    for f in sorted(glob.glob(os.path.join(facade_dir, "MeosOps*.java"))):
        if os.path.basename(f) in EXCLUDE:
            continue
        txt = open(f).read()
        new = _BLOCK.sub(lambda m: emit(m.group("jd"), m.group("name"), sigs) or m.group(0), txt)
        if new != txt:
            open(f, "w").write(new)
            changed += 1
    print(f"jar signatures: {len(sigs)}; facade files rewritten: {changed}")


if __name__ == "__main__":
    main()
