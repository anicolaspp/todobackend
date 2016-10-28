name := "todobackend"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.11.0-M4",
  "com.github.finagle" %% "finch-circe" % "0.11.0-M4",
  "com.github.finagle" %% "finch-test" % "0.11.0-M4",

  "com.twitter" %% "twitter-server" % "1.23.0",

  "io.circe" %% "circe-core" % "0.4.1",
  "io.circe" %% "circe-generic" % "0.4.1",
  "io.circe" %% "circe-parser" % "0.4.1",

  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"


)

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.6"

libraryDependencies += "com.softwaremill.macwire" % "macros_2.11" % "2.2.5"

resolvers ++= Seq(
  "twttr" at "https://maven.twttr.com/"
)

