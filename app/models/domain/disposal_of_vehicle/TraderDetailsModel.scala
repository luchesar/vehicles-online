package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import play.api.libs.json.Json

case class TraderDetailsModel(traderName: String, traderAddress: AddressViewModel)

object TraderDetailsModel {
  implicit val traderDetailsModelFormat = Json.format[TraderDetailsModel]
  implicit val cacheKey = CacheKey[TraderDetailsModel](value = TraderDetailsCacheKey)
}
