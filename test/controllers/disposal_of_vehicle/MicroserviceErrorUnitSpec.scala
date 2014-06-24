package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.common.CookieHelper
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.MicroServiceErrorPage
import CookieHelper._
import scala.Some
import play.api.Play

final class MicroserviceErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val result = microServiceError.present(FakeRequest())
      status(result) should equal(OK)
    }
  }

  private val microServiceError = new MicroServiceError()
}
