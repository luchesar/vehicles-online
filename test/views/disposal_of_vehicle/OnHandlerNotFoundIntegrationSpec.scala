package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.tags.UiTag
import helpers.webbrowser.{TestHarness, WebDriverFactory}
import pages.disposal_of_vehicle.OnHandlerNotFoundPage.{exit, hasTryAgain, tryAgain}
import pages.disposal_of_vehicle.{BeforeYouStartPage, OnHandlerNotFoundPage}
import helpers.disposal_of_vehicle.ProgressBar.progressStep

final class OnHandlerNotFoundIntegrationSpec extends UiSpec with TestHarness {
  "go to not found page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      page.title should equal(OnHandlerNotFoundPage.title)
    }

    "not display any progress indicator when progressBar is set to true" taggedAs UiTag in new ProgressBarTrue {
      go to OnHandlerNotFoundPage

      page.title should not contain progressStep
    }
  }

  "javascript disabled" should {
    // This test needs to run with javaScript disabled.
    "not display the try again button" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      hasTryAgain should equal(true)
    }
  }

  "javascript enabled" should {
    val webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)
    // This test needs to run with javaScript enabled.
    "display the try again button" taggedAs UiTag in new WebBrowser(webDriver = webDriver) {
      go to OnHandlerNotFoundPage

      hasTryAgain should equal(true)
    }


    // This test needs to run with javaScript enabled.
    "redirect the user to the previous page when the try again button is pressed" taggedAs UiTag in new HtmlUnitWithJs {
      go to BeforeYouStartPage
      go to OnHandlerNotFoundPage

      click on tryAgain

      page.title should equal(BeforeYouStartPage.title)
    }
  }

  "exit" should {
    "redirect to BeforeYouStartPage" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      click on exit

      page.title should equal(BeforeYouStartPage.title)
    }
  }
}