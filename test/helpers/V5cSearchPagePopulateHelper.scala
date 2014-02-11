package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import models.domain.common.Address
import controllers.Mappings

object V5cSearchPagePopulateHelper extends WordSpec with Matchers with Mockito {

  def v5cSearchPagePopulate(browser: TestBrowser, v5cReferenceNumber: String = "12345678910") = {
    browser.goTo("/v5c-search")
    browser.fill(s"#${app.ChangeOfAddress.v5cReferenceNumberID}") `with` v5cReferenceNumber
    browser.fill(s"#${app.ChangeOfAddress.v5cRegistrationNumberID}") `with` "A2"
    browser.fill(s"#${app.ChangeOfAddress.v5cPostcodeID}") `with` "SA991BD"
    browser.submit("button[type='submit']")
  }
}