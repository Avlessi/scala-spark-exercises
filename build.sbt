name := "scala-spark-exercises"

version := "1.0"

scalaVersion := "2.11.12"

val scalatestV = "3.1.0"
val sparkV = "2.4.4"

lazy val sparkCore: ModuleID = "org.apache.spark" %% "spark-core" % sparkV
lazy val sparkSql: ModuleID = "org.apache.spark" %% "spark-sql" % sparkV
lazy val scalatest: ModuleID = "org.scalatest" %% "scalatest" % scalatestV % "test"

val deps: Seq[ModuleID] = Seq(
  sparkCore,
  sparkSql,
  scalatest
)

libraryDependencies ++= deps