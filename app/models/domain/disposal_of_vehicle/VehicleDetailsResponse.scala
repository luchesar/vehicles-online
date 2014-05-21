package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class VehicleDetailsResponse (responseCode: Option[String], vehicleDetailsDto: Option[VehicleDetailsDto])

object VehicleDetailsResponse {
  implicit final val JsonFormat = Json.format[VehicleDetailsResponse]
}