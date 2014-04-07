package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.Helper._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec

class VehicleLookupIntegrationSpec extends UiSpec with TestHarness {

  "VehicleLookupIntegrationSpec Integration" should {

    "be presented" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupPage

      assert(page.title equals VehicleLookupPage.title)
    }


    "Redirect when no traderBusinessName is cached" in new WebBrowser {
      go to VehicleLookupPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

     "go to the next page when correct data is entered" in  new WebBrowser {
       CacheSetup.businessChooseYourAddress()
       VehicleLookupPage.happyPath

        assert(page.title equals DisposePage.title)
     }

    "display three validation error messages when no referenceNumber is entered" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display two validation error messages when no registrationNumber is entered" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display one validation error message when a registrationNumber is entered containing one character" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "a")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a registrationNumber is entered containing special characters" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "$^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display five validation error messages when no details are entered" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "", vehicleRegistrationNumber = "")

      assert(ErrorPanel.numberOfErrors equals 6)
    }

    "display two validation error messages when only a valid referenceNumber is entered" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, vehicleRegistrationNumber = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display three validation error messages when only a valid registrationNumber is entered" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      VehicleLookupPage.happyPath(webDriver, referenceNumber = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "redirect when no dealerBusinessName is cached" in new WebBrowser {
      go to VehicleLookupPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display previous page when back link is clicked with uprn present" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress(addressWithUprn)
      go to VehicleLookupPage
      click on VehicleLookupPage.back

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display previous page when back link is clicked with no uprn present" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress()
      go to VehicleLookupPage
      click on VehicleLookupPage.back

      assert(page.title equals EnterAddressManuallyPage.title)
    }
  }
}