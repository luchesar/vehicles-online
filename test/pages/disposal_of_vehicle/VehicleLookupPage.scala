package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeVehicleLookupWebService._
import mappings.disposal_of_vehicle.VehicleLookup._

object VehicleLookupPage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/vehicle-lookup"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Find vehicle details"

  def vehicleRegistrationNumber(implicit driver: WebDriver): TextField = textField(id(registrationNumberId))

  def documentReferenceNumber(implicit driver: WebDriver): TextField = textField(id(referenceNumberId))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(referenceNumber: String = referenceNumberValid, vehicleRegistrationNumber: String = registrationNumberValid)(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    VehicleLookupPage.documentReferenceNumber.value = referenceNumber
    VehicleLookupPage.vehicleRegistrationNumber.value = vehicleRegistrationNumber
    click on VehicleLookupPage.findVehicleDetails
  }
}
