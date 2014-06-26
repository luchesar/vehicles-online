package views.disposal_of_vehicle

import com.google.inject.name.Names
import com.google.inject.{Guice, Injector}
import com.tzavellas.sse.guice.ScalaModule
import common.GlobalLike
import composition.TestComposition
import filters.AccessLoggingFilter._
import filters.MockLogger
import helpers.UiSpec
import helpers.webbrowser.{TestHarness, WebBrowserDSL}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.scalatest.mock.MockitoSugar
import pages.disposal_of_vehicle.{BeforeYouStartPage, BusinessChooseYourAddressPage}
import play.api.LoggerLike
import play.api.test.FakeApplication
import scala.collection.JavaConversions._


class AccessLoggingIntegrationSpec extends UiSpec with TestHarness with MockitoSugar  with WebBrowserDSL {
  val mockLogger = new MockLogger

  "Access Logging" should {
    "Log access that complete with success" in new WebBrowser(testApp) {
      go to BeforeYouStartPage

      val infoLogs = mockLogger.captureLogInfos(2)
      infoLogs.get(0) should include("""] "GET / HTTP/1.1" 303""")
      infoLogs.get(1) should include("""] "GET /disposal-of-vehicle/before-you-start HTTP/1.1" 200""")
    }

    "Log access that are completed because of Exception" in new WebBrowser(testApp) {
      val httpClient = HttpClients.createDefault()
      val post = new HttpPost(BusinessChooseYourAddressPage.url)
      val httpResponse = httpClient.execute(post)
      httpResponse.close()

      val infoLogs = mockLogger.captureLogInfos(4)
      for (log <- infoLogs) info("Log message:" + log)

      infoLogs.get(2) should include("""] "POST /disposal-of-vehicle/business-choose-your-address HTTP/1.1" 303""")
      infoLogs.get(3) should include("""] "GET /disposal-of-vehicle/error/""")
    }
  }

  class TGlobal extends GlobalLike with TestComposition {

    override lazy val injector: Injector = Guice.createInjector(testModule(new ScalaModule {
      override def configure(): Unit = {
        bind[LoggerLike].annotatedWith(Names.named(AccessLoggerName)).toInstance(mockLogger)
      }
    }))
  }

  def testApp = FakeApplication(withGlobal = Some(new TGlobal))
}
