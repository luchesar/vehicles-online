package helpers

import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import java.util.concurrent.TimeUnit
import com.typesafe.config.ConfigFactory

class Config {
  private val systemProperties = System.getProperties()
  private val conf = ConfigFactory.load()

  val baseUrl: String = conf.getString("base_url")

  val webDriver: WebDriver = {
    var selectedDriver: WebDriver = null

    if (conf.hasPath("browser")) {
      val targetBrowser = conf.getString("browser")

      if (targetBrowser.equalsIgnoreCase("chrome")) {
        if (conf.hasPath("webdriver.chrome.driver")) {
          systemProperties.setProperty("webdriver.chrome.driver", conf.getString("webdriver.chrome.driver"))
        }
        else {
          systemProperties.setProperty("webdriver.chrome.driver", "drivers/chromedriver-2.9_macosx")
        }
        selectedDriver = new ChromeDriver()
      }
      else if (targetBrowser.equalsIgnoreCase("ie") || targetBrowser.equalsIgnoreCase("internetexplorer")) {
        selectedDriver = new InternetExplorerDriver()
      }
      else if (targetBrowser.equalsIgnoreCase("safari")) {
        selectedDriver = new SafariDriver()
      }
      else if (targetBrowser.equalsIgnoreCase("htmlunit")) {
        selectedDriver = new HtmlUnitDriver()
        // Disable javascript as jQuery crashes HtmlUnit!!
        selectedDriver.asInstanceOf[HtmlUnitDriver].setJavascriptEnabled(false)
      }
      else if (targetBrowser.equalsIgnoreCase("phantomjs")) {

        if (conf.hasPath("webdriver.phantomjs.binary")) {
          systemProperties.setProperty("webdriver.phantomjs.binary", conf.getString("webdriver.phantomjs.binary"))
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

        selectedDriver = new PhantomJSDriver(capabilities)
      }
    }

    if (selectedDriver == null ) {
      val firefoxProfile = new FirefoxProfile()
      firefoxProfile.setAcceptUntrustedCertificates(true)
      selectedDriver = new FirefoxDriver(firefoxProfile)
    }

    // TODO probably should make the timeout configurable
    selectedDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)

    selectedDriver
  }
}

object Config extends Config
