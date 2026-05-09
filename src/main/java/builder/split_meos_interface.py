#!/usr/bin/env python3
"""
Split functions.java's monolithic MeosLibrary JNR-FFI interface (1486 methods)
into four ≤ 400-method sub-interfaces so that JNR-FFI's generated proxy
<clinit>()V never exceeds the JVM 64 KB bytecode limit.

Background
----------
JNR-FFI generates an ASM proxy class for each interface it loads. The proxy's
static initialiser (<clinit>()V) sets up native-dispatch for every method.
With 1486 methods the initialiser exceeds 64 KB and the JVM raises:

  jdk.internal.org.objectweb.asm.MethodTooLargeException:
    Method too large: jdk/proxy2/$ProxyN.<clinit> ()V

The fix: split MeosLibrary into four private inner interfaces (PartA–PartD),
load each independently, and delegate from four package-visible static
instances. All public static wrappers in functions.java keep their signatures
unchanged — zero API break for callers (MobilitySpark, PyMEOS, etc.).
"""
import re
import sys
from pathlib import Path

SRC = Path('/tmp/JMEOS_fork/src/main/java/functions/functions.java')
N_GROUPS = 4
MAX_PER_GROUP = 400  # hard ceiling — never exceed this


# ── helpers ──────────────────────────────────────────────────────────────────

def find_interface_block(text):
    """Return (start, end) character offsets of MeosLibrary interface block."""
    marker = '\tpublic interface MeosLibrary {'
    start = text.index(marker)
    depth, end = 0, -1
    for j in range(start, len(text)):
        if text[j] == '{':
            depth += 1
        elif text[j] == '}':
            depth -= 1
            if depth == 0:
                end = j + 1
                break
    assert end != -1, "Could not find closing brace of MeosLibrary"
    return start, end


def extract_constants(iface_body):
    """
    Return the String constant declarations from the interface.
    Skip MeosLibrary-typed fields (INSTANCE, meos) — we regenerate those.
    """
    consts = []
    for line in iface_body.splitlines():
        s = line.strip()
        if (s and '=' in s and ';' in s and '(' not in s
                and not s.startswith('//')
                and not s.startswith('MeosLibrary')):
            consts.append(s)
    return consts


def extract_methods(iface_body):
    """
    Return an ordered list of (method_name, full_signature) pairs.
    Signatures are single-line (verified by caller).
    """
    methods = []
    in_iface = False
    for line in iface_body.splitlines():
        s = line.strip()
        if 'public interface MeosLibrary' in s:
            in_iface = True
            continue
        if not in_iface:
            continue
        if not s or s.startswith('//') or s.startswith('*') or s.startswith('/*'):
            continue
        if '=' in s or s in ('{', '}'):
            continue
        if s.endswith(';') and '(' in s:
            m = re.search(r'(\w+)\s*\(', s)
            if m:
                methods.append((m.group(1), s))
    return methods


def method_arg_names(sig):
    """Extract parameter names from a method signature like 'void foo(int x, Pointer y);'."""
    m = re.match(r'\w+\s+\w+\s*\(([^)]*)\)\s*;', sig)
    if not m:
        return []
    params_str = m.group(1).strip()
    if not params_str:
        return []
    names = []
    for param in params_str.split(','):
        parts = param.strip().split()
        if parts:
            names.append(parts[-1])
    return names


def method_return_type(sig):
    m = re.match(r'(\w+)\s+\w+\s*\(', sig)
    return m.group(1) if m else 'void'


def method_name(sig):
    m = re.search(r'(\w+)\s*\(', sig)
    return m.group(1) if m else ''


def method_params_str(sig):
    m = re.match(r'\w+\s+\w+\s*\(([^)]*)\)\s*;', sig)
    return m.group(1) if m else ''


# ── code builders ─────────────────────────────────────────────────────────────

def build_sub_interface(part_name, methods, indent='\t'):
    lines = [
        f'{indent}/**',
        f'{indent} * Internal sub-interface {part_name} — loaded by JNR-FFI as a separate proxy.',
        f'{indent} * Kept private; callers use the public static wrappers in this class.',
        f'{indent} */',
        f'{indent}private interface {part_name} {{',
    ]
    for _, sig in methods:
        lines.append(f'{indent}\t{sig}')
    lines.append(f'{indent}}}')
    return '\n'.join(lines)


def build_loader_fields(groups, lib_field='_LIB', indent='\t'):
    """Build static final fields that load each sub-interface via JNR-FFI."""
    lines = [
        f'{indent}// Native library name',
        f'{indent}private static final String {lib_field} = "meos";',
        '',
        f'{indent}// One JNR-FFI proxy per sub-interface.',
        f'{indent}// Each proxy <clinit>()V has ≤ {max(len(g) for g in groups)} methods → well under the JVM 64 KB limit.',
    ]
    for i, group in enumerate(groups):
        var = f'_meos_{chr(97+i)}'
        part = f'MeosLibraryPart{chr(65+i)}'
        lines.append(
            f'{indent}static final {part} {var} = '
            f'JarLibraryLoader.create({part}.class, {lib_field}).getLibraryInstance();'
        )
    return '\n'.join(lines)


def build_replacement_block(groups, constants, indent='\t'):
    """Build the full replacement for the old MeosLibrary interface block."""
    parts = []

    # Header comment
    parts.append('\n'.join([
        f'{indent}// ── MeosLibrary interface split ─────────────────────────────────────────────────',
        f'{indent}// The original single MeosLibrary interface ({sum(len(g) for g in groups)} methods) caused',
        f'{indent}// MethodTooLargeException: JNR-FFI generates a proxy whose <clinit>()V initialises',
        f'{indent}// native dispatch for every method, and {sum(len(g) for g in groups)} methods exceeds the JVM 64 KB',
        f'{indent}// bytecode limit. Fix: {N_GROUPS} private sub-interfaces of ≤{MAX_PER_GROUP} methods each.',
        f'{indent}// All public static wrappers keep their signatures — no API break.',
        f'{indent}// ─────────────────────────────────────────────────────────────────────────────────',
    ]))

    # Sub-interfaces
    for i, group in enumerate(groups):
        parts.append('')
        parts.append(build_sub_interface(f'MeosLibraryPart{chr(65+i)}', group, indent))

    # Loader fields
    parts.append('')
    parts.append(build_loader_fields(groups, indent=indent))

    # Deprecated MeosLibrary facade (for backward compat with FunctionsGenerator output)
    total = sum(len(g) for g in groups)
    name_to_var = {}
    for i, group in enumerate(groups):
        for name, _ in group:
            name_to_var[name] = f'_meos_{chr(97+i)}'

    parts.append('')
    parts.append('\n'.join([
        f'{indent}/**',
        f'{indent} * @deprecated Use the public static wrappers directly.',
        f'{indent} *   This interface is kept for backward compatibility with generated code.',
        f'{indent} *   Do NOT pass MeosLibrary.class to JNR-FFI — it still',
        f'{indent} *   has {total} methods and would recreate the MethodTooLargeException.',
        f'{indent} */',
        f'{indent}@Deprecated',
        f'{indent}public interface MeosLibrary {{',
    ]))
    # Constants
    for c in constants:
        parts.append(f'{indent}\t{c}')
    parts.append(f'{indent}\t/** @deprecated */')
    parts.append(f'{indent}\tMeosLibrary INSTANCE = new MeosLibraryDelegate();')
    parts.append(f'{indent}\t/** @deprecated */')
    parts.append(f'{indent}\tMeosLibrary meos = INSTANCE;')
    # Methods
    for group in groups:
        for _, sig in group:
            parts.append(f'{indent}\t{sig}')
    parts.append(f'{indent}}}')

    # Delegate implementation
    parts.append('')
    parts.append(f'{indent}/** Delegates MeosLibrary calls to the appropriate sub-interface proxy. */')
    parts.append(f'{indent}@Deprecated')
    parts.append(f'{indent}private static final class MeosLibraryDelegate implements MeosLibrary {{')
    for group in groups:
        for _, sig in group:
            rtype = method_return_type(sig)
            fname = method_name(sig)
            params_str = method_params_str(sig)
            arg_names = method_arg_names(sig)
            args = ', '.join(arg_names)
            var = name_to_var.get(fname, '_meos_a')
            ret_kw = '' if rtype == 'void' else 'return '
            parts.append(
                f'{indent}\t@Override public {rtype} {fname}({params_str})'
                f' {{ {ret_kw}{var}.{fname}({args}); }}'
            )
    parts.append(f'{indent}}}')

    return '\n'.join(parts)


def update_static_wrappers(text, name_to_var):
    """Replace MeosLibrary.meos.foo( with _meos_X.foo( in static wrapper methods."""
    def replacer(m):
        fname = m.group(1)
        var = name_to_var.get(fname)
        if var:
            return f'{var}.{fname}('
        return m.group(0)
    return re.sub(r'MeosLibrary\.meos\.(\w+)\(', replacer, text)


# ── main ──────────────────────────────────────────────────────────────────────

def main():
    text = SRC.read_text()

    iface_start, iface_end = find_interface_block(text)
    iface_body = text[iface_start:iface_end]

    constants = extract_constants(iface_body)
    methods = extract_methods(iface_body)
    print(f"Interface methods: {len(methods)}", file=sys.stderr)
    print(f"Constants: {constants}", file=sys.stderr)

    # Validate
    group_size = (len(methods) + N_GROUPS - 1) // N_GROUPS
    if group_size > MAX_PER_GROUP:
        sys.exit(f"Group size {group_size} > {MAX_PER_GROUP} — increase N_GROUPS")

    # Split
    groups = [methods[i * group_size:(i + 1) * group_size] for i in range(N_GROUPS)]
    for i, g in enumerate(groups):
        if g:
            print(f"  Part{chr(65+i)}: {len(g)} methods ({g[0][0]} … {g[-1][0]})", file=sys.stderr)

    name_to_var = {}
    for i, group in enumerate(groups):
        for name, _ in group:
            name_to_var[name] = f'_meos_{chr(97+i)}'

    before = text[:iface_start]
    after  = text[iface_end:]
    after_updated = update_static_wrappers(after, name_to_var)

    replacement = build_replacement_block(groups, constants)

    new_text = before + replacement + after_updated
    SRC.write_text(new_text)
    print(f"Written {len(new_text)} chars", file=sys.stderr)

    updated = len(re.findall(r'_meos_[a-d]\.', new_text))
    remaining = len(re.findall(r'MeosLibrary\.meos\.', new_text))
    print(f"Static wrapper calls updated: {updated}", file=sys.stderr)
    print(f"MeosLibrary.meos. calls remaining (should be in delegate only): {remaining}", file=sys.stderr)


if __name__ == '__main__':
    main()
