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
import services.fakes.FakeAddressLookupService.PostcodeValid
import models.domain.common.{BruteForcePreventionResponse, AddressLinesModel, AddressAndPostcodeModel}
import mappings.disposal_of_vehicle.RelatedCacheKeys.SeenCookieMessageKey
import common.{ClientSideSessionFactory, CookieFlags, ClearTextClientSideSession}
import composition.TestComposition.{testInjector => injector}
import play.api.mvc.Cookie
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel.BruteForcePreventionViewModelCacheKey
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

  def setupTradeDetails(traderPostcode: String = PostcodeValid) = {
    val key = SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = TraderBusinessNameValid,
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
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = Line1Valid,
      line2 = Some(Line2Valid),
      line3 = Some(Line3Valid),
      line4 = Line4Valid),
      postcode = PostcodeValid))
    createCookie(key, value)
  }

  def traderDetailsModel(uprn: Option[Long] = None, line1: String = Line1Valid, line2: String = Line2Valid, line3: String = Line3Valid, line4: String = Line4Valid, traderPostcode: String = PostcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(line1, line2, line3, line4, traderPostcode)))
    createCookie(key, value)
  }

  def bruteForcePreventionViewModel(permitted: Boolean = true, attempts: Int = 0, maxAttempts: Int = MaxAttemptsOneBased) = {
    val key = BruteForcePreventionViewModelCacheKey
    val value = BruteForcePreventionViewModel(permitted, attempts, maxAttempts)
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
      dateOfDisposal = DayMonthYear(FakeDateServiceImpl.DateOfDisposalDayValid.toInt,
        FakeDateServiceImpl.DateOfDisposalMonthValid.toInt, FakeDateServiceImpl.DateOfDisposalYearValid.toInt),
      consent = FakeDisposeWebServiceImpl.consentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.consentValid)
    createCookie(key, value)
  }

  def trackingIdModel(value: String) = {
    createCookie(ClientSideSessionFactory.SessionIdCookieName, value)
  }

  def disposeFormRegistrationNumber(registrationNumber: String = registrationNumberValid) =
    createCookie(DisposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeFormTimestamp(timestamp: String = s"$DateOfDisposalYearValid-$DateOfDisposalMonthValid-${DateOfDisposalDayValid}") =
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
      consent = "true",
      lossOfRegistrationConsent = "true",
      mileage = mileage)
    createCookie(key, value)
  }

}
