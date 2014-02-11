package helpers

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import app.ChangeOfAddress.V5cSearch._

object V5cSearchPagePopulateHelper extends WordSpec with Matchers with Mockito {

  def v5cSearchPagePopulate(browser: TestBrowser, v5cReferenceNumber: String = "12345678910") = {
    browser.goTo("/v5c-search")
    browser.fill(s"#${v5cReferenceNumberID}") `with` v5cReferenceNumber
    browser.fill(s"#${v5cRegistrationNumberID}") `with` "A2"
    browser.fill(s"#${v5cPostcodeID}") `with` "SA991BD"
    browser.submit("button[type='submit']")
  }
}