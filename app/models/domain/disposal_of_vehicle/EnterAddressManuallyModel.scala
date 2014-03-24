package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import mappings.common.StripFromString._
import mappings.disposal_of_vehicle.EnterAddressManually

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {

  def stripCharsNotAccepted = {

    val line1 = stripEndOfLineRequiredField(addressAndPostcodeModel.addressLinesModel.line1)
    val line2 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line2)
    val line3 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line3)
    val line4 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line4)

    // code below is no longer being used -- keep for now if business rules require us to strip any characters from inside the line of each address
    //val line1Stripped = stripLineRequiredField(line1, EnterAddressManually.charsNotAcceptedInLine)
    //val line2Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line2, EnterAddressManually.charsNotAcceptedInLine)
    //val line3Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line3, EnterAddressManually.charsNotAcceptedInLine)
    //val line4Stripped = stripLineOptionalField(addressAndPostcodeModel.addressLinesModel.line4, EnterAddressManually.charsNotAcceptedInLine)

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1, line2, line3, line4)))
 }
}
