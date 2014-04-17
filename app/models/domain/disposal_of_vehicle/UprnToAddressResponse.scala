package models.domain.disposal_of_vehicle

case class UprnToAddressResponse(addressViewModel: Option[AddressViewModel])

object UprnToAddressResponse {

  import play.api.libs.json.Json

  implicit val uprnToAddressResponseFormat = Json.format[UprnToAddressResponse]
}