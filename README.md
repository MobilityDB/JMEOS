# MobilityDB-JMEOS
Java MEOS library for MobilityDB


## Dependencies:

### 1. MEOS library
It needs to be installed from the **develop** branch of MobilityDB with the flag **-DMEOS=ON** while cmaking the repository.

### 2. MAVEN
This project uses IntelliJ development environment with Maven project management tool.

### 3. JNR-FFI
Java Native Runtime Foreign Function Interface used to wrap the C functions obtained from **MEOS** library. JNR-FFI is directly installed through Maven in the **Pom.xml** file.
