package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import mappings.disposal_of_vehicle.RelatedCacheKeys
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import pages.disposal_of_vehicle.ErrorPage.startAgain

final class ErrorIntegrationSpec extends UiSpec with TestHarness {
  "present" should {
    "display page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to ErrorPage

      assert(page.title equals ErrorPage.title)
    }
  }

  "submit" should {
    "remove redundant cookies when 'start again' button is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to ErrorPage

      click on startAgain

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.foreach(cacheKey => assert(webDriver.manage().getCookieNamed(cacheKey) == null))
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetailsIntegration().
      businessChooseYourAddressIntegration().
      dealerDetailsIntegration().
      vehicleDetailsModelIntegration().
      disposeFormModelIntegration().
      disposeTransactionIdIntegration().
      vehicleRegistrationNumberIntegration()
}
