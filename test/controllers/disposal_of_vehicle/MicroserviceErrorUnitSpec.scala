package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle._
import mappings.common.Postcode
import Postcode._
import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import services.session.{SessionState, PlaySessionState}
import services.fakes.FakeVehicleLookupWebService._
import scala.Some
import mappings.disposal_of_vehicle.VehicleLookup._
import scala.Some
import models.domain.disposal_of_vehicle.{VehicleDetailsRequest, VehicleDetailsResponse}
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupWebService}
import org.mockito.Mockito._
import scala.Some
import org.mockito.Matchers._
import scala.Some
import scala.concurrent.Future
import play.api.libs.json.{Json, JsValue}
import services.fakes.FakeResponse
import controllers.disposal_of_vehicle

class MicroserviceErrorUnitSpec extends UnitSpec {

  "MicroserviceError controller" should {

    "present when microservice is unavailable" in new WithApplication {
      val result = microserviceError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }

  private def microserviceError = new BeforeYouStart()
}
