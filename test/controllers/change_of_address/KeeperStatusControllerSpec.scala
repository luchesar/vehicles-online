package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import helpers.change_of_address.VerifyIdentityPage
import helpers.UnitSpec

class KeeperStatusUnitSpec extends UnitSpec {

  "KeeperStatus - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.KeeperStatus.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the i'm a private individual button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.KeeperStatus.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(VerifyIdentityPage.url))
    }
  }
}