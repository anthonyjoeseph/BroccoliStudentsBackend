import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import ScalateKeys._

val ScalatraVersion = "2.5.0"

ScalatraPlugin.scalatraSettings

scalateSettings

organization := "com.readbroccoli"

name := "BroccoliStudents"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.1"

resolvers += Classpaths.typesafeReleases

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % "2.5.0",
  "org.scalatra" %% "scalatra-auth" % "2.5.0",
  "org.json4s"   %% "json4s-jackson" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  jdbc,
  "com.typesafe.play" %% "anorm" % "2.5.3",
  "com.zaxxer" % "HikariCP" % "2.3.2",
  "mysql" % "mysql-connector-java" % "5.1.24",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5",
  "com.github.pathikrit" %% "better-files" % "3.0.0"
)

flywayUrl := "jdbc:mysql://readbroccoli.com:3306/broccoli_students"

flywayUser := "remote_broc_dev"

flywayPassword := "Clap3000!"

javaOptions ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
)

enablePlugins(JettyPlugin)