package mappings.disposal_of_vehicle

import play.api.data.Mapping
import play.api.data.Forms._
import mappings.disposal_of_vehicle.AddressLines._
import constraints.AddressLines._
import mappings.common.PostCode._
import models.domain.disposal_of_vehicle.AddressAndPostcodeModel
import mappings.common.Uprn
import mappings.common.Uprn.uprn

object AddressAndPostcode {
  val id = "addressAndPostcode"
  val postcodeMaxLength = "9"

  val addressAndPostcode: Mapping[AddressAndPostcodeModel] = mapping(
    Uprn.id -> uprn,
    AddressLines.id -> addressLines.verifying(validAddressLines),
    postcodeId -> postcode()
  )(AddressAndPostcodeModel.apply)(AddressAndPostcodeModel.unapply)

}