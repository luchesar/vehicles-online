package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import mappings.disposal_of_vehicle.DisposeFailure._

object DisposeFailurePage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/dispose-failure"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Dispose a vehicle into the motor trade: failure"

  def setuptradedetails(implicit driver: WebDriver): Element = find(id(SetupTradeDetailsId)).get

  def vehiclelookup(implicit driver: WebDriver): Element = find(id(VehicleLookupId)).get
}