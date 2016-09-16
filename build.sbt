organization := "cchantep"

name := "RM-SBT-Playground"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions := Seq("-feature", "-language:postfixOps", "-language:reflectiveCalls")

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

// Dependencies
val rmVer = sys.env.get("RM_VERSION").getOrElse("0.12-RC3")
val slf4jVer = "1.7.12"
val log4jVer = "2.5"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % rmVer changing(),
  "org.reactivemongo" %% "reactivemongo-jmx" % rmVer changing(),
  "org.slf4j" % "slf4j-api" % slf4jVer % "provided"
) ++ Seq("log4j-api", "log4j-core", "log4j-slf4j-impl").map(
  "org.apache.logging.log4j" % _ % log4jVer)
