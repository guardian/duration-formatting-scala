import ReleaseTransformations.*
import sbtversionpolicy.withsbtrelease.ReleaseVersion

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / crossScalaVersions := Seq(
  scalaVersion.value,
  "3.3.6"
)
ThisBuild / scalacOptions := Seq("-deprecation", "-release","11")
ThisBuild / licenses := Seq(License.Apache2)


lazy val baseSettings = Seq(
  organization := "com.gu.duration-formatting",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.19" % Test
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
    // releaseVersion := ReleaseVersion.fromAggregatedAssessedCompatibilityWithLatestRelease().value,
    releaseCrossBuild := true, // true if you cross-build the project for multiple Scala versions
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      setNextVersion,
      commitNextVersion
    )
  )
