# Readme to generate the jar file and integrate it in an other directory - IntelliJ IDE 2023.1
## A. Generation (Already done for you)
### 1. Libmeos.so
  Add libmeos.so file, containing the basic meos library, into the lib directory. For simplicity, it was already done in this project.
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
  Copy the generated **src/jar/JMEOS.jar** file into the new project repository you desire. It is recommended to put it into a specific directory (ex: src/jar).

### 2. Add dependency
  Open the **pom.xml** file (if using Maven) or a the **build.gradle** (if using Gradle) and add the **jmeos** dependency. For Maven: 
  ```
  <dependency>
    <groupId>org.jmeos</groupId>
    <artifactId>jmeos</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

### 3. Install the dependency

  Install the dependency into your system by opening the terminal and execute the following command line:

_Linux_:
  ```console
  example@john:~$ mvn install:install-file -Dfile=$path_to_jar_file/JMEOS.jar -DgroupId=org.jmeos -DartifactId=jmeos -Dversion=1.0-SNAPSHOT -Dpackaging=jar
  ```

_Windows_:
  ```cmd
  example@john:~$ mvn install:install-file -Dfile="$path_to_jar_file/JMEOS.jar" -DgroupId="org.jmeos" -DartifactId=jmeos -Dversion="1.0-SNAPSHOT" -Dpackaging=jar
  ```

  where **$path_to_jar_file** is the absolute path to the **.jar** file previously generated and placed.

### 4. Add other dependencies

As JMEOS uses other dependencies *you should normally* add them but for simplicity **the dependencies have been added** with the JAR.

You just need to refresh Maven with the ***"Reload all Maven Projects"*** button, and you should see all the dependencies appear in the ***"External Libraries"*** of your project.
  
