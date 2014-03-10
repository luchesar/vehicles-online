package helpers

import play.api.test.TestServer
import org.openqa.selenium.WebDriver

trait TestHarness {

  def UseTestServer[T](block: => T): T = {
    val testServer = new TestServer(9001)
    try {
      testServer.start()
      block
    } finally {
      testServer.stop()
    }
  }

  def UseWebBrowser[T](block: => T): T = {
    implicit val webDriver = WebDriverFactory.webDriver
    //val capability = DesiredCapabilities.firefox()
    //val capability = DesiredCapabilities.internetExplorer()
    //implicit val browser = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability)
    try {
      block
    } finally {
      webDriver.quit()
    }
  }
}
