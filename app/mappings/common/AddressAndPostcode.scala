package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common
import common.AddressLines._
import mappings.common.Uprn._
import mappings.common.AddressLines._
import mappings.common.Postcode._
import models.domain.common.AddressAndPostcodeModel

object AddressAndPostcode {
  final val AddressAndPostcodeId = "addressAndPostcode"
  final val AddressAndPostcodeCacheKey = "addressAndPostcodeCacheKey"

  val addressAndPostcode: Mapping[AddressAndPostcodeModel] = mapping(
    UprnId -> uprn,
    AddressLinesId -> addressLines.verifying(validAddressLines)
  )(AddressAndPostcodeModel.apply)(AddressAndPostcodeModel.unapply)
}