package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object DisposeFailurePage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/dispose-failure"
  override val title: String = "Dispose a vehicle into the motor trade: failure"

  def setuptradedetails(implicit driver: WebDriver): Element = find(id("setuptradedetails")).get

  def vehiclelookup(implicit driver: WebDriver): Element = find(id("vehiclelookup")).get
}