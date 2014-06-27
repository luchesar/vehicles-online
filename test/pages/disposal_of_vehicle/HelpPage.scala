package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, WebDriverFactory, WebBrowserDSL, Page}
import org.openqa.selenium.WebDriver
import mappings.disposal_of_vehicle.Help._

object HelpPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/help"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Help"

  def exit(implicit driver: WebDriver): Element = find(id(ExitId)).get
}
