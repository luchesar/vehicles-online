package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object BusinessChangeYourAddressPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/business-choose-your-address"
  override val title = "Business: Choose your address"

  def chooseAddress(implicit driver: WebDriver): SingleSel = singleSel(id("disposal_businessChooseYourAddress_addressSelect"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def select(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}