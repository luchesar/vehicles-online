package controllers.disposal_of_vehicle

import helpers.{UnitSpec, WithApplication}
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      status(present) should equal(OK)
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }
  }

  private lazy val present = {
    val duplicateDisposalError = injector.getInstance(classOf[DuplicateDisposalError])
    val newFakeRequest = FakeRequest()
    duplicateDisposalError.present(newFakeRequest)
  }
}
