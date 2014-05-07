package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey

case class SetupTradeDetailsModel(traderBusinessName: String, traderPostcode: String)

object SetupTradeDetailsModel {
  import play.api.libs.json.Json
  implicit val setupTradeDetailsModelFormat = Json.format[SetupTradeDetailsModel]

  implicit val cacheKey = CacheKey[SetupTradeDetailsModel](SetupTradeDetailsCacheKey)
}