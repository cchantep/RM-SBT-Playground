#! /bin/bash

if [ $# -lt 1 ]; then
  echo "Usage: ./connect.sh [mongo-uri]"
  exit 1
fi

SBT_VER="0.13.11"
SBT_LAUNCHER_HOME="$HOME/.sbt/launchers/$SBT_VER"
SBT_LAUNCHER_JAR="$SBT_LAUNCHER_HOME/sbt-launch.jar"

if [ `which sbt | wc -l` -eq 1 ]; then
    SBT_CMD="sbt"
else 
    SBT_CMD="java -jar $SBT_LAUNCHER_JAR"
    
    if [ ! -r "$SBT_LAUNCHER_JAR" ]; then
        mkdir -p $SBT_LAUNCHER_HOME
        curl -L -o "$SBT_LAUNCHER_JAR" "http://dl.bintray.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/$SBT_VER/sbt-launch.jar"
    else
        echo -n "SBT already available: "
        ls -v -1 "$SBT_LAUNCHER_JAR"
    fi
fi
    
echo "SBT command: $SBT_CMD"
$SBT_CMD console << EOF
import Playground._
rm connect "$1"
while (true) { rm.database.get; Thread.sleep(500) }
EOF
