package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val duplicateDisposalError = new DuplicateDisposalError()
      val newFakeRequest = FakeCSRFRequest()
      val result = duplicateDisposalError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }
}
