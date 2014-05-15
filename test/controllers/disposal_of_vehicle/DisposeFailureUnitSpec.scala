package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}

class DisposeFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
      val noCookieEncryption = new NoEncryption with CookieEncryption
      val noCookieNameHashing = new NoHash with CookieNameHashing
      val result = new DisposeFailure()(noCookieEncryption, noCookieNameHashing).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}

