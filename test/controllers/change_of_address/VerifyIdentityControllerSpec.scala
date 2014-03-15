package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import helpers.change_of_address.AreYouRegisteredPage

class VerifyIdentityControllerSpec extends WordSpec with Matchers {

  "VerifyIdentity - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.VerifyIdentity.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.VerifyIdentity.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(AreYouRegisteredPage.url))
    }
  }
}