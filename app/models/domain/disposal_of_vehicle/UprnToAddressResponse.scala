package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class UprnToAddressResponse(addressViewModel: Option[AddressViewModel])

object UprnToAddressResponse {
  implicit val JsonFormat = Json.format[UprnToAddressResponse]
}