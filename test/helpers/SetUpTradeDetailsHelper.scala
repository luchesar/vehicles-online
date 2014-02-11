package helpers

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito

object SetUpTradeDetailsHelper extends WordSpec with Matchers with Mockito {
  val traderBusinessNameValid = "example trader name"
  val traderPostcodeValid = "SA99 1DD"

  def traderLookupIntegrationHelper(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/setup-trade-details")
    browser.fill("#traderBusinessName") `with` traderBusinessName
    browser.fill("#traderPostcode") `with` traderPostcode
    browser.submit("button[type='submit']")
  }
}