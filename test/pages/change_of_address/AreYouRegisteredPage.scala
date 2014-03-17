package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object AreYouRegisteredPage extends Page with WebBrowserDSL {
  val address = "/are-you-registered"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Change of keeper address4"

  def signIn(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}