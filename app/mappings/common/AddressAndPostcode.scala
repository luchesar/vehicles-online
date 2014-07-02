package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.mapping
import constraints.common
import common.AddressLines.validAddressLines
import mappings.common.Uprn.{uprn, UprnId}
import mappings.common.AddressLines.{addressLines, AddressLinesId}
import models.domain.common.AddressAndPostcodeModel

object AddressAndPostcode {
  final val AddressAndPostcodeId = "addressAndPostcode"
  final val AddressAndPostcodeCacheKey = "addressAndPostcodeCacheKey"

  val addressAndPostcode: Mapping[AddressAndPostcodeModel] = mapping(
    UprnId -> uprn,
    AddressLinesId -> addressLines.verifying(validAddressLines)
  )(AddressAndPostcodeModel.apply)(AddressAndPostcodeModel.unapply)
}