# JMEOS Examples Guide

This guide walks you through all JMEOS example programs in order. Each
program demonstrates key concepts for working with spatiotemporal data
using the MEOS library in Java.

## Prerequisites

- JDK 21+
- Maven
- Docker

## Quick Start

```bash
# Clone JMEOS
git clone https://github.com/MobilityDB/JMEOS.git

# Build the Docker Image for JMEOS
git clone https://github.com/nmareghn/Docker-JMEOS.git
cd .\JMEOS\
docker build -t mbjmeos:lasted .

# Run the JMEOS Docker Container (replace the path)
docker run -it --name my_container \
  -v absolute\path\to\JMEOS:/usr/local/jmeos \
  mbjmeos:lasted /bin/bash

# Enter in the container in interactive mode
docker exec -it my_container /bin/bash

# If necessary
cd /usr/local/jmeos

# Compile all examples
mvn clean compile

# Run any example
mvn exec:java -Dexec.mainClass="examples.ProgramName"

# Run tests
mvn test
mvn test -Dtest=TestFile
mvn test -Dtest=TestFile#testName
```

---

## Programs Overview


#### 1. `N01_Hello_World` - Introduction to Temporal Types
**Concepts**: Temporal instant, sequence, sequence set, interpolation

Creates and displays temporal geometric points with different
interpolations:
- **Instant**: Single point at one timestamp
- **Discrete Sequence**: Unconnected points
- **Linear Sequence**: Points connected by straight lines
- **Step Sequence**: Points connected by steps (constant value)
- **Sequence Set**: Multiple sequences

```bash
mvn exec:java -Dexec.mainClass="examples.N01_Hello_World"
```

**Output**: WKT and MF-JSON representations of temporal types

**Key Functions**:
- `TGeomPointInst()` - Create temporal instant
- `TGeomPointSeq()` - Create temporal sequence
- `as_mfjson()` - Convert to Moving Features JSON

---

#### 2. `N01_Hello_World_Geodetic` - Geographic Coordinates
**Concepts**: Geographic vs geometric coordinates, EPSG:4326

Same as Hello_World but uses **geodetic coordinates** (latitude
longitude on Earth's surface) instead of planar coordinates.

```bash
mvn exec:java -Dexec.mainClass="examples.N01_Hello_World_Geodetic"
```

**Difference from Hello_World**:
- Uses `TGeogPoint` instead of `TGeomPoint`
- Coordinates in EPSG:4326 (WGS84)
- Distances measured on Earth's surface (geodesic)

---

#### 3. `N01_ErrorHandlingDemo` - Managing MEOS Exceptions
**Concepts**: exceptions thrown by MEOS

An introduction to MEOS exception types.
This demo demonstrates scenarios where MEOS throws exceptions and how to handle them correctly using JMEOS.

```bash
mvn exec:java -Dexec.mainClass="examples.N01_ErrorHandlingDemo"
```

---

#### 4. `N02_AIS_Read` - Parse CSV Data
**Concepts**: Reading CSV, creating temporal instants, coordinate systems

Reads AIS (Automatic Identification System) ship tracking data from CSV
and creates temporal point instants.

**Input**: `ais_instants.csv` (50K+ ship observations)
```csv
T,MMSI,Latitude,Longitude,SOG
2009-06-01 00:01:11+00,228041600,39.84917,-3.55917,11.7
```

```bash
mvn exec:java -Dexec.mainClass="examples.N02_AIS_Read"
```

**Output**: Sample records converted to temporal points
```
MMSI: 228041600, Location: SRID=4326;Point(-3.56 39.85
@2009-06-0100:01:11+00
```

**Key Functions**:
- `pg_timestamptz_in()` - Parse timestamp
- `geogpoint_make2d()` - Create geographic point
- `tpointinst_make()` - Create temporal point instant
- `tfloatinst_make()` - Create temporal float (for SOG)

---

#### 5. `N03_AIS_Assemble` - Build Trajectories
**Concepts**: Aggregating instants, constructing sequences, distance
calculation

Assembles individual observations into complete ship trajectories.

**Input**: `ais_instants.csv` &#8594; **Output**:
`ais_trips_new_assemble.csv`

```bash
mvn exec:java -Dexec.mainClass="examples.N03_AIS_Assemble"
```

**Process**:
1. Group observations by MMSI (ship ID)
2. Accumulate instants per ship
3. Build temporal sequence from instants
4. Calculate trajectory length and time-weighted average SOG

**Output**:
```
MMSI: 228041600, Number of input instants: 10523
  Trip -> Number of instants: 10523, Distance travelled 1234.56 km
  SOG -> Time-weighted average: 8.34 knots
```

**Key Functions**:
- `tsequence_make()` - Build sequence from instants array
- `tpoint_length()` - Calculate trajectory length
- `tnumber_twavg()` - Time-weighted average

---

#### 6. `N03_BerlinMOD_Assemble` - Vehicle Trip Assembly
**Concepts**: Synthetic trajectory data, HexWKB encoding

Similar to AIS_Assemble but for synthetic vehicle data in Brussels.

**Input**: `berlinmod_instants.csv` (89K observations from 5 vehicles)
**Output**: `berlinmod_trips_new_assemble.csv`

```bash
mvn exec:java -Dexec.mainClass="examples.N03_BerlinMOD_Assemble"
```

**Differences from AIS**:
- Uses **EPSG:3857** (Web Mercator) instead of WGS84
- Writes as **HexWKB** (compact binary encoding)
- Includes trip metadata (vehicle ID, day, sequence)

**Key Functions**:
- `pg_date_in()` / `pg_date_out()` - Date handling
- `geom_in()` - Parse geometry (planar coordinates)
- `temporal_as_hexwkb()` - Export as HexWKB

---


#### 7. `N04_AIS_Store` - Write to MobilityDB
**Concepts**: Database connectivity, SQL insertion, MobilityDB types

Reads AIS data and stores it directly in PostgreSQL/MobilityDB.

**Setup**:
```bash
# Start MobilityDB in Docker
docker run --name postgres-mobilitydb \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d mobilitydb/mobilitydb
```

```bash
mvn exec:java -Dexec.mainClass="examples.N04_AIS_Store"
```

**Note**: as explained in the source file and in the "Troubleshooting"
section below, you may have to modify the JDBC URL in the program if
you are using Linux

**Process**:
1. Connect to PostgreSQL
2. Create PostGIS and MobilityDB extensions
3. Create table `AISInstants(MMSI, location, SOG)`
4. Bulk insert temporal data (batches of 20)

**Key Concepts**:
- JDBC connection
- MobilityDB data types: `tgeogpoint`, `tfloat`
- Batch insertion for performance
- Transaction management

---

#### 8. `N04_AIS_Stream_DB` - Streaming to Database
**Concepts**: Expandable sequences, memory-efficient streaming, incremental updates

Processes large AIS datasets by streaming to database instead of holding
everything in memory.

```bash
mvn exec:java -Dexec.mainClass="examples.N04_AIS_Stream_DB"
```

**Architecture**: Expandable Sequences (FAITHFUL to C)
```
Record → Append to expandable sequence → When full (1000 instants) 
       → Send to database → Restart with last 2 instants → Continue
```


**Process**:
1. Create expandable sequence with first instant
2. Append subsequent instants using `temporal_append_tinstant()`
3. When count reaches 1000: INSERT/UPDATE to database
4. Restart sequence keeping last 2 instants for continuity
5. Continue until end of file

**Key Functions**:
- `temporal_append_tinstant()` - Append instant to expandable sequence
- `restartSequence()` - Simulate MEOS C's internal function `tsequence_restart()`
- `getSequenceCount()` - Get instant count (simulates `seq->count`)
- `update()` - MobilityDB function to merge temporal values


**Continuity Between Batches**:
- Keeps last 2 instants when restarting
- Creates seamless trajectory when merged in database
- MobilityDB `update()` function connects sequences

---

#### 9. `N04_AIS_Stream_File` - Streaming to File
**Concepts**: Expandable sequences, file streaming, memory management

Same concept as AIS_Stream_DB but writes to CSV file instead.

```bash
mvn exec:java -Dexec.mainClass="examples.N04_AIS_Stream_File"
```

**Output**: `ais_trips_new_stream.csv`

**Architecture**: Same as AIS_Stream_DB
```
Record → Append to expandable sequence → When full (1000 instants)
       → Write to file → Restart with last 2 instants → Continue
```

**Key Functions**:
- `temporal_append_tinstant()` - Build sequence incrementally
- `restartSequence()` - Keep last 2 instants for continuity
- `tspatial_out()` - Convert sequence to WKT string

**Output Format**:
```csv
228041600, SRID=4326;LINESTRING(...) @ [2009-06-01 00:01:11+00, ...)
230907000, SRID=4326;LINESTRING(...) @ [2009-06-01 00:02:45+00, ...)
```

---


#### 10. `N05_BerlinMOD_Disassemble` - Extract Observations
**Concepts**: Temporal decomposition, sorting, coordinate reference
systems

**Reverse** of assembly: Takes complete trips and extracts individual
observations.

**Input**: `berlinmod_trips.csv` (154 trips in HexWKB)
**Output**: `berlinmod_instants_disassemble.csv` (89,091 sorted
observations)

```bash
mvn exec:java -Dexec.mainClass="examples.N05_BerlinMOD_Disassemble"
```

**Process**:
1. Read trips (HexWKB format)
2. Extract each instant using `temporal_instant_n()`
3. Parse geometry and timestamp
4. Sort all instants by timestamp
5. Write ordered CSV

**Key Functions**:
- `temporal_from_hexwkb()` - Parse HexWKB
- `temporal_num_instants()` - Count instants
- `temporal_instant_n()` - Get Nth instant (1-indexed!)
- `tspatial_out()` - Convert to WKT string

**Use case**: Converting from trajectory format to observation format

---

#### 11. `N06_BerlinMOD_Clip` - Spatial Analysis
**Concepts**: Spatial clipping, geometric operations, administrative
boundaries

Analyzes how much distance vehicles travel in each Brussels commune
(municipality).

**Input Files**:
- `brussels_communes.csv` - 19 communes with geometries
- `brussels_region.csv` - Brussels boundary (union of communes)
- `berlinmod_trips.csv` - 154 vehicle trips

```bash
mvn exec:java -Dexec.mainClass="examples.N06_BerlinMOD_Clip"
```

**Process**:
1. For each trip:
   - Total distance
   - Clip to each commune &#8594; distance in commune
   - Clip to Brussels region &#8594; inside/outside

**Output**: Distance matrix (km)
```
Veh | Distance |  1    2    3  ... | Inside | Outside
 1  |  643.763 | 0.0  40.1  0.0 ... | 91.378 | 552.385
```

**Key Functions**:
- `tgeo_at_geom()` - Extract part INSIDE geometry
- `tpoint_minus_geom()` - Extract part OUTSIDE geometry
- `tpoint_length()` - Calculate length
- `geom_in()` - Parse WKT geometry

**Use cases**: 
- Road taxation by municipality
- Pollution analysis
- Urban planning

---

#### 12. `N07_BerlinMOD_Tile` - Grid-Based Aggregation
**Concepts**: Spatial tiling, temporal binning, 2D grids

Divides space and time into regular grids (tiles) and aggregates trips.

```bash
mvn exec:java -Dexec.mainClass="examples.N07_BerlinMOD_Tile"
```

**Two types of tiles**:

**1. Spatial tiles** (5km x 5km grid)
```
┌─────┬─────┬─────┐
│ T0  │ T1  │ T2  │
├─────┼─────┼─────┤
│ T3  │ T4  │ T5  │
└─────┴─────┴─────┘
```

**2. Value-time tiles** (10 km/h x 1 day grid)
```
Speed
 35├─────┬─────┬─────┐
   │     │     │     │
 20├─────┼─────┼─────┤
   │  T2 │  T3 │  T4 │
  0└─────┴─────┴─────┘
    1/06  2/06  3/06
```

**For each tile**: Count, Duration, Distance (spatial only)

**Key Functions**:
- `stbox_space_tiles()` - Create spatial grid
- `tfloatbox_value_time_tiles()` - Create value-time grid
- `tgeo_at_stbox()` - Clip to spatial tile
- `tnumber_at_tbox()` - Clip to value-time tile
- `tpoint_speed()` - Calculate speed

**Use cases**:
- Traffic hotspot detection
- Heatmap generation
- Pattern discovery

---

#### 13. `N08_BerlinMOD_Simplify` - Trajectory Simplification
**Concepts**: Douglas-Peucker, data compression, tolerance

Reduces trajectory complexity while preserving shape.

```bash
mvn exec:java -Dexec.mainClass="examples.N08_BerlinMOD_Simplify"
```

**Two algorithms**:
- **DP** (Douglas-Peucker): Classic geometric simplification
- **SED** (Synchronized Euclidean Distance): Preserves temporal coherence

**Example**:
```
Original:  623 instants
DP (2m):   312 instants (50% reduction)
SED (2m):  298 instants (52% reduction)
```

```
Vehicle: 1, Date: 2020-06-01, Seq: 1
  Original: 623 instants
  DP:       312 instants (49.9% reduction)
  SED:      298 instants (52.1% reduction)
```

**Key Function**:
- `temporal_simplify_dp(trip, epsilon, synchronize)`
  - `synchronize=false` &#8594; DP
  - `synchronize=true` &#8594; SED

**Use cases**:
- Data compression (50% reduction typical)
- Faster visualization
- Bandwidth reduction for transmission

---

#### 14. `N09_BerlinMOD_Aggregate` - Temporal Count
**Concepts**: Temporal aggregation, overlap analysis, time-based
statistics

Calculates how many vehicles are active simultaneously at each hour.

```bash
mvn exec:java -Dexec.mainClass="examples.N09_BerlinMOD_Aggregate"
```

**Process**:
1. Extract time periods when each trip is active
2. Group by 1-hour bins
3. Count overlapping trips per hour

**Output 1: Extent** (Bounding box)
```
STBOX X((473212,6578740),(499152,6607165)), T([2020-06-01, 2020-06-11])
```

**Output 2: Temporal Count** (Vehicles per hour)
```
[1@2020-06-01 00:00:00, 1@2020-06-01 01:00:00)
[2@2020-06-01 01:00:00, 2@2020-06-01 02:00:00)
[3@2020-06-01 02:00:00, 3@2020-06-01 03:00:00)
```

**Interpretation**: 
- 0-1h: 1 vehicle active
- 1-2h: 2 vehicles active
- 2-3h: 3 vehicles active (peak hour)

**Key Functions**:
- `tpoint_extent_transfn()` - Spatial-temporal extent
- `temporal_time()` - Extract time periods
- `tstzspanset_tprecision()` - Round to hour
- `tstzspanset_tcount_transfn()` - Count overlaps
- `temporal_tagg_finalfn()` - Finalize aggregation

**Use cases**:
- Fleet capacity planning
- Rush hour detection
- Resource allocation

---


#### 15. `N10_AIS_Assemble_Full` - Batch Processing
**Concepts**: Large-scale trajectory assembly, data validation

```bash
mvn exec:java -Dexec.mainClass="examples.N10_AIS_Assemble_Full"
```

**Process**:
1. Read CSV line by line (European date format DD/MM/YYYY)
2. Validate coordinates (Denmark: 40-84°N, -16 to 33°E)
3. Filter duplicates (same timestamp = skip)
4. Accumulate instants in ArrayList per ship
5. Build complete sequences at end
6. Calculate distance & time-weighted average SOG

**Data Validation**:
```java
LAT: 40.18° to 84.17°
LON: -16.1° to 32.88°
SOG: 0.0 to 1022.0 (0-102.2 knots)
Duplicate timestamps: Filtered
```

**Output Example**:
```
|   MMSI    |   #Rec  | #TrInst |  #SInst |     Distance    |     Speed     |
| 219000001 |   1243  |   1187  |   1198  |   134567.234567 |      8.234567 |
```

**Key Functions**:
- `geogpoint_make2d()` - Create geographic points
- `tsequence_make()` - Build sequences
- `tpoint_length()` - Calculate distance
- `tnumber_twavg()` - Time-weighted average

**Use cases**:
- Historical trajectory analysis
- Fleet statistics & reporting
- Traffic pattern analysis

---

#### 16. `N11_AIS_Expand_Full` - Incremental Building
**Concepts**: Expandable sequences, memory optimization

```bash
mvn exec:java -Dexec.mainClass="examples.N11_AIS_Expand_Full"
```

**Architecture**: Incremental Building
```
Record → Create/Append to sequence → Sequence ALWAYS ready
If the sequence needs more space → MEOS auto-expands it with memory optimization 
```

**Key Difference from N10**:
```
N10: [Inst1] [Inst2] ... [InstN] → tsequence_make() at END
N11: [I1]→[I1-I2]→[I1-I2-I3] → temporal_append_tinstant() CONTINUOUSLY
```

**Process**:
1. Read CSV (same validation as N10)
2. For FIRST instant: Create initial sequence
3. For subsequent: Append with `temporal_append_tinstant()`
4. MEOS auto-expands capacity (doubles: 64→128→256...)
5. Sequence always available for queries

**Core Function**:
```java
Pointer newSeq = temporal_append_tinstant(
    sequence, instant, 0.0, null, true);
```


**Use cases**:
- Real-time GPS tracking
- Streaming data ingestion
- Memory-constrained environments
- 24/7 continuous monitoring

---

#### 17. `N12_AIS_Transform_Full` - Coordinates Transformation
**Concepts**: Coordinate system transformation

Transform AIS coordinates from **geographic** (lat/lon) to **projected** (meters).

```bash
mvn exec:java -Dexec.mainClass="examples.N12_AIS_Transform_Full"
```

**Transformation**:
- FROM: EPSG:4326 (WGS84 - latitude/longitude in degrees)
- TO:   EPSG:25832 (ETRS89 / UTM Zone 32N - meters for Denmark)

**Why Transform?**

Geographic (EPSG:4326):
```
Copenhagen: 55.6761°N, 12.5683°E
Distance: Complex geodesic formulas
```

Projected (EPSG:25832):
```
Copenhagen: X=691,875m, Y=6,176,943m  
Distance: √((Δx)² + (Δy)²) ← Simple!
```

**Key Functions**:
```java
Pointer geog = geogpoint_make2d(4326, lon, lat);
Pointer utm = geo_transform(geog, 25832);
String ewkt = geo_as_ewkt(utm, 6);
```

**Use cases**:
- Accurate distance calculations (meters!)
- Grid-based spatial analysis
- GIS system integration
- ETL pipelines

---

#### 18. `N13_Aggregation_Demo` - SQL Aggregate Functions
**Concepts**: Aggregate transfn/finalfn pattern, union operations

Demonstrates the PostgreSQL aggregate function pattern (transition function + final function) for combining multiple temporal/spatial objects.

```bash
mvn exec:java -Dexec.mainClass="examples.N13_Aggregation_Demo"
```

**Three Aggregation Examples**:

**1. IntSpan Union** (Simple aggregation)
```
Input: [1,5], [3,8], [10,15], [12,20]
Process: Merge overlapping spans
Output: {[1,8], [10,20]}
```

**2. FloatSpanSet Grouped** (GROUP BY aggregation)
```
Input: 100 spansets, grouped by k % 10
Process: 10 accumulators (one per group)
Output: 10 FloatSpanSets
```

**3. TextSet Grouped** (Set aggregation)
```
Input: TextSets grouped by k % 10
Process: Union sets in each group
Output: 10 TextSets 
```

**Pattern Explained**:
```java
// PHASE 1: Accumulation (transfn)
Pointer state = null;
for (each row) {
    Pointer value = parse(row);
    state = transfn(state, value);  // Accumulate
}

// PHASE 2: Finalization (finalfn)
Pointer result = finalfn(state);  // Produce result
```

**Key Functions**:
- `span_union_transfn()` / `spanset_union_finalfn()`
- `spanset_union_transfn()` / `spanset_union_finalfn()`
- `set_union_transfn()` / `set_union_finalfn()`

**Use cases**:
- Merging availability time slots
- Combining room occupancy periods
- Aggregating sensor data ranges

---

#### 19. `N14_RTree_Index` - Spatial Indexing
**Concepts**: RTree spatial index, bounding box searches, performance optimization

Demonstrates RTree spatial indexing for fast spatial/temporal queries.

```bash
mvn exec:java -Dexec.mainClass="examples.N14_RTree_Index"
```

**The Problem**: Finding boxes in a region
```
Brute force: Check ALL 5,000,000 boxes → 400 ms
RTree index: Check 200,000 boxes in the specified region/bounding box → 180 ms
```

**Program Flow**:
1. **Build Index** - One-time cost
2. **Search with RTree** - Fast
3. **Search Brute Force** - Slow
4. **Validate** - Both find same 142 boxes

**Note**: RTree is not a silver bullet   
For small datasets, Brute Force can outperform the R-Tree because:
1. Initialization Cost
   - Building the index and managing native memory pointers adds a fixed overhead. 
     - For small datasets, this setup time can outpace the actual search gains, making Brute Force faster.
2. Search Threshold
   - The R-Tree only becomes profitable when the time saved by "pruning" the search space exceeds the time spent traversing the tree structure.

Rule of thumb: Use R-Trees for large-scale spatial datasets (like the 5M boxes in the program) or when making frequent, repeated queries on the same data.

**Key Functions**:
```java
Pointer rtree = rtree_create_stbox();
rtree_insert(rtree, box, id);
Pointer ids = rtree_search(rtree, query, countPtr);
```

**Use cases**:
- Maritime traffic queries
- Event detection in regions

---

#### 20. `N15_TPoint_MakeCoords` - Coordinate Arrays Construction
**Concepts**: Alternative construction, coordinate arrays

Demonstrates building temporal point sequences from coordinate arrays instead of individual instants.

```bash
mvn exec:java -Dexec.mainClass="examples.N15_TPoint_MakeCoords"
```

Pass arrays directly to create your sequence of TPoints without
having to manually instantiate each one of them manually and then assembling them into your final sequence
```java
double[] x = {2.349, 2.350, 2.351};
double[] y = {48.853, 48.854, 48.855};
double[] z = {10.5, 12.3, 11.8};

// Efficient: One single call for the entire sequence
Pointer seq = tpointseq_make_coords(xPtr, yPtr, zPtr, timesPtr, ...);
```

**Use cases**:
- GPS logger data (CSV format)
- Data conversion (GPS/CSV → MEOS)

---

#### 21. `N16_Clustering_KMeans` - K-means Clustering
**Concepts**: K-means algorithm, centroid-based clustering

Groups geographic points into K clusters based on proximity.

```bash
mvn exec:java -Dexec.mainClass="examples.N16_Clustering_KMeans"
```

**Input**: `popplaces.csv` (30 cities)
**Output**: Same + cluster column (0-9)

**Algorithm** (K=10):
1. Choose 10 initial centers
2. Assign each city to nearest center
3. Recalculate centers
4. Repeat until stable

**Key Functions**:
```java
Pointer geo_cluster_kmeans(geometries, count, k)
```

**Use cases**:
- Delivery zones
- Service areas

---

#### 22. `N17_Clustering_Topological` - Topological Clustering
**Concepts**: Clustering by spatial relationships, automatic K

Groups geometries based on spatial relationships (touching/proximity).

```bash
mvn exec:java -Dexec.mainClass="examples.N17_Clustering_Topological"
```

**Input**: `regions.csv`
**Output**: `regions_new.csv` with clusters

**Two Methods**:
1. **ClusterIntersecting** - Groups that touch/overlap
2. **ClusterWithin(1000m)** - Groups within distance

**Key Difference**: Number of clusters emerges from data (not fixed K)

**Key Functions**:
```java
geo_cluster_intersecting(geometries, count, numClustersPtr);
geo_cluster_within(geometries, count, distance, numClustersPtr);
```

**Use cases**:
- Road networks (connected components)
- Land parcels (adjacency)
- Building blocks

---

#### 23. `N18_Clustering_DBSCAN` - Density-Based Clustering
**Concepts**: DBSCAN algorithm, density clustering, outlier detection

Finds clusters based on density and identifies isolated points as noise.

```bash
mvn exec:java -Dexec.mainClass="examples.N18_Clustering_DBSCAN"
```

**Input**: `US.txt` (geonames schools)
**Output**: `geonames_new.csv` with clusters

**Parameters**:
- eps: 2000 meters (neighbor distance)
- minpoints: 5 (minimum density required for a point to be considered as a "CORE" one)

**Point Types**:
- **CORE**: ≥5 neighbors → Forms cluster
- **BORDER**: Near core → In cluster
- **NOISE**: Isolated → Outlier

**Key Functions**:
```java
geo_cluster_dbscan(geometries, count, eps, minpoints, clusters)
```

**Advantages**:
- Automatic cluster count
- Arbitrary shapes
- Identifies outliers

**Use cases**:
- Urban planning (underserved areas)
- Hot spot detection
- Service gap analysis

---

#### 24. `N19_ParisTrajectoryStaticMaps` — Animated Map Video (Static Trajectory)
**Concepts**: Data Visualization, OpenStreetMap tile download, video/.mov file

Generates an animated `.mov` video showing a predefined trajectory (Paris ring road)
overlaid on an OpenStreetMap background. The map is fixed; only the moving point and its
trail are redrawn frame by frame.

```bash
mvn exec:java -Dexec.mainClass="examples.N19_ParisTrajectoryStaticMaps"
```

**Output**: `paris_trajectory.mov`

**Structure**
1. Define a list of `Coordinate(lat, lon)` points
2. Compute the bounding box of the trajectory → choose optimal zoom level and map centre
3. Download and assemble OSM tiles into a single base-map image
4. For each frame: copy the base map, draw the fading trail + current red dot + overlay
5. Encode all frames into a `.mov` file at 7 FPS using JCodec

**Trail rendering**: the last 15 positions are drawn as blue dots with increasing size and
opacity (older = smaller and more transparent), then connected by a blue line.

**OSM tile URL format**: `https://tile.openstreetmap.org/{zoom}/{x}/{y}.png`
A `User-Agent` header is mandatory, otherwise OSM returns a 403.

**Use cases**:
- Visualising GPS/trajectory dataset
- Route animation for presentations
- Debugging trajectory geometry

---

#### 25. `N20_AISTrajectoryWithTimestamp` — Animated Map Video from MobilityDB
**Concepts**: MobilityDB query & data visualisation, MEOS temporal decomposition, .mov file

Extends N19 with an important addition: trajectory data is pulled live from a
**MobilityDB** database instead of being hardcoded

```bash
mvn exec:java -Dexec.mainClass="examples.N20_AISTrajectoryWithTimestamp"
```

**Output**: `ais_trajectory.mov`

**Prerequisites**: A running MobilityDB instance with the `AISTrips` table populated
(see N04_AIS_Store). On Windows/macOS with Docker Desktop, the JDBC URL is:
```
jdbc:postgresql://host.docker.internal:5432/postgres?user=postgres&password=postgres
```
On Linux, replace `host.docker.internal` with the container's IP or use a Docker network
(see the Troubleshooting section in this guide or the comments in the N04_AIS_Store program).

**Structure**
1. Connect to MobilityDB and fetch one ship's trip: `SELECT trip::text FROM AISTrips WHERE MMSI = ?`
2. Parse with `tgeogpoint_in(wkt)` → MEOS `Temporal*` pointer
3. Decompose into instants with `temporal_instants(ptr, countPtr)`
4. For each instant: extract timestamp via `temporal_start_timestamptz` and coordinates via `temporal_start_value`
5. Compute optimal view (same adaptive zoom as N19)
6. Render video frame by frame (same trail + red dot as N19)
7. Display real timestamp in the bottom-right overlay

**Key MEOS functions used**:
```java
Pointer tempPtr = tgeogpoint_in(wkt);                          // parse trip WKT
Pointer instants = temporal_instants(tempPtr, countPtr);       // decompose to instants
OffsetDateTime t = temporal_start_timestamptz(instantPtr);     // extract timestamp
Pointer geo     = temporal_start_value(instantPtr);            // extract geometry
Point  point    = ConversionUtils.gserialized_to_shapely_point(geo, 15);
```

**Use cases**:
- Visualising real ship trajectories stored in MobilityDB
- Temporal animations for AIS data analysis
- Debugging temporal data stored in the database

---


## Data Files

All data files are in `src/main/java/examples/data/`:

### AIS Dataset (Ship Tracking)
- `ais_instants.csv` - 50K+ ship observations (5 ships, ~24 hours)
- Format: `T,MMSI,Latitude,Longitude,SOG`
- Coordinate system: EPSG:4326 (WGS84 lat/lon)

### Full-Scale AIS Dataset (Danish Maritime Authority)
- Download from: http://aisdata.ais.dk/
- Format: `Timestamp,Type,MMSI,Latitude,Longitude,NavStatus,ROT,SOG,...`
- Date format: **DD/MM/YYYY HH:MM:SS** (European)
- Coordinate system: EPSG:4326 (WGS84 lat/lon)

### BerlinMOD Dataset (Vehicle Tracking)
- `berlinmod_instants.csv` - 89K observations (5 vehicles, 11 days)
- `berlinmod_trips.csv` - 154 trips in HexWKB format
- `brussels_communes.csv` - 19 Brussels municipalities
- `brussels_region.csv` - Brussels boundary
- `regions.csv` - BerlinMOD regions (for topological clustering)
- Coordinate system: EPSG:3857 (Web Mercator meters)

### Clustering Datasets
- `popplaces.csv` - 30 populated places worldwide (for K-means)
    - Format: `name,pop_max,geom`
    - Natural Earth data: https://www.naturalearthdata.com/
    - Coordinate system: EPSG:4326 (WGS84)

### Geonames Dataset (US Schools)
- `US.txt` - Full geonames dump for USA (for DBSCAN)
    - Download from: https://download.geonames.org/export/dump/US.zip
    - Format: TSV with 19+ fields
    - Used fields: `geonameid,name,admin1,lat,lon,fcode`
    - Filter: `fcode='SCH'` (schools only)
    - Size: ~2.5M records (~500MB uncompressed)

### Aggregation Test Data
- `intspans.csv` - 10 integer spans
- `floatspansets.csv` - 100 float span sets
- `textsets.csv` - 100 text sets

---

## Common Functions

### Initialization
```java
meos_initialize_timezone("UTC");
meos_initialize_error_handler(errorHandler);
// ... your code ...
meos_finalize();
```

### Creating Temporal Points
```java
// Geographic (lat/lon)
Pointer gs = geogpoint_make2d(4326, longitude, latitude);
Pointer inst = tpointinst_make(gs, timestamp);

// Geometric (planar)
Pointer gs = geom_in("POINT(x y)", -1);
Pointer inst = tpointinst_make(gs, timestamp);

// Sequence from instants
Pointer seq = tsequence_make(instantsArray, count, 
    lowerInc, upperInc, interpolation, normalize);
```

### Expandable Sequences (Streaming)
```java
// Create initial sequence with first instant
Pointer seq = tsequence_make(instArray, 1, 
    true, true, TInterpolation.LINEAR.getValue(), true);

// Append subsequent instants (auto-expands!)
Pointer newSeq = temporal_append_tinstant(
    seq,        // Current sequence
    instant,    // New instant to add
    0.0,        // maxdist (0 = no spatial gap limit)
    null,       // maxt (null = no time gap limit)
    true        // expand (auto-expand capacity)
);

// Update pointer
seq = newSeq;

// Get instant count
int count = temporal_num_instants(seq);

// Extract instant by index (1-indexed!)
Pointer inst = temporal_instant_n(seq, index);
```

**When to use**:
- Streaming scenarios (N04_AIS_Stream_DB, N04_AIS_Stream_File)
- Unknown final size
- Memory-efficient incremental building needed

**Comparison**:

| Approach | When to Use |
|----------|-------------|
| `tsequence_make()` | All instants available upfront |
| `temporal_append_tinstant()` | Streaming/incremental build |


### Parsing
```java
// Timestamp
OffsetDateTime t = pg_timestamptz_in("2020-06-01 08:30:00+00", -1);

// Date
int d = pg_date_in("2020-06-01");

// Geometry
Pointer geom = geom_in("SRID=3857;POINT(500000 6600000)", -1);

```

### Metrics
```java
// Length (meters)
double length = tpoint_length(trip);

// Speed
Pointer speed = tpoint_speed(trip);

// Duration
Pointer duration = temporal_duration(trip, boundspan);

// Number of instants
int count = temporal_num_instants(trip);
```

### Spatial Operations
```java
// Clip inside geometry
Pointer inside = tgeo_at_geom(trip, geometry);

// Clip outside geometry
Pointer outside = tpoint_minus_geom(trip, geometry, zspan);

// Distance between trajectories
double dist = tpoint_distance(trip1, trip2);
```

### Coordinate Transformation
```java
// Transform to different CRS
Pointer geog = geogpoint_make2d(4326, lon, lat);
Pointer transformed = point_transform(geog, 25832);

// Get coordinates as EWKT (Extended Well-Known Text)
String ewkt = geo_as_ewkt(transformed, 6);  // 6 decimal places
// → "SRID=25832;POINT(691875.234567 6176943.876543)"

// Get coordinates as WKB (Well-Known Binary - hex)
String wkb = geo_out(transformed);
// → "0101000020E8640000..." (binary format)
```

## Troubleshooting

### ClassNotFoundException
```bash
# ❌ Wrong
mvn exec:java -Dexec.mainClass="AIS_Read"

# ✅ Correct (include package)
mvn exec:java -Dexec.mainClass="examples.N02_AIS_Read"
```


### Database Connection (Linux + Docker)
```bash
# Create docker network
docker network create mobilitydb-network

# Run MobilityDB on network
docker run --name postgres-mobilitydb \
  --network mobilitydb-network \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d mobilitydb/mobilitydb

# Update JDBC URL in code to:
jdbc:postgresql://postgres-mobilitydb:5432/postgres?user=postgres
password=postgres
```


## Additional Resources

- **MEOS Documentation**: https://libmeos.org/
- **MobilityDB**: https://mobilitydb.com/
- **AIS Data**: 
  - Marine vessel tracking system
  - https://coast.noaa.gov/htdata/CMSP/AISDataHandler/2020/index.html
- **BerlinMOD**: 
  - Benchmark for moving object databases
  - https://github.com/MobilityDB/MobilityDB-BerlinMOD
