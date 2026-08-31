FROM ubuntu:24.04

LABEL maintainer=nidhalmareghni8@gmail.com

# The MEOS build dependencies are the set MobilityDB's own MEOS workflow installs
# (.github/workflows/meos.yml), on the distribution it installs them on: `libh3-dev`
# is what -DALL=ON needs for the H3 family and Debian bookworm has no such package,
# while Ubuntu 24.04 carries it. A -DMEOS=ON build needs neither the PostgreSQL
# server headers nor GSL nor PostGIS, so none of the three is installed.
#
# Corretto is installed after verifying that the key is the one we expect.
RUN apt-get update \
  && apt-get install -y git curl gnupg build-essential cmake \
       libgeos-dev libproj-dev libjson-c-dev libgdal-dev libh3-dev libxml2-dev zlib1g-dev \
  && export GNUPGHOME="$(mktemp -d)" \
  && curl -fL https://apt.corretto.aws/corretto.key | gpg --batch --import \
  && gpg --batch --export '6DC3636DAE534049C8B94623A122542AB04F24E3' > /usr/share/keyrings/corretto.gpg \
  && rm -r "$GNUPGHOME" \
  && unset GNUPGHOME \
  && echo "deb [signed-by=/usr/share/keyrings/corretto.gpg] https://apt.corretto.aws stable main" > /etc/apt/sources.list.d/corretto.list \
  && apt-get update \
  && apt-get install -y java-21-amazon-corretto-jdk 

# BUILD MobilityDB. GeneratedFunctions is emitted from the catalog of master
# with every family on, so the library has to answer for that same surface:
# -DALL=ON is the flag CMakeLists.txt turns into the family list, and without it
# the jar names symbols libmeos does not carry.
RUN git clone --depth 1 https://github.com/MobilityDB/MobilityDB.git -b master /usr/local/src/MobilityDB
RUN mkdir -p /usr/local/src/MobilityDB/build
RUN cd /usr/local/src/MobilityDB/build && \
    cmake -DMEOS=ON -DALL=ON .. && \
    make -j$(nproc) && \
    make install && \
    ldconfig

# COMMON FOR ALL IMAGES
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG="/root/.m2"

COPY --from=maven:3.9.6-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.6-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.6-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# ADD PROJECT MobilityDB-JMEOS. The image carries the tree it is built from
# rather than cloning this repository into itself: a clone names a ref, and a ref
# named here is one nothing updates.
COPY . /usr/local/jmeos

# Place libmeos where the build reads it: jmeos-core/pom.xml names
# ${project.basedir}/src/libmeos.so, so the module's own src/ is the destination,
# the same path tools/regen-from-catalog.sh and the CI build copy it to.
# JarLibraryLoader extracts it from the jar at run time, with LD_LIBRARY_PATH the
# fallback.
RUN cp /usr/local/lib/libmeos.so /usr/local/jmeos/jmeos-core/src/libmeos.so

ENV LD_LIBRARY_PATH=/usr/local/lib

# CLEAN_UP
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*lists/*
RUN apt-get remove --purge --autoremove -y curl gnupg

# END_POINT
WORKDIR /usr/local/jmeos
CMD ["/bin/bash"]