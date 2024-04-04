scalaVersion := "3.4.1"

ThisBuild / libraryDependencies += "com.eed3si9n.verify" %% "verify" % "1.0.0" % Test
//ThisBuild / libraryDependencies += "org.apache.calcite" % "calcite-core" % "1.36.0"

ThisBuild / testFrameworks += new TestFramework("verify.runner.Framework")

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8", // Option and arguments on same line
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:experimental",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)