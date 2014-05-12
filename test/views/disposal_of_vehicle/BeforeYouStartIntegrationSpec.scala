package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.BeforeYouStartPage.startNow
import pages.disposal_of_vehicle._
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.disposal_of_vehicle.TraderDetails._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.Dispose._

class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness  {

  "BeforeYouStart Integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage

      assert(page.title equals BeforeYouStartPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to BeforeYouStartPage

      click on startNow

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "remove redundant cookies when 'exit' button is clicked" in new WebBrowser {
      def cacheSetup()(implicit webDriver: WebDriver) =
        CookieFactoryForUISpecs.setupTradeDetailsIntegration().
          businessChooseYourAddressIntegration().
          dealerDetailsIntegration().
          vehicleDetailsModelIntegration().
          disposeFormModelIntegration().
          disposeTransactionIdIntegration().
          vehicleRegistrationNumberIntegration()

      go to BeforeYouStartPage
      cacheSetup()
      go to BeforeYouStartPage

      // Expected to be removed
      assert(webDriver.manage().getCookieNamed(SetupTradeDetailsCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(businessChooseYourAddressCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(traderDetailsCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(vehicleLookupDetailsCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(vehicleLookupResponseCodeCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(vehicleLookupFormModelCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormModelCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormTransactionIdCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormTimestampIdCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormRegistrationNumberCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeModelCacheKey) == null)
    }
  }
}