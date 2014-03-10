package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object EnterAddressManuallyPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/enter-address-manually"
  override val title: String = "Enter address manually"

  def addressLine1(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line1"))

  def addressLine2(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line2"))

  def addressLine3(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line3"))

  def addressLine4(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line4"))

  def postcode(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_postcode"))

  def back(implicit driver: WebDriver): Element = find(id("next")).get

  def next(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}