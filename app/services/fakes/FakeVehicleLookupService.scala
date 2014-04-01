package services.fakes

import services.VehicleLookupService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.VehicleDetailsResponse

class FakeVehicleLookupService extends VehicleLookupService {
  val successMessage = "Fake Web Lookup Service - Good response"
  val failMessage = "Fake Web Dispose Service - Bad response"

  def generateVehicleLookupFormModel(statusReturned: Boolean = false, messageReturned: String = failMessage) =
    VehicleDetailsResponse(statusReturned, message = messageReturned, vehicleDetailsDto =
    VehicleDetailsDto(registrationNumber = "PJ056YY", vehicleMake = "Alfa Romeo", vehicleModel = "Alfasud ti", keeperName =
      "Keeper Name", keeperAddress = AddressDto(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))))

  override def invoke(cmd: VehicleDetailsRequest): Future[VehicleDetailsResponse] = Future {
    if (cmd.referenceNumber == "9" * 11) generateVehicleLookupFormModel(false, failMessage)
    else generateVehicleLookupFormModel(true, successMessage)
  }
}


class FakeVehicleLookupWebService extends VehicleLookupWebService