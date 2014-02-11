package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.DOVValidValues._

object VehicleLookup extends WordSpec with Matchers with Mockito {

  def vehicleLookupIntegrationHelper(browser: TestBrowser, v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")
    browser.fill("#v5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#v5cRegistrationNumber") `with` v5cVehicleRegistrationNumber
    browser.fill("#v5cKeeperName") `with` v5cKeeperName
    browser.fill("#v5cPostcode") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }
}
