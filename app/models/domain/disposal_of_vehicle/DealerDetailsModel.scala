package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.DealerDetails.dealerDetailsCacheKey
import play.api.libs.json.Json

case class DealerDetailsModel(dealerName: String, dealerAddress: AddressViewModel)

object DealerDetailsModel {
  implicit val dealerDetailsModelFormat = Json.format[DealerDetailsModel]
  implicit val cacheKey = CacheKey[DealerDetailsModel](value = dealerDetailsCacheKey)
}
