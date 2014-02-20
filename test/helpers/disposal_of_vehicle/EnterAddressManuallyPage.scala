package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.AddressAndPostcode
import mappings.disposal_of_vehicle.AddressLines
import mappings.disposal_of_vehicle.AddressLines._
import mappings.disposal_of_vehicle.Postcode._

object EnterAddressManuallyPage {
  val url = "/disposal-of-vehicle/enter-address-manually"
  val title = "Enter address manually"
  val line1Valid = "123 Street"
  val line2Valid = "line2 stub"
  val line3Valid = "line3 stub"
  val line4Valid = "line4 stub"
  val postCodeValid = "SE1 6EH"

  def happyPath(browser:TestBrowser, line1: String = line1Valid, line2: String = line2Valid, line3:String = line3Valid, line4:String = line4Valid, postcode: String = postCodeValid){
    browser.goTo(url)

    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line1Id") `with` line1
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line2Id") `with` line2
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line3Id") `with` line3
    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line4Id") `with` line4
    browser.fill(s"#${AddressAndPostcode.id}_$postcodeID") `with` postcode

    browser.submit("button[type='submit']")
  }

  def happyPathMandatoryFieldsOnly(browser:TestBrowser, line1: String = line1Valid, postcode: String = postCodeValid){
    browser.goTo(url)

    browser.fill(s"#${AddressAndPostcode.id}_${AddressLines.id}_$line1Id") `with` line1
    browser.fill(s"#${AddressAndPostcode.id}_$postcodeID") `with` postcode

    browser.submit("button[type='submit']")
  }
}
