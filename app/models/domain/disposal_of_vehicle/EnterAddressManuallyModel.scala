package models.domain.disposal_of_vehicle

import mappings.disposal_of_vehicle.EnterAddressManually.EnterAddressManuallyCacheKey
import models.domain.common.{AddressAndPostcodeModel, CacheKey}
import play.api.libs.json.Json

final case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel)

object EnterAddressManuallyModel {
  implicit val JsonFormat = Json.format[EnterAddressManuallyModel]
  implicit val Key = CacheKey[EnterAddressManuallyModel](EnterAddressManuallyCacheKey)
}