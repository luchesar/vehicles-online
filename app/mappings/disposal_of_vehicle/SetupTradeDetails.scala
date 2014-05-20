package mappings.disposal_of_vehicle

import constraints.disposal_of_vehicle.TraderBusinessName
import play.api.data.Mapping
import play.api.data.Forms._

object SetupTradeDetails {
  val TraderNameMaxLength = 30
  val TraderNameMinLength = 2
  val TraderNameId = "traderName"

  val TraderPostcodeId = "traderPostcode"
  val SetupTradeDetailsCacheKey = "setupTraderDetails"
  val SubmitId = "submit"

  def traderBusinessName (minLength: Int = TraderNameMinLength, maxLength: Int = TraderNameMaxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying TraderBusinessName.validTraderBusinessName
  }
}