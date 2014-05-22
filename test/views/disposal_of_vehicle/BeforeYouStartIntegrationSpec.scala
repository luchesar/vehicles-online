package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import mappings.disposal_of_vehicle.RelatedCacheKeys
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle.BeforeYouStartPage.startNow
import pages.disposal_of_vehicle._

final class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to BeforeYouStartPage

      page.title should equal(BeforeYouStartPage.title)
    }

    "remove redundant cookies (needed for when a user exits the service and comes back)" in new WebBrowser {
      def cacheSetup()(implicit webDriver: WebDriver) =
        CookieFactoryForUISpecs.setupTradeDetailsIntegration().
          businessChooseYourAddressIntegration().
          enterAddressManuallyIntegration().
          dealerDetailsIntegration().
          vehicleDetailsModelIntegration().
          disposeFormModelIntegration().
          disposeTransactionIdIntegration().
          vehicleRegistrationNumberIntegration()

      go to BeforeYouStartPage
      cacheSetup()
      go to BeforeYouStartPage

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.foreach(cacheKey => webDriver.manage().getCookieNamed(cacheKey) should equal(null))
    }
  }

  "startNow button" should {
    "go to next page" in new WebBrowser {
      go to BeforeYouStartPage

      click on startNow

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }
}