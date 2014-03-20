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
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.present(request)

      status(result) should equal(OK)
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.address))
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }
  }
}
