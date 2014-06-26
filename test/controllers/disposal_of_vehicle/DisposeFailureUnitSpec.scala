package controllers.disposal_of_vehicle

import helpers.common.CookieHelper
import play.api.test.FakeRequest
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import helpers.WithApplication
import pages.disposal_of_vehicle.DisposeFailurePage
import CookieHelper._
import scala.Some
import play.api.Play

final class DisposeFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) {
        r => r.header.status should equal(OK)
      }
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }
  }

  private lazy val present = {
    val disposeFailure = injector.getInstance(classOf[DisposeFailure])
    val request = FakeRequest().
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
      withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
    disposeFailure.present(request)
  }
}

