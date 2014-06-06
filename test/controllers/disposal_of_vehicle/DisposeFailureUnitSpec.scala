package controllers.disposal_of_vehicle

import play.api.test.FakeRequest
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import composition.TestComposition.{testInjector => injector}
import helpers.WithApplication

final class DisposeFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
      val result = injector.getInstance(classOf[DisposeFailure]).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}

