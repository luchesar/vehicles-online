package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object BeforeYouStartPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.testUrl
  override val title: String = "Dispose a vehicle into the motor trade"

  def startNow(implicit driver: WebDriver): Element = find(id("next")).get
}