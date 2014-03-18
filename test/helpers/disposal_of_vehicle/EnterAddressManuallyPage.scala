package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.common.{Postcode, AddressAndPostcode, AddressLines}
import mappings.common.AddressLines._
import Postcode._
import helpers.disposal_of_vehicle.Helper._

object EnterAddressManuallyPage {
  val url = "/disposal-of-vehicle/enter-address-manually"
  val title = "Enter address manually"
  val line1Valid = "1234"
  val line2Valid = "line-2 stub"
  val line3Valid = "line-3 stub"
  val line4Valid = "line-4 stub"

  def happyPath(browser:TestBrowser){
    browser.goTo(url)
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line1Id") `with` line1Valid
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line2Id") `with` line2Valid
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line3Id") `with` line3Valid
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line4Id") `with` line4Valid
    browser.fill(s"#${AddressAndPostcode.id}_$postcodeId") `with` postcodeValid
    browser.submit("button[type='submit']")
  }

  def sadPath(browser:TestBrowser, line1: String = line1Valid, line2: String = line2Valid, line3:String = line3Valid, line4:String = line4Valid, postcode: String = postcodeValid){
    browser.goTo(url)
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line1Id") `with` line1
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line2Id") `with` line2
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line3Id") `with` line3
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line4Id") `with` line4
    browser.fill(s"#${AddressAndPostcode.id}_$postcodeId") `with` postcode
    browser.submit("button[type='submit']")
  }

  def happyPathMandatoryFieldsOnly(browser:TestBrowser){
    browser.goTo(url)
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line1Id") `with` line1Valid
    browser.fill(s"#${AddressAndPostcode.id}_$postcodeId") `with` postcodeValid
    browser.submit("button[type='submit']")
  }
}
