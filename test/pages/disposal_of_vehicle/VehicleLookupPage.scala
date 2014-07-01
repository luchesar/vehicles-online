package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, TextField, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.VehicleLookup.{BackId, DocumentReferenceNumberId, ExitId, SubmitId, VehicleRegistrationNumberId}
import org.openqa.selenium.WebDriver
import services.fakes.FakeVehicleLookupWebService.{ReferenceNumberValid, RegistrationNumberValid}
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl

object VehicleLookupPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/vehicle-lookup"
  final val progressStep = "Step 4 of 6"
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
