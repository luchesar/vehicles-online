package helpers.disposal_of_vehicle

import services.fakes.FakeAddressLookupService._
import models.domain.disposal_of_vehicle.{VehicleLookupFormModel, AddressViewModel, DealerDetailsModel, SetupTradeDetailsModel}
import helpers.disposal_of_vehicle.Helper._
import play.api.libs.json.Json
import services.fakes.FakeVehicleLookupWebService._

object CookieFactory { // TODO setup the cookies for the Unit Specs here, removing them from CacheSetup
  def setupTradeDetails(traderPostcode: String = postcodeValid) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }

  def dealerDetails(uprn: Option[Long] = None, line1: String = "my house", traderPostcode: String = postcodeValid) = {
    val key = mappings.disposal_of_vehicle.DealerDetails.dealerDetailsCacheKey
    val value = DealerDetailsModel(dealerName = traderBusinessNameValid,
      dealerAddress = AddressViewModel(uprn = uprn, address = Seq(line1, "my street", "my area", "my town", "CM81QJ")))
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }

  def vehicleLookupFormModel(referenceNumber: String = referenceNumberValid,
                             registrationNumber: String = registrationNumberValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber, registrationNumber = registrationNumber)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }
}