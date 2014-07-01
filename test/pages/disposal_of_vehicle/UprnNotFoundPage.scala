package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.UprnNotFound.{ManualaddressbuttonId, SetuptradedetailsbuttonId}
import org.openqa.selenium.WebDriver

object UprnNotFoundPage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "Error confirming postcode"
  final val address = "/disposal-of-vehicle/uprn-not-found"

  def setupTradeDetails(implicit driver: WebDriver): Element = find(id(SetuptradedetailsbuttonId)).get

  def manualAddress(implicit driver: WebDriver): Element = find(id(ManualaddressbuttonId)).get
}