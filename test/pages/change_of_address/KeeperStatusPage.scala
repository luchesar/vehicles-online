package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object KeeperStatusPage extends Page with WebBrowserDSL {
  val address = "/keeper-status"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Change of keeper address2"

  def privateIndividual(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}