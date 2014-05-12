package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle.VehicleLookupFailurePage._
import pages.disposal_of_vehicle._
import mappings.disposal_of_vehicle.VehicleLookup._

class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness {

  "VehicleLookupFailureIntegration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupFailurePage

      assert(page.title equals VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.vehicleLookupFormModelIntegration()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only dealerDetails cache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.dealerDetailsIntegration()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VehicleLookupFailurePage

      click on vehicleLookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to beforeyoustart when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VehicleLookupFailurePage

      click on beforeYouStart

      assert(page.title equals BeforeYouStartPage.title)
    }

    "remove redundant cookies when displayed" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupFailurePage

      assert(webDriver.manage().getCookieNamed(vehicleLookupResponseCodeCacheKey) == null)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      dealerDetailsIntegration().
      vehicleLookupFormModelIntegration()
}
