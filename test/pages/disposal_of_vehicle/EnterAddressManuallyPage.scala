package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import pages.BasePage
import helpers.Config
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait EnterAddressManuallyPage extends BasePage { this :WebBrowser =>

  object EnterAddressManuallyPage extends EnterAddressManuallyPage

  class EnterAddressManuallyPage extends Page {

    override val url: String = Config.baseUrl + "disposal-of-vehicle/enter-address-manually"

    def addressLine1(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line1"))

    def addressLine2(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line2"))

    def addressLine3(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line3"))

    def addressLine4(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line4"))

    def postcode(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_postcode"))

    def back(implicit driver: WebDriver): Element = find(id("next")).get

    def next(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  }
}