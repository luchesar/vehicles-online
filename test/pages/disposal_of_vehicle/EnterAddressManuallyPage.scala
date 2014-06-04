package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeAddressLookupService._
import mappings.common.AddressLines._
import mappings.common.Postcode._
import mappings.common.AddressAndPostcode._
import mappings.disposal_of_vehicle.EnterAddressManually._

object EnterAddressManuallyPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/enter-address-manually"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Enter address manually"

  def addressBuildingNameOrNumber(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcodeId}_${AddressLinesId}_$BuildingNameOrNumberId"))

  def addressLine2(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcodeId}_${AddressLinesId}_$Line2Id"))

  def addressLine3(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcodeId}_${AddressLinesId}_$Line3Id"))

  def addressLine4(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcodeId}_${AddressLinesId}_$Line4Id"))

  def postcode(implicit driver: WebDriver): TextField = textField(id(s"${AddressAndPostcodeId}_$PostcodeId"))

  def next(implicit driver: WebDriver): Element = find(id(NextId)).get

  def back(implicit driver: WebDriver): Element = find(id(BackId)).get

  def happyPath(buildingNameOrNumber: String = BuildingNameOrNumberValid, line2: String = Line2Valid, line3: String = Line3Valid, line4:String = Line4Valid, postcode:String = PostcodeValid)(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage
    addressBuildingNameOrNumber.value = buildingNameOrNumber
    addressLine2.value = line2
    addressLine3.value = line3
    addressLine4.value = line4
    EnterAddressManuallyPage.postcode.value = postcode
    click on next
  }

  def happyPathMandatoryFieldsOnly(buildingNameOrNumber: String = BuildingNameOrNumberValid, line4: String = Line4Valid, postcode:String = PostcodeValid)(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage
    addressBuildingNameOrNumber.value = buildingNameOrNumber
    addressLine4.value = line4
    EnterAddressManuallyPage.postcode.value = postcode
    click on next
  }

  def sadPath(implicit driver: WebDriver) ={
    go to EnterAddressManuallyPage
    click on next
  }
}