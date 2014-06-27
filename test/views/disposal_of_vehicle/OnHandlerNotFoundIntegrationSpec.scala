package views.disposal_of_vehicle

import helpers.{WithApplication, UiSpec}
import helpers.tags.UiTag
import helpers.webbrowser.{WebDriverFactory, TestHarness}
import pages.disposal_of_vehicle.{BeforeYouStartPage, OnHandlerNotFoundPage}
import mappings.disposal_of_vehicle.OnHandlerNotFound.TryAgainId
import play.api.test.Helpers._
import scala.Some

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

      webDriver.getPageSource shouldNot contain(TryAgainId)
    }
  }
  "javascript enabled" should {
    // This test needs to run with javaScript enabled.
    "display the try again button" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to OnHandlerNotFoundPage

      assert(OnHandlerNotFoundPage.tryAgain.isDisplayed == true)
    }

    // This test needs to run with javaScript enabled.
    "redirect the user to the previous page when the try again button is pressed" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to BeforeYouStartPage
      go to OnHandlerNotFoundPage

      click on OnHandlerNotFoundPage.tryAgain

      page.title should equal(BeforeYouStartPage.title)
    }
  }

  "exit" should {
    "redirect to BeforeYouStartPage" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      click on OnHandlerNotFoundPage.exit

      page.title should equal(BeforeYouStartPage.title)
    }
  }
}