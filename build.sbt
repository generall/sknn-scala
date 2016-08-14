name := "sknn-scala"

version := "1.0-SNAPSHOT"

organization := "com.generall"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.jgrapht" % "jgrapht-core" % "0.9.2",
  "org.jgrapht" % "jgrapht-ext" % "0.9.2"
)


resolvers += Resolver.mavenLocal
