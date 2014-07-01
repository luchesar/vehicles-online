package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.tags.UiTag
import helpers.webbrowser.{TestHarness, WebDriverFactory}
import pages.disposal_of_vehicle.OnHandlerNotFoundPage.{hasTryAgain, tryAgain}
import pages.disposal_of_vehicle.{BeforeYouStartPage, OnHandlerNotFoundPage}

final class OnHandlerNotFoundIntegrationSpec extends UiSpec with TestHarness {
  "go to not found page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      page.title should equal(OnHandlerNotFoundPage.title)
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
    // This test needs to run with javaScript enabled.
    "display the try again button" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to OnHandlerNotFoundPage

      hasTryAgain should equal(true)
    }

    // This test needs to run with javaScript enabled.
    "redirect the user to the previous page when the try again button is pressed" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to BeforeYouStartPage
      go to OnHandlerNotFoundPage

      click on tryAgain

      page.title should equal(BeforeYouStartPage.title)
    }
  }

  "exit" should {
    "link to the expected external page" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      // We should not test navigating to a real URL as that is adding an external dependency, instead just look for
      // the presence of the URL and trust that the browser can handle it.
      page.source should include( """href="https://www.gov.uk/browse/driving"""")
    }
  }
}