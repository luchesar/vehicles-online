package controllers.disposal_of_vehicle

import composition.TestComposition.{testInjector => injector}
import helpers.UnitSpec
import helpers.WithApplication
import pages.disposal_of_vehicle._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import common.CookieHelper._
import scala.Some
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import scala.Some
import mappings.disposal_of_vehicle.TraderDetails._
import scala.Some
import play.api.Play

final class BeforeYouStartUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val result = beforeYouStart.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  "submit" should {
    "redirect to next page after the button is clicked" in new WithApplication {
      val result = beforeYouStart.submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "withLanguageEn" should {
    "redirect back to the same page" in new WithApplication {
      val result = beforeYouStart.withLanguageEn(newFakeRequest)
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie" in new WithApplication {
      val result = beforeYouStart.withLanguageEn(newFakeRequest)
      status(result) should equal(SEE_OTHER)
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

  private def newFakeRequest = {
    FakeCSRFRequest().withSession()
  }

  private val beforeYouStart = injector.getInstance(classOf[BeforeYouStart])
}
