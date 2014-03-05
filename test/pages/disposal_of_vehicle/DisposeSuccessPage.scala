package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object DisposeSuccessPage extends Page with WebBrowser {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/dispose-success"
  override val title: String = "Dispose a vehicle into the motor trade: summary"

  def newDisposal(implicit driver: WebDriver): Element = find(id("newDisposal")).get
}