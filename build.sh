#!/bin/sh
# jdk 8
export PATH=/home/scmtools/buildkit/java/jdk1.8.0_60/bin:$PATH

# mvn 3+
export PATH=/home/scmtools/buildkit/maven/apache-maven-3.3.9/bin:$PATH

# compile
mvn clean package -DskipTests

# save compile output
mkdir output && cp ./target/data-processor-0.0.1-SNAPSHOT.jar ./output && ls ./output
