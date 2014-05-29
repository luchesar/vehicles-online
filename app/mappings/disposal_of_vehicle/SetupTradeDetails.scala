package mappings.disposal_of_vehicle

import constraints.disposal_of_vehicle.TraderBusinessName
import play.api.data.Mapping
import play.api.data.Forms._

object SetupTradeDetails {
  final val TraderNameMaxLength = 30
  final val TraderNameMinLength = 2
  final val TraderNameId = "traderName"

  final val TraderPostcodeId = "traderPostcode"
  final val SetupTradeDetailsCacheKey = "setupTraderDetails"
  final val SubmitId = "submit"

  def traderBusinessName (minLength: Int = TraderNameMinLength, maxLength: Int = TraderNameMaxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying TraderBusinessName.validTraderBusinessName
  }
}