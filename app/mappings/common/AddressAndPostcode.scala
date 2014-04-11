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
  val addressAndPostcodeId = "addressAndPostcode"

  val addressAndPostcode: Mapping[AddressAndPostcodeModel] = mapping(
    uprnId -> uprn,
    addressLinesId -> addressLines.verifying(validAddressLines),
    postcodeId -> postcode
  )(AddressAndPostcodeModel.apply)(AddressAndPostcodeModel.unapply)

}