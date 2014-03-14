package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object BeforeYouStartPage extends Page with WebBrowserDSL {
  val address = "before-you-start"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Dispose a vehicle into the motor trade"

  def startNow(implicit driver: WebDriver): Element = find(id("next")).get
}