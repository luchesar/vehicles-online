package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeVehicleLookupWebService._
import mappings.disposal_of_vehicle.VehicleLookup._
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl

object VehicleLookupPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/vehicle-lookup"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Find vehicle details"

  def vehicleRegistrationNumber(implicit driver: WebDriver): TextField = textField(id(VehicleRegistrationNumberId))

  def documentReferenceNumber(implicit driver: WebDriver): TextField = textField(id(DocumentReferenceNumberId))

  def back(implicit driver: WebDriver): Element = find(id(BackId)).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(id(SubmitId)).get

  def happyPath(referenceNumber: String = referenceNumberValid, registrationNumber: String = registrationNumberValid)(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    documentReferenceNumber.value = referenceNumber
    VehicleLookupPage.vehicleRegistrationNumber.value = registrationNumber
    click on findVehicleDetails
  }

  def tryLockedVrm()(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    documentReferenceNumber.value = referenceNumberValid
    VehicleLookupPage.vehicleRegistrationNumber.value = FakeBruteForcePreventionWebServiceImpl.VrmLocked
    click on findVehicleDetails
  }
}
