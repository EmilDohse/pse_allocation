name := """ellipse"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.pac4j" % "play-pac4j" % "2.6.1",
  "org.xerial" % "sqlite-jdbc" % "3.15.1"
)

resolvers += "SQLite-JDBC Repository" at "https://oss.sonatype.org/content/repositories/snapshots"

playEnhancerEnabled := false

fork in run := true

fork in run := true