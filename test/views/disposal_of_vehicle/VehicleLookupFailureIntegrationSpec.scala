package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec

class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness  {
  "VehicleLookupFailureIntegration" should {
    "be presented" in new WebBrowser {
      //Arrange & Act
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      go to VehicleLookupFailurePage

      // Assert
      assert(page.title equals VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      //Arrange & Act
      go to VehicleLookupFailurePage

      //Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      // Arrange & Act
      CacheSetup.vehicleLookupFormModel()
      go to VehicleLookupFailurePage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only BusinessChooseYourAddress cache is populated" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupFailurePage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      go to VehicleLookupFailurePage

      // Act
      click on VehicleLookupFailurePage.vehicleLookup

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      go to VehicleLookupFailurePage

      // Act
      click on VehicleLookupFailurePage.setupTradeDetails

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}
