organization := "cchantep"

name := "RM-SBT-Playground"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.8"

scalacOptions := Seq("-feature", "-language:postfixOps", "-language:reflectiveCalls")

resolvers ++= Seq(
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

// Dependencies
val rmVer = sys.env.get("RM_VERSION").getOrElse("0.17.1")
val slf4jVer = "1.7.12"
val log4jVer = "2.12.0"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % rmVer changing(),
  //"org.reactivemongo" % "reactivemongo-shaded-native" % s"${rmVer}-{linux|osx}-x86-64" % "runtime" changing(),
  "org.slf4j" % "slf4j-api" % slf4jVer % Provided
)

libraryDependencies ++= {
  if (rmVer startsWith "0.11.") Seq.empty
  else Seq(
    "org.reactivemongo" %% "reactivemongo-jmx" % rmVer changing()
  )
}

libraryDependencies ++= Seq(
  "log4j-api", "log4j-core", "log4j-slf4j-impl").map(
  "org.apache.logging.log4j" % _ % log4jVer)
