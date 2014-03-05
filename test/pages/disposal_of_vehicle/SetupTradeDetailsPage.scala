package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object SetupTradeDetailsPage extends Page with WebBrowser {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/setup-trade-details"
  override val title: String = "Dispose a vehicle into the motor trade: set-up"

  def dealerName(implicit driver: WebDriver): TextField = textField(id("dealerName"))

  def dealerPostcode(implicit driver: WebDriver): TextField = textField(id("dealerPostcode"))

  def lookup(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}