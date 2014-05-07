package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import services.fakes.FakeAddressLookupService._
import EnterAddressManuallyPage._
import services.session.{PlaySessionState, SessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {

  "EnterAddressManually integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      go to EnterAddressManuallyPage

      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath()

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPathMandatoryFieldsOnly()

      assert(page.title equals VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      sadPath

      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) =
    new CacheSetup(sessionState).setupTradeDetailsIntegration()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
