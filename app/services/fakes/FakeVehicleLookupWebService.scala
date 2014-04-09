package services.fakes

import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import services.vehicle_lookup.{VehicleLookupWebService}
import play.api.libs.json.Json
import play.api.Logger
import FakeVehicleLookupWebService._

class FakeVehicleLookupWebService extends VehicleLookupWebService {
  override def callVehicleLookupService(request: VehicleDetailsRequest) = Future {
    val vehicleDetails = VehicleDetailsDto(registrationNumber = registrationNumberValid,
      vehicleMake = vehicleMakeValid,
      vehicleModel = vehicleModelValid,
      keeperName = keeperNameValid,
      keeperAddress = AddressDto(uprn = Some(keeperUprnValid), address = Seq("line1", "line2", "line2")))

    val vehicleDetailsResponse =
      if (request.referenceNumber == "9" * 11) VehicleDetailsResponse(success = false,
        message = "Fake Web Dispose Service - Bad response",
        vehicleDetailsDto = vehicleDetails)
      else VehicleDetailsResponse(success = true,
        message = "Fake Web Lookup Service - Good response",
        vehicleDetailsDto = vehicleDetails)

    val responseAsJson = Json.toJson(vehicleDetailsResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeVehicleLookupWebService {
  val registrationNumberValid = "AB12AWR"
  val referenceNumberValid = "12345678910"
  val vehicleMakeValid = "Alfa Romeo"
  val vehicleModelValid = "Alfasud ti"
  val keeperNameValid = "Keeper Name"
  val keeperUprnValid = 10123456789L
}