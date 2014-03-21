package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import mappings.disposal_of_vehicle.EnterAddressManually._

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {
  def stripPunctuation = {

    def stripLine(inputline: Option[String]) = {
      inputline match {
        case Some(line) => {
          Some(line.replace(invalidCharacter1, StringEmpty).replace(invalidCharacter2, StringEmpty))
        }
        case _ => None
      }
    }

    val line1Stripped = addressAndPostcodeModel.addressLinesModel.line1.replace(invalidCharacter1, StringEmpty)replace(invalidCharacter2, StringEmpty)
    val line2Stripped = stripLine(addressAndPostcodeModel.addressLinesModel.line2)
    val line3Stripped = stripLine(addressAndPostcodeModel.addressLinesModel.line3)
    val line4Stripped = stripLine(addressAndPostcodeModel.addressLinesModel.line4)

    copy(addressAndPostcodeModel =
      addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1Stripped, line2Stripped, line3Stripped, line4Stripped)))
 }
}
