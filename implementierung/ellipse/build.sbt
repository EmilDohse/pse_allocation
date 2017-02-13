name := """ellipse"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.pac4j" % "play-pac4j" % "2.6.1",
  "org.pac4j" % "pac4j-http" % "1.9.5",
  "org.xerial" % "sqlite-jdbc" % "3.15.1",
  "org.mockito" % "mockito-all" % "1.9.5",  
  "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test",
  "org.apache.commons" % "commons-email" % "1.2",
  "com.offbytwo.keyczar" % "keyczar" % "0.71g"
)

resolvers += "SQLite-JDBC Repository" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

playEnhancerEnabled := false

fork in run := true

routesGenerator := InjectedRoutesGenerator

 libraryDependencies += cache
 libraryDependencies += evolutions

fork in run := true