package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UiSpec
import VehicleLookupFailurePage._
import services.session.{SessionState, PlaySessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState

class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness  {

  "VehicleLookupFailureIntegration" should {

    "be presented" in new WebBrowser {
      cacheSetup(newSessionState.inner)

      go to VehicleLookupFailurePage

      assert(page.title equals VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      new CacheSetup(newSessionState.inner).vehicleLookupFormModel()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only BusinessChooseYourAddress cache is populated" in new WebBrowser {
      new CacheSetup(newSessionState.inner).businessChooseYourAddress()

      go to VehicleLookupFailurePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      cacheSetup(newSessionState.inner)
      go to VehicleLookupFailurePage

      click on vehicleLookup

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WebBrowser {
      cacheSetup(newSessionState.inner)
      go to VehicleLookupFailurePage

      click on setupTradeDetails

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState) = 
    new CacheSetup(sessionState).
      businessChooseYourAddress().
      vehicleLookupFormModel()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
