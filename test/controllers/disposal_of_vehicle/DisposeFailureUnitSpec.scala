package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec
import services.session.{SessionState, PlaySessionState}

class DisposeFailureUnitSpec extends UnitSpec {

  "DisposalFailure - Controller" should {
    "present" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val result = disposeFailure(sessionState).present(newFakeRequest)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val result = disposeFailure(sessionState).submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val result = disposeFailure(newSessionState).submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def cacheSetup(sessionState: SessionState) = {
    val cacheSetup = new CacheSetup(sessionState)
    cacheSetup.businessChooseYourAddress()
    cacheSetup.vehicleDetailsModel()
    cacheSetup.disposeFormModel()
    cacheSetup.disposeTransactionId()
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }

  private def disposeFailure(sessionState: DisposalOfVehicleSessionState) =
    new DisposeFailure(sessionState)

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}

