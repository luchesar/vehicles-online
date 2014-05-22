package helpers.hooks

import org.openqa.selenium.WebDriver
import helpers.webbrowser.WebBrowserDriver
import cucumber.api.java.{Before,After}

final class WebBrowserHooks(webBrowserDriver:WebBrowserDriver) {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  /*@Before
  def configureBrowser() = {
  }*/

  @After
  def quitBrowser() = {
    webDriver.quit()
  }
}

