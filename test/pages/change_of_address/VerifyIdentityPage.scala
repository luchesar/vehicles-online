package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object VerifyIdentityPage extends Page with WebBrowserDSL {
  val address = "/verify-identity"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Change of keeper address3"

  def existingIdentityProfile(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}