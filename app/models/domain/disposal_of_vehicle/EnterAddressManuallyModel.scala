package models.domain.disposal_of_vehicle

import models.domain.common.{CacheKey, AddressLinesModel, AddressAndPostcodeModel}
import scala.annotation.tailrec
import play.api.libs.json.Json
import mappings.disposal_of_vehicle.EnterAddressManually.EnterAddressManuallyCacheKey

final case class EnterAddressManuallyModel(addressAndPostcodeModel: AddressAndPostcodeModel) {
  def stripCharsNotAccepted: EnterAddressManuallyModel = {
    @tailrec
    def stripEndOfLine(inputLine: Option[String]): Option[String] = inputLine match {
      case Some(line) =>
        val whitelist = """^[A-Za-z0-9\s\-]*$""".r
        line.takeRight(1) match {
          case lastChar if !whitelist.pattern.matcher(lastChar).matches => stripEndOfLine(Some(line.dropRight(1)))
          case _ => inputLine
        }
      case _ => None
    }

    val buildingNameOrNumber = stripEndOfLine(Some(addressAndPostcodeModel.addressLinesModel.buildingNameOrNumber))
    require(buildingNameOrNumber.isDefined, "Address buildingNameOrNumber must have content")

    val line2 = stripEndOfLine(addressAndPostcodeModel.addressLinesModel.line2)
    val line3 = stripEndOfLine(addressAndPostcodeModel.addressLinesModel.line3)
    
    val postTown = stripEndOfLine(Some(addressAndPostcodeModel.addressLinesModel.postTown))
    require(postTown.isDefined, "Address postTown must have content")

    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(addressLinesModel = AddressLinesModel(buildingNameOrNumber.get, line2, line3, postTown.get)))
  }

  def toUpperCase: EnterAddressManuallyModel = {
    copy(addressAndPostcodeModel = addressAndPostcodeModel.copy(
      addressLinesModel = AddressLinesModel(addressAndPostcodeModel.addressLinesModel.buildingNameOrNumber.toUpperCase,
        addressAndPostcodeModel.addressLinesModel.line2.map(_.toUpperCase),
        addressAndPostcodeModel.addressLinesModel.line3.map(_.toUpperCase),
        addressAndPostcodeModel.addressLinesModel.postTown.toUpperCase),
      postcode = addressAndPostcodeModel.postcode.toUpperCase))
  }
}

object EnterAddressManuallyModel {
  implicit val JsonFormat = Json.format[EnterAddressManuallyModel]
  implicit val Key = CacheKey[EnterAddressManuallyModel](EnterAddressManuallyCacheKey)
}