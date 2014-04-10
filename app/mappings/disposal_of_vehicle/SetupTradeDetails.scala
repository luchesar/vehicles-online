package mappings.disposal_of_vehicle

import constraints.disposal_of_vehicle.TraderBusinessName
import play.api.data.Mapping
import play.api.data.Forms._

object SetupTradeDetails {
  val dealerNameMaxLength = 30
  val dealerNameMinLength = 2
  val dealerNameId = "dealerName"

  val dealerPostcodeId = "dealerPostcode"
  val SetupTradeDetailsCacheKey = "setupTradeDetails"
  val submitId = "submit"

  def traderBusinessName (minLength: Int = dealerNameMinLength, maxLength: Int = dealerNameMaxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying TraderBusinessName.validTraderBusinessName
  }
}