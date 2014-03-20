package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._
import scala.Some
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec

class VehicleLookupFailureUnitSpec extends UnitSpec {

  "VehicleLookupFailurePage - Controller" should {

    "present" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      status(result) should equal(OK)
    }

    "redirect to vehiclelookup on submit" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)

      redirectLocation(result) should equal (Some(VehicleLookupPage.address))
    }

    "redirect to setuptraderdetails when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)


      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on if only BusinessChooseYourAddress cache is populated" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on if only VehicleLookupFormModelCache is populated" in new WithApplication {
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }
  }
}
