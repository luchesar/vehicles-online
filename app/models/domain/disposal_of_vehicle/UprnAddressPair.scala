package models.domain.disposal_of_vehicle

case class UprnAddressPair(uprn: String, address: String)

object UprnAddressPair {
  import play.api.libs.json.Json
  implicit val UprnAddressPairFormat = Json.format[UprnAddressPair]
}
