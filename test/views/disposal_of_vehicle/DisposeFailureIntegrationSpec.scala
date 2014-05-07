package views.disposal_of_vehicle

import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec
import DisposeFailurePage._
import services.session.{SessionState, PlaySessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class DisposeFailureIntegrationSpec extends UiSpec with TestHarness {

  "DisposeFailureIntegration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposeFailurePage

      assert(page.title equals DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to DisposeFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposeFailurePage

      click on vehiclelookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposeFailurePage

      click on setuptradedetails

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) =
    new CacheSetup(sessionState).
      dealerDetailsIntegration().
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId().
      vehicleRegistrationNumber()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
