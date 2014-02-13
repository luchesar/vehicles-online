package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import models.domain.common.Address
import play.api.Play.current

object VehicleLookupPage {
  val url = "/disposal-of-vehicle/vehicle-lookup"

  def happyPath(browser: TestBrowser, v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill("#v5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#v5cRegistrationNumber") `with` v5cVehicleRegistrationNumber
    browser.fill("#v5cKeeperName") `with` v5cKeeperName
    browser.fill("#v5cPostcode") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }

  def setupCache() = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheKey
    val value = VehicleDetailsModel(vehicleMake = "make", vehicleModel = "model",
      keeperName = "keeper", keeperAddress = Address(line1 = "line1", postCode = "postcode"))
    play.api.cache.Cache.set(key, value)
  }
}
