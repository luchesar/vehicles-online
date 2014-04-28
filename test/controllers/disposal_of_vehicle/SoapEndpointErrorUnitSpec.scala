package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.UnitSpec

class SoapEndpointErrorUnitSpec extends UnitSpec {
  "Soap endpoint error - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = controllers.disposal_of_vehicle.SoapEndpointError.present(request)

      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

  }
}
