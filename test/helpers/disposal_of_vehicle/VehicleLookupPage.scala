package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, VehicleLookupFormModel}
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

import play.api.Play.current

object VehicleLookupPage {
  val url = "/disposal-of-vehicle/vehicle-lookup"
  val title = "Dispose a vehicle into the motor trade: vehicle"

  def happyPath(browser: TestBrowser, referenceNumber: String = documentReferenceNumberValid, vehicleRegistrationNumber: String = vehicleRegistrationNumberValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill("#referenceNumber") `with` referenceNumber
    browser.fill("#registrationNumber") `with` vehicleRegistrationNumber
    browser.submit("button[type='submit']")
  }

  def setupVehicleDetailsModelCache(vehicleMake: String = "make", vehicleModel:String = "model", keeperName:String = "keeper") = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(
      vehicleMake,
      vehicleModel,
      keeperName,
      keeperAddress = address1)
    play.api.cache.Cache.set(key, value)
  }

  def setupVehicleLookupFormModelCache (referenceNumber: String = documentReferenceNumberValid, registrationNumber: String = vehicleRegistrationNumberValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber, registrationNumber)
    play.api.cache.Cache.set(key, value)
  }
}
