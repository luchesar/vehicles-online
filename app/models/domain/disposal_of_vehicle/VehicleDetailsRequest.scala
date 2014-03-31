package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsRequest(referenceNumber: String, registrationNumber: String, consent: String)

object VehicleDetailsRequest {
  implicit val vehicleDetailsRequest = Json.writes[VehicleDetailsRequest]
}