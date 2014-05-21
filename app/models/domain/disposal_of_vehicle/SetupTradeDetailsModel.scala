package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import play.api.libs.json.Json

case class SetupTradeDetailsModel(traderBusinessName: String, traderPostcode: String)

object SetupTradeDetailsModel {
  implicit final val JsonFormat = Json.format[SetupTradeDetailsModel]
  implicit final val Key = CacheKey[SetupTradeDetailsModel](SetupTradeDetailsCacheKey)
}