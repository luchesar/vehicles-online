package mappings.disposal_of_vehicle

/**
 * Set of mapping ids to use when handling the posted form data in the setup trade details page
 * Also includes the cache key to use when storing the dealer name
 */
object SetupTradeDetails {
  val dealerNameMinLength = 2
  val dealerNameMaxLength = 100
  val dealerNameId = "dealerName"
  val dealerPostcodeId = "dealerPostcode"
  val SetupTradeDetailsCacheKey = "setupTradeDetails"
}
