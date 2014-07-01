package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.Error.SubmitId
import org.openqa.selenium.WebDriver

object ErrorPage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "An unrecoverable error has occurred"
  final val exceptionDigest = "fake-exception-digest"
  final val address = "/disposal-of-vehicle/error/" + exceptionDigest

  def startAgain(implicit driver: WebDriver): Element = find(id(SubmitId)).get
}