package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import models.domain.common.Address

object VehicleLookupHelper extends WordSpec with Matchers with Mockito {
  val v5cDocumentReferenceNumberValid = "12345678910"
  val v5cVehicleRegistrationNumberValid = "AB12AWR"
  val v5cKeeperNameValid = "John Smith"
  val v5cPostcodeValid = "Sa991DD"

  def vehicleLookupIntegrationHelper(browser: TestBrowser, v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill("#v5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#v5cRegistrationNumber") `with` v5cVehicleRegistrationNumber
    browser.fill("#v5cKeeperName") `with` v5cKeeperName
    browser.fill("#v5cPostcode") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }
}
