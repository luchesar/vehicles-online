package helpers.webbrowser

import app.ConfigProperties._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}

//import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
//import org.openqa.selenium.remote.DesiredCapabilities

import java.util.concurrent.TimeUnit

object WebDriverFactory {
  private val systemProperties = System.getProperties()

  def baseUrl: String = {
    getProperty("base_url", "http://localhost:9000/")
  } // TODO Use the play method to get the current.config as in the vehicles-online and do this for every use of conf.

  def webDriver: WebDriver = {
    val targetBrowser = getProperty("browser", "htmlunit")
    val selectedDriver: WebDriver = {

      targetBrowser match {
        case "chrome" => chromeDriver
        case "ie" => new InternetExplorerDriver()
        case "internetexplorer" => new InternetExplorerDriver()
        case "safari" => new SafariDriver()
        case "htmlunit" => htmlunitDriver
        case _ => firefoxDriver // Default
      }
    }
    defineTimeout(selectedDriver, 5, TimeUnit.SECONDS)
    selectedDriver
  }

  private def chromeDriver = {
    systemProperties.setProperty("webdriver.chrome.driver", getProperty("webdriver.chrome.driver", "drivers/chromedriver-2.9_macosx"))
    new ChromeDriver()
  }

  private def htmlunitDriver = {
    val driver = new HtmlUnitDriver()
    driver.setJavascriptEnabled(true)
    driver
  }

  private def firefoxDriver = {
    val firefoxProfile = new FirefoxProfile()
    firefoxProfile.setAcceptUntrustedCertificates(true)
    new FirefoxDriver(firefoxProfile)
  }

  private def defineTimeout(selectedDriver: WebDriver, seconds: Long, timeUnit: TimeUnit) {
    selectedDriver.manage().timeouts().implicitlyWait(seconds, timeUnit)
  }

}