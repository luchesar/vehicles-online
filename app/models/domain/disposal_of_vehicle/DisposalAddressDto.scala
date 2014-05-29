package models.domain.disposal_of_vehicle

case class DisposalAddressDto(line: Seq[String], postTown: Option[String], postCode: String, uprn: Option[Long])

object DisposalAddressDto {
  import play.api.libs.json.Json
  implicit val addressDto = Json.writes[DisposalAddressDto]
}