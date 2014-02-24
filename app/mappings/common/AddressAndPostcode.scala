package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.AddressLines._
import mappings.common.PostCode._
import mappings.common.Uprn.uprn
import models.domain.disposal_of_vehicle.AddressAndPostcodeModel
import mappings.common.AddressLines._

object AddressAndPostcode {
  val id = "addressAndPostcode"
  val postcodeMaxLength = "9"

  val addressAndPostcode: Mapping[AddressAndPostcodeModel] = mapping(
    Uprn.id -> uprn,
    AddressLines.id -> addressLines.verifying(validAddressLines),
    postcodeId -> postcode()
  )(AddressAndPostcodeModel.apply)(AddressAndPostcodeModel.unapply)

}