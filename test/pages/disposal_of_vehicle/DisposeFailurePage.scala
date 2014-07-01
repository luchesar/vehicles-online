package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.DisposeFailure.{SetupTradeDetailsId, VehicleLookupId}
import org.openqa.selenium.WebDriver

object DisposeFailurePage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "Sell a vehicle into the motor trade: failure"
  final val address = "/disposal-of-vehicle/dispose-failure"

  def setuptradedetails(implicit driver: WebDriver): Element = find(id(SetupTradeDetailsId)).get

  def vehiclelookup(implicit driver: WebDriver): Element = find(id(VehicleLookupId)).get
}