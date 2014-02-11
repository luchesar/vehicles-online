package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.ValidValues._

object V5cSearchPagePopulate extends WordSpec with Matchers with Mockito {
  def v5cSearchPagePopulate(browser: TestBrowser, v5cDocumentReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cPostcode: String = v5cPostcodeValid) = {

    browser.goTo("/v5c-search")
    browser.fill(s"#${app.ChangeOfAddress.v5cReferenceNumberID}") `with` v5cDocumentReferenceNumber
    browser.fill(s"#${app.ChangeOfAddress.v5cRegistrationNumberID}") `with` v5cVehicleRegistrationNumber
    browser.fill(s"#${app.ChangeOfAddress.v5cPostcodeID}") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }
}