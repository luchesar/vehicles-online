package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsModel(registrationNumber: String, vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: AddressViewModel)

object VehicleDetailsModel {
  implicit val vehicleDetailsModel = Json.format[VehicleDetailsModel]
}