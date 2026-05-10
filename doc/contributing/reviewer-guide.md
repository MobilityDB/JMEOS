<!--
Copyright(c) MobilityDB Contributors

This documentation is licensed under a
Creative Commons Attribution-Share Alike 3.0 License
https://creativecommons.org/licenses/by-sa/3.0/
-->

# JMEOS PR Reviewer Guide

Quick reference for anyone reviewing open pull requests in **MobilityDB/JMEOS**.
Updated in the same commit as any PR that changes PR state or adds new branches.
**Last updated: 2026-05-10 — 3 open PRs (#8, #9, #12) after consolidation pass closed #11 (superseded).**

---

## How to find this guide

- **In the repo:** `doc/contributing/reviewer-guide.md`
- **Rule:** every commit that opens, closes, or restructures a PR must update this file in the same commit. A one-liner status change is enough; a fuller rewrite is needed when the dependency graph changes.

Reviewers landing in any of the four ecosystem repos (MobilityDB / MobilityDuck /
MobilitySpark / JMEOS) find the same canonical structure at the same path.

---

## CI legend

| Symbol | Meaning |
|--------|---------|
| ✅ | All checks green |
| ❌ | Real failure — needs investigation before review |
| ⏳ | CI running |
| ❓ | No CI result yet (doc-only, draft, or external PR) |
| ⚠️ | Non-blocking failure (e.g. macOS/Windows `continue-on-error`, Codacy ACTION_REQUIRED — maintainer overrides in UI) |

---

## Dependency chain — land in this order

```
PR #9   JashanReel:fix-tests-using-docker
          (multi-module Maven layout: jmeos-core/, jmeos-tests/; MEOS 1.3 API; test fixes)
  └─► PR #12  estebanzimanyi:fix/multimodule-with-split-interface
          (split MeosLibrary into 4 public-static sub-interfaces — ARM64/Windows/macOS fix
           for JNR-FFI MethodTooLargeException)
        └─► downstream: MobilitySpark PR #5 / #7 (depend on the published JMEOS jar)
```

**PR #9 should land first** — it carries the multi-module Maven foundation that
PR #12 builds on. PR #12's own incremental contribution is exactly **one commit**
on top of #9 (verified via `git log <#9-tip>..<#12-tip>`).

**PR #8** (`SachaDelsaux:JMEOS_v1.3`) is subsumed by #9 — recommended for closure
but left in place pending the original author's signal.

---

## Tier 1 — Foundation (land first)

| PR | Branch | Description | CI | Notes |
|----|--------|-------------|----|-------|
| #9 | `JashanReel:fix-tests-using-docker` | Multi-module Maven layout (`jmeos-core/`, `jmeos-tests/`); MEOS 1.3 API; test fixes | ❓ | Needs cleanup review (IDE files, binary blobs, `*.class`); large branch (119 commits) |

---

## Tier 2 — Stacks on Tier 1

| PR | Branch | Description | CI | Notes |
|----|--------|-------------|----|-------|
| #12 | `estebanzimanyi:fix/multimodule-with-split-interface` | Split `MeosLibrary` 1685-method JNR-FFI interface into 4 `public static` sub-interfaces (`MeosLibraryPartA/B/C/D`) to avoid `MethodTooLargeException` on ARM64/Windows/macOS; removes binary blobs + stray `.class` files; updates `.gitignore` | ❓ | One incremental commit on top of #9 (`git log <#9-tip>..<#12-tip>`) — Axis 2 already satisfied |

---

## Tier 3 — Recommended for closure (subsumed)

| PR | Branch | Description | CI | Notes |
|----|--------|-------------|----|-------|
| #8 | `SachaDelsaux:JMEOS_v1.3` | Original JMEOS 1.3 upgrade attempt; subsumed by #9's multi-module rewrite | ❓ | External author — comment posted, awaiting signal; do not close unilaterally |

---

## Closed during consolidation (2026-05-10)

| PR | Why closed | Folded into |
|----|------------|-------------|
| #11 | Same `MeosLibrary` JNR-FFI split fix on the old flat `src/` layout; fully superseded by #12 (which carries the equivalent fix on top of #9's multi-module layout) | #12 |

---

## Review checklist

For every JMEOS PR, verify:

- [ ] PostgreSQL License header on every new `.java` file
- [ ] No large binary data files (`*.jar`, `*.so`, `*.class`) committed — these are build artifacts
- [ ] No IDE-local files (`.idea/`, `*.iml`, `.vscode/`) committed
- [ ] `MeosLibrary` interface changes preserve the 4-sub-interface split (ARM64/Windows/macOS constraint)
- [ ] DBL_MAX MEOS sentinel mapped to a Java `null` in any new wrapper that exposes a distance / NAD return
- [ ] CI green on Linux before requesting merge (macOS/Windows are non-blocking until upstream JMEOS issues are resolved)
- [ ] If the change adds or removes a binding, the corresponding MobilityDB function name and signature appear in the commit message for traceability

---

## Cross-repo links

- **MobilityDB:** [doc/contributing/reviewer-guide.md](https://github.com/MobilityDB/MobilityDB/blob/master/doc/contributing/reviewer-guide.md)
- **MobilityDuck:** [doc/contributing/reviewer-guide.md](https://github.com/MobilityDB/MobilityDuck/blob/master/doc/contributing/reviewer-guide.md)
- **MobilitySpark:** [doc/contributing/reviewer-guide.md](https://github.com/MobilityDB/MobilitySpark/blob/main/doc/contributing/reviewer-guide.md)
