package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

final class SoapEndpointErrorUnitSpec extends UnitSpec {
  "Soap endpoint error - Controller" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = new controllers.disposal_of_vehicle.SoapEndpointError().present(request)

      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}