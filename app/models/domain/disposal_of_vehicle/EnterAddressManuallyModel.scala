package models.domain.disposal_of_vehicle

import models.domain.common.{CacheKey, AddressLinesModel, AddressAndPostcodeModel}
import scala.annotation.tailrec
import play.api.libs.json.Json
import mappings.disposal_of_vehicle.EnterAddressManually.EnterAddressManuallyCacheKey

final case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel)

object EnterAddressManuallyModel {
  implicit val JsonFormat = Json.format[EnterAddressManuallyModel]
  implicit val Key = CacheKey[EnterAddressManuallyModel](EnterAddressManuallyCacheKey)
}