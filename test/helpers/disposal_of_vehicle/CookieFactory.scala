package helpers.disposal_of_vehicle

import services.fakes.FakeAddressLookupService._
import models.domain.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import play.api.libs.json.Json
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.{FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import models.DayMonthYear
import mappings.disposal_of_vehicle.Dispose._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._

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
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }

  def vehicleDetailsModel(registrationNumber: String = registrationNumberValid,
                          vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid,
                          vehicleModel: String = vehicleModelValid,
                          keeperName: String = keeperNameValid) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }

  def disposeFormModel() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear.today,
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }

  def disposeFormRegistrationNumber(registrationNumber: String = registrationNumberValid) = {
    val key = disposeFormRegistrationNumberCacheKey
    val value = registrationNumber
    play.api.mvc.Cookie(key, value)
  }

  def disposeFormTimestamp(timestamp: String = s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-${dateOfDisposalDayValid}T00:00:00.000+01:00") = {
    val key = disposeFormTimestampIdCacheKey
    val value = timestamp
    play.api.mvc.Cookie(key, value)
  }

  def disposeTransactionId(transactionId: String = transactionIdValid) = {
    val key = disposeFormTransactionIdCacheKey
    val value = transactionId
    play.api.mvc.Cookie(key, value)
  }

  def vehicleRegistrationNumber(registrationNumber: String = registrationNumberValid) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormRegistrationNumberCacheKey
    val value = registrationNumber
    play.api.mvc.Cookie(key, value)
  }

  def disposeModel(referenceNumber: String = referenceNumberValid,
                   registrationNumber: String = registrationNumberValid,
                   dateOfDisposal: DayMonthYear = DayMonthYear.today,
                   mileage: Option[Int] = None) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      mileage = mileage)
    val valueAsString = Json.toJson(value).toString()
    play.api.mvc.Cookie(key, valueAsString)
  }
}