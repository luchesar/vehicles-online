package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import mappings.common.StripFromString._
import mappings.disposal_of_vehicle.EnterAddressManually

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {

  def stripCharsNotAccepted = {
    
    val line1Stripped = stripLineRequiredField(addressAndPostcodeModel.addressLinesModel.line1, EnterAddressManually.charsNotAccepted)
    val line2Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line2, EnterAddressManually.charsNotAccepted)
    val line3Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line3, EnterAddressManually.charsNotAccepted)
    val line4Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line4, EnterAddressManually.charsNotAccepted)

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1Stripped, line2Stripped, line3Stripped, line4Stripped)))
 }
}
