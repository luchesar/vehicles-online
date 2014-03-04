package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait BusinessChangeYourAddressPage extends WebBrowser { this :WebBrowser =>

   object BusinessChangeYourAddressPage extends BusinessChangeYourAddressPage

   class BusinessChangeYourAddressPage extends Page {

     override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/business-choose-your-address"
     override val title = "Business: Choose your address"

     def chooseAddress(implicit driver: WebDriver): SingleSel = singleSel(id("disposal_businessChooseYourAddress_addressSelect"))

     def back(implicit driver: WebDriver): Element = find(id("backButton")).get

     def select(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

   }
 }