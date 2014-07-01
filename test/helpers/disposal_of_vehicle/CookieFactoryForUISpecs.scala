package helpers.disposal_of_vehicle

import mappings.common.AlternateLanguages._
import models.DayMonthYear
import mappings.disposal_of_vehicle.BusinessChooseYourAddress.BusinessChooseYourAddressCacheKey
import mappings.disposal_of_vehicle.SetupTradeDetails.SetupTradeDetailsCacheKey
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import mappings.disposal_of_vehicle.EnterAddressManually.EnterAddressManuallyCacheKey
import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel.BruteForcePreventionViewModelCacheKey
import models.domain.disposal_of_vehicle.{SetupTradeDetailsModel, BusinessChooseYourAddressModel, EnterAddressManuallyModel}
import models.domain.disposal_of_vehicle.{AddressViewModel, TraderDetailsModel, BruteForcePreventionViewModel}
import models.domain.disposal_of_vehicle.{VehicleLookupFormModel, VehicleDetailsModel, DisposeFormModel, DisposeModel}
import org.openqa.selenium.{WebDriver, Cookie}
import play.api.libs.json.{Writes, Json}
import services.fakes.FakeAddressLookupService.{TraderBusinessNameValid, Line2Valid, Line3Valid, PostTownValid}
import services.fakes.FakeAddressLookupService.{PostcodeValid, BuildingNameOrNumberValid, addressWithoutUprn}
import services.fakes.FakeDisposeWebServiceImpl.TransactionIdValid
import services.fakes.FakeVehicleLookupWebService.{ReferenceNumberValid, RegistrationNumberValid, VehicleModelValid, KeeperNameValid}
import services.fakes.FakeAddressLookupWebServiceImpl.traderUprnValid
import services.fakes.{FakeDisposeWebServiceImpl, FakeVehicleLookupWebService}
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl.MaxAttempts
import play.api.Play
import play.api.Play.current

object CookieFactoryForUISpecs {
  private def addCookie[A](key: String, value: A)(implicit tjs: Writes[A], webDriver: WebDriver): Unit = {
    val valueAsString = Json.toJson(value).toString()
    val manage = webDriver.manage()
    val cookie = new Cookie(key, valueAsString)
    manage.addCookie(cookie)
  }

  def withLanguageCy()(implicit webDriver: WebDriver) = {
    val key = Play.langCookieName
    val value = CyId
    addCookie(key, value)
    this
  }

  def withLanguageEn()(implicit webDriver: WebDriver) = {
    val key = Play.langCookieName
    val value = EnId
    addCookie(key, value)
    this
  }

  def setupTradeDetails(traderPostcode: String = PostcodeValid)(implicit webDriver: WebDriver) = {
    val key = SetupTradeDetailsCacheKey
    val value = SetupTradeDetailsModel(traderBusinessName = TraderBusinessNameValid,
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
    val value = EnterAddressManuallyModel(addressAndPostcodeModel = AddressAndPostcodeModel(
      addressLinesModel = AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
      line2 = Some(Line2Valid),
      line3 = Some(Line3Valid),
      postTown = PostTownValid)))
    addCookie(key, value)
    this
  }

  def dealerDetails(address: AddressViewModel = addressWithoutUprn)(implicit webDriver: WebDriver) = {
    val key = TraderDetailsCacheKey
    val value = TraderDetailsModel(traderName = TraderBusinessNameValid, traderAddress = address)
    addCookie(key, value)
    this
  }

  def bruteForcePreventionViewModel(permitted: Boolean = true,
                                    attempts: Int = 0,
                                    maxAttempts: Int = MaxAttempts,
                                    dateTimeISOChronology: String = org.joda.time.DateTime.now().toString)(implicit webDriver: WebDriver) = {
    val key = BruteForcePreventionViewModelCacheKey
    val value = BruteForcePreventionViewModel(
      permitted,
      attempts,
      maxAttempts,
      dateTimeISOChronology
    )
    addCookie(key, value)
    this
  }

  def vehicleLookupFormModel(referenceNumber: String = ReferenceNumberValid,
                                        registrationNumber: String = RegistrationNumberValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupFormModelCacheKey
    val value = VehicleLookupFormModel(referenceNumber = referenceNumber,
      registrationNumber = registrationNumber)
    addCookie(key, value)
    this
  }

  def vehicleDetailsModel(registrationNumber: String = RegistrationNumberValid,
                                     vehicleMake: String = FakeVehicleLookupWebService.VehicleMakeValid,
                                     vehicleModel: String = VehicleModelValid,
                                     keeperName: String = KeeperNameValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupDetailsCacheKey
    val value = VehicleDetailsModel(registrationNumber = registrationNumber,
      vehicleMake = vehicleMake,
      vehicleModel = vehicleModel)
    addCookie(key, value)
    this
  }

  def vehicleLookupResponseCode(responseCode: String = "disposal_vehiclelookupfailure")(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupResponseCodeCacheKey
    val value = responseCode
    addCookie(key, value)
    this
  }

  def disposeFormModel()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
    val value = DisposeFormModel(mileage = None,
      dateOfDisposal = DayMonthYear.today,
      consent = FakeDisposeWebServiceImpl.ConsentValid,
      lossOfRegistrationConsent = FakeDisposeWebServiceImpl.ConsentValid)
    addCookie(key, value)
    this
  }

  def disposeModel(referenceNumber: String = ReferenceNumberValid,
                              registrationNumber: String = RegistrationNumberValid,
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

  def disposeTransactionId(transactionId: String = TransactionIdValid)(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormTransactionIdCacheKey
    val value = transactionId
    addCookie(key, value)
    this
  }

  def vehicleRegistrationNumber()(implicit webDriver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormRegistrationNumberCacheKey
    val value = RegistrationNumberValid
    addCookie(key, value)
    this
  }

  def preventGoingToDisposePage(url: String)(implicit webDriver: WebDriver) = {
    val key = mappings.common.PreventGoingToDisposePage.PreventGoingToDisposePageCacheKey
    val value = url
    addCookie(key, value)
    this
  }

  def disposeOccurred(implicit webDriver: WebDriver) = {
    val key = mappings.common.PreventGoingToDisposePage.DisposeOccurredCacheKey
    addCookie(key, "")
    this
  }
}