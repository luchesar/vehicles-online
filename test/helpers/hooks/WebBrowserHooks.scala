package helpers.hooks

import cucumber.api.java.After
import helpers.webbrowser.WebBrowserDriver
import org.openqa.selenium.WebDriver

final class WebBrowserHooks(webBrowserDriver: WebBrowserDriver) {
  /*@Before
  def configureBrowser() = {
  }*/

  @After
  def quitBrowser() = {
    implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]
    webDriver.quit()
  }
}

