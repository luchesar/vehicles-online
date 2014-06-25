package views.disposal_of_vehicle

import ch.qos.logback.classic.{Level, Logger}
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import helpers.UiSpec
import helpers.webbrowser.{WebBrowserDSL, WebDriverFactory, TestHarness}
import org.mockito.ArgumentCaptor
import org.openqa.selenium.WebDriver
import org.scalatest.mock.MockitoSugar
import org.slf4j.LoggerFactory
import org.specs2.execute.{Result, AsResult}
import org.specs2.mutable.Around
import org.specs2.specification.Scope
import pages.disposal_of_vehicle.BeforeYouStartPage
import play.api.test.{TestServer, Helpers, FakeApplication}


class AccessLoggingIntegrationSpec extends UiSpec with TestHarness with MockitoSugar  with WebBrowserDSL {
//  implicit protected val webDriver: WebDriver = WebDriverFactory.webDriver

  val accessLogger = LoggerFactory.getLogger("dvla.common.AccessLogger").asInstanceOf[Logger]
  val appender = mock[Appender[ILoggingEvent]]



  "Access Logging" ignore {
    "Log access that complete with success" in new WebBrowser {
      accessLogger.addAppender(appender)
      import scala.collection.JavaConversions._
      accessLogger.iteratorForAppenders().foreach { appender =>
        println(appender)
      }
      val captor = ArgumentCaptor.forClass(classOf[ILoggingEvent])

      go to BeforeYouStartPage

      accessLogger.info("")

      appender.doAppend(captor.capture())

      captor.getAllValues should have size 1
      captor.getValue.getLevel should be(Level.INFO)
      captor.getValue.getFormattedMessage should equal("")
    }

    "Log access that are completed because of Exception" in {

    }

    "Log access completed with redirect" in {

    }
  }
}
