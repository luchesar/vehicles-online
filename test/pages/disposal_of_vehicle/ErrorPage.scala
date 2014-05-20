package pages.disposal_of_vehicle

import helpers.webbrowser._
import mappings.disposal_of_vehicle.Error.submitId
import org.openqa.selenium.WebDriver

object ErrorPage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/error"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "An unrecoverable error has occurred"

  def startAgain(implicit driver: WebDriver): Element = find(id(submitId)).get
}