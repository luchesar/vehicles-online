package views.disposal_of_vehicle

import pages.disposal_of_vehicle.DisposeFailurePage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle._

class DisposeFailureIntegrationSpec extends UiSpec with TestHarness {

  "DisposeFailureIntegration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeFailurePage

      page.title should equal(DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to DisposeFailurePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeFailurePage

      click on vehiclelookup

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeFailurePage

      click on setuptradedetails

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      dealerDetailsIntegration().
      vehicleDetailsModelIntegration().
      disposeFormModelIntegration().
      disposeTransactionIdIntegration().
      vehicleRegistrationNumberIntegration()

}
