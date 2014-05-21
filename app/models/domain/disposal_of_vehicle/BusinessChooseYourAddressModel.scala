package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import play.api.libs.json.Json

case class BusinessChooseYourAddressModel(uprnSelected: Long)

object BusinessChooseYourAddressModel {
  implicit final val JsonFormat = Json.format[BusinessChooseYourAddressModel]
  implicit final val Key = CacheKey[BusinessChooseYourAddressModel](value = BusinessChooseYourAddressCacheKey)
}
