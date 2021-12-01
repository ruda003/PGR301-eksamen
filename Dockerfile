FROM debian

RUN \
    apt update && apt -y --no-install-recommends install \
    lsb-release gpg git build-essential maven git curl wget supervisor
WORKDIR /opt
RUN \
    wget -qO- https://repos.influxdata.com/influxdb.key | gpg --dearmor | tee /etc/apt/trusted.gpg.d/influxdb.gpg > /dev/null && \
    export DISTRIB_ID=$(lsb_release -si) && export DISTRIB_CODENAME=$(lsb_release -sc) && \
    echo "deb [signed-by=/etc/apt/trusted.gpg.d/influxdb.gpg] https://repos.influxdata.com/debian bullseye stable" | tee /etc/apt/sources.list.d/influxdb.list > /dev/null && \
    apt update && apt install influxdb2 -y
VOLUME ["/opt/influxdb/shared/data"]

EXPOSE 80 8083 8086 8096

CMD ["supervisord", "-n"]

WORKDIR ~
RUN \
    wget https://download.java.net/java/early_access/jdk18/25/GPL/openjdk-18-ea+25_linux-x64_bin.tar.gz && \
    tar xzvf openjdk-18-ea+25_linux-x64_bin.tar.gz && \
    cd jdk-18/bin && \
    mv /usr/bin/java /usr/bin/java11 && \
    ln -s ~/jdk-18/java /usr/bin/java && \
    git clone https://github.com/ruda003/PGR301-eksamen.git && cd PGR301-eksamen && \
    mvn -B package --file pom.xml && \
    cd target && \
    JAVA_HOME=/ java -jar demo-0.0.1-SNAPSHOT.jar