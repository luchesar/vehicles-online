package views.disposal_of_vehicle

import pages.disposal_of_vehicle.VehicleLookupFailurePage._
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import helpers.UiSpec
import helpers.disposal_of_vehicle.CacheSetup
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle._
import services.session.{SessionState, PlaySessionState}

class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness {

  "VehicleLookupFailureIntegration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      go to VehicleLookupFailurePage

      assert(page.title equals VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup(newSessionState.inner).vehicleLookupFormModel()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only dealerDetails cache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup(newSessionState.inner).
        dealerDetailsIntegration()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to VehicleLookupFailurePage

      click on vehicleLookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to beforeyoustart when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to VehicleLookupFailurePage

      click on beforeYouStart

      assert(page.title equals BeforeYouStartPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) =
    new CacheSetup(sessionState).
      dealerDetailsIntegration().
      vehicleLookupFormModel()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
