import sbt._
import sbt.Keys._
import play.Project._
import net.litola.SassPlugin
import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {
  val appName         = "vehicles-online"

  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    cache,
    "org.seleniumhq.selenium" % "selenium-java" % "2.40.0" % "test" withSources() withJavadoc(),
    "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0" % "test" withSources() withJavadoc(),
    "org.specs2" %% "specs2" % "2.3.7" % "test" withSources() withJavadoc(),
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1", // Scala DSL for Guice
    "org.mongodb" %% "casbah" % "2.7.0-RC0", // officially supported Scala driver for MongoDB
    "com.novus" % "salat-core_2.10" % "1.9.5" // Mongodb BSON encoders
  )

  val sOrg = Seq(organization := "Driver & Vehicle Licensing Agency")

  val sO = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  val sV = Seq(scalaVersion := "2.10.3")

  val scalaCheck = org.scalastyle.sbt.ScalastylePlugin.Settings

  val sTest =
  if (System.getProperty("include") != null ) {
    Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  } else if (System.getProperty("exclude") != null ) {
    Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  } else Seq.empty[Def.Setting[_]]

  val jO = Seq(javaOptions in Test += System.getProperty("waitSeconds"))

  val gS = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  val f = Seq(sbt.Keys.fork in Test := false)

  val jcoco = Seq(parallelExecution in jacoco.Config := false)

  val appSettings: Seq[Def.Setting[_]] = sOrg ++ SassPlugin.sassSettings ++ sV ++ sO ++ gS ++ sTest ++ jO ++ f ++ jcoco ++ scalaCheck

  val main = play.Project(appName, appVersion, appDependencies, settings = play.Project.playScalaSettings ++ jacoco.settings).settings(appSettings: _*)
}
