package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import play.api.Play.current
import play.api.Logger
import models.domain.disposal_of_vehicle.{AddressLinesModel, AddressAndPostcodeModel, DealerDetailsModel}
import mappings.disposal_of_vehicle.BusinessAddressSelect

object BusinessChooseYourAddressPage {
  val url = "/disposal-of-vehicle/business-choose-your-address"
  val title = "Business: Choose your address"

  val address1 = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = Some("44 Hythe Road"),
    line2 = Some("White City"),
    line3 = Some("London"),
    line4 = None),
    postcode = "NW10 6RJ")
  val address2 = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = Some("Penarth Road"),
    line2 = Some("Cardiff"),
    line3 = None,
    line4 = None),
    postcode = "CF11 8TT")

  def setupCache() = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = "", dealerAddress = address1)

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
