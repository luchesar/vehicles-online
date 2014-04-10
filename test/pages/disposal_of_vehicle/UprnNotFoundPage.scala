package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object UprnNotFoundPage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/uprn-not-found"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Error confirming postcode"

  def setupTradeDetails(implicit driver: WebDriver): Element = find(id("setuptradedetailsbutton")).get

  def manualAddress(implicit driver: WebDriver): Element = find(id("manualaddressbutton")).get
}