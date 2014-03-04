package services.fakes

import services.{VehicleLookupService, LoginWebService}
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, VehicleDetailsResponse, VehicleLookupFormModel, AddressViewModel}
import models.domain.change_of_address.{LoginConfirmationModel, LoginResponse, LoginPageModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class FakeVehicleLookupService extends VehicleLookupService {
  val successMessage = "Fake Web Lookup Service - Good response"
  val failMessage = "Fake Web Dispose Service - Bad response"
  val vehicleLookupFormModelSuccess =
    VehicleDetailsResponse(true, message = successMessage, vehicleDetailsModel =
    VehicleDetailsModel(vehicleMake = "Alfa Romeo", vehicleModel = "Alfasud ti", keeperName =
      "Mr Success", keeperAddress = AddressViewModel(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))))
  val vehicleLookupFormModelFailure =
    VehicleDetailsResponse(false, message = failMessage, vehicleDetailsModel =
      VehicleDetailsModel(vehicleMake = "Alfa Romeo", vehicleModel = "Alfasud ti", keeperName =
        "Mr Fail", keeperAddress = AddressViewModel(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))))

  override def invoke(cmd: VehicleLookupFormModel): Future[VehicleDetailsResponse] = Future {
    if (cmd.referenceNumber == "9" * 11) vehicleLookupFormModelFailure
    else vehicleLookupFormModelSuccess
  }
}
