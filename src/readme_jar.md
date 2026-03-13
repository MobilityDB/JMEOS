# Readme to generate the jar file and integrate it in an other directory - IntelliJ IDE 2023.1
## A. Generation

### 1. JAR (already done for you)
  You can find it in the /jar directory, but you can run this command if you'd like to generate a new one:
   ```console
  example@john:~$ mvn install
  ```

### 2. Uber JAR
  You'll sometimes need a Fat/Uber JAR bundling all the dependencies JMEOS needs internally (such as jnr.ffi) so
  you don't have to manage them yourself (e.g. when creating a Jupyter Notebook using JMEOS).
  This command will generate a Fat Jar in the /jar directory alongside the classic Jar  
```console
  example@john:~$ mvn package -DskipTests
  ```

  Here's an article explaining everything about Fat/Uber, Thin, Skinny and Hollow JARs: https://dzone.com/articles/the-skinny-on-fat-thin-hollow-and-uber
 
  In the end, the structure of your personal project should look like this:  
    my-project/   
    ├── Dockerfile  
    ├── pom.xml  
    ├── lib/  
    │      └── JMEOS-fat.jar  
    └── src/main/java/org/example/Main.java

## B. Integration

### 1. Copy of the Jar
  Copy the generated **src/jar/JMEOS-fat.jar** file into the new project repository you desire. It is recommended to put it into a specific directory (ex: src/lib).

### 2. Add dependency
  Open the **pom.xml** file (if using Maven) or a the **build.gradle** (if using Gradle) and add the **jmeos** dependency.   
  For Maven: 
  ```
  <dependency>
    <groupId>org.jmeos</groupId>
    <artifactId>jmeos</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

### 3. Install the dependency

Since JMEOS is not available on Maven Central, you need to register it in your local Maven repository so that the dependency
can be resolved:  
_Linux_:
  ```console
  example@john:~$ mvn install:install-file -Dfile=$path_to_jar_file/JMEOS-fat.jar -DgroupId=org.jmeos -DartifactId=jmeos -Dversion=1.0-SNAPSHOT -Dpackaging=jar
  ```

_Windows_:
  ```cmd
  example@john:~$ mvn install:install-file -Dfile="$absolute_path_to_jar_file/JMEOS-fat.jar" -DgroupId="org.jmeos" -DartifactId=jmeos -Dversion="1.0-SNAPSHOT" -Dpackaging=jar
  ```

  where **$path_to_jar_file** is the absolute path to the **.jar** file previously copied and placed in your project.

You just need to refresh Maven with the ***"Reload all Maven Projects"*** button, and you should see all the dependencies appear in the ***"External Libraries"*** of your project.
  
## C. Running

On Windows, your project using JMEOS may not run well using the IDE. Therefore, using Docker will simplify the process.

Here's an example for a Dockerfile for a basic fresh project with no other dependencies:

```cmd
FROM debian:bookworm-slim

RUN apt-get update \
  && apt-get install -y git curl gnupg build-essential cmake \
     postgresql-server-dev-15 libproj-dev libjson-c-dev libgsl-dev libgeos-dev postgis \
  && export GNUPGHOME="$(mktemp -d)" \
  && curl -fL https://apt.corretto.aws/corretto.key | gpg --batch --import \
  && gpg --batch --export '6DC3636DAE534049C8B94623A122542AB04F24E3' > /usr/share/keyrings/corretto.gpg \
  && rm -r "$GNUPGHOME" \
  && unset GNUPGHOME \
  && echo "deb [signed-by=/usr/share/keyrings/corretto.gpg] https://apt.corretto.aws stable main" > /etc/apt/sources.list.d/corretto.list \
  && apt-get update \
  && apt-get install -y java-21-amazon-corretto-jdk

# Compile MobilityDB with MEOS (generates libmeos.so)
RUN git clone https://github.com/MobilityDB/MobilityDB.git -b stable-1.3 /usr/local/src/MobilityDB
RUN mkdir -p /usr/local/src/MobilityDB/build
RUN cd /usr/local/src/MobilityDB/build && \
    cmake -DMEOS=ON .. && \
    make -j$(nproc) && \
    make install && \
    ldconfig

# Maven
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG="/root/.m2"
COPY --from=maven:3.9.6-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.6-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.6-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# Install JMEOS in the local Maven repository
# (edit the JMEOS.jar path if needed at the root of your project)
COPY lib/JMEOS-fat.jar /tmp/JMEOS-fat.jar
RUN mvn install:install-file \
    -Dfile=/tmp/JMEOS-fat.jar \
    -DgroupId=org.jmeos \
    -DartifactId=jmeos \
    -Dversion=1.0-SNAPSHOT \
    -Dpackaging=jar

WORKDIR /app
COPY pom.xml .
# dependencies cache
RUN mvn dependency:go-offline -q   
COPY src ./src
RUN mvn package -DskipTests

ENV LD_LIBRARY_PATH=/usr/local/lib

# Entrypoint
CMD ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-Djava.library.path=/usr/local/lib", "-cp", "target/untitled-1.0-SNAPSHOT.jar:/tmp/JMEOS-fat.jar", "org.example.Main"]
```

Note:
- when cloning MobilityDB in the Dockerfile, you might want to change its version, e.g. "-b stable-1.3" instead of "-b master" depending on your needs (functions such as geom_in only available in meos_geo.h):  
  ```Ln 16: RUN git clone https://github.com/MobilityDB/MobilityDB.git -b master```
- do not forget to adapt the entrypoint parameters to your project:
  - the parameters of target/ here depending on the artifactId and version of your pom, here:   
  ```target/untitled-1.0-SNAPSHOT.jar:/tmp/JMEOS-fat.jar"```
  - the program you're trying to run, here:   
  ```"org.example.Main"```