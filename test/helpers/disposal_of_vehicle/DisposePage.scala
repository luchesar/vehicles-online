package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.Dispose._
import models.DayMonthYear
import play.api.Play.current
import models.domain.disposal_of_vehicle.{DisposeModel, DisposeFormModel}
import helpers.disposal_of_vehicle.Helper._

object DisposePage {
  val url = "/disposal-of-vehicle/dispose"
  val title = "Dispose a vehicle into the motor trade: confirm"

  def happyPath(browser: TestBrowser, day: String = "1", month :String = "1", year: String = "2000") = {
    BusinessChooseYourAddressPage.setupCache()
    VehicleLookupPage.setupVehicleDetailsModelCache()
    VehicleLookupPage.setupVehicleLookupFormModelCache()
    browser.goTo(url)
    browser.click(s"#${dateOfDisposalId}_day option[value='$day']")
    browser.click(s"#${dateOfDisposalId}_month option[value='$month']")
    browser.fill(s"#${dateOfDisposalId}_year") `with` year
    browser.click(s"#${consentId}")
    browser.submit("button[type='submit']")
  }

  def setupDisposeFormModelCache() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormModelCacheKey
    val value = DisposeFormModel(consent = "true", dateOfDisposal = DayMonthYear.today, emailAddress = None)
    play.api.cache.Cache.set(key, value)
  }

  def setupDisposeModelCache(v5cReferenceNumber:String = v5cDocumentReferenceNumberValid, v5cRegistrationNumber:String = v5cVehicleRegistrationNumberValid, v5cKeeperName:String = "fail", v5cPostcode:String = v5cPostcodeValid) = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeModelCacheKey
    val value = DisposeModel(v5cReferenceNumber, v5cRegistrationNumber, v5cKeeperName, v5cPostcode)
    play.api.cache.Cache.set(key, value)
  }

  def setupDisposeTransactionIdCache() = {
    val key = mappings.disposal_of_vehicle.Dispose.disposeFormTransactionIdCacheKey
    val value = "1234"
    play.api.cache.Cache.set(key, value)
  }

}
