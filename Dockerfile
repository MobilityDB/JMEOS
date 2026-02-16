FROM debian:bookworm-slim

LABEL maintainer=nidhalmareghni8@gmail.com

# install corretto after verifying that the key is the one we expect.
RUN apt-get update \
  && apt-get install -y git curl gnupg build-essential tree vim cmake postgresql-server-dev-15 libproj-dev libjson-c-dev libgsl-dev libgeos-dev postgis \
  && export GNUPGHOME="$(mktemp -d)" \
  && curl -fL https://apt.corretto.aws/corretto.key | gpg --batch --import \
  && gpg --batch --export '6DC3636DAE534049C8B94623A122542AB04F24E3' > /usr/share/keyrings/corretto.gpg \
  && rm -r "$GNUPGHOME" \
  && unset GNUPGHOME \
  && echo "deb [signed-by=/usr/share/keyrings/corretto.gpg] https://apt.corretto.aws stable main" > /etc/apt/sources.list.d/corretto.list \
  && apt-get update \
  && apt-get install -y java-21-amazon-corretto-jdk 

# BUILD MobilityDB
RUN git clone https://github.com/MobilityDB/MobilityDB.git -b master /usr/local/src/MobilityDB
RUN mkdir -p /usr/local/src/MobilityDB/build 
RUN cd /usr/local/src/MobilityDB/build && \
    cmake -DMEOS=ON .. && \
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

# ADD PROJECT MobilityDB-JMEOS
RUN git clone https://github.com/nmareghn/MobilityDB-JMEOS  /usr/local/jmeos
RUN rm /usr/local/jmeos/src/main/resources/lib/libmeos.so
RUN cp /usr/local/lib/libmeos.so /usr/local/jmeos/src/main/resources/lib/libmeos.so
RUN rm /usr/local/jmeos/jar/*

ENV LD_LIBRARY_PATH=/usr/local/lib

# CLEAN_UP
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*lists/* 
RUN apt-get remove --purge --autoremove -y curl gnupg 

# END_POINT
WORKDIR /usr/local/jmeos
CMD ["/bin/bash"]



