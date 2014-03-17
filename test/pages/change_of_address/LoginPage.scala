package pages.change_of_address

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object LoginPage extends Page with WebBrowserDSL {
  val address = "/login-page"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Verified login id"

  def username(implicit driver: WebDriver): TextField = textField(id("username"))

  def password(implicit driver: WebDriver): PasswordField = pwdField(id("password"))

  def signIn(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver) = {
    go to LoginPage
    LoginPage.username enter "username"
    LoginPage.password enter "password"
    click on LoginPage.signIn
  }
}