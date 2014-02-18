package helpers.change_of_address

import play.api.test.TestBrowser
import mappings.change_of_address.V5cSearch
import V5cSearch._
import helpers.change_of_address.Helper._

object V5cSearchPagePopulate {
  val url = "/v5c-search"


  def happyPath(browser: TestBrowser, v5cDocumentReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/v5c-search")
    browser.fill(s"#${v5cReferenceNumberId}") `with` v5cDocumentReferenceNumber
    browser.fill(s"#${v5cRegistrationNumberId}") `with` v5cVehicleRegistrationNumber
    browser.fill(s"#${v5cPostcodeId}") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }
}