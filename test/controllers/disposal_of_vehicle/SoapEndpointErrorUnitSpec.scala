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
      val result = soapEndpointError.present(FakeRequest())
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  private val soapEndpointError = new controllers.disposal_of_vehicle.SoapEndpointError()
}