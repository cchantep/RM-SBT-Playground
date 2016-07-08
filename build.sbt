organization := "cchantep"

name := "RM-SBT-Playground"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

val rmVer = "0.11.14"
val slf4jVer = "1.7.12"
val log4jVer = "2.5"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % rmVer,
  "org.slf4j" % "slf4j-api" % slf4jVer % "provided"
) ++ Seq("log4j-api", "log4j-core", "log4j-slf4j-impl").map(
  "org.apache.logging.log4j" % _ % log4jVer)
