package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle.CacheSetup
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._

class DisposeFailureIntegrationSpec extends Specification with TestHarness {
  "DisposeFailureIntegration" should {

    "be presented" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      go to DisposeFailurePage.url

      // Assert
      assert(page.title equals DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      //Arrange & Act
      go to DisposeFailurePage.url

      //Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      go to DisposeFailurePage.url

      // Act
      click on DisposeFailurePage.vehiclelookup

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      go to DisposeFailurePage.url

      // Act
      click on DisposeFailurePage.setuptradedetails

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}
