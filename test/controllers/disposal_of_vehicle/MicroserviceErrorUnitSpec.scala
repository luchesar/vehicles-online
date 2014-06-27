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
      status(present) should equal(OK)
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }
  }

  private val microServiceError = injector.getInstance(classOf[MicroServiceError])

  private lazy val present = {
    microServiceError.present(FakeRequest())
  }
}
