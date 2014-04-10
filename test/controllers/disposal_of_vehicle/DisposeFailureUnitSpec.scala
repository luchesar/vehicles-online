package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import controllers.{disposal_of_vehicle}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec

class DisposeFailureUnitSpec extends UnitSpec {
  private def cacheSetup() = {
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.disposeFormModel()
    CacheSetup.disposeTransactionId()
  }

  "DisposalFailure - Controller" should {
    "present" in new WithApplication {
      cacheSetup
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeFailure.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      cacheSetup
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }
}

