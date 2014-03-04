package helpers

import app.ConfigProperties._
import org.openqa.selenium.WebDriver

//import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
//import org.openqa.selenium.remote.DesiredCapabilities

import java.util.concurrent.TimeUnit
import com.typesafe.config.ConfigFactory
import org.scalatest.selenium._

object WebDriverFactory {
  private val systemProperties = System.getProperties()

  val baseUrl: String = getProperty("base.url", "http://localhost:9000/") // TODO Use the play method to get the current.config as in the vehicles-online and do this for every use of conf.

  def webDriver: WebDriver = {
    val targetBrowser = getProperty("browser", "htmlunit")

    val selectedDriver: WebDriver = {
      // TODO replace the if-elseif with a match
      if (targetBrowser.equalsIgnoreCase("chrome")) {
        systemProperties.setProperty("webdriver.chrome.driver", getProperty("webdriver.chrome.driver", "drivers/chromedriver-2.9_macosx"))
        Chrome.webDriver
      }
      else if (targetBrowser.equalsIgnoreCase("ie") || targetBrowser.equalsIgnoreCase("internetexplorer")) InternetExplorer.webDriver
      else if (targetBrowser.equalsIgnoreCase("safari")) Safari.webDriver
      else if (targetBrowser.equalsIgnoreCase("htmlunit")) HtmlUnit.webDriver
      /* Comment out pahntomjs support as need newer version of selenium
      else if (targetBrowser.equalsIgnoreCase("phantomjs")) {
        systemProperties.setProperty("webdriver.phantomjs.binary", getProperty("webdriver.phantomjs.binary", "drivers/phantomjs-1.9.7_macosx"))

        val capabilities = new DesiredCapabilities
        capabilities.setJavascriptEnabled(true)
        capabilities.setCapability("takesScreenshot", false)
        capabilities.setCapability(
          PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
          systemProperties.getProperty("webdriver.phantomjs.binary"))
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, Array("--ignore-ssl-errors=yes", "--web-security=false", "--ssl-protocol=any"));

        PhantomJSDriverObject(capabilities)
      }*/
      else {
        // Default
        Firefox.firefoxProfile.setAcceptUntrustedCertificates(true)
        Firefox.webDriver
      }
    }

    // TODO probably should make the timeout configurable
    selectedDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)

    selectedDriver
  }
}
