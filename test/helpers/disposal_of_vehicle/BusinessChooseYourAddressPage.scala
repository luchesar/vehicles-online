package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import play.api.Play.current
import play.api.Logger
import models.domain.disposal_of_vehicle.DealerDetailsModel
import mappings.disposal_of_vehicle.BusinessAddressSelect

object BusinessChooseYourAddressPage {
  val url = "/disposal-of-vehicle/business-choose-your-address"
  val title = "Business: Choose your address"

  def setupCache() = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = "", dealerAddress = BusinessAddressSelect.address1)

    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddressPage stored data in cache: key = $key, value = ${value}")
  }

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
