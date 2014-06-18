package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.{WebDriverFactory, TestHarness}
import helpers.tags.UiTag
import pages.common.InterstitialPage
import pages.disposal_of_vehicle.BeforeYouStartPage
import org.scalatest.concurrent.Eventually._
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Millis, Seconds, Span}

class InterstitialIntegrationSpec  extends UiSpec with TestHarness {
  "go to page" should {
    //This is running without javascript, it should call the meta refresh
    "redirect to start page when cookie not present" taggedAs UiTag in new WebBrowser {
      go to InterstitialPage

      page.title should equal(BeforeYouStartPage.title)
    }

    // This test needs to run with javaScript enabled.
    "display DisposeSuccess page on correct submission with javascript enabled" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to InterstitialPage

      // We want to wait for the javascript to execute and redirect to the next page. For build servers we may need to
      // wait longer than the default.
      val timeout: Span = scaled(Span(2, Seconds))
      implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = timeout)

      eventually {page.title should equal(InterstitialPage.title)}
    }
  }
}
