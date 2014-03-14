package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._


object SetupTradeDetailsPage extends Page with WebBrowserDSL {
  val address = "disposal-of-vehicle/setup-trade-details"
  val urlControllerTest: String = "/" + address
  override val url: String = WebDriverFactory.testUrl + address
  override val title: String = "Dispose a vehicle into the motor trade: set-up"

  def dealerName(implicit driver: WebDriver): TextField = textField(id("dealerName"))

  def dealerPostcode(implicit driver: WebDriver): TextField = textField(id("dealerPostcode"))

  def lookup(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    go to SetupTradeDetailsPage
    SetupTradeDetailsPage.dealerName enter traderBusinessName
    SetupTradeDetailsPage.dealerPostcode enter traderPostcode
    click on SetupTradeDetailsPage.lookup
  }

}