package models.domain.disposal_of_vehicle

case class VehicleDetailsResponse (responseCode: Option[String], vehicleDetailsDto: Option[VehicleDetailsDto])

object VehicleDetailsResponse {
  import play.api.libs.json.Json
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
}