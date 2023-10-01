JMEOS
=====

A Java driver of the MEOS library.

<img src="https://github.com/MobilityDB/MobilityDB/blob/master/doc/images/mobilitydb-logo.svg" width="200" alt="MobilityDB Logo" />

MobilityDB is developed by the Computer & Decision Engineering Department of the [Université libre de Bruxelles](https://www.ulb.be/) (ULB) under the direction of [Prof. Esteban Zimányi](http://cs.ulb.ac.be/members/esteban/). ULB is an OGC Associate Member and member of the OGC Moving Feature Standard Working Group ([MF-SWG](https://www.ogc.org/projects/groups/movfeatswg)).

<img src="https://github.com/MobilityDB/MobilityDB/blob/master/doc/images/OGC_Associate_Member_3DR.png" width="100" alt="OGC Associate Member Logo" />

More information about MobilityDB, including publications, presentations, etc., can be found in the MobilityDB [website](https://mobilitydb.com).


## Dependencies:

### 1. MEOS library
It needs to be installed from the **develop** branch of MobilityDB with the flag **-DMEOS=ON** while cmaking the repository.

### 2. MAVEN
This project uses IntelliJ development environment with Maven project management tool.

### 3. JNR-FFI
Java Native Runtime Foreign Function Interface used to wrap the C functions obtained from **MEOS** library. JNR-FFI is directly installed through Maven in the **Pom.xml** file.
