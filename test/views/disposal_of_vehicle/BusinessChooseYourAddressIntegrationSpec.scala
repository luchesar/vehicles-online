package views.disposal_of_vehicle

import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import BusinessChooseYourAddressPage.{sadPath, happyPath, manualAddress, back}
import services.fakes.FakeAddressLookupService.postcodeValid

class BusinessChooseYourAddressIntegrationSpec extends UiSpec with TestHarness {
  private def cacheSetup() = {
    CacheSetup.setupTradeDetails()
  }

  "Business choose your address - Integration" should {
    "be presented" in new WebBrowser {
      cacheSetup()
      go to BusinessChooseYourAddressPage

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      cacheSetup()
      happyPath

      assert(page.title equals VehicleLookupPage.title)
    }

    "go to the manual address entry page when manualAddressButton is clicked" in new WebBrowser {
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on manualAddress

      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on back

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to BusinessChooseYourAddressPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WebBrowser {
      cacheSetup()
      sadPath

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "not display 'No addresses found' message when address service returns addresses" in new WebBrowser {
      SetupTradeDetailsPage.happyPath()

      val result = page.source

      assert(result.contains("No addresses found for that postcode") equals false) // Does not contain message
    }

    "display expected addresses in dropdown when address service returns addresses" in new WebBrowser {
      SetupTradeDetailsPage.happyPath()

      val result = page.source

      assert(BusinessChooseYourAddressPage.getListCount equals 4) // The first option is the "Please select..." and the other options are the addresses.
      assert(result.contains(s"presentationProperty stub, 123, property stub, street stub, town stub, area stub, $postcodeValid"))
      assert(result.contains(s"presentationProperty stub, 456, property stub, street stub, town stub, area stub, $postcodeValid"))
      assert(result.contains(s"presentationProperty stub, 789, property stub, street stub, town stub, area stub, $postcodeValid"))
    }

    "display 'No addresses found' message when address service returns no addresses" in new WebBrowser {
      SetupTradeDetailsPage.submitInvalidPostcode

      val result = page.source

      assert(result.contains("No addresses found for that postcode") equals true) // Does not contain message
    }
  }
}
