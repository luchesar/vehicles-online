package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

final class MicroserviceErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val microserviceError = new MicroServiceError()
      val newFakeRequest = FakeRequest().withSession()
      val result = microserviceError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }
}
