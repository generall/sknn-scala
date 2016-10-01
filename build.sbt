name := "sknn-scala"

version := "1.0-SNAPSHOT"

organization := "ml.generall"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.jgrapht" % "jgrapht-core" % "0.9.2",
  "org.jgrapht" % "jgrapht-ext" % "0.9.2"
)


resolvers += Resolver.mavenLocal
