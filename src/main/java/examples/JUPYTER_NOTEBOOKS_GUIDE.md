# JMEOS — Jupyter Notebooks Guide

This guide covers how to use the two existing notebooks
(`Jupyter_AIS.ipynb` and `Jupyter_BerlinMod_Brussels.ipynb`)
and how to create new ones from scratch.

---

## Table of Contents

1. [Environment setup](#1-environment-setup)
2. [rapaio-jupyter-kernel constraints](#2-rapaio-jupyter-kernel-constraints)
3. [Notebook: AIS Data Processing](#3-notebook-ais-data-processing)
4. [Notebook: BerlinMod Brussels](#4-notebook-berlinmod-brussels)
5. [Creating a new notebook](#5-creating-a-new-notebook)
6. [Common pitfalls and solutions](#6-common-pitfalls-and-solutions)

---

## 1. Environment setup

### Prerequisites

- Docker Desktop installed (Windows: https://www.docker.com/products/docker-desktop/)
- JMEOS

### Starting the container

```powershell
docker run -it --name jupyter-jmeos -p 8888:8888 `
  -v "C:\path\to\JMEOS:/workspace" `
  eclipse-temurin:22-jdk bash
```

Replace `C:\path\to\JMEOS` with your actual project path.

### Installing Jupyter inside the container

```bash
apt-get update && apt-get install -y python3 python3-pip wget
pip3 install jupyterlab --break-system-packages
```

### Installing the Java kernel

```bash
cd /tmp
wget https://github.com/padreati/rapaio-jupyter-kernel/releases/download/2.1.0/rapaio-jupyter-kernel-2.1.0.jar
java -jar rapaio-jupyter-kernel-2.1.0.jar -i -auto
jupyter kernelspec list   # should show "rapaio-java"
```

> Use version **2.1.0** (compiled for Java 22). Newer versions require Java 23+.

### Compiling libmeos.so (inside the jupyter container)

The libmeos.so file is already available but just in case you ever need to create another one :

The native MEOS library must be compiled for Linux. This only needs to be done once.
```bash
apt-get install -y cmake gcc g++ libgeos-dev libproj-dev libjson-c-dev libssl-dev

cd /tmp
wget https://github.com/MobilityDB/MobilityDB/archive/refs/tags/v1.3.0rc1.tar.gz
tar -xzf v1.3.0rc1.tar.gz
cd MobilityDB-1.3.0rc1
mkdir build && cd build
cmake .. -DMEOS=ON -DCMAKE_BUILD_TYPE=Release
make -j$(nproc)

cp libmeos.so /workspace/src/libmeos.so
```

Verify the library is correctly built:
```bash
nm -D /workspace/src/libmeos.so | grep meos_initialize_timezone
# Expected output: T meos_initialize_timezone
# T = defined (exported symbol). U = undefined (missing — rebuild needed).
```

### Building the JMEOS fat JAR (inside the jmeos container)

The fat JAR bundles all Java dependencies (jnr-ffi, etc.) into a single file,
avoiding the `%maven` limitations of the rapaio-jupyter-kernel.
Run this from the root of the JMEOS project (where `pom.xml` is located), on your host machine:
```bash
mvn package -DskipTests
```

The fat JAR is generated at `jar/JMEOS-fat.jar`.

### Starting Jupyter

```bash
cd /workspace
jupyter lab --ip=0.0.0.0 --port=8888 --allow-root --no-browser
```

Open `http://127.0.0.1:8888`.

### Resuming an existing session

```bash
docker start -ai jupyter-jmeos
cd /workspace
jupyter lab --ip=0.0.0.0 --port=8888 --allow-root --no-browser
```

> Compiling `libmeos.so` and running `mvn package` only need to be done once.
> After a kernel crash
>   - manually rerun all cells before the failing cell
>   - or use **Run > Run All Cells** to restore the full JShell state.

---

## 2. rapaio-jupyter-kernel constraints

The rapaio-jupyter-kernel executes cells as JShell snippets. Several rules differ from a standard Java class and are easy to overlook.

### `%jars` must be alone in its cell

```java
// WRONG — mixing %jars with code or comments crashes the kernel
%jars ../../../../jar/JMEOS-fat.jar
import jnr.ffi.Pointer;
```

```java
// CORRECT — %jars in its own cell, imports in the next cell
%jars ../../../../jar/JMEOS-fat.jar
```

### `%maven`

I've used a pre-built fat JAR loaded with `%jars` since I couldn't make `%maven` work but you could also try it out instead.

### Top-level method declarations

JShell supports top-level method declarations (no surrounding class needed).

```java
// top-level method
Pointer makeShiftedBox(int base, double shiftY) {
    CommuneBox c = communes.get(base);
    String s = String.format(java.util.Locale.US,
        "SRID=3857;STBOX X((%f %f),(%f %f))",
        c.xmin(), c.ymin() + shiftY,
        c.xmax(), c.ymax() + shiftY);
    return meos.stbox_in(s);
}
```

### State is lost after a kernel crash

Every variable, record, and method is held in the JShell session.
A kernel crash wipes everything. Use **Run > Run All Cells** to restore or re-run every cell before the crashing one.

---

## 3. Notebook: AIS Data Processing

**File:** `Jupyter_AIS.ipynb`  
**Data:** `ais_instants.csv` (50 000+ AIS observations, 5 ships, EPSG:4326)

### What it demonstrates

| Section                 | What happens |
|-------------------------|-------------|
| 1 - Read CSV            | Parse each row into a temporal point instant and a temporal float (SOG) |
| 2 - Assemble trips      | Group observations by MMSI → build a `tgeogpoint` sequence per ship |
| 3 - Store in MobilityDB | Insert assembled trips into PostgreSQL/MobilityDB via JDBC |
| 4 - Port detection      | Detect whether each AIS position falls inside a port using Brute Force and RTree |

### Key design choices

**Interface functions**

functions.functions relies on an internal loader (JarLibraryLoader) that extracts and maps libmeos.so from the JAR's 
resources - a mechanism that doesn't work correctly in a JShell/rapaio-jupyter-kernel environment because the expected 
paths don't exist in the Docker container. By defining a custom JNR-FFI interface and calling 
LibraryLoader.create(...).search("/workspace/src").load("meos") directly, we bypass that mechanism entirely and tell 
JNR-FFI exactly where the native library lives on disk. As a side benefit, the interface only declares the handful of 
functions the notebook actually needs, keeping it self-contained and readable. 

```java
interface MeosAIS {
    Pointer tgeogpoint_in(String wkt);          // parse geographic point WKT
    Pointer tpointinst_make(Pointer gs, long t); // create temporal instant
    Pointer tsequence_make(...);                 // build sequence from instants
    double  tpoint_length(Pointer seq);          // trajectory length in metres
    Pointer geo_from_text(String wkt, int srid); // exact geometry for intersection check
    boolean overlaps_stbox_stbox(Pointer a, Pointer b);
    Pointer rtree_create_stbox();
    void    rtree_insert(Pointer rt, Pointer box, int id);
    Pointer rtree_search(Pointer rt, Pointer box, Pointer countPtr);
    void    rtree_free(Pointer rt);
}
```

```
String MEOS_LIB_PATH = "/workspace/src";

MeosAIS meos = LibraryLoader.create(MeosAIS.class)
.search(MEOS_LIB_PATH)
.load("meos");
```

---

## 4. Notebook: BerlinMod Brussels

**File:** `Jupyter_BerlinMod_Brussels.ipynb`  
**Data:** `berlinmod_instants.csv` (89 000 vehicle positions, EPSG:3857) and `brussels_communes.csv` (19 communes, EPSG:3857)

### What it demonstrates

| Section                   | What happens |
|---------------------------|-------------|
| 2 - Load communes         | Parse EWKB hex → `geom_from_hexewkb` → `geo_to_stbox` → store bbox doubles |
| 3 - Load positions        | Parse `SRID=3857;POINT(x y)` → build point STBOX → build POINT geometry |
| 4 - Brute Force           | `overlaps_stbox_stbox` over all 19 communes for every position |
| 5 - RTree                 | Same query via `rtree_search` |
| 6 - Exact check           | `geom_intersects2d` to eliminate false positives from the MBR filter |
| 7 - Summary table         | Timing + match counts for all 4 combinations |
| 8 - Scalability benchmark | 19 / 100 / 500 / 2000 synthetic regions, all 4 combinations |

### Key design choices

**Both datasets share SRID 3857** - no coordinate conversion is needed. This is the main difference from the AIS notebook where UTM→WGS84 conversion was required.

**SRID on vehicle STBoxes.** The STBox of a vehicle position must carry `SRID=3857;` so it matches the commune STBoxes (which inherit SRID 3857 from `geo_to_stbox`). Without the prefix, MEOS raises `[MEOS ERROR 12] Operation on mixed SRID`.

**Scalability benchmark - tiling logic.** The 19 real commune bounding boxes are tiled on a grid to simulate N > 19 regions. For index `j`:

```
base  = j % 19          → which commune to copy (cycles 0 → 18 → 0 → 18 → ...)
row   = j / 19          → grid row (integer division: 0, 0, ..., 1, 1, ..., 2, ...)
shift = row × 50 000 m  → Y offset (northward, SRID 3857 metres)
```

The exact check uses `commune[j % 19].geom()` (unshifted polygon) even for shifted boxes. Because the polygon sits at Brussels while the box is 50-150 km north, `geom_intersects2d` almost always returns `false` which correctly benchmarks the elimination cost without inflating the match count.

---

## 5. Creating a new notebook

**1. Declare only the MEOS functions you need.**

Copy the minimal interface pattern and add only the signatures required:

```java
interface MeosMyNotebook {

    // Lifecycle
    void meos_initialize_timezone(String tz);
    void meos_initialize_error_handler(error_handler_fn handler);
    void meos_finalize();

    // Add only what you use:
    Pointer geo_from_text(String wkt, int srid);
    Pointer geom_from_hexewkb(String hex);
    Pointer geo_to_stbox(Pointer geom);
    double  stbox_xmin(Pointer box);
    double  stbox_ymin(Pointer box);
    double  stbox_xmax(Pointer box);
    double  stbox_ymax(Pointer box);
    boolean overlaps_stbox_stbox(Pointer a, Pointer b);
    boolean geom_intersects2d(Pointer gs1, Pointer gs2);
    Pointer stbox_in(String str);
    Pointer rtree_create_stbox();
    void    rtree_insert(Pointer rt, Pointer box, int id);
    Pointer rtree_search(Pointer rt, Pointer box, Pointer countPtr);
    void    rtree_free(Pointer rt);
}
```

**2. Use `record` for data holders.**

Java records (supported in JShell) are the cleanest way to bundle geometry pointers with metadata:

```java
record RegionBox(
    int    id,
    String name,
    Pointer geom,   // polygon geometry - used in geom_intersects2d
    Pointer bbox,   // bounding STBOX - used in overlaps_stbox_stbox / rtree
    double xmin, double ymin, 
    double xmax, double ymax
) {}

record ObservationPoint(
    int    id,
    String t,       // timestamp string (for display)
    double x,
    double y,
    Pointer bbox,   // point STBOX (collapsed: min == max)
    Pointer geom    // POINT geometry - used in geom_intersects2d
) {}
```

**3. Always clean up.**

```java
meos.meos_finalize();
```

### Minimal skeleton ("copy-paste" template)

```java
// Cell 1: %jars (ALONE)
%jars ../../../../jar/JMEOS-fat.jar

// Cell 2: imports 
import jnr.ffi.Pointer;
import jnr.ffi.LibraryLoader;
import java.util.*;
import java.io.*;
System.out.println("Imports OK");

// Cell 3: interface + helper 
interface error_handler_fn {
    @jnr.ffi.annotations.Delegate
    void call(int level, int code, String message);
}

interface MeosMyNotebook {
    void    meos_initialize_timezone(String tz);
    void    meos_initialize_error_handler(error_handler_fn h);
    void    meos_finalize();
    Pointer geo_from_text(String wkt, int srid);
    Pointer geom_from_hexewkb(String hex);
    Pointer geo_to_stbox(Pointer geom);
    double  stbox_xmin(Pointer box);
    double  stbox_ymin(Pointer box);
    double  stbox_xmax(Pointer box);
    double  stbox_ymax(Pointer box);
    boolean overlaps_stbox_stbox(Pointer a, Pointer b);
    boolean geom_intersects2d(Pointer gs1, Pointer gs2);
    Pointer stbox_in(String str);
    Pointer rtree_create_stbox();
    void    rtree_insert(Pointer rt, Pointer box, int id);
    Pointer rtree_search(Pointer rt, Pointer box, Pointer countPtr);
    void    rtree_free(Pointer rt);
}

String ptrToString(Pointer p) {
    return p == null ? "(null)" : p.getString(0);
}
System.out.println("Interface defined");

// Cell 4: load library + initialise 
MeosMyNotebook meos = LibraryLoader.create(MeosMyNotebook.class)
    .search("/workspace/src")
    .load("meos");

error_handler_fn errHandler =
    (lvl, code, msg) -> System.err.printf("[MEOS ERROR %d] %s%n", code, msg);

meos.meos_initialize_timezone("UTC");
meos.meos_initialize_error_handler(errHandler);
System.out.println("MEOS initialised");
```

---

## 6. Common pitfalls and solutions

| Problem | Symptom | Fix                                                        |
|---------|---------|------------------------------------------------------------|
| `%jars` mixed with code | Kernel crash on first cell | Put `%jars` alone in its own cell                          |
| Missing `Locale.US` | Coordinates contain commas, WKT rejected | Add `java.util.Locale.US` to all `String.format()` calls   |
| STBox SRID mismatch | `[MEOS ERROR 12] Operation on mixed SRID` | Prefix vehicle STBoxes with `SRID=3857;`                   |
| `stbox_out` decimal issue | `NumberFormatException: multiple points` | Use `stbox_xmin/ymin/xmax/ymax` accessors instead          |
| JNR-FFI string crash | JVM crash on string-returning function | Declare return type as `Pointer`, read with `.getString(0)` |
| RTree overhead > savings | RTree slower than brute force | Expected with low N; RTree wins at larger N   |
| State lost after error | Variables undefined in later cells | Run > Run All Cells to restore full JShell session         |
| `geom_intersects2d` always false | Exact check never confirms matches | Check that both geometries share the same SRID             |
