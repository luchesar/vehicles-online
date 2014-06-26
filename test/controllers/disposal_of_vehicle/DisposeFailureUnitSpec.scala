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
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
      val result = disposeFailure.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  private val disposeFailure = injector.getInstance(classOf[DisposeFailure])
}

