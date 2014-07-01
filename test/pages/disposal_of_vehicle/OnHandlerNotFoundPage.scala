package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.OnHandlerNotFound.{ExitId, TryAgainId}
import org.openqa.selenium.WebDriver

object OnHandlerNotFoundPage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "This page cannot be found"
  final val address = "/disposal-of-vehicle/nosuchpage/"

  def tryAgain(implicit driver: WebDriver): Element = find(id(TryAgainId)).get

  def exit(implicit driver: WebDriver): Element = find(id(ExitId)).get

  def hasTryAgain(implicit driver: WebDriver): Boolean = find(id(TryAgainId)).isDefined
}