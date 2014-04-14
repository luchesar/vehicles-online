package helpers.hooks

import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebDriverFactory}
import cucumber.api.java.{Before,After}

trait WebBrowser extends WebBrowserDSL {

  //implicit lazy val webDriver: WebDriver = new SharedBrowser

  implicit lazy val webDriver: WebDriver = WebDriverFactory.webDriver

  @Before
  def configureBrowser() = {
  }

  @After
  def quitBrowser() = {
    webDriver.quit()
  }
}

