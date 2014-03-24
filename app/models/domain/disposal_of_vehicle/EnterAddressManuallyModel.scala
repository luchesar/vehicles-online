package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {

  def stripEndOfLineRequiredField(inputline: String): String = {
    inputline.takeRight(1) match {
      case (",") => inputline.dropRight(1)
      case (".") => inputline.dropRight(1)
      case (",.") => inputline.dropRight(2)
      case (".,") => inputline.dropRight(2)
      case _ => inputline
    }
  }

  def stripEndOfLineOptionalField(inputline: Option[String]): Option[String] = {
    inputline match {
      case Some(inputline) => {
        inputline.takeRight(1) match {
          case (",") => Some(inputline.dropRight(1))
          case (".") => Some(inputline.dropRight(1))
          case (",.") => Some(inputline.dropRight(2))
          case (".,") => Some(inputline.dropRight(2))
          case _ => Some(inputline)
        }
      }
      case _ => None
    }
  }

  def stripCharsNotAccepted = {

    val line1 = stripEndOfLineRequiredField(addressAndPostcodeModel.addressLinesModel.line1)
    val line2 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line2)
    val line3 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line3)
    val line4 = stripEndOfLineOptionalField(addressAndPostcodeModel.addressLinesModel.line4)

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1, line2, line3, line4)))
 }
}
