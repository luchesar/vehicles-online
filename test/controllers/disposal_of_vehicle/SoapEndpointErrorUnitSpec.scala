package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication

final class SoapEndpointErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = new controllers.disposal_of_vehicle.SoapEndpointError().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}