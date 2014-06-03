package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

final case class VehicleDetailsRequest(referenceNumber: String,
                                       registrationNumber: String,
                                       trackingId: String,
                                       userName: String)

object VehicleDetailsRequest {
  implicit val JsonFormat = Json.format[VehicleDetailsRequest]
}