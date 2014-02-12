package helpers.disposal_of_vehicle

import play.api.Play.current
import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import mappings.disposal_of_vehicle.SetupTradeDetails._

object SetUpTradeDetailsPage {
  val url = "/disposal-of-vehicle/setup-trade-details"

  def setupCache() = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.dealerNameId
    val value = s"#${dealerNameId}"

    play.api.cache.Cache.set(key, value)
  }

  def happyPath(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo(url)
    browser.fill(s"#${dealerNameId}") `with` traderBusinessName
    browser.fill(s"#${dealerPostcodeId}") `with` traderPostcode
    browser.submit("button[type='submit']")
  }
}
