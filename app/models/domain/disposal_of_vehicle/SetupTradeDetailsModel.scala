package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import play.api.libs.json.Json

case class SetupTradeDetailsModel(traderBusinessName: String, traderPostcode: String)

object SetupTradeDetailsModel {
  implicit val setupTradeDetailsModelFormat = Json.format[SetupTradeDetailsModel]
  implicit val cacheKey = CacheKey[SetupTradeDetailsModel](SetupTradeDetailsCacheKey)
}