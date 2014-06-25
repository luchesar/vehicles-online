package controllers.disposal_of_vehicle

import helpers.{UnitSpec, WithApplication}
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val newFakeRequest = FakeRequest()
      val result = duplicateDisposalError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  private val duplicateDisposalError = injector.getInstance(classOf[DuplicateDisposalError])
}
