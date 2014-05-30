package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, WebBrowserDSL, Page, WebDriverFactory}
import org.openqa.selenium.WebDriver
import mappings.disposal_of_vehicle.VrmLocked._

object VrmLockedPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/vrm-locked"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)

  final override val title = "Registration mark is locked"

  def newDisposal(implicit driver: WebDriver): Element = find(id(NewDisposalId)).get

  def exit(implicit driver: WebDriver): Element = find(id(ExitDisposalId)).get
}
