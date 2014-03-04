package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait DisposeFailurePage extends WebBrowser { this :WebBrowser =>

  object DisposeFailurePage extends DisposeFailurePage

  class DisposeFailurePage extends Page {

    override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/dispose-failure"
    override val title: String = "Dispose a vehicle into the motor trade: failure"

    def setuptradedetails(implicit driver: WebDriver): Element = find(id("setuptradedetails")).get

    def vehiclelookup(implicit driver: WebDriver): Element = find(id("vehiclelookup")).get

  }
}