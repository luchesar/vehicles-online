package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.common.ErrorPanel
import pages.disposal_of_vehicle.BusinessChooseYourAddressPage.{sadPath, happyPath, manualAddress, back}
import pages.disposal_of_vehicle._
import services.fakes.FakeAddressLookupService.postcodeValid

final class BusinessChooseYourAddressIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage
      page.title should equal(BusinessChooseYourAddressPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to BusinessChooseYourAddressPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "not display 'No addresses found' message when address service returns addresses" in new WebBrowser {
      SetupTradeDetailsPage.happyPath()

      page.source.contains("No addresses found for that postcode") should equal(false) // Does not contain message
    }

    "display expected addresses in dropdown when address service returns addresses" in new WebBrowser {
      SetupTradeDetailsPage.happyPath()

      BusinessChooseYourAddressPage.getListCount should equal(4) // The first option is the "Please select..." and the other options are the addresses.
      page.source should include(s"presentationProperty stub, 123, property stub, street stub, town stub, area stub, $postcodeValid")
      page.source should include(s"presentationProperty stub, 456, property stub, street stub, town stub, area stub, $postcodeValid")
      page.source should include(s"presentationProperty stub, 789, property stub, street stub, town stub, area stub, $postcodeValid")
    }

    "display 'No addresses found' message when address service returns no addresses" in new WebBrowser {
      SetupTradeDetailsPage.submitInvalidPostcode

      page.source should include("No addresses found for that postcode") // Does not contain the positive message
    }
  }

  "manualAddress button" should {
    "go to the manual address entry page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on manualAddress

      page.title should equal(EnterAddressManuallyPage.title)
    }
  }

  "back button" should {
    "display previous page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on back

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  "select button" should {
    "go to the next page when correct data is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      happyPath

      page.title should equal(VehicleLookupPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      sadPath

      ErrorPanel.numberOfErrors should equal(1)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.setupTradeDetailsIntegration()
}
