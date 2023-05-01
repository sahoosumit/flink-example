import sbt.{Credentials, Path}

name := "flink-example"

version := "0.1"

scalaVersion := "2.12.4"
publishMavenStyle := true

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"


libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1";
libraryDependencies += "org.apache.flink" % "flink-core" % "1.15.0"
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % "1.15.0";
libraryDependencies += "org.apache.flink" % "flink-connector-kafka" % "1.15.0";
libraryDependencies += "org.apache.flink" % "flink-clients" % "1.15.0";


