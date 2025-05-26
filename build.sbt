name := "EcommerceSparkAnalysis"

version := "0.1"

scalaVersion := "2.12.15"


resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.0",
  "org.apache.spark" %% "spark-sql" % "3.3.0"
)
libraryDependencies += "org.jfree" % "jfreechart" % "1.5.3"
