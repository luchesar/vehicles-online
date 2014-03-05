package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object VehicleLookupFailurePage extends Page with WebBrowser {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/vehicle-lookup-failure"
  override val title: String = "Dispose a vehicle into the motor trade: vehicle lookup failure"

  def setupTradeDetails(implicit driver: WebDriver): Element = find(id("setuptradedetails")).get

  def vehicleLookup(implicit driver: WebDriver): Element = find(id("vehiclelookup")).get
}