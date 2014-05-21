package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import play.api.libs.json.Json

case class BusinessChooseYourAddressModel(uprnSelected: Long)

object BusinessChooseYourAddressModel {
  implicit val JsonFormat = Json.format[BusinessChooseYourAddressModel]
  implicit val cacheKey = CacheKey[BusinessChooseYourAddressModel](value = BusinessChooseYourAddressCacheKey)
}
