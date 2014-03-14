package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object DisposeSuccessPage extends Page with WebBrowserDSL {
  val address = "disposal-of-vehicle/dispose-success"
  val urlControllerTest: String = "/" + address
  override val url: String = WebDriverFactory.testUrl + address
  override val title: String = "Dispose a vehicle into the motor trade: summary"

  def newDisposal(implicit driver: WebDriver): Element = find(id("newDisposal")).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposeSuccessPage.url
    click on DisposeSuccessPage.newDisposal
  }
}