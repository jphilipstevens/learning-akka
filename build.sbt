lazy val root = (project in file(".")).
  aggregate(messages, server)

lazy val commonSettings = Seq(
  organization := "jono.app",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

lazy val messages = (project in file("messages"))
  .settings(commonSettings: _*)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .dependsOn(messages)
