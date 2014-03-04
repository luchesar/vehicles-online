package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, VehicleLookupFormModel}
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

import play.api.Play.current

object VehicleLookupPage {
  val url = "/disposal-of-vehicle/vehicle-lookup"
  val title = "Dispose a vehicle into the motor trade: vehicle"

  def happyPath(browser: TestBrowser, v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill("#v5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#v5cRegistrationNumber") `with` v5cVehicleRegistrationNumber
    browser.fill("#v5cKeeperName") `with` v5cKeeperName
    browser.fill("#v5cPostcode") `with` v5cPostcode
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

  def setupVehicleLookupFormModelCache (v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(v5cReferenceNumber, v5cRegistrationNumber, v5cKeeperName, v5cPostcode)
    play.api.cache.Cache.set(key, value)
  }
}
