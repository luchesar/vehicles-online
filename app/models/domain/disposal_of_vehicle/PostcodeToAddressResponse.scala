package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class PostcodeToAddressResponse(addresses: Seq[UprnAddressPair])

object PostcodeToAddressResponse{

  implicit val JsonFormat = Json.format[PostcodeToAddressResponse]
}