package models.domain.disposal_of_vehicle

import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import play.api.libs.json.Json

final case class BusinessChooseYourAddressModel(uprnSelected: Long)

object BusinessChooseYourAddressModel {
  implicit val JsonFormat = Json.format[BusinessChooseYourAddressModel]
  implicit val Key = CacheKey[BusinessChooseYourAddressModel](value = BusinessChooseYourAddressCacheKey)
}
