package helpers.disposal_of_vehicle

import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import models.DayMonthYear
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.{FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import services.fakes.FakeAddressLookupService._
import services.session.SessionState
import org.openqa.selenium.{WebDriver, Cookie}
import play.api.libs.json.Json

class CacheSetup() { // TODO change to an object.

  def setupTradeDetailsIntegration(traderPostcode: String = postcodeValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def dealerDetailsIntegration(address: AddressViewModel = addressWithoutUprn)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.DealerDetails.dealerDetailsCacheKey
    val value = DealerDetailsModel(dealerName = "", // TODO [SKW] why are we caching an empty string?
      dealerAddress = address)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def vehicleLookupFormModelIntegration(referenceNumber: String = referenceNumberValid, registrationNumber: String = registrationNumberValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def vehicleDetailsModelIntegration(registrationNumber: String = registrationNumberValid,
                                     vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid,
                                     vehicleModel: String = vehicleModelValid,
                                     keeperName: String = keeperNameValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def disposeFormModelIntegration()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear.today,
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def disposeModelIntegration(referenceNumber: String = referenceNumberValid,
                              registrationNumber: String = registrationNumberValid,
                              dateOfDisposal: DayMonthYear = DayMonthYear.today,
                              mileage: Option[Int] = None)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      mileage = mileage)
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }

  def disposeTransactionIdIntegration(transactionId: String = transactionIdValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormTransactionIdCacheKey
    val value = transactionId
    val manage = webDriver.manage()
    val cookie = new Cookie(key, value)
    manage.addCookie(cookie)
    this
  }

  def vehicleRegistrationNumberIntegration()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormRegistrationNumberCacheKey
    val value = registrationNumberValid
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
    this
  }
}
