package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.BeforeYouStartPage.startNow
import pages.disposal_of_vehicle._
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import mappings.disposal_of_vehicle.RelatedCacheKeys

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

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.map(cacheKey => assert(webDriver.manage().getCookieNamed(cacheKey) == null))
    }
  }
}