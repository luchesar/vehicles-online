package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness

class DisposeSuccessIntegrationSpec extends Specification with TestHarness {
  "Dispose confirmation integration" should {

    "be presented" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WebBrowser {
      // Arrange & Act
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.vehicleDetailsModel()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.disposeFormModel()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.disposeFormModel()
      CacheSetup.vehicleDetailsModel()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WebBrowser {
      // Arrange & Act
      CacheSetup.disposeFormModel()
      CacheSetup.businessChooseYourAddress()
      go to DisposeSuccessPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WebBrowser  {
      // Arrange & Act
      DisposeSuccessPage.happyPath

      //Assert
      assert(page.title equals VehicleLookupPage.title)
    }
  }
}
