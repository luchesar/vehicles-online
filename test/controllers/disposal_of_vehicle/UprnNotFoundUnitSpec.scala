package controllers.disposal_of_vehicle

import helpers.UnitSpec
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import pages.disposal_of_vehicle.UprnNotFoundPage
import common.CookieHelper._
import scala.Some
import play.api.Play

final class UprnNotFoundUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest()
      val result = uprnNotFound.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  val uprnNotFound = new UprnNotFound()
}
