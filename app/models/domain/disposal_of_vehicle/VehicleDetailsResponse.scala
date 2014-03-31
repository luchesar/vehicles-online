package models.domain.disposal_of_vehicle

case class VehicleDetailsResponse (success: Boolean, message: String, vehicleDetailsDto: VehicleDetailsDto)

object VehicleDetailsResponse {
  import play.api.libs.json.Json
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
}