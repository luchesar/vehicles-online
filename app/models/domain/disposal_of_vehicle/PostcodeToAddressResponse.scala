package models.domain.disposal_of_vehicle

case class PostcodeToAddressResponse(addresses: Seq[UprnAddressPair])

object PostcodeToAddressResponse{
  import play.api.libs.json.Json
  implicit val postcodeToAddressResponseFormat = Json.format[PostcodeToAddressResponse]
}