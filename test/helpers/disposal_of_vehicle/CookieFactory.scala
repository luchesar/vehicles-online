package helpers.disposal_of_vehicle

import services.fakes.FakeAddressLookupService._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import helpers.disposal_of_vehicle.Helper._
import play.api.libs.json.Json

object CookieFactory { // TODO setup the cookies for the Unit Specs here, removing them from CacheSetup
  def setupTradeDetails(traderPostcode: String = postcodeValid) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }
}