package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication

final class UprnNotFoundUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = new UprnNotFound().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}
