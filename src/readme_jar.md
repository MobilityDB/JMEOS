# Readme to generate the jar file and integrate it in an other directory - IntelliJ IDE 2023.1
## A. Generation (Already done for you)
### 1. Libmeos.so
  Add libmeos.so file, containing the basic meos library, into the libs directory. For simplicity, it was already done in this project.
### 2. Pom.xml
  Add manually the libmeos plugin into the Maven dependency through the pom.xml file **<build>**. Again for simplicity, it was already done for you.
### 3. Installation
  To install it, open the terminal where the **pom.xml** is located and run the command: 
  ```console
  example@john:~$ mvn install
  ```
  A jar file will be generated into the target folder of the project.

### 4. Verification
  You can verify the installation of the plugin by running the following command: 
   ```console
  example@john:~$ mvn verify
  ```

## B. Integration

### 1. Copy of the Jar
  Copy the generated **.jar** file into the new project repository you desire. It is recommended to put it into a specific directory.

### 2. Add dependency
  Open the **pom.xml** file (if using Maven) or a the **build.gradle** (if using Gradle) and add the **jmeos** dependency. For Maven: 
  ```
  <dependency>
    <groupId>org.jmeos</groupId>
    <artifactId>meos</artifactId>
    <version>1.0</version>
  </dependency>
  ```

### 3. Install the dependency

  Install the dependency into your system by opening the terminal and execute the following command line:
  ```console
  example@john:~$ mvn install:install-file -Dfile=$path_to_jar_file DgroupId=org.jmeos -DartifactId=meos -Dversion=1.0 -Dpackaging=jar
  ```
  where **$path_to_jar_file** is the absolute path to the **.jar** file previously generated and placed.

### 4. Add other dependencies

  Since JMEOS has some dependecies, you also need to add them into the associated file (pom.xml for Maven). Example for **JNR-FFI:**
  ```
  <dependency>
    <groupId>com.github.jnr</groupId>
    <artifactId>jnr-ffi</artifactId>
    <version>2.2.11</version>
  </dependency>
  ```
  
