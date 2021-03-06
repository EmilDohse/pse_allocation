name := """ellipse"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  filters,
  "org.pac4j" % "play-pac4j" % "2.6.1",
  "org.pac4j" % "pac4j-http" % "1.9.5",
  "org.xerial" % "sqlite-jdbc" % "3.15.1",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",  
  "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test",
  "org.apache.commons" % "commons-email" % "1.2",
  "com.offbytwo.keyczar" % "keyczar" % "0.71g"
)

resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

playEnhancerEnabled := false

fork in run := true

routesGenerator := InjectedRoutesGenerator
 
jacoco.settings
 
parallelExecution in jacoco.Config := false

jacoco.excludes in jacoco.Config := Seq("org.pac4j.play.*", "controllers.javascript.*", "router.*", "security.BCrypt", "controllers.routes.javascript" , "views.html.*")