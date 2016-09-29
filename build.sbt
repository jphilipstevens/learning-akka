lazy val akkaDb = (project in file(".")).
  aggregate(messages, server)

lazy val commonSettings = Seq(
  organization := "jono.app",
  scalaVersion := "2.11.8"
)

lazy val messages = (project in file("messages"))
  .settings(commonSettings: _*)

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .dependsOn(messages)

lazy val client = (project in file("client"))
  .settings(commonSettings: _*)
  .dependsOn(messages)

