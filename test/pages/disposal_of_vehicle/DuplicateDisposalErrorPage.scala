package pages.disposal_of_vehicle

import helpers.webbrowser.{WebBrowserDSL, Page, Element, WebDriverFactory}
import org.openqa.selenium.WebDriver
import mappings.disposal_of_vehicle.MicroserviceError._

object DuplicateDisposalErrorPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/duplicate-disposal-error"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)

  final override val title = "We are sorry"

  def tryAgain(implicit driver: WebDriver): Element = find(id(TryAgainId)).get

  def exit(implicit driver: WebDriver): Element = find(id(ExitId)).get
}
