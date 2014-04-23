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

  def back(implicit driver: WebDriver): Element = find(id(backId)).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(id(submitId)).get

  def happyPath(referenceNumber: String = referenceNumberValid, registrationNumber: String = registrationNumberValid)(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    documentReferenceNumber.value = referenceNumber
    VehicleLookupPage.vehicleRegistrationNumber.value = registrationNumber
    click on findVehicleDetails
  }
}
