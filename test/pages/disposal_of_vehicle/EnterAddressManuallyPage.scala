package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object EnterAddressManuallyPage extends Page with WebBrowserDSL {

  val line1Valid = "123 Some street"
  val line2Valid = "line-2 stub"
  val line3Valid = "line-3 stub"
  val line4Valid = "line-4 stub"
  val postcodeValid = "SE16EH"

  val urlControllerTest: String = "/disposal-of-vehicle/enter-address-manually"
  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/enter-address-manually"
  override val title: String = "Enter address manually"

  def addressLine1(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line1"))

  def addressLine2(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line2"))

  def addressLine3(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line3"))

  def addressLine4(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_addressLines_line4"))

  def postcode(implicit driver: WebDriver): TextField = textField(id("addressAndPostcode_postcode"))

  def back(implicit driver: WebDriver): Element = find(id("next")).get

  def next(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver, line1: String = line1Valid, line2: String = line2Valid, line3: String = line3Valid, line4:String = line4Valid, postcode:String = postcodeValid) ={
    CacheSetup.setupTradeDetails()
    go to EnterAddressManuallyPage.url
    EnterAddressManuallyPage.addressLine1.value = line1
    EnterAddressManuallyPage.addressLine2.value = line2
    EnterAddressManuallyPage.addressLine3.value = line3
    EnterAddressManuallyPage.addressLine4.value = line4
    EnterAddressManuallyPage.postcode.value = postcode

    click on EnterAddressManuallyPage.next
  }

  def happyPathMandatoryFieldsOnly(implicit driver: WebDriver, line1: String = line1Valid, postcode:String = postcodeValid) ={
    CacheSetup.setupTradeDetails()
    go to EnterAddressManuallyPage.url
    EnterAddressManuallyPage.addressLine1.value = line1
    EnterAddressManuallyPage.postcode.value = postcode

    click on EnterAddressManuallyPage.next
  }

  def sadPath(implicit driver: WebDriver) ={
    CacheSetup.setupTradeDetails()
    go to EnterAddressManuallyPage.url

    click on EnterAddressManuallyPage.next
  }
}