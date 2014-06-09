package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.{DuplicateDisposalErrorPage, BeforeYouStartPage}
import common.CookieHelper._
import scala.Some
import play.api.Play

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val newFakeRequest = FakeRequest()
      val result = duplicateDisposalError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  "withLanguageCy" should {
    "redirect back to the same page" in new WithApplication {
      val result = duplicateDisposalError.withLanguageCy(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(DuplicateDisposalErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'cy'" in new WithApplication {
      val result = duplicateDisposalError.withLanguageCy(FakeRequest())
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
      val result = duplicateDisposalError.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(DuplicateDisposalErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val result = duplicateDisposalError .withLanguageEn(FakeRequest())
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

  val duplicateDisposalError = new DuplicateDisposalError()
}
