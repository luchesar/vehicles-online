package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import pages.disposal_of_vehicle.{UprnNotFoundPage, VehicleLookupFailurePage}
import common.CookieHelper._
import scala.Some
import play.api.Play

final class UprnNotFoundUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest()
      val result = uprnNotFound.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  "withLanguageEn" should {
    "redirect back to the same page" in new WithApplication {
      val request = FakeRequest()
      val result = uprnNotFound.withLanguageEn(request)
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(UprnNotFoundPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val request = FakeRequest()
      val result = uprnNotFound.withLanguageEn(request)
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

  val uprnNotFound = new UprnNotFound()
}
