package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait VehicleLookupPage extends WebBrowser { this :WebBrowser =>

   object VehicleLookupPage extends VehicleLookupPage

   class VehicleLookupPage extends Page {

     override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/vehicle-lookup"
     override val title: String = "Dispose a vehicle into the motor trade: vehicle"

     def v5cReferenceNumber(implicit driver: WebDriver): TextField = textField(id("v5cReferenceNumber"))

     def v5cRegistrationNumber(implicit driver: WebDriver): TextField = textField(id("v5cRegistrationNumber"))

     def v5cKeeperName(implicit driver: WebDriver): TextField = textField(id("v5cKeeperName"))

     def v5cPostcode(implicit driver: WebDriver): TextField = textField(id("v5cPostcode"))

     def back(implicit driver: WebDriver): Element = find(id("backButton")).get

     def findVehicleDetails(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

   }
 }