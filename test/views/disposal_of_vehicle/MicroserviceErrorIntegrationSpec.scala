package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.UiSpec
import MicroServiceErrorPage.{tryAgain, exit}
import services.session.{PlaySessionState, SessionState}
import helpers.disposal_of_vehicle.CacheSetup
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class MicroserviceErrorIntegrationSpec extends UiSpec with TestHarness {

  "MicroserviceError Integration" should {

    "be presented" in new WebBrowser {
      go to MicroServiceErrorPage

      assert(page.title equals MicroServiceErrorPage.title)
    }

    "redirect to vehiclelookup when try again is clicked" in new WebBrowser {
      cacheSetup(newSessionState.inner)
      go to MicroServiceErrorPage

      click on tryAgain

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when no details are cachd and try again is click" in new WebBrowser {
      go to MicroServiceErrorPage

      click on tryAgain

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect to beforeyoustart when exit is clicked" in new WebBrowser {
      cacheSetup(newSessionState.inner)
      go to MicroServiceErrorPage

      click on exit

      assert(page.title equals BeforeYouStartPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) =
    new CacheSetup(sessionState).
      setupTradeDetailsIntegration().
      businessChooseYourAddress()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
