package mappings.disposal_of_vehicle

import constraints.disposal_of_vehicle.TraderBusinessName
import play.api.data.Mapping
import play.api.data.Forms._

object SetupTradeDetails {
  val traderNameMaxLength = 30
  val traderNameMinLength = 2
  val traderNameId = "traderName"

  val traderPostcodeId = "traderPostcode"
  val SetupTradeDetailsCacheKey = "setupTraderDetails"
  val submitId = "submit"

  def traderBusinessName (minLength: Int = traderNameMinLength, maxLength: Int = traderNameMaxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying TraderBusinessName.validTraderBusinessName
  }
}