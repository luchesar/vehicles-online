package models.domain.disposal_of_vehicle

case class VehicleDetailsResponse (success: Boolean, message: String, vehicleDetailsDto: VehicleDetailsDto)  // TODO vehicleDetailsDto should be an option as when we fail to find any vehicle we should return None

object VehicleDetailsResponse {
  import play.api.libs.json.Json
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
}