package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.DuplicateDisposalErrorPage
import common.CookieHelper._
import scala.Some
import play.api.Play

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val newFakeRequest = FakeRequest()
      val result = duplicateDisposalError.present(newFakeRequest)
      status(result) should equal(OK)
    }
  }

  private val duplicateDisposalError = new DuplicateDisposalError()
}
