package models.domain.disposal_of_vehicle

import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import models.domain.common.CacheKey
import play.api.libs.json.Json

final case class BusinessChooseYourAddressModel(uprnSelected: String)

object BusinessChooseYourAddressModel {
  implicit val JsonFormat = Json.format[BusinessChooseYourAddressModel]
  implicit val Key = CacheKey[BusinessChooseYourAddressModel](value = BusinessChooseYourAddressCacheKey)
}