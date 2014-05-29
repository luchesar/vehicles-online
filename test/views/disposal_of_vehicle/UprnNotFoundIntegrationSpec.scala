package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.UprnNotFoundPage._
import pages.disposal_of_vehicle._

final class UprnNotFoundIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to UprnNotFoundPage

      page.title should equal(UprnNotFoundPage.title)
    }
  }

  "setupTradeDetails button" should {
    "go to setuptradedetails page" in new WebBrowser {
      go to UprnNotFoundPage

      click on setupTradeDetails

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  "manualAddress button" should {
    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.setupTradeDetails()
      go to UprnNotFoundPage

      click on manualAddress

      page.title should equal (EnterAddressManuallyPage.title)
    }

    "go to setuptradedetails page when trade details have not been set up in cache" in new WebBrowser {
      go to UprnNotFoundPage

      click on manualAddress

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }
}
