package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeAddressLookupWebServiceImpl.traderUprnValid
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._

object BusinessChooseYourAddressPage extends Page with WebBrowserDSL {
  final val address: String = "/disposal-of-vehicle/business-choose-your-address"
  final val progressStep = "Step 3 of 6"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "Select your trade address"
  final val titleCy = "Dewiswch eich cyfeiriad masnach"

  def chooseAddress(implicit driver: WebDriver): SingleSel = singleSel(id(AddressSelectId))

  def back(implicit driver: WebDriver): Element = find(id(BackId)).get

  def manualAddress(implicit driver: WebDriver): Element = find(id(EnterAddressManuallyButtonId)).get

  def getList(implicit driver: WebDriver) = singleSel(id(AddressSelectId)).getOptions

  def getListCount(implicit driver: WebDriver): Int = getList.size

  def select(implicit driver: WebDriver): Element = find(id(SelectId)).get

  def happyPath(implicit driver: WebDriver) = {
    go to BusinessChooseYourAddressPage
    chooseAddress.value = traderUprnValid.toString
    click on select
  }

  def sadPath(implicit driver: WebDriver) = {
    go to BusinessChooseYourAddressPage
    click on select
  }
}