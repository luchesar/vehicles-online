package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait SetupTradeDetailsPage extends WebBrowser { this :WebBrowser =>

   object SetupTradeDetailsPage extends SetupTradeDetailsPage

   class SetupTradeDetailsPage extends Page {

     override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/setup-trade-details"
     override val title: String = "Dispose a vehicle into the motor trade: set-up"

     def dealerName(implicit driver: WebDriver): TextField = textField(id("dealerName"))

     def dealerPostcode(implicit driver: WebDriver): TextField = textField(id("dealerPostcode"))

     def lookup(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

   }
 }