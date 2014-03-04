package helpers

import org.openqa.selenium.WebDriver
//import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
//import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import java.util.concurrent.TimeUnit
import com.typesafe.config.ConfigFactory
import org.scalatest.selenium._

object Config {
  private val systemProperties = System.getProperties()
  private val conf = ConfigFactory.load()

  val baseUrl: String = conf.getString("base_url")

  def webDriver: WebDriver = {

    val selectedDriver: WebDriver =
      if (conf.hasPath("browser")) {
        val targetBrowser = conf.getString("browser")

        if (targetBrowser.equalsIgnoreCase("chrome")) { // TODO replace the if-elseif with a match
          if (conf.hasPath("webdriver.chrome.driver")) {
            systemProperties.setProperty("webdriver.chrome.driver", conf.getString("webdriver.chrome.driver"))
          }
          else {
            systemProperties.setProperty("webdriver.chrome.driver", "drivers/chromedriver-2.9_macosx")
          }
          Chrome.webDriver
        }
        else if (targetBrowser.equalsIgnoreCase("ie") || targetBrowser.equalsIgnoreCase("internetexplorer")) {
          InternetExplorer.webDriver
        }
        else if (targetBrowser.equalsIgnoreCase("safari")) {
          Safari.webDriver
        }
        else if (targetBrowser.equalsIgnoreCase("htmlunit")) {
          HtmlUnit.webDriver
        }
        /*else if (targetBrowser.equalsIgnoreCase("phantomjs")) {

          if (conf.hasPath("webdriver.phantomjs.binary")) {
            systemProperties.setProperty("webdriver.phantomjs.binary", conf.getString("webdriver.phantomjs.driver"))
          }
          else {
            systemProperties.setProperty("webdriver.phantomjs.binary", "drivers/phantomjs-1.9.7_macosx")
          }

          val capabilities = new DesiredCapabilities
          capabilities.setJavascriptEnabled(true)
          capabilities.setCapability("takesScreenshot", false)
          capabilities.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            systemProperties.getProperty("webdriver.phantomjs.binary"))
          capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, Array("--ignore-ssl-errors=yes", "--web-security=false", "--ssl-protocol=any"));

          PhantomJSDriverObject(capabilities)
        }*/
        else { // Default
          Firefox.firefoxProfile.setAcceptUntrustedCertificates(true)
          Firefox.webDriver
        }
      }
      else { // Default
        Firefox.firefoxProfile.setAcceptUntrustedCertificates(true)
        Firefox.webDriver
      }

    // TODO probably should make the timeout configurable
    selectedDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)

    selectedDriver
  }
}
