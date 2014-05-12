package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.UprnNotFoundPage._
import pages.disposal_of_vehicle._

class UprnNotFoundIntegrationSpec extends UiSpec with TestHarness {

  "UprnNotFound Integration" should {

    "be presented" in new WebBrowser {
      go to UprnNotFoundPage

      assert(page.title equals UprnNotFoundPage.title)
    }

    "go to setuptradedetails page after the Setup Trade Details button is clicked" in new WebBrowser {
      go to UprnNotFoundPage

      click on setupTradeDetails

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.setupTradeDetailsIntegration()
      go to UprnNotFoundPage

      click on manualAddress

      assert(page.title equals EnterAddressManuallyPage.title)

    }

    "go to setuptradedetails page after the Manual Address button is clicked and trade details have not been set up in cache" in new WebBrowser {
      go to UprnNotFoundPage

      click on manualAddress

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}
