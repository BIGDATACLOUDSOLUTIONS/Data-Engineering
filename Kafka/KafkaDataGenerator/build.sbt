lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.datagenerator",
	  version := "0.1.0",
      scalaVersion := "2.11.12"
    )),
    name := "datagenerator",
    libraryDependencies ++= List(
      "org.scalatest" %% "scalatest" % "3.0.5",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
      "log4j" % "log4j" % "1.2.17",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe" % "config" % "1.3.2",
      "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
      "org.apache.kafka" % "kafka-clients" % "2.1.0"
	)
  )

