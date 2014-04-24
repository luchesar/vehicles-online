package services.fakes

import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import services.vehicle_lookup.VehicleLookupWebService
import play.api.libs.json.Json
import play.api.Logger
import FakeVehicleLookupWebService._
import play.api.http.Status._
import scala.Some

class FakeVehicleLookupWebService extends VehicleLookupWebService {
  override def callVehicleLookupService(request: VehicleDetailsRequest) = Future {
    val vehicleDetailsResponse =
      if (request.referenceNumber == "9" * 11) vehicleDetailsResponseFailure
      else vehicleDetailsResponseSuccess

    val responseAsJson = Json.toJson(vehicleDetailsResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeVehicleLookupWebService {
  val registrationNumberValid = "AB12AWR"
  val registrationNumberWithSpaceValid = "AB12 AWR"
  val referenceNumberValid = "12345678910"
  val vehicleMakeValid = "Alfa Romeo"
  val vehicleModelValid = "Alfasud ti"
  val keeperNameValid = "Keeper Name"
  val keeperUprnValid = 10123456789L
  val consentValid = "true"

  private val vehicleDetails = VehicleDetailsDto(registrationNumber = registrationNumberValid,
    vehicleMake = vehicleMakeValid,
    vehicleModel = vehicleModelValid)
  val vehicleDetailsResponseSuccess = VehicleDetailsResponse(responseCode = None,
    vehicleDetailsDto = Some(vehicleDetails))
  val vehicleDetailsResponseFailure = VehicleDetailsResponse(responseCode = Some("fail"),
    vehicleDetailsDto = None)
}