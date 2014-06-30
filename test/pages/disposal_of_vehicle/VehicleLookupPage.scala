package pages.disposal_of_vehicle

import helpers.webbrowser._
import mappings.disposal_of_vehicle.VehicleLookup._
import org.openqa.selenium.WebDriver
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl

object VehicleLookupPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/vehicle-lookup"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Enter vehicle details"

  def vehicleRegistrationNumber(implicit driver: WebDriver): TextField = textField(id(VehicleRegistrationNumberId))

  def documentReferenceNumber(implicit driver: WebDriver): TextField = textField(id(DocumentReferenceNumberId))

  def back(implicit driver: WebDriver): Element = find(id(BackId)).get

  def exit(implicit driver: WebDriver): Element = find(id(ExitId)).get

  def findVehicleDetails(implicit driver: WebDriver): Element = find(id(SubmitId)).get

  def happyPath(referenceNumber: String = ReferenceNumberValid, registrationNumber: String = RegistrationNumberValid)(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    documentReferenceNumber.value = referenceNumber
    VehicleLookupPage.vehicleRegistrationNumber.value = registrationNumber
    click on findVehicleDetails
  }

  def tryLockedVrm()(implicit driver: WebDriver) = {
    go to VehicleLookupPage
    documentReferenceNumber.value = ReferenceNumberValid
    VehicleLookupPage.vehicleRegistrationNumber.value = FakeBruteForcePreventionWebServiceImpl.VrmLocked
    click on findVehicleDetails
  }
}
