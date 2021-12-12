
#build
pushd /home/ci/metahospital-datacollector-backend
git pull
export PATH=/home/software/maven/apache-maven-3.8.4/bin:$PATH
mvn clean package -DskipTests

#deploy
rm -rf /home/metahospital/datacollector
mkdir -p /home/metahospital/datacollector
cp ./target/data-collector-0.0.1-SNAPSHOT.jar /home/metahospital/datacollector

#restart
DATA_COLLECTOR_PID=`ps -ef | grep data-collector-0.0.1-SNAPSHOT.jar | { grep -v grep || true; } | awk '{print $2}'`
if [ -n "${DATA_COLLECTOR_PID}" ]; then
  kill -9 ${DATA_COLLECTOR_PID}
fi
pushd /home/metahospital/datacollector
java -jar data-collector-0.0.1-SNAPSHOT.jar &



