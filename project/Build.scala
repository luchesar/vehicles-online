import sbt._
import sbt.Keys._
import play.Project._
import net.litola.SassPlugin
import de.johoop.jacoco4sbt.JacocoPlugin._
import org.scalastyle.sbt.ScalastylePlugin

object ApplicationBuild extends Build {
  val appName         = "vehicles-online"

  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    cache,
    filters,
    "org.seleniumhq.selenium" % "selenium-java" % "2.40.0" % "test" withSources() withJavadoc(),
    "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-scala_2.10" % "1.1.5" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-java" % "1.1.5" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-junit" % "1.1.5" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-picocontainer" % "1.1.5" % "test" withSources() withJavadoc(),
    "org.specs2" %% "specs2" % "2.3.10" % "test" withSources() withJavadoc(),
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
    "org.scalatest" % "scalatest_2.10" % "2.1.1" % "test" withSources() withJavadoc(),
    "com.google.inject" % "guice" % "4.0-beta" withSources() withJavadoc(),
    "com.tzavellas" % "sse-guice" % "0.7.1" withSources() withJavadoc(), // Scala DSL for Guice
    "commons-codec" % "commons-codec" % "1.9" withSources() withJavadoc()
  )

  val myOrganization = Seq(organization := "Driver & Vehicle Licensing Agency")

  val compilerOptions = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  val myScalaVersion = Seq(scalaVersion := "2.10.3")

  val scalaCheck = org.scalastyle.sbt.ScalastylePlugin.Settings

  val myTestOptions =
  if (System.getProperty("include") != null ) {
    Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  } else if (System.getProperty("exclude") != null ) {
    Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  } else Seq.empty[Def.Setting[_]]

  // If tests are annotated with @LiveTest then they are excluded when running sbt test
  val excludeTest = testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "helpers.tags.LiveTest")

  val myJavaOptions = Seq(javaOptions in Test += System.getProperty("waitSeconds"))

  val myConcurrentRestrictions = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  val fork = Seq(sbt.Keys.fork in Test := false)

  val jcoco = Seq(parallelExecution in jacoco.Config := false)

  val appSettings: Seq[Def.Setting[_]] = myOrganization ++ SassPlugin.sassSettings ++ myScalaVersion ++ compilerOptions ++ myConcurrentRestrictions ++
    myTestOptions ++ excludeTest ++ myJavaOptions ++ fork ++ jcoco ++ scalaCheck

  val main = play.Project(appName, appVersion, appDependencies, settings = play.Project.playScalaSettings ++ jacoco.settings ++ ScalastylePlugin.Settings).settings(appSettings: _*)
}
