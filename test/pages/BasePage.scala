package pages

import org.openqa.selenium.WebDriver
import helpers.Config
import helpers.WebBrowser

trait BasePage extends WebBrowser {

  implicit val driver: WebDriver = Config.webDriver

}
