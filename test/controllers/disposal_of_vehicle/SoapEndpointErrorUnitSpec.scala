package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.common.CookieHelper
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.SoapEndpointErrorPage
import CookieHelper._
import scala.Some
import play.api.Play

final class SoapEndpointErrorUnitSpec extends UnitSpec {
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

  private val soapEndpointError = injector.getInstance(classOf[controllers.disposal_of_vehicle.SoapEndpointError])
  private lazy val present = soapEndpointError.present(FakeRequest())
}