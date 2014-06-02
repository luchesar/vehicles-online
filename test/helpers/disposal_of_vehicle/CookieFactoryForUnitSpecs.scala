package helpers.disposal_of_vehicle

import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
import mappings.disposal_of_vehicle.Dispose.DisposeFormRegistrationNumberCacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupDetailsCacheKey
import mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupFormModelCacheKey
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import mappings.disposal_of_vehicle.EnterAddressManually.EnterAddressManuallyCacheKey
import models.DayMonthYear
import models.domain.disposal_of_vehicle._
import play.api.libs.json.{Writes, Json}
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.{FakeDateServiceImpl, FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import services.fakes.FakeAddressLookupWebServiceImpl._
import services.fakes.FakeAddressLookupService.postcodeValid
import models.domain.common.{BruteForcePreventionResponse, AddressLinesModel, AddressAndPostcodeModel}
import mappings.disposal_of_vehicle.RelatedCacheKeys.SeenCookieMessageKey
import common.{ClientSideSessionFactory, CookieFlags, ClearTextClientSideSession}
import composition.TestComposition.{testInjector => injector}
import play.api.mvc.Cookie
import models.domain.common.BruteForcePreventionResponse._
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl._

object CookieFactoryForUnitSpecs { // TODO can we make this more fluent by returning "this" at the end of the defs

  implicit private val cookieFlags = injector.getInstance(classOf[CookieFlags])
  private val session = new ClearTextClientSideSession("trackingId")

  private def createCookie[A](key: String, value: A)(implicit tjs: Writes[A]): Cookie = {
    val json = Json.toJson(value).toString()
    val cookieName = session.nameCookie(key)
    session.newCookie(cookieName, json)
  }

  private def createCookie[A](key: String, value: String): Cookie = {
    val cookieName = session.nameCookie(key)
    session.newCookie(cookieName, value)
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
    val value = BusinessChooseYourAddressModel(uprnSelected = traderUprnValid.toString)
    createCookie(key, value)
  }

  def enterAddressManually() = {
    val key = EnterAddressManuallyCacheKey
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = line1Valid,
      line2 = Some(line2Valid),
      line3 = Some(line3Valid),
      line4 = line4Valid),
      postcode = postcodeValid))
    createCookie(key, value)
  }

  def traderDetailsModel(uprn: Option[Long] = None, line1: String = line1Valid, line2: String = line2Valid, line3: String = line3Valid, line4: String = line4Valid, traderPostcode: String = postcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = traderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(line1, line2, line3, line4, traderPostcode)))
    createCookie(key, value)
  }

  def bruteForcePreventionResponse(attempts: Int = 0, maxAttempts: Int = MaxAttempts) = {
    val key = BruteForcePreventionResponseCacheKey
    val value = BruteForcePreventionResponse(attempts, maxAttempts)
    createCookie(key, value)
  }

  def vehicleLookupFormModel(referenceNumber: String = referenceNumberValid,
                             registrationNumber: String = registrationNumberValid) = {
    val key = VehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    createCookie(key, value)
  }

  def vehicleDetailsModel(registrationNumber: String = registrationNumberValid,
                          vehicleMake: String = FakeVehicleLookupWebService.vehicleMakeValid,
                          vehicleModel: String = vehicleModelValid,
                          keeperName: String = keeperNameValid) = {
    val key = VehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    createCookie(key, value)
  }

  def disposeFormModel() = {
    val key = DisposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear(FakeDateServiceImpl.dateOfDisposalDayValid.toInt,
        FakeDateServiceImpl.dateOfDisposalMonthValid.toInt, FakeDateServiceImpl.dateOfDisposalYearValid.toInt),
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    createCookie(key, value)
  }

  def trackingIdModel(value: String) = {
    createCookie(ClientSideSessionFactory.SessionIdCookieName, value)
  }

  def disposeFormRegistrationNumber(registrationNumber: String = registrationNumberValid) =
    createCookie(DisposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeFormTimestamp(timestamp: String = s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-${dateOfDisposalDayValid}") =
    createCookie(DisposeFormTimestampIdCacheKey, timestamp)

  def disposeTransactionId(transactionId: String = transactionIdValid) =
    createCookie(DisposeFormTransactionIdCacheKey, transactionId)

  def vehicleRegistrationNumber(registrationNumber: String = registrationNumberValid) =
    createCookie(DisposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeModel(referenceNumber: String = referenceNumberValid,
                   registrationNumber: String = registrationNumberValid,
                   dateOfDisposal: DayMonthYear = DayMonthYear.today,
                   mileage: Option[Int] = None) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeModelCacheKey
    val value = DisposeModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber,
      dateOfDisposal = dateOfDisposal,
      mileage = mileage)
    createCookie(key, value)
  }

}
