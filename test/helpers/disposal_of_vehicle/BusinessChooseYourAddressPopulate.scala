package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.Helper._
import mappings.disposal_of_vehicle.BusinessAddressSelect._

object BusinessChooseYourAddressPopulate {
  val url = "/disposal-of-vehicle/business-choose-your-address"

  def happyPath(browser: TestBrowser, addressSelected: String = address1.toViewFormat()) = {
    browser.goTo(url)
    browser.click(s"#${addressSelectId} option[value='${addressSelected}']")
    browser.submit("button[type='submit']")
  }

  def sadPath(browser: TestBrowser) = {
    browser.goTo(url)
    browser.submit("button[type='submit']")
  }
}
