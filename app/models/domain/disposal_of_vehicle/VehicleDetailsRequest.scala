package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsRequest(referenceNumber: String, registrationNumber: String)

object VehicleDetailsRequest {
  implicit val JsonFormat = Json.format[VehicleDetailsRequest]
}