package helpers.disposal_of_vehicle

import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import models.DayMonthYear
import models.domain.disposal_of_vehicle._
import org.openqa.selenium.{WebDriver, Cookie}
import play.api.libs.json.{Writes, Json}
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeAddressLookupService.postcodeValid
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeAddressLookupWebServiceImpl._
import services.fakes.{FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import mappings.disposal_of_vehicle.EnterAddressManually._
import models.domain.common.{BruteForcePreventionResponse, AddressLinesModel, AddressAndPostcodeModel}
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl._
import models.domain.common.BruteForcePreventionResponse._

object CookieFactoryForUISpecs {
  private def addCookie[A](key: String, value: A)(implicit tjs: Writes[A], webDriver: WebDriver): Unit = {
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
  }

  def setupTradeDetails(traderPostcode: String = postcodeValid)(implicit webDriver: WebDriver) = {
    val key = SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    addCookie(key, value)
    this
  }

  def businessChooseYourAddress(uprn: Long = traderUprnValid)(implicit webDriver: WebDriver) = {
    val key = BusinessChooseYourAddressCacheKey
    val value = BusinessChooseYourAddressModel(uprnSelected = uprn.toString)
    addCookie(key, value)
    this
  }

  def enterAddressManually()(implicit webDriver: WebDriver) = {
    val key = EnterAddressManuallyCacheKey
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = line1Valid,
      line2 = Some(line2Valid),
      line3 = Some(line3Valid),
      line4 = line4Valid),
      postcode = postcodeValid))
    addCookie(key, value)
    this
  }

  def dealerDetails(address: AddressViewModel = addressWithoutUprn)(implicit webDriver: WebDriver) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = traderBusinessNameValid, traderAddress = address)
    addCookie(key, value)
    this
  }

  def bruteForcePreventionResponse(attempts: Int = 0, maxAttempts: Int = MaxAttempts)(implicit webDriver: WebDriver) = {
    val key = BruteForcePreventionResponseCacheKey
    val value = BruteForcePreventionResponse(attempts, maxAttempts)
    addCookie(key, value)
    this
  }

  def vehicleLookupFormModel(referenceNumber: String = referenceNumberValid,
                                        registrationNumber: String = registrationNumberValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    addCookie(key, value)
    this
  }

  def vehicleDetailsModel(registrationNumber: String = registrationNumberValid,
                                     vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid,
                                     vehicleModel: String = vehicleModelValid,
                                     keeperName: String = keeperNameValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    addCookie(key, value)
    this
  }

  def disposeFormModel()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear.today,
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    addCookie(key, value)
    this
  }

  def disposeModel(referenceNumber: String = referenceNumberValid,
                              registrationNumber: String = registrationNumberValid,
                              dateOfDisposal: DayMonthYear = DayMonthYear.today,
                              mileage: Option[Int] = None)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      consent = "true",
      lossOfRegistrationConsent = "true",
      mileage = mileage)
    addCookie(key, value)
    this
  }

  def disposeTransactionId(transactionId: String = transactionIdValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormTransactionIdCacheKey
    val value = transactionId
    addCookie(key, value)
    this
  }

  def vehicleRegistrationNumber()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormRegistrationNumberCacheKey
    val value = registrationNumberValid
    addCookie(key, value)
    this
  }
}
