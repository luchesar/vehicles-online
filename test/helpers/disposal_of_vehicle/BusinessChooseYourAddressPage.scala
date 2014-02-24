package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import play.api.Play.current
import play.api.Logger
import models.domain.disposal_of_vehicle.{AddressViewModel, AddressLinesModel, AddressAndPostcodeModel, DealerDetailsModel}
import mappings.disposal_of_vehicle.BusinessAddressSelect

object BusinessChooseYourAddressPage {
  val url = "/disposal-of-vehicle/business-choose-your-address"
  val title = "Business: Choose your address"

  val address1 = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(address= Seq("Penarth Road", "Cardiff", "CF11 8TT"))

  def setupCache = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = "", dealerAddress = address1)

    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddressPage stored data in cache: key = $key, value = ${value}")
  }

  def happyPath(browser: TestBrowser, addressSelected: String = "1234") = {
    browser.goTo(url)
    browser.click(s"#${addressSelectId} option[value='${addressSelected}']")
    browser.submit("button[type='submit']")
  }

  def sadPath(browser: TestBrowser) = {
    browser.goTo(url)
    browser.submit("button[type='submit']")
  }
}
