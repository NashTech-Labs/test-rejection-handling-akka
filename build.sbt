name := "sampleJson"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.1",
  "net.liftweb"                %% "lift-json"                      % "2.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.16",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)