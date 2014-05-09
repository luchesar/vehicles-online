package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.UiSpec
import services.session.{PlaySessionState, SessionState}
import org.openqa.selenium.WebDriver
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.TraderDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import pages.disposal_of_vehicle.DisposeSuccessPage._

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
      new CookieFactoryForUISpecs().dealerDetailsIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CookieFactoryForUISpecs().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CookieFactoryForUISpecs().
        disposeFormModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CookieFactoryForUISpecs().
        dealerDetailsIntegration().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CookieFactoryForUISpecs().
        disposeFormModelIntegration().
        vehicleDetailsModelIntegration()

      go to DisposeSuccessPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CookieFactoryForUISpecs().
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

    "remove redundant cookies when 'new disposal' button is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeSuccessPage

      click on newDisposal

      // Expected to be removed
      assert(webDriver.manage().getCookieNamed(vehicleLookupDetailsCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(vehicleLookupResponseCodeCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(vehicleLookupFormModelCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormModelCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormTransactionIdCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormTimestampIdCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeFormRegistrationNumberCacheKey) == null)
      assert(webDriver.manage().getCookieNamed(disposeModelCacheKey) == null)

      // Expected to be present
      assert(webDriver.manage().getCookieNamed(SetupTradeDetailsCacheKey) != null)
      assert(webDriver.manage().getCookieNamed(businessChooseYourAddressCacheKey) != null)
      assert(webDriver.manage().getCookieNamed(traderDetailsCacheKey) != null)
    }

    "remove redundant cookies when 'exit' button is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeSuccessPage

      click on exitDisposal

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

  private def cacheSetup()(implicit webDriver: WebDriver) =
    new CookieFactoryForUISpecs().
      setupTradeDetailsIntegration().
      businessChooseYourAddressIntegration().
      dealerDetailsIntegration().
      vehicleDetailsModelIntegration().
      disposeFormModelIntegration().
      disposeTransactionIdIntegration().
      vehicleRegistrationNumberIntegration()
}
