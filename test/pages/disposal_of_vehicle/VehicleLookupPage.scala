package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._

object VehicleLookupPage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/vehicle-lookup"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Dispose a vehicle into the motor trade: vehicle"

  def vehicleRegistrationNumber(implicit driver: WebDriver): TextField = textField(id("registrationNumber"))

  def documentReferenceNumber(implicit driver: WebDriver): TextField = textField(id("referenceNumber"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def consent(implicit driver: WebDriver): Checkbox = checkbox(id("consent"))

  def happyPath(implicit driver: WebDriver, referenceNumber: String = referenceNumberValid, vehicleRegistrationNumber: String = registrationNumberValid) = {
    go to VehicleLookupPage
    VehicleLookupPage.documentReferenceNumber.value = referenceNumber
    VehicleLookupPage.vehicleRegistrationNumber.value = vehicleRegistrationNumber
    click on VehicleLookupPage.consent
    click on VehicleLookupPage.findVehicleDetails
  }
}
