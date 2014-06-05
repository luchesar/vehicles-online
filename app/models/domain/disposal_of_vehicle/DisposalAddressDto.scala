package models.domain.disposal_of_vehicle

import mappings.common.AddressLines._
import scala.Some
import scala.annotation.tailrec

case class DisposalAddressDto(line: Seq[String], postTown: Option[String], postCode: String, uprn: Option[Long])

object DisposalAddressDto {
  import play.api.libs.json.Json
  implicit val addressDto = Json.writes[DisposalAddressDto]

  def from(addressViewModel: AddressViewModel): DisposalAddressDto = {
    val sourceAddressToCheck = assignEmptyLines(addressViewModel.address)

    val (line2Empty, line3Empty) = (sourceAddressToCheck(Line2Index) == emptyLine, sourceAddressToCheck(Line3Index) == emptyLine)
    val (buildingNameOrNumberOverMax, line2OverMax) = (sourceAddressToCheck(BuildingNameOrNumberIndex).size > LineMaxLength, sourceAddressToCheck(Line2Index).size > LineMaxLength)

    val sourceAddressAmendedLines = addressLinesOverMaxToEmptyLines(buildingNameOrNumberOverMax, line2OverMax, line2Empty, line3Empty, sourceAddressToCheck)

    val legacyAddressLines = lineLengthCheck(sourceAddressAmendedLines.dropRight(2), Nil)
    val postTownToCheck = sourceAddressToCheck.takeRight(2).head
    val postTown = if (postTownToCheck.size > LineMaxLength) postTownToCheck.substring(0, LineMaxLength) else postTownToCheck
    val postcode = addressViewModel.address.last.replaceAll(" ","")

    //Logger.debug("DisposalAddressDto is " + legacyAddressLines + ", " + Some(postTown) + ", " + postcode + ", " + sourceAddress.uprn)
    DisposalAddressDto(legacyAddressLines, Some(postTown), postcode, addressViewModel.uprn)
  }

  private def assignEmptyLines(sourceAddress: Seq[String]) : Seq[String] = {
    sourceAddress.size match { //every address returned by OS contains at least one address line and a postcode
      case 2 => Seq(BuildingNameOrNumberHolder) ++ sourceAddress
      case 3 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(emptyLine) ++ sourceAddress.tail
      case 4 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(sourceAddress(Line2Index)) ++ Seq(emptyLine) ++ sourceAddress.tail.tail
      case _ => sourceAddress
    }
  }


  private def addressLinesOverMaxToEmptyLines(buildingNameOrNumberOverMax: Boolean, line2OverMax: Boolean, line2Empty: Boolean, line3Empty: Boolean, sourceAddressToCheck: Seq[String]) : Seq[String]= {
    (buildingNameOrNumberOverMax, line2OverMax, line2Empty, line3Empty) match {
      case (true, _, true, _) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
        Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
        sourceAddressToCheck.tail.tail
      case (true, _, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
        Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
        Seq(sourceAddressToCheck(Line2Index)) ++
        sourceAddressToCheck.tail.tail.tail
      case (false, true, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex)) ++
        Seq(sourceAddressToCheck(Line2Index).substring(0, LineMaxLength)) ++
        Seq(sourceAddressToCheck(Line2Index).substring(LineMaxLength)) ++
        sourceAddressToCheck.tail.tail.tail
      case (_) => sourceAddressToCheck
    }
  }

  @tailrec
  private def lineLengthCheck(existingAddress: Seq[String], accAddress: Seq[String]) : Seq[String] = {
    if (existingAddress.isEmpty) accAddress
    else if (existingAddress.head.size > LineMaxLength) lineLengthCheck(existingAddress.tail, accAddress :+ existingAddress.head.substring(0, LineMaxLength))
    else lineLengthCheck(existingAddress.tail, accAddress :+ existingAddress.head)
  }
}