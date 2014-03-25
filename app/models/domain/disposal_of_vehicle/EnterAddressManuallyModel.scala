package models.domain.disposal_of_vehicle

import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}

case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {

  /*
  def stripEndOfLineRequiredField(inputline: String): String = {
    inputline.takeRight(1) match {
      case (",") => inputline.dropRight(1)
      case (".") => inputline.dropRight(1)
      case _ => inputline
    }
  }

  def stripEndOfLineOptionalField(inputline: Option[String]): Option[String] = {
    inputline match {
      case Some(inputline) => {
        inputline.takeRight(1) match {
          case (",") => Some(inputline.dropRight(1))
          case (".") => Some(inputline.dropRight(1))
          case _ => Some(inputline)
        }
      }
      case _ => None
    }
  }
  */

  //TODO - discuss use of below two methods which remove multiple commas and full stops from end of address
  val charToExclude = List(",",".")

  def stripEndOfLineRequiredFieldLoop(inputline:String): String = {
    var line = inputline
    while (line.takeRight(1) == charToExclude(0) || line.takeRight(1) == charToExclude(1)) {
      line = line.dropRight(1)
    }
    return line
  }

  def stripEndOfLineOptionalFieldLoop(inputline:Option[String]): Option[String] = {
    inputline match {
      case Some(inputline) => {
        var line = inputline
        while (line.takeRight(1) == charToExclude(0) || line.takeRight(1) == charToExclude(1)){
          line = line.dropRight(1)
        }
        return Some(line)
        }
      case _ => None
    }
  }

  def stripCharsNotAccepted = {

    val line1 = stripEndOfLineRequiredFieldLoop(addressAndPostcodeModel.addressLinesModel.line1)
    val line2 = stripEndOfLineOptionalFieldLoop(addressAndPostcodeModel.addressLinesModel.line2)
    val line3 = stripEndOfLineOptionalFieldLoop(addressAndPostcodeModel.addressLinesModel.line3)
    val line4 = stripEndOfLineOptionalFieldLoop(addressAndPostcodeModel.addressLinesModel.line4)

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(line1, line2, line3, line4)))
 }
}
