package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{AddressViewModel, VehicleLookupFormModel, VehicleDetailsModel}
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

import play.api.Play.current
import mappings.disposal_of_vehicle.VehicleLookup._

object VehicleLookupPage {
  val url = "/disposal-of-vehicle/vehicle-lookup"
  val title = "Dispose a vehicle into the motor trade: vehicle"
  val address1 = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))

  def happyPath(browser: TestBrowser, referenceNumber: String = referenceNumberValid, vehicleRegistrationNumber: String = registrationNumberValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill(s"#${referenceNumberId}") `with` referenceNumber
    browser.fill(s"#${registrationNumberId}") `with` vehicleRegistrationNumber
    browser.click(s"#${consentId}")
    browser.submit("button[type='submit']")
  }

  def setupVehicleDetailsModelCache(vehicleMake: String = "make", vehicleModel:String = "model", keeperName:String = "keeper") = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(vehicleMake,vehicleModel,keeperName,keeperAddress = address1)
    play.api.cache.Cache.set(key, value)
  }

  def setupVehicleLookupFormModelCache (referenceNumber: String = referenceNumberValid, registrationNumber: String = registrationNumberValid, consent: String = consentValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber, registrationNumber, consent)
    play.api.cache.Cache.set(key, value)
  }
}
