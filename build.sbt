lazy val commonSettings = Seq(
  name := "cabby",
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(cache , specs2 % Test )
)

lazy val `cabby` = (project in file(".")).settings(commonSettings: _*).enablePlugins(PlayScala)

unmanagedResourceDirectories in Test <+=  (baseDirectory( _ /"target/web/public/test" ))

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
