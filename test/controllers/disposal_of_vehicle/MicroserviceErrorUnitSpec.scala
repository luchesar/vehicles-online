package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

final class MicroserviceErrorUnitSpec extends UnitSpec {

  "MicroserviceError controller" should {

    "present when microservice is unavailable" in new WithApplication {
      val result = microserviceError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }

  private def microserviceError = new MicroServiceError()
}
