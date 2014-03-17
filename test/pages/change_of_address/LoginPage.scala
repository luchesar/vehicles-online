package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object LoginPage extends Page with WebBrowserDSL {
  val address = "/sign-in-provider"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Change of keeper address5"

  def postOffice(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}