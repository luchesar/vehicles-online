package models.domain.disposal_of_vehicle

// TODO : Consider the implications of using strings fot dates, and interaction with the microservice
case class DisposalAddressDto(line: Seq[String], postTown: Option[String], postCode: String, uprn: Option[Long])

object DisposalAddressDto {
  import play.api.libs.json.Json
  implicit val addressDto = Json.format[DisposalAddressDto] // TODO US66 if we only ever write and never read then we can change the 'Json.format' to 'Json.writes' to reduce the compilation.
}