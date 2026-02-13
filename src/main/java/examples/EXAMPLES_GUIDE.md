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
cd .\Docker-JMEOS\.devcontainer\
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


#### 1. `Hello_World` - Introduction to Temporal Types
**Concepts**: Temporal instant, sequence, sequence set, interpolation

Creates and displays temporal geometric points with different
interpolations:
- **Instant**: Single point at one timestamp
- **Discrete Sequence**: Unconnected points
- **Linear Sequence**: Points connected by straight lines
- **Step Sequence**: Points connected by steps (constant value)
- **Sequence Set**: Multiple sequences

```bash
mvn exec:java -Dexec.mainClass="examples.Hello_World"
```

**Output**: WKT and MF-JSON representations of temporal types

**Key Functions**:
- `TGeomPointInst()` - Create temporal instant
- `TGeomPointSeq()` - Create temporal sequence
- `as_mfjson()` - Convert to Moving Features JSON

---

#### 2. `Hello_World_Geodetic` - Geographic Coordinates
**Concepts**: Geographic vs geometric coordinates, EPSG:4326

Same as Hello_World but uses **geodetic coordinates** (latitude
longitude on Earth's surface) instead of planar coordinates.

```bash
mvn exec:java -Dexec.mainClass="examples.Hello_World_Geodetic"
```

**Difference from Hello_World**:
- Uses `TGeogPoint` instead of `TGeomPoint`
- Coordinates in EPSG:4326 (WGS84)
- Distances measured on Earth's surface (geodesic)

---


#### 3. `AIS_Read` - Parse CSV Data
**Concepts**: Reading CSV, creating temporal instants, coordinate systems

Reads AIS (Automatic Identification System) ship tracking data from CSV
and creates temporal point instants.

**Input**: `ais_instants.csv` (50K+ ship observations)
```csv
T,MMSI,Latitude,Longitude,SOG
2009-06-01 00:01:11+00,228041600,39.84917,-3.55917,11.7
```

```bash
mvn exec:java -Dexec.mainClass="examples.AIS_Read"
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

#### 4. `AIS_Assemble` - Build Trajectories
**Concepts**: Aggregating instants, constructing sequences, distance
calculation

Assembles individual observations into complete ship trajectories.

**Input**: `ais_instants.csv` &#8594; **Output**:
`ais_trips_new_assemble.csv`

```bash
mvn exec:java -Dexec.mainClass="examples.AIS_Assemble"
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

#### 5. `BerlinMOD_Assemble` - Vehicle Trip Assembly
**Concepts**: Synthetic trajectory data, HexWKB encoding

Similar to AIS_Assemble but for synthetic vehicle data in Brussels.

**Input**: `berlinmod_instants.csv` (89K observations from 5 vehicles)
**Output**: `berlinmod_trips_new_assemble.csv`

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Assemble"
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


#### 6. `AIS_Store` - Write to MobilityDB
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
mvn exec:java -Dexec.mainClass="examples.AIS_Store"
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

#### 7. `AIS_Stream_DB` - Streaming to Database
**Concepts**: Memory-efficient streaming, incremental updates

Processes large AIS datasets by streaming to database instead of holding
everything in memory.

```bash
mvn exec:java -Dexec.mainClass="examples.AIS_Stream_DB"
```

**Strategy**:
1. Accumulate 1000 instants per ship in memory
2. Build sequence and INSERT/UPDATE in database
3. Keep last 2 instants for continuity
4. Clear memory and continue

**Key Function**:
- `update()` - MobilityDB function to merge temporal values

**Advantage**: Can process datasets larger than available RAM

---

#### 8. `AIS_Stream_File` - Streaming to File
**Concepts**: File streaming, memory management

Same concept as AIS_Stream_DB but writes to CSV file instead.

```bash
mvn exec:java -Dexec.mainClass="examples.AIS_Stream_File"
```

**Output**: `ais_trips_new_stream.csv`

**Use case**: When database is unavailable or file export is needed

---


#### 9. `BerlinMOD_Disassemble` - Extract Observations
**Concepts**: Temporal decomposition, sorting, coordinate reference
systems

**Reverse** of assembly: Takes complete trips and extracts individual
observations.

**Input**: `berlinmod_trips.csv` (154 trips in HexWKB)
**Output**: `berlinmod_instants_disassemble.csv` (89,091 sorted
observations)

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Disassemble"
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

#### 10. `BerlinMOD_Clip` - Spatial Analysis
**Concepts**: Spatial clipping, geometric operations, administrative
boundaries

Analyzes how much distance vehicles travel in each Brussels commune
(municipality).

**Input Files**:
- `brussels_communes.csv` - 19 communes with geometries
- `brussels_region.csv` - Brussels boundary (union of communes)
- `berlinmod_trips.csv` - 154 vehicle trips

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Clip"
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

#### 11. `BerlinMOD_Tile` - Grid-Based Aggregation
**Concepts**: Spatial tiling, temporal binning, 2D grids

Divides space and time into regular grids (tiles) and aggregates trips.

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Tile"
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

#### 12. `BerlinMOD_Simplify` - Trajectory Simplification
**Concepts**: Douglas-Peucker, data compression, tolerance

Reduces trajectory complexity while preserving shape.

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Simplify"
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

#### 13. `BerlinMOD_Aggregate` - Temporal Count
**Concepts**: Temporal aggregation, overlap analysis, time-based
statistics

Calculates how many vehicles are active simultaneously at each hour.

```bash
mvn exec:java -Dexec.mainClass="examples.BerlinMOD_Aggregate"
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

## Data Files

All data files are in `src/main/java/examples/data/`:

### AIS Dataset (Ship Tracking)
- `ais_instants.csv` - 50K+ ship observations (5 ships, ~24 hours)
- Format: `T,MMSI,Latitude,Longitude,SOG`
- Coordinate system: EPSG:4326 (WGS84 lat/lon)

### BerlinMOD Dataset (Vehicle Tracking)
- `berlinmod_instants.csv` - 89K observations (5 vehicles, 11 days)
- `berlinmod_trips.csv` - 154 trips in HexWKB format
- `brussels_communes.csv` - 19 Brussels municipalities
- `brussels_region.csv` - Brussels boundary
- Coordinate system: EPSG:3857 (Web Mercator meters)


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

## Troubleshooting

### ClassNotFoundException
```bash
# ❌ Wrong
mvn exec:java -Dexec.mainClass="AIS_Read"

# ✅ Correct (include package)
mvn exec:java -Dexec.mainClass="examples.AIS_Read"
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
