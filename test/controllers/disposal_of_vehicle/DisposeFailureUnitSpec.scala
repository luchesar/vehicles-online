package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import controllers.{disposal_of_vehicle}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec

class DisposeFailureUnitSpec extends UnitSpec {

  "DisposalFailure - Controller" should {
    "present" in new WithApplication {
      cacheSetup
      val result = disposal_of_vehicle.DisposeFailure.present(newFakeRequest)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      cacheSetup
      val result = disposal_of_vehicle.DisposeFailure.submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val result = disposal_of_vehicle.DisposeFailure.submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def cacheSetup() = {
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.disposeFormModel()
    CacheSetup.disposeTransactionId()
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }
}

