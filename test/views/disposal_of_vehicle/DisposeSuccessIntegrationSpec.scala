package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec

class DisposeSuccessIntegrationSpec extends UiSpec with TestHarness {

  "Dispose confirmation integration" should {

    "be presented" in new WebBrowser {
      cacheSetup()

      go to DisposeSuccessPage

      assert(page.title equals DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WebBrowser {
      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WebBrowser {
      CacheSetup.vehicleDetailsModel()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WebBrowser {
      CacheSetup.disposeFormModel()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WebBrowser {
      CacheSetup.disposeFormModel()
      CacheSetup.vehicleDetailsModel()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WebBrowser {
      CacheSetup.disposeFormModel()
      CacheSetup.businessChooseYourAddress()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WebBrowser  {
      cacheSetup()
      DisposeSuccessPage.happyPath

      assert(page.title equals VehicleLookupPage.title)
    }
  }

  private def cacheSetup() = {
    CacheSetup.
      businessChooseYourAddress().
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId().
      vehicleRegistrationNumber()
  }
}
