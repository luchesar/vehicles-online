package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.DOVValidValues._

object SetUpTradeDetails extends WordSpec with Matchers with Mockito {
  def traderLookupIntegrationHelper(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/setup-trade-details")
    browser.fill("#traderBusinessName") `with` traderBusinessName
    browser.fill("#traderPostcode") `with` traderPostcode
    browser.submit("button[type='submit']")
  }
}