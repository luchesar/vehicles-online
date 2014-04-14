package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec

class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness  {
  private def cacheSetup() = {
    CacheSetup.businessChooseYourAddress().
      vehicleLookupFormModel()
  }

  "VehicleLookupFailureIntegration" should {
    "be presented" in new WebBrowser {
      cacheSetup()
      go to VehicleLookupFailurePage

      assert(page.title equals VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      CacheSetup.vehicleLookupFormModel()
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only BusinessChooseYourAddress cache is populated" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      cacheSetup()
      go to VehicleLookupFailurePage

      click on VehicleLookupFailurePage.vehicleLookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      cacheSetup()
      go to VehicleLookupFailurePage

      click on VehicleLookupFailurePage.setupTradeDetails

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}
