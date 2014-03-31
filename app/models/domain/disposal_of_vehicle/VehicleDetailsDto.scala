package models.domain.disposal_of_vehicle

case class VehicleDetailsDto(registrationNumber: String, vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: AddressViewModel)

object VehicleDetailsDto {
  import play.api.libs.json.Json
  implicit val vehicleDetailsDto = Json.format[VehicleDetailsDto]
}