#!/usr/bin/env python3
"""Post-process JMEOS's auto-generated functions.java.

The FunctionsGenerator emits two patterns that are wrong for some
specific signatures and we patch them here. The script is idempotent:
running it twice is a no-op.

1. rtree_search / rtree_search_temporal — the C signature is
       int foo(in, in, void *query, MeosArray *result)
   but the generator's heuristic miscompiles this as if it were a
   bool-out-param wrapper, producing a malformed Java method that does
   not compile. We rewrite both wrappers to a straight delegation that
   takes a caller-supplied Pointer for the result.

2. bool foo(in, ..., T *result)  versus  bool foo(in, ..., T **result)
   The generator emits the same wrapper for both:
       Pointer result = Memory.allocateDirect(runtime, Long.BYTES);
       bool out = MeosLibrary.meos.foo(args, result);
       Pointer new_result = result.getPointer(0);
       return out ? new_result : null;
   For pointer-out (T **result, INDIR) this is correct: the native
   call writes the pointer value into the buffer and getPointer(0)
   reads it back. For value-out (T *result, DIRECT — e.g.
   double *result, int *result, TimestampTz *result, bool *result) it
   is wrong: getPointer(0) reads the value as if it were an address.
   Callers that did r.getDouble(0) on the returned pointer would crash
   in Unsafe_GetDouble with SIGSEGV.

   We rewrite the DIRECT cases to return the buffer directly. The
   classification below is hand-derived from MEOS C signatures (see
   the table in scripts/README.md) — there is no reliable way to
   recover it from the Java signature alone.

Run after FunctionsGenerator:
    python3 scripts/post_regen_patch.py src/main/java/functions/functions.java
"""

import re
import sys
from pathlib import Path

# Out-param signatures of shape  bool foo(args, T *result)  — the wrapper
# must return the buffer directly so the caller can read the value with
# r.getDouble(0) / getInt(0) / getLong(0) / getByte(0).
DIRECT = {
    "bearing_point_point",          # double *result
    "bigintset_value_n",            # int64 *
    "dateset_value_n",              # DateADT *
    "datespanset_date_n",           # DateADT *
    "floatset_value_n",             # double *
    "geom_azimuth",                 # double *
    "intset_value_n",               # int *
    "tbool_value_n",                # bool *
    "temporal_timestamptz_n",       # TimestampTz *
    "tfloat_value_n",               # double *
    "tint_value_n",                 # int *
    "tpoint_direction",             # double *
    "tstzset_value_n",              # TimestampTz *
    "tstzspanset_timestamptz_n",    # TimestampTz *
    # bbox accessors — also single-value out-params
    "stbox_xmin", "stbox_xmax", "stbox_ymin", "stbox_ymax",
    "stbox_zmin", "stbox_zmax", "stbox_tmin", "stbox_tmax",
    "stbox_tmin_inc", "stbox_tmax_inc",
    "tbox_xmin", "tbox_xmax", "tbox_tmin", "tbox_tmax",
    "tbox_tmin_inc", "tbox_tmax_inc",
    "tbox_xmin_inc", "tbox_xmax_inc",
    "tboxfloat_xmin", "tboxfloat_xmax",
    "tboxint_xmin", "tboxint_xmax",
}
# Everything else matching the broken pattern is INDIR (T **result —
# Pose **, GSERIALIZED **, text **, …) and is left untouched.

RTREE_PATTERN = re.compile(
    r'@SuppressWarnings\("unused"\)\s*'
    r'public static int (rtree_search(?:_temporal)?)\(Pointer (\w+), int op, Pointer (\w+)\)\s*\{'
    r'[^\}]*\}',
    re.DOTALL,
)

OUT_PARAM_PATTERN = re.compile(
    r'public static Pointer (\w+)\(([^)]*)\) \{\s*'
    r'boolean out;\s*'
    r'Runtime runtime = Runtime\.getSystemRuntime\(\);\s*'
    r'Pointer result = Memory\.allocateDirect\(runtime, Long\.BYTES\);\s*'
    r'out = MeosLibrary\.meos\.\w+\(([^)]*)\);\s*'
    r'Pointer new_result = result\.getPointer\(0\);\s*'
    r'return out \? new_result : null ;\s*\}',
    re.DOTALL,
)


def patch_rtree(content: str) -> tuple[str, int]:
    def repl(m):
        name = m.group(1)
        first_param = m.group(2)
        third_param = m.group(3)
        return (
            f'@SuppressWarnings("unused")\n'
            f'\tpublic static int {name}(Pointer {first_param}, int op, '
            f'Pointer {third_param}, Pointer result) {{\n'
            f'\t\treturn MeosLibrary.meos.{name}({first_param}, op, '
            f'{third_param}, result);\n'
            f'\t}}'
        )
    new, n = RTREE_PATTERN.subn(repl, content)
    return new, n


def patch_out_params(content: str) -> tuple[str, int, int]:
    direct_count = 0
    indirect_count = 0

    def repl(m):
        nonlocal direct_count, indirect_count
        name = m.group(1)
        params = m.group(2)
        call_args = m.group(3)
        if name in DIRECT:
            direct_count += 1
            return (
                f'public static Pointer {name}({params}) {{\n'
                f'\t\tRuntime runtime = Runtime.getSystemRuntime();\n'
                f'\t\tPointer result = Memory.allocateDirect(runtime, 8);\n'
                f'\t\tboolean out = MeosLibrary.meos.{name}({call_args});\n'
                f'\t\treturn out ? result : null;\n'
                f'\t}}'
            )
        indirect_count += 1
        return m.group(0)

    new = OUT_PARAM_PATTERN.sub(repl, content)
    return new, direct_count, indirect_count


def main() -> int:
    if len(sys.argv) != 2:
        print(f"usage: {sys.argv[0]} <path-to-functions.java>", file=sys.stderr)
        return 2
    path = Path(sys.argv[1])
    content = path.read_text()
    content, rtree_n = patch_rtree(content)
    content, direct, indirect = patch_out_params(content)
    path.write_text(content)
    print(f"rtree wrappers patched:        {rtree_n}")
    print(f"out-param wrappers DIRECT:     {direct}")
    print(f"out-param wrappers INDIR kept: {indirect}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
