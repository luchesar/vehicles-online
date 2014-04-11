package helpers.disposal_of_vehicle

import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle._
import play.api.Play.current
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import models.DayMonthYear
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.{FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import services.fakes.FakeAddressLookupService._

object CacheSetup {

  def setupTradeDetails(traderPostcode: String = postcodeValid) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    play.api.cache.Cache.set(key, value)
  }

  def businessChooseYourAddress(address: AddressViewModel = addressWithoutUprn) = {
    val key = mappings.disposal_of_vehicle.DealerDetails.dealerDetailsCacheKey
    val value = DealerDetailsModel(dealerName = "", // TODO [SKW] why are we caching an empty string?
      dealerAddress = address)
    play.api.cache.Cache.set(key, value)
  }

  def vehicleDetailsModel(registrationNumber: String = registrationNumberValid, vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid, vehicleModel:String = vehicleModelValid, keeperName:String = keeperNameValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    play.api.cache.Cache.set(key, value)
  }

  def vehicleLookupFormModel (referenceNumber: String = referenceNumberValid, registrationNumber: String = registrationNumberValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    play.api.cache.Cache.set(key, value)
  }

  def disposeFormModel() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear.today,
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    play.api.cache.Cache.set(key, value)
  }

  def disposeModel(referenceNumber:String = referenceNumberValid, registrationNumber:String = registrationNumberValid, dateOfDisposal:DayMonthYear = DayMonthYear.today, mileage:Option[Int] = None) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      mileage = mileage)
    play.api.cache.Cache.set(key, value)
  }

  def disposeTransactionId() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormTransactionIdCacheKey
    val value = transactionIdValid
    play.api.cache.Cache.set(key, value)
  }

  def vehicleRegistrationNumber() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormRegistrationNumberCacheKey
    val value = registrationNumberValid
    play.api.cache.Cache.set(key, value)
  }
}