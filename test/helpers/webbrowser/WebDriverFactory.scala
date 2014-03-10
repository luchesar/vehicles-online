package helpers.webbrowser

import app.ConfigProperties._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import com.typesafe.config.ConfigFactory

//import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
//import org.openqa.selenium.remote.DesiredCapabilities

import java.util.concurrent.TimeUnit

object WebDriverFactory {
  private val systemProperties = System.getProperties()
  private val conf = ConfigFactory.load()

  def webDriver: WebDriver = {
    val targetBrowser = getProperty("browser.type", "htmlunit")

    val selectedDriver: WebDriver = {

      targetBrowser match {
        case "chrome" => {
          systemProperties.setProperty("webdriver.chrome.driver", getProperty("webdriver.chrome.driver", "drivers/chromedriver-2.9_macosx"))
          new ChromeDriver()
        }
        case "ie" => new InternetExplorerDriver()
        case "internetexplorer" => new InternetExplorerDriver()
        case "safari" => new SafariDriver()
        case "firefox" => {
          val firefoxProfile = new FirefoxProfile()
          firefoxProfile.setAcceptUntrustedCertificates(true)
          new FirefoxDriver(firefoxProfile)
        }
        /*case "phantomjs" => {
          systemProperties.setProperty("webdriver.phantomjs.binary", getProperty("webdriver.phantomjs.binary", "drivers/phantomjs-1.9.7_macosx"))

          val capabilities = new DesiredCapabilities
          capabilities.setJavascriptEnabled(true)
          capabilities.setCapability("takesScreenshot", false)
          capabilities.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            systemProperties.getProperty("webdriver.phantomjs.binary"))
          capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, Array("--ignore-ssl-errors=yes", "--web-security=false", "--ssl-protocol=any"));

          new PhantomJSDriver(capabilities)
        }*/
        case _ => {
          // Default - HTMLUnit
          val driver = new HtmlUnitDriver()
          driver.setJavascriptEnabled(false) // TODO HTMLUnit blows up when navigating site!
          driver
        }
      }
    }

    val implicitlyWait = getProperty("browser.implicitlyWait", 5000)
    selectedDriver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.MILLISECONDS)
    selectedDriver
  }

  def testUrl: String = {
    if (testRemote) {
      getProperty("test.url", "http://localhost:9000/")
    }
    else {
      // Default if testing locally
      new String("http://localhost:9001/")
    }
  }

  def testRemote: Boolean = {
    getProperty("test.remote", false)
  }
}