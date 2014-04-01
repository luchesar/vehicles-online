package services.fakes

import services.VehicleLookupService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import services.vehicle_lookup.VehicleLookupWebService
import play.api.libs.json.Json

class FakeVehicleLookupWebService extends VehicleLookupWebService {
  override def callVehicleLookupService(request: VehicleDetailsRequest) = Future {
    val vehicleDetails = VehicleDetailsDto(registrationNumber = "PJ056YY",
      vehicleMake = "Alfa Romeo",
      vehicleModel = "Alfasud ti",
      keeperName = "Keeper Name",
      keeperAddress = AddressDto(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2")))

    val vehicleDetailsResponse =
      if (request.referenceNumber == "9" * 11) VehicleDetailsResponse(false,
        message = "Fake Web Dispose Service - Bad response",
        vehicleDetailsDto = vehicleDetails)
      else VehicleDetailsResponse(true,
        message = "Fake Web Lookup Service - Good response",
        vehicleDetailsDto = vehicleDetails)

    val responseAsJson = Json.toJson(vehicleDetailsResponse)

    new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}