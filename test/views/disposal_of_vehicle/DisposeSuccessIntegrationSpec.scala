package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec
import services.session.{PlaySessionState, SessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class DisposeSuccessIntegrationSpec extends UiSpec with TestHarness {

  "Dispose confirmation integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposeSuccessPage

      assert(page.title equals DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WebBrowser {
      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().dealerDetailsIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().
        disposeFormModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().
        dealerDetailsIntegration().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().
        disposeFormModelIntegration().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup().
        dealerDetailsIntegration().
        disposeFormModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      DisposeSuccessPage.happyPath

      assert(page.title equals VehicleLookupPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    new CacheSetup().
      dealerDetailsIntegration().
      vehicleDetailsModelIntegration().
      disposeFormModelIntegration().
      disposeTransactionIdIntegration().
      vehicleRegistrationNumberIntegration()
}
