package models.domain.disposal_of_vehicle

case class VehicleDetailsModel(registrationNumber: String, vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: AddressViewModel)

object VehicleDetailsModel {
  import play.api.libs.json.Json
  implicit val vehicleDetailsModel = Json.format[VehicleDetailsModel]
}

//case class VehicleDetailsDto(registrationNumber: String, vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: AddressViewModel)
//
//object VehicleDetailsDto {
//  import play.api.libs.json.Json
//  implicit val vehicleDetailsModel = Json.format[VehicleDetailsModel]
//
////  def apply(model: VehicleDetailsModel) = VehicleDetailsDto()
//}