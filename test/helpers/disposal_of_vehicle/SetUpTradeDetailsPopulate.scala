package helpers.disposal_of_vehicle

import org.specs2.mock.Mockito
import play.api.Play.current
import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._

object SetUpTradeDetailsPopulate {
  def setupCache() = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.traderBusinessNameId
    val value = "traderBusinessName"

    play.api.cache.Cache.set(key, value)
  }

  def happyPath(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/setup-trade-details")
    browser.fill("#traderBusinessName") `with` traderBusinessName
    browser.fill("#traderPostcode") `with` traderPostcode
    browser.submit("button[type='submit']")
  }
}
