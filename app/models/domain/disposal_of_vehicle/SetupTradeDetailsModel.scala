package models.domain.disposal_of_vehicle

case class SetupTradeDetailsModel(traderBusinessName: String, traderPostcode: String)

object SetupTradeDetailsModel {
  import play.api.libs.json.Json
  implicit val setupTradeDetailsModelFormat = Json.format[SetupTradeDetailsModel]
}