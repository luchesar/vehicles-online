package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsResponse (success: Boolean, message: String, vehicleDetailsModel: VehicleDetailsModel)

object VehicleDetailsResponse {
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
}