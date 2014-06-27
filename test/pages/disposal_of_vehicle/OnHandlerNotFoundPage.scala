package pages.disposal_of_vehicle

import helpers.webbrowser._
import mappings.disposal_of_vehicle.OnHandlerNotFound.{TryAgainId, ExitId}
import org.openqa.selenium.WebDriver

object OnHandlerNotFoundPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/nosuchpage/"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "This page cannot be found"

  def tryAgain(implicit driver: WebDriver): Element = find(id(TryAgainId)).get
  def exit(implicit driver: WebDriver): Element = find(id(ExitId)).get

}
