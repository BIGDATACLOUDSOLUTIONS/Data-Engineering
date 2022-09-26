name := "ScalaAPIForMySQL"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.10"

//libraryDependencies += "org.apache.hive" % "hive-jdbc" % "1.1.0-cdh5.14.4"
//libraryDependencies += "org.apache.hive" % "hadoop-common" % "2.6.0-cdh5.14.4"

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.3"
libraryDependencies += "commons-logging" % "commons-logging" % "1.1.3"
libraryDependencies += "org.apache.thrift" % "libfb303" % "0.9.3" pomOnly()
libraryDependencies += "org.apache.thrift" % "libthrift" % "0.10.0" pomOnly()

libraryDependencies += "org.apache.hive" % "hive-exec" % "1.2.1"
libraryDependencies += "org.apache.hive" % "hive-jdbc" % "1.2.1"

