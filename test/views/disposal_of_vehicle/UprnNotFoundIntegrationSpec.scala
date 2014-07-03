package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.UprnNotFoundPage._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.ProgressBar._

final class UprnNotFoundIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to UprnNotFoundPage

      page.title should equal(UprnNotFoundPage.title)
    }

    "not display any progress indicator when progressBar is set to true" taggedAs UiTag in new WebBrowser(app = fakeApplicationWithProgressBarTrue) {
      go to UprnNotFoundPage

      page.title should not contain ProgressStep
    }
  }

  "setupTradeDetails button" should {
    "go to setuptradedetails page" taggedAs UiTag in new WebBrowser {
      go to UprnNotFoundPage

      click on setupTradeDetails

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  "manualAddress button" should {
    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.setupTradeDetails()
      go to UprnNotFoundPage

      click on manualAddress

      page.title should equal (EnterAddressManuallyPage.title)
    }

    "go to setuptradedetails page when trade details have not been set up in cache" taggedAs UiTag in new WebBrowser {
      go to UprnNotFoundPage

      click on manualAddress

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }
}