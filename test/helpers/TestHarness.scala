package helpers

import play.api.test.TestServer
import org.openqa.selenium.WebDriver

trait TestHarness {

  def UseTestServer[T](block: => T): T = {
    println("** INSIDE UseTestServer")
    val testServer = new TestServer(9001)
    try {
      println("** GOT INSIDE TRY CATCH")
      testServer.start()
      block
    } finally {
      println("** GOT INSIDE TRY FINALLY")
      testServer.stop()
    }
  }

  def UseWebBrowser[T](block: => T): T = {
    println("** INSIDE UseWebBrowser")
    implicit val webDriver = WebDriverFactory.webDriver
    //val capability = DesiredCapabilities.firefox()
    //val capability = DesiredCapabilities.internetExplorer()
    //implicit val browser = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability)
    println("** CREATED WEB DRIVER")
    try {
      println("** GOT INSIDE TRY CATCH")
      block
    } finally {
      println("** GOT INSIDE TRY FINALLY")
      webDriver.quit()
    }
  }



}
