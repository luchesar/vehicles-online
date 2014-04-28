package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.UnitSpec

class MicroserviceErrorUnitSpec extends UnitSpec {
  "Micro-service error - Controller" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = new MicroServiceError().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}
