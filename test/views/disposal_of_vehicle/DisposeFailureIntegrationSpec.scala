package views.disposal_of_vehicle

import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec

class DisposeFailureIntegrationSpec extends UiSpec with TestHarness {
  private def cacheSetup() = {
    CacheSetup.businessChooseYourAddress().
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId().
      vehicleRegistrationNumber()
  }

  "DisposeFailureIntegration" should {

    "be presented" in new WebBrowser {
      cacheSetup()
      go to DisposeFailurePage.url

      assert(page.title equals DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to DisposeFailurePage.url

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      cacheSetup()
      go to DisposeFailurePage.url

      click on DisposeFailurePage.vehiclelookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      cacheSetup()
      go to DisposeFailurePage.url

      click on DisposeFailurePage.setuptradedetails

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}
