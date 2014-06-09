package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.SoapEndpointErrorPage
import common.CookieHelper._
import scala.Some
import play.api.Play

final class SoapEndpointErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val result = soapEndpointError.present(FakeRequest())
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  "withLanguageCy" should {
    "redirect back to the same page" in new WithApplication {
      val result = soapEndpointError.withLanguageCy(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'cy'" in new WithApplication {
      val result = soapEndpointError.withLanguageCy(FakeRequest())
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == Play.langCookieName) match {
            case Some(cookie) => cookie.value should equal("cy")
            case None => fail("langCookieName not found")
          }
      }
    }
  }

  "withLanguageEn" should {
    "redirect back to the same page" in new WithApplication {
      val result = soapEndpointError.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val result = soapEndpointError.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == Play.langCookieName) match {
            case Some(cookie) => cookie.value should equal("en")
            case None => fail("langCookieName not found")
          }
      }
    }
  }

  private val soapEndpointError = new controllers.disposal_of_vehicle.SoapEndpointError()
}