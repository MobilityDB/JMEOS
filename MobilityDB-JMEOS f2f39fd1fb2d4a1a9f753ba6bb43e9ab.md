# MobilityDB-JMEOS

# Contents

## **Requirements**

The project is based on MEOS and developed in Java

- 🚀 MobilityDB with MEOS
- 📝 Maven 3.9.6
- ☕ Java 21

## **Dependencies**

The following dependencies are obtained through Maven and are necessary to develop JMEOS.

- 🔗 JNR-FFI
- 🛠️ Maven Plugin
- ✅ JUnit
- 🌍 Jts Core

Every dependency with their version is in the pom.xml file, so to make changes to the dependencies, you need to change the pom.xml file

## Installation of specified JAVA and Maven versions

### Maven 3.9.6 installation

- **Download Maven 3.9.6**:
    
    ```bash
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
    ```
    
- **Extract the downloaded archive**:
    
    ```bash
    tar -xvzf apache-maven-3.9.6-bin.tar.gz
    ```
    
- **Move the extracted directory to `/opt`**:
    
    ```bash
    sudo mv apache-maven-3.9.6 /opt/
    ```
    
- **Create a symbolic link**:
    
    ```bash
    sudo ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn
    ```
    
- **Add Maven to the PATH**:
    
    ```bash
    echo "export M2_HOME=/opt/apache-maven-3.9.6" >> ~/.bashrc
    echo "export PATH=\$M2_HOME/bin:\$PATH" >> ~/.bashrc
    source ~/.bashrc
    ```
    

### JAVA 21 installation

Using `wget` to download from OpenJDK:

1. **Open a terminal** and navigate to the directory where you want to download Java 21:
    
    ```bash
    cd /tmp
    ```
    
2. **Download the tarball**:
    
    ```bash
    wget https://download.java.net/java/GA/jdk21/0c1b5ad9a42e44b58cc1e4a15f6f2e6f/34/GPL/openjdk-21_linux-x64_bin.tar.gz
    ```
    

Extract the tarball

Extract the downloaded tarball to the `/opt` directory or any other directory you choose.

```bash
sudo tar -xvzf openjdk-21_linux-x64_bin.tar.gz -C /opt/
```

This will extract the contents into a directory like `/opt/jdk-21`

Set up JAVA_HOME and PATH

1. **Open bash file**:
    
    ```bash
    nano ~/.bashrc
    ```
    
2. **Add the following lines to set `JAVA_HOME` and update `PATH`** (adjust the path to match your extracted directory):
    
    ```bash
    export JAVA_HOME=/opt/jdk-21
    export PATH=$JAVA_HOME/bin:$PATH
    ```
    
3. **Save the file and apply the changes**:
    
    ```bash
    source ~/.bashrc
    ```
    

Verify the Installation

1. **Check the Java version**:
    
    ```bash
    java -version
    ```
    
    This should display something like:
    
    ```java
    javaCopy code
    openjdk version "21" 2023-09-19
    OpenJDK Runtime Environment (build 21+35-2514)
    OpenJDK 64-Bit Server VM (build 21+35-2514, mixed mode, sharing)
    
    ```
    
2. **Check `JAVA_HOME`**:
    
    ```bash
    echo $JAVA_HOME
    ```
    
    This should output `/opt/jdk-21`.
    
3. **Check `PATH`**:
    
    ```bash
    echo $PATH
    ```
    
    Ensure that `/opt/jdk-21/bin` is included in the output.
    

## Installation of vanilla Mobility DB

Installation of Java and Maven will not be detailed here since many tutorials exists online. The installation of MobilityDB with MEOS needs to follow these subsequent commands:

```bash
#Install MobilityDB with MEOS
git clone https://github.com/MobilityDB/MobilityDB
mkdir MobilityDB/build
cd MobilityDB/build
cmake -DMEOS=on ..
make
sudo make install
```

Now install the dependencies in the pom.xml file using 

```bash
make clean install 
```

Then generate the javadoc using 

```bash
mvn javadoc:javadoc
```

## Unit test

Multiple unit tests were implemented and are located under the **test** folder. The folder is structured similarly to the source file, as enforced by Java/IntelliJ rules. The following command allows you to run all tests at once:

```bash
mvn test
```

One can prefer running only one file (class):

```bash
mvn test -Dtest="FileTest"
```

It is also possible to run only one method of a class:

```bash
mvn test -Dtest="FileTest#method"
```

## GENERATING BINDINGS

1. Manually- Not possible as the file is huge 
2. Using JAVA- create functions in Java to extract datatypes and function signatures (already used in this project).
3. Using SWIG- Automatic tool for binding generation but hard to interpret and integrate for large pieces of code.
4. Using Python- Using clang library and writing a script to parse the c header file and extract the functions and datatypes from the meos.h file automatically.

## IMPORTANT

Use the meos.h and [libmeos.so](http://libmeos.so) files from the tag v1.1.0-beta1 to make the unit tests work on JAVA. If you don't use this then you will not get the tests passed. The tests failining are related to TGeompoint and TFloat due the truncation of the float datatypes. It think it can be fixed by using Double instead of float.

## PROBLEMS IN CONVERSIONS

1. LWPROJ* pj -FIX→ Pointer pj
2. Pointer* spacy_buckets -FIX→ PointerByReference spacy_buckets
3. … representing any number of arguments -FIX→ Object... args

## TYPE CONVERSIONS

1. Initial conversions from c to Java 

```java
line = line.replaceAll("extern ", "");
line = line.replaceAll("const ", "");
line = line.replaceAll("static inline ", "");

/* Changing types with * */
line = line.replaceAll("char\\s?\\*\\*", "**char ");
line = line.replaceAll("char\\s?\\*", "*char ");
line = line.replaceAll("\\w+\\s?(\\*+)", "* "); // ex- Temporal *temporal_scale_time, Temporal **tfloatarr_round, GSERIALIZED ***space_buckets 
line = line.replaceAll("\\s\\.\\.\\.", " * args"); // ex- void meos_error(int errlevel, int errcode, String format, ...)

/* Changing special types or names */
line = line.replaceAll("\\(void\\)", "()"); 
line = line.replaceAll("synchronized", "synchronize");
```

1. Conversions after step 1 to jnr-ffi types 

```java
HashMap<String, String> types = new HashMap<>();
types.put("\\*", "Pointer");
types.put("\\*char", "String");
types.put("\\*\\*char", "Pointer");
types.put("Pointer\\[\\]", "Pointer"); 
types.put("float", "float");
types.put("double", "double");
types.put("void", "void");
types.put("int", "int");
types.put("short", "short");
types.put("long", "long");
types.put("int8", "byte");
types.put("int16", "short");
types.put("int32", "int");
types.put("int64", "long");
types.put("int8_t", "byte");
types.put("int16_t", "short");
types.put("int32_t", "int");
types.put("int64_t", "long");
types.put("uint8", "byte");
types.put("uint16", "short");
types.put("uint32", "int");
types.put("uint64", "long");
types.put("uint8_t", "byte");
types.put("uint16_t", "short");
types.put("uint32_t", "int");
types.put("uint64_t", "long");
types.put("uintptr_t", "long");
types.put("size_t", "long");
types.put("interType", "int");
```

## CLASSES CHANGED

1. Span, SpanSet, Set-

There is a difficulty in changing the distance function as in the base classes it only throws an exception but in the child classes, it has to calculate the distance. Hence, there is a return-type conflict between the

1. Type chnages in **types.collections.base**-
- [x]  Set
- [x]  Span
- [x]  SpanSet

Interfaces changed-

- [x]  Base
- [x]  Collection

1. Type Changes in **types.collections.numbers**-
- [x]  FloatSpanSet
- [x]  IntSpanSet
- [x]  IntSpan
- [x]  FloatSpan
- [x]  IntSet
- [x]  FloatSet

Interfaces changed-

- [x]  Number

1. Type of changes in **types.collections.time**-
- [x]  TimeStampset → tstzspan
- [x]  PeriodSet → tstzspanset
- [x]  Period → tstzset

Extra types added-

- [ ]  dateset
- [ ]  datespan
- [ ]  datespanset

Interfaces added-

- [x]  Time
- [x]  TimeCollection

1. Type changes in **types.temporal**
- [ ]  Factory
- [x]  Temporal
- [x]  TInstant
- [x]  TSequence
- [x]  Tsequenceset

Enum type changes-

- [x]  TemporalType
- [x]  TInterpolation → interpolation

1. Type changes in **types.boxes**
- [ ]  STBox
- [ ]  TBox

Interfaces changed

- [ ]  Box

## UNIT TESTS

1. meos_initilize and meos_finalize functions in the [functions.java](http://functions.java) file need to be called or else it will give errors, since the library is not initialized you can’t use any datatype or functions mentioned in the meos library connected to java using the java wrapper.
2. List of Test modules for **Types.collections.numbers**
- [x]  IntSpanTest
- [x]  FloatSpanTest
- [x]  IntSetTest
- [x]  FloatSetTest
- [x]  IntSpanSetTest
- [x]  FloatSpanSetTest