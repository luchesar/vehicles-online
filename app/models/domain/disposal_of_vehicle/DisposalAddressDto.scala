package models.domain.disposal_of_vehicle

import mappings.common.AddressLines._
import scala.Some
import scala.annotation.tailrec

case class DisposalAddressDto(line: Seq[String], postTown: Option[String], postCode: String, uprn: Option[Long])

object DisposalAddressDto {
  import play.api.libs.json.Json
  implicit val addressDto = Json.writes[DisposalAddressDto]

  def from(addressViewModel: AddressViewModel): DisposalAddressDto = {

    val trimmingRequired = checkForLinesOverMaxLength(addressViewModel.address)
    val addressWithMandatoryLines = if (addressViewModel.address.size == 2) AddressViewModel(addressViewModel.uprn, Seq(BuildingNameOrNumberHolder) ++ addressViewModel.address)
                                    else addressViewModel

    if (trimmingRequired) rebuildDisposalAddressDto(addressWithMandatoryLines) else buildStandardDisposalAddressDto(addressWithMandatoryLines)
  }

  @tailrec
  private def checkForLinesOverMaxLength(sourceAddress: Seq[String]): Boolean = {
    if (sourceAddress.isEmpty) false
    else if (sourceAddress.head.size > LineMaxLength) true
    else checkForLinesOverMaxLength(sourceAddress.tail)
  }

  private def buildStandardDisposalAddressDto(sourceAddress: AddressViewModel): DisposalAddressDto = {
    val postcode = sourceAddress.address.last.replace(" ","")
    val postTown = Some(sourceAddress.address.takeRight(2).head)
    DisposalAddressDto(sourceAddress.address.dropRight(2), postTown , postcode, sourceAddress.uprn)
  }

  private def rebuildDisposalAddressDto(addressViewModel: AddressViewModel): DisposalAddressDto = {
    val sourceAddressToCheck = assignEmptyLines(addressViewModel.address)

    val line2Empty = sourceAddressToCheck(Line2Index) == emptyLine
    val line3Empty = sourceAddressToCheck(Line3Index) == emptyLine
    val buildingNameOrNumberOverMax = sourceAddressToCheck(BuildingNameOrNumberIndex).size > LineMaxLength
    val line2OverMax = sourceAddressToCheck(Line2Index).size > LineMaxLength

    val sourceAddressAmendedLines = addressLinesOverMaxToEmptyLines(buildingNameOrNumberOverMax, line2OverMax, line2Empty, line3Empty, sourceAddressToCheck)
    val legacyAddressLines = trimLines(sourceAddressAmendedLines.dropRight(1), Nil)

    val postcode = addressViewModel.address.last.replaceAll(" ","")
    DisposalAddressDto(legacyAddressLines.dropRight(1), Some(legacyAddressLines.last), postcode, addressViewModel.uprn)
  }

  private def assignEmptyLines(sourceAddress: Seq[String]) : Seq[String] = {
    sourceAddress.size match { //every address returned by OS contains at least one address line and a postcode
      case 3 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(emptyLine) ++ sourceAddress.tail
      case 4 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(sourceAddress(Line2Index)) ++ Seq(emptyLine) ++ sourceAddress.drop(2)
      case _ => sourceAddress
    }
  }

  private def addressLinesOverMaxToEmptyLines(buildingNameOrNumberOverMax: Boolean, line2OverMax: Boolean, line2Empty: Boolean, line3Empty: Boolean, sourceAddressToCheck: Seq[String]) : Seq[String]= {
    (buildingNameOrNumberOverMax, line2OverMax, line2Empty, line3Empty) match {
      case (true, _, true, _) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
                                 Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
                                 sourceAddressToCheck.drop(2)
      case (true, _, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
                                 Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
                                 Seq(sourceAddressToCheck(Line2Index)) ++
                                 sourceAddressToCheck.drop(3)
      case (false, true, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex)) ++
                                         Seq(sourceAddressToCheck(Line2Index).substring(0, LineMaxLength)) ++
                                         Seq(sourceAddressToCheck(Line2Index).substring(LineMaxLength)) ++
                                         sourceAddressToCheck.drop(3)
      case (_) => sourceAddressToCheck
    }
  }

  @tailrec
  private def trimLines(existingAddress: Seq[String], accAddress: Seq[String]) : Seq[String] = {
    if (existingAddress.isEmpty) accAddress
    else if (existingAddress.head.size > LineMaxLength) trimLines(existingAddress.tail, accAddress :+ existingAddress.head.substring(0, LineMaxLength))
    else trimLines(existingAddress.tail, accAddress :+ existingAddress.head)
  }
}