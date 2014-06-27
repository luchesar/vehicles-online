package helpers.disposal_of_vehicle

import composition.TestComposition
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
import mappings.disposal_of_vehicle.Dispose.DisposeFormRegistrationNumberCacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import mappings.disposal_of_vehicle.VehicleLookup._
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
import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import mappings.disposal_of_vehicle.RelatedCacheKeys.SeenCookieMessageKey
import common.{ClientSideSessionFactory, CookieFlags, ClearTextClientSideSession}
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel.BruteForcePreventionViewModelCacheKey
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl._
import mappings.common.PreventGoingToDisposePage.PreventGoingToDisposePageCacheKey
import play.api.mvc.Cookie

object CookieFactoryForUnitSpecs extends TestComposition { // TODO can we make this more fluent by returning "this" at the end of the defs

  implicit private val cookieFlags = injector.getInstance(classOf[CookieFlags])
  final val TrackingIdValue = "trackingId"
  private val session = new ClearTextClientSideSession(TrackingIdValue)

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
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
      line2 = Some(Line2Valid),
      line3 = Some(Line3Valid),
      postTown = PostTownValid)))
    createCookie(key, value)
  }

  def traderDetailsModel(uprn: Option[Long] = None, buildingNameOrNumber: String = BuildingNameOrNumberValid, line2: String = Line2Valid, line3: String = Line3Valid, postTown: String = PostTownValid, traderPostcode: String = PostcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(buildingNameOrNumber, line2, line3, postTown, traderPostcode)))
    createCookie(key, value)
  }

  def traderDetailsModelBuildingNameOrNumber(uprn: Option[Long] = None, buildingNameOrNumber: String = BuildingNameOrNumberValid, postTown: String = PostTownValid, traderPostcode: String = PostcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(buildingNameOrNumber, postTown, traderPostcode)))
    createCookie(key, value)
  }

  def traderDetailsModelLine2(uprn: Option[Long] = None, buildingNameOrNumber: String = BuildingNameOrNumberValid, line2: String = Line2Valid, postTown: String = PostTownValid, traderPostcode: String = PostcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(buildingNameOrNumber, line2, postTown, traderPostcode)))
    createCookie(key, value)
  }

  def traderDetailsModelPostTown(uprn: Option[Long] = None, postTown: String = PostTownValid, traderPostcode: String = PostcodeValid) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid,
      traderAddress = AddressViewModel(uprn = uprn, address = Seq(postTown, traderPostcode)))
    createCookie(key, value)
  }

  def bruteForcePreventionViewModel(permitted: Boolean = true,
                                    attempts: Int = 0,
                                    maxAttempts: Int = MaxAttempts,
                                    dateTimeISOChronology: String = org.joda.time.DateTime.now().toString) = {
    val key = BruteForcePreventionViewModelCacheKey
    val value = BruteForcePreventionViewModel(permitted,
      attempts,
      maxAttempts,
      dateTimeISOChronology = dateTimeISOChronology)
    createCookie(key, value)
  }

  def vehicleLookupFormModel(referenceNumber: String = ReferenceNumberValid,
                             registrationNumber: String = RegistrationNumberValid) = {
    val key = VehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    createCookie(key, value)
  }

  def vehicleDetailsModel(registrationNumber: String = RegistrationNumberValid,
                          vehicleMake: String = FakeVehicleLookupWebService.VehicleMakeValid,
                          vehicleModel: String = VehicleModelValid,
                          keeperName: String = KeeperNameValid) = {
    val key = VehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    createCookie(key, value)
  }

  def vehicleLookupResponseCode(responseCode: String = "disposal_vehiclelookupfailure") =
    createCookie(VehicleLookupResponseCodeCacheKey, responseCode)

  def disposeFormModel() = {
    val key = DisposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear(FakeDateServiceImpl.DateOfDisposalDayValid.toInt,
        FakeDateServiceImpl.DateOfDisposalMonthValid.toInt, FakeDateServiceImpl.DateOfDisposalYearValid.toInt),
      consent = FakeDisposeWebServiceImpl.ConsentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.ConsentValid)
    createCookie(key, value)
  }

  def trackingIdModel(value: String = TrackingIdValue) = {
    createCookie(ClientSideSessionFactory.TrackingIdCookieName, value)
  }

  def disposeFormRegistrationNumber(registrationNumber: String = RegistrationNumberValid) =
    createCookie(DisposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeFormTimestamp(timestamp: String = s"$DateOfDisposalYearValid-$DateOfDisposalMonthValid-$DateOfDisposalDayValid") =
    createCookie(DisposeFormTimestampIdCacheKey, timestamp)

  def disposeTransactionId(transactionId: String = TransactionIdValid) =
    createCookie(DisposeFormTransactionIdCacheKey, transactionId)

  def vehicleRegistrationNumber(registrationNumber: String = RegistrationNumberValid) =
    createCookie(DisposeFormRegistrationNumberCacheKey, registrationNumber)

  def disposeModel(referenceNumber: String = ReferenceNumberValid,
                   registrationNumber: String = RegistrationNumberValid,
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

  def preventGoingToDisposePage(payload: String = "") =
    createCookie(PreventGoingToDisposePageCacheKey, payload)

  def disposeSurveyUrl(surveyUrl: String) =
    createCookie(SurveyRequestTriggerDateCookieKey, surveyUrl)
}
