package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object DisposeFailurePage extends Page with WebBrowserDSL {
  val urlControllerTest: String = "/disposal-of-vehicle/dispose-failure"
  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/dispose-failure"
  override val title: String = "Dispose a vehicle into the motor trade: failure"

  def setuptradedetails(implicit driver: WebDriver): Element = find(id("setuptradedetails")).get

  def vehiclelookup(implicit driver: WebDriver): Element = find(id("vehiclelookup")).get
}