package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {

  def stripEndOfLineRequired(inputline:String): String = {
    val submitRegex = """^[A-Za-z0-9\s\-]*$""".r
    inputline.takeRight(1) match {
      case a if !submitRegex.pattern.matcher(a).matches => stripEndOfLineRequired(inputline.dropRight(1))
      case _ => inputline
    }
  }

  def stripEndOfLineOptionalField(inputline:Option[String]): Option[String] = {
    inputline match {
      case Some(line) => Some(stripEndOfLineRequired(line))
      case _ => None
    }
  }

  def stripCharsNotAccepted = {
    val line1 = stripEndOfLineRequired(addressAndPostcodeModel.addressLinesModel.line1)
    val line2 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line2)
    val line3 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line3)
    val line4 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line4)

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1, line2, line3, line4)))
 }
}
