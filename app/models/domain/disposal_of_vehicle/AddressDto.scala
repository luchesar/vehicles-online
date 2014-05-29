package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

final case class AddressDto(uprn: Option[Long], address: Seq[String])

object AddressDto {
  implicit val JsonFormat = Json.format[AddressDto]
}