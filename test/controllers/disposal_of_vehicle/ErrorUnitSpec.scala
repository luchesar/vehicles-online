package controllers.disposal_of_vehicle

import helpers.common.CookieHelper
import CookieHelper._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.{UnitSpec, WithApplication}
import pages.disposal_of_vehicle.ErrorPage
import play.api.Play
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class ErrorUnitSpec extends UnitSpec {

  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) {
        r => r.header.status should equal(OK)
      }
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }
  }

  // TODO please add test for 'submit'.


  private val errorController = injector.getInstance(classOf[Error])

  private lazy val present = {
    val request = FakeRequest().
      withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
    errorController.present(ErrorPage.exceptionDigest)(request)
  }
}
