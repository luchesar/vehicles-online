package models.domain.common

import play.api.libs.json.Json
import mappings.common.AddressAndPostcode.AddressAndPostcodeCacheKey

case class AddressAndPostcodeModel(uprn: Option[Int] = None, addressLinesModel: AddressLinesModel, postcode: String) {
  def toViewFormat: Seq[String] = addressLinesModel.toViewFormat :+ postcode
}

object AddressAndPostcodeModel {
  implicit val addressAndPostcodeModelFormat = Json.format[AddressAndPostcodeModel]
  implicit val cacheKey = CacheKey[AddressAndPostcodeModel](AddressAndPostcodeCacheKey)
}