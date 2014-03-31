package models.domain.disposal_of_vehicle

case class AddressDto(uprn: Option[Long], address: Seq[String])

object AddressDto {
  import play.api.libs.json.Json
  implicit val addressDto = Json.format[AddressDto]
}