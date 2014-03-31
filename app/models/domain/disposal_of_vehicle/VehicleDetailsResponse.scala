package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

// TODO Replace VehicleDetailsModel with a Dto because we don't want form models being sent end to end. Ian has volunteered
case class VehicleDetailsResponse (success: Boolean, message: String, vehicleDetailsModel: VehicleDetailsModel)

object VehicleDetailsResponse {
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
}