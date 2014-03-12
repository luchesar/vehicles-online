package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object BusinessChooseYourAddressPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/business-choose-your-address"
  override val title = "Business: Choose your address"

  def chooseAddress(implicit driver: WebDriver): SingleSel = singleSel(id("disposal_businessChooseYourAddress_addressSelect"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def manualAddress(implicit driver: WebDriver): Element = find(id("enterAddressManuallyButton")).get

  def select(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver) = {
    CacheSetup.setupTradeDetails()
    go to BusinessChooseYourAddressPage
    BusinessChooseYourAddressPage.chooseAddress.value = "1234"
    click on BusinessChooseYourAddressPage.select
  }
}