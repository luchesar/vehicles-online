package controllers.disposal_of_vehicle

import helpers.{WithApplication, UnitSpec}
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import play.api.test.Helpers._
import composition.TestComposition.{testInjector => injector}
import play.api.test.FakeRequest
import pages.disposal_of_vehicle.ErrorPage
import common.CookieHelper._
import scala.Some
import play.api.Play

final class ErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = errorController.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  // TODO please add test for 'submit'.

  "withLanguageCy" should {
    "redirect back to the same page" in new WithApplication {
      val result = errorController.withLanguageCy(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(ErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'cy'" in new WithApplication {
      val result = errorController.withLanguageCy(FakeRequest())
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
      val result = errorController.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(ErrorPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val result = errorController.withLanguageEn(FakeRequest())
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

  private val errorController = injector.getInstance(classOf[Error])
}
