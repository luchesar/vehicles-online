package pages.disposal_of_vehicle

import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import play.api.Play.current

object CacheSetup {

  def setupTradeDetails(traderPostcode: String = traderPostcodeValid) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid, traderPostcode = traderPostcode)
    play.api.cache.Cache.set(key, value)
  }
}