package helpers.disposal_of_vehicle

import play.api.Play.current
import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel

object SetUpTradeDetailsPage {
  val url = "/disposal-of-vehicle/setup-trade-details"
  val title = "Dispose a vehicle into the motor trade: set-up"

  def setupCache(dealerPostcode: String = traderPostcodeValid) = {
    val model = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid, traderPostcode = dealerPostcode)
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.cacheKey
    val value = model

    play.api.cache.Cache.set(key, value)
  }

  def happyPath(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo(url)
    browser.fill(s"#${dealerNameId}") `with` traderBusinessName
    browser.fill(s"#${dealerPostcodeId}") `with` traderPostcode
    browser.submit("button[type='submit']")
  }
}
