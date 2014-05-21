package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}

final class ErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val noCookieEncryption = new NoEncryption with CookieEncryption
      val noCookieNameHashing = new NoHash with CookieNameHashing
      val result = new Error().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}
