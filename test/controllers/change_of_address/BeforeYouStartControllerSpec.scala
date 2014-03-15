package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import helpers.change_of_address.KeeperStatusPage

class BeforeYouStartControllerSpec extends WordSpec with Matchers {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.BeforeYouStart.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.BeforeYouStart.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(KeeperStatusPage.url))
    }
  }
}