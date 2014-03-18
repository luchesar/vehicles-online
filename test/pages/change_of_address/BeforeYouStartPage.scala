package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object BeforeYouStartPage extends Page with WebBrowserDSL {
  val address = "/before-you-start"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Change of keeper address1"

  def startNow(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}