import scala.collection.Seq

val commonSettings = Seq(
  scalaVersion := "3.7.2",
  scalacOptions ++= Seq(
    "-encoding",
    "utf8", // Option and arguments on same line
    "-deprecation",
    "-unchecked",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    "-language:postfixOps"
  ),
  libraryDependencies += "com.eed3si9n.verify" %% "verify" % "1.0.0" % Test,
  testFrameworks += new TestFramework("verify.runner.Framework")
)
val core: Project = project
  .settings(commonSettings)

val names: Project = project
  .dependsOn(core)
  .settings(commonSettings)

val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  .aggregate(
    core,
    names
  )
