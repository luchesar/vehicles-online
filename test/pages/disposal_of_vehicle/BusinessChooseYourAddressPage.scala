package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeWebServiceImpl.traderUprnValid
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._

object BusinessChooseYourAddressPage extends Page with WebBrowserDSL {
  val address: String = "/disposal-of-vehicle/business-choose-your-address"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title = "Select your trade address"

  def chooseAddress(implicit driver: WebDriver): SingleSel = singleSel(id("disposal_businessChooseYourAddress_addressSelect"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def manualAddress(implicit driver: WebDriver): Element = find(id(enterAddressManuallyButtonId)).get

  def getList(implicit driver: WebDriver) = singleSel(id(addressSelectId)).getOptions

  def getListCount(implicit driver: WebDriver): Int = getList.size

  def select(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver) = {
    go to BusinessChooseYourAddressPage
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
  }

  def sadPath(implicit driver: WebDriver) = {
    go to BusinessChooseYourAddressPage
    click on BusinessChooseYourAddressPage.select
  }
}