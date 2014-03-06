package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object VehicleLookupPage extends Page with WebBrowser {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/vehicle-lookup"
  override val title: String = "Dispose a vehicle into the motor trade: vehicle"

  def vehicleRegistrationNumber(implicit driver: WebDriver): TextField = textField(id("registrationNumber"))

  def documentReferenceNumber(implicit driver: WebDriver): TextField = textField(id("referenceNumber"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def consent(implicit driver: WebDriver): Checkbox = checkbox(id("consent"))
}