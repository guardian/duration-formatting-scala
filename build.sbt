import ReleaseTransformations.*

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / scalacOptions := Seq("-deprecation", "-release","11")
ThisBuild / licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))


lazy val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.17" % Test
  ),
  Test / testOptions +=
    Tests.Argument(TestFrameworks.ScalaTest, "-u", s"test-results/scala-${scalaVersion.value}", "-o")
)

lazy val core =
  project.settings(baseSettings)

lazy val `spire-intervals` =
  project.in(file("spire-intervals")).dependsOn(core).settings(
    baseSettings,
    libraryDependencies += "org.typelevel" %% "spire" % "0.18.0"
  )

lazy val `duration-formatting-root` = (project in file("."))
  .aggregate(
    core,
    `spire-intervals`
  ).settings(baseSettings).settings(
    publish / skip := true,
    releaseCrossBuild := false, // true if you cross-build the project for multiple Scala versions
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      // For cross-build projects, use releaseStepCommand("+publishSigned")
      releaseStepCommandAndRemaining("publishSigned"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
