package helpers.disposal_of_vehicle

import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.Dispose.disposeFormModelCacheKey
import mappings.disposal_of_vehicle.Dispose.disposeFormRegistrationNumberCacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import mappings.disposal_of_vehicle.TraderDetails.traderDetailsCacheKey
import mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupDetailsCacheKey
import mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import mappings.disposal_of_vehicle.EnterAddressManually.enterAddressManuallyCacheKey
import models.DayMonthYear
import models.domain.disposal_of_vehicle._
import play.api.libs.json.{Writes, Json}
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.{FakeDateServiceImpl, FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import services.fakes.FakeWebServiceImpl._
import services.fakes.FakeAddressLookupService.postcodeValid
import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import scala.Some
import play.api.mvc.Cookie
import utils.helpers.CryptoHelper
import mappings.disposal_of_vehicle.RelatedCacheKeys.SeenCookieMessageKey

object CookieFactoryForUnitSpecs {
  private def createCookie[A](key: String, value: A)(implicit tjs: Writes[A]): Cookie = {
    val valueAsString = Json.toJson(value).toString()
    val cookie = CryptoHelper.createCookie(name = key,
      value = valueAsString)
    assert(cookie.maxAge.get > 0, "MaxAge is an option, and in testing we are not setting maxAge, so our understanding " +
      s"is that a Cookie will be created with maxAge = None and therefore does not expire during a test. Value was: ${cookie.maxAge}")
    cookie
  }

  private def createCookie[A](key: String, value: String): Cookie = {
    CryptoHelper.createCookie(name = key,
      value = value)
  }

  def seenCookieMessage() = {
    val key = SeenCookieMessageKey
    val value = "yes" // TODO make a constant
    createCookie(key, value)
  }

  def setupTradeDetails(traderPostcode: String = postcodeValid) = {
    val key = SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = traderBusinessNameValid,
      traderPostcode = traderPostcode)
    createCookie(key, value)
  }

  def businessChooseYourAddress() = {
    val key = BusinessChooseYourAddressCacheKey
    val value = BusinessChooseYourAddressModel(uprnSelected = traderUprnValid)
    createCookie(key, value)
  }

  def enterAddressManually() = {
    val key = enterAddressManuallyCacheKey
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = line1Valid,
      line2 = Some(line2Valid),
      line3 = Some(line3Valid),
      line4 = Some(line4Valid)),
      postcode = postcodeValid))
    createCookie(key, value)
  }

  def traderDetailsModel(uprn: Option[Long] = None, line1: String = "my house", traderPostcode: String = postcodeValid) = {
    val key = traderDetailsCacheKey
    val value = TraderDetailsModel(traderName = traderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(line1, "my street", "my area", "my town", "CM81QJ")))
    createCookie(key, value)
  }

  def vehicleLookupFormModel(referenceNumber: String = referenceNumberValid,
                             registrationNumber: String = registrationNumberValid) = {
    val key = vehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    createCookie(key, value)
  }

  def vehicleDetailsModel(registrationNumber: String = registrationNumberValid,
                          vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid,
                          vehicleModel: String = vehicleModelValid,
                          keeperName: String = keeperNameValid) = {
    val key = vehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    createCookie(key, value)
  }

  def disposeFormModel() = {
    val key = disposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear(FakeDateServiceImpl.dateOfDisposalDayValid.toInt,
        FakeDateServiceImpl.dateOfDisposalMonthValid.toInt, FakeDateServiceImpl.dateOfDisposalYearValid.toInt),
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    createCookie(key, value)
  }

  def disposeFormRegistrationNumber(registrationNumber: String = registrationNumberValid) =
    createCookie(disposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeFormTimestamp(timestamp: String = s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-${dateOfDisposalDayValid}") =
    createCookie(disposeFormTimestampIdCacheKey, timestamp)

  def disposeTransactionId(transactionId: String = transactionIdValid) =
    createCookie(disposeFormTransactionIdCacheKey, transactionId)

  def vehicleRegistrationNumber(registrationNumber: String = registrationNumberValid) =
    createCookie(disposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeModel(referenceNumber: String = referenceNumberValid,
                   registrationNumber: String = registrationNumberValid,
                   dateOfDisposal: DayMonthYear = DayMonthYear.today,
                   mileage: Option[Int] = None) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      mileage = mileage)
    createCookie(key, value)
  }
}