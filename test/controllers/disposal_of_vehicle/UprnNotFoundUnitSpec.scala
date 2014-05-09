package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class UprnNotFoundUnitSpec extends UnitSpec {

  "UprnNotFound - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = new UprnNotFound().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}
