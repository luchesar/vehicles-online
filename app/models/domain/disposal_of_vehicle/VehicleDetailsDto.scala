package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsDto(registrationNumber: String, vehicleMake: String, vehicleModel: String)

object VehicleDetailsDto {
  implicit final val JsonFormat = Json.format[VehicleDetailsDto]
}