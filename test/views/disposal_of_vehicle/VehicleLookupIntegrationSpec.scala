package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.Helper._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel

class VehicleLookupIntegrationSpec extends Specification with TestHarness {

  "VehicleLookupIntegrationSpec Integration" should {

    "be presented" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupPage

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }


    "Redirect when no traderBusinessName is cached" in new WebBrowser {
      // Arrange & Act
      go to VehicleLookupPage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

     "go to the next page when correct data is entered" in  new WebBrowser {
       // Arrange & Act
       CacheSetup.businessChooseYourAddress()

       VehicleLookupPage.happyPath

       // Assert
       assert(page.title equals DisposePage.title)
     }

    "display three validation error messages when no referenceNumber is entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display two validation error messages when no registrationNumber is entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a registrationNumber is entered containing one character" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "a")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a registrationNumber is entered containing special characters" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "$^")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display five validation error messages when no details are entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "", vehicleRegistrationNumber = "")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display two validation error messages when only a valid referenceNumber is entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display three validation error messages when only a valid registrationNumber is entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "")

      //Assert
      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "redirect when no dealerBusinessName is cached" in new WebBrowser {
      // Arrange & Act
      go to VehicleLookupPage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display previous page when back link is clicked with uprn present" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress(addressWithUprn)
      go to VehicleLookupPage
      click on VehicleLookupPage.back

      //Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display previous page when back link is clicked with no uprn present" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupPage
      click on VehicleLookupPage.back

      //Assert
      assert(page.title equals EnterAddressManuallyPage.title)
    }
  }
}