package models.domain.disposal_of_vehicle

case class VehicleDetailsRequest(referenceNumber: String, registrationNumber: String)

object VehicleDetailsRequest {
  import play.api.libs.json.Json
  implicit val vehicleDetailsRequest = Json.writes[VehicleDetailsRequest]
}