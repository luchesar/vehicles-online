package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeAddressLookupService._
import mappings.common.{AddressLines, AddressAndPostcode}
import mappings.common.AddressLines._
import mappings.common.Postcode._

object EnterAddressManuallyPage extends Page with WebBrowserDSL {

  val address = "/disposal-of-vehicle/enter-address-manually"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Enter address manually"

  def addressLine1(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcode.id}_${AddressLines.id}_$line1Id"))

  def addressLine2(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcode.id}_${AddressLines.id}_$line2Id"))

  def addressLine3(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcode.id}_${AddressLines.id}_$line3Id"))

  def addressLine4(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcode.id}_${AddressLines.id}_$line4Id"))

  def postcode(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcode.id}_$postcodeId"))

  def back(implicit driver: WebDriver): Element = find(id("next")).get

  def next(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(line1: String = line1Valid, line2: String = line2Valid, line3: String = line3Valid, line4:String = line4Valid, postcode:String = postcodeValid)(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage.url
    EnterAddressManuallyPage.addressLine1.value = line1
    EnterAddressManuallyPage.addressLine2.value = line2
    EnterAddressManuallyPage.addressLine3.value = line3
    EnterAddressManuallyPage.addressLine4.value = line4
    EnterAddressManuallyPage.postcode.value = postcode
    click on EnterAddressManuallyPage.next
  }

  def happyPathMandatoryFieldsOnly(line1: String = line1Valid, postcode:String = postcodeValid)(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage.url
    EnterAddressManuallyPage.addressLine1.value = line1
    EnterAddressManuallyPage.postcode.value = postcode
    click on EnterAddressManuallyPage.next
  }

  def sadPath(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage.url
    click on EnterAddressManuallyPage.next
  }
}