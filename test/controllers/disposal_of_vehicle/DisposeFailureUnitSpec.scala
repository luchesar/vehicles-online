package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.{CookieFactory, CacheSetup}
import helpers.UnitSpec
import services.session.{SessionState, PlaySessionState}

class DisposeFailureUnitSpec extends UnitSpec {

  "DisposalFailure - Controller" should {
    "present" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails())
      val result = disposeFailure(sessionState).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails())
      val result = disposeFailure(sessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val request = FakeRequest().withSession()
      val result = disposeFailure(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def cacheSetup(sessionState: SessionState) = {
    new CacheSetup(sessionState).
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId()
  }

  private def disposeFailure(sessionState: DisposalOfVehicleSessionState) =
    new DisposeFailure(sessionState)

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}

