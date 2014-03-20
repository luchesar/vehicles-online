package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {
  def stripPunctuation = {
    val line1Stripped = addressAndPostcodeModel.addressLinesModel.line1.replace(",", "")

    val line2Stripped = addressAndPostcodeModel.addressLinesModel.line2 match {
      case Some(line) => Some(line.replace(",", ""))
      case _ => None
    }

    val line3Stripped = addressAndPostcodeModel.addressLinesModel.line3 match {
      case Some(line) => Some(line.replace(",", ""))
      case _ => None
    }

    val line4Stripped = addressAndPostcodeModel.addressLinesModel.line4 match {
      case Some(line) => Some(line.replace(",", ""))
      case _ => None
    }

    copy(addressAndPostcodeModel =
      addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1Stripped, line2Stripped, line3Stripped, line4Stripped)))
 }
}
