sonatypeProfileName := "com.gu"

ThisBuild / publishTo := sonatypePublishToBundle.value

ThisBuild / organization := "com.gu.duration-formatting"

ThisBuild / scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/duration-formatting"),
  "scm:git:git@github.com:guardian/duration-formatting.git"
))

ThisBuild / pomExtra := (
  <url>https://github.com/guardian/duration-formatting</url>
    <developers>
      <developer>
        <id>rtyley</id>
        <name>Roberto Tyley</name>
        <url>https://github.com/rtyley</url>
      </developer>
    </developers>
  )