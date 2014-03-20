package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import helpers.change_of_address.LoginCachePopulate._
import helpers.change_of_address.{AreYouRegisteredPage, AuthenticationPopulate}
import helpers.UnitSpec

class LoginConfirmationUnitSpec extends UnitSpec {

  "LoginConfirmation - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      setupCache

      val result = change_of_address.LoginConfirmation.present(request)

      status(result) should equal(OK)
    }

    "redirect to login page when user is not logged in" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.LoginConfirmation.present(request)

      redirectLocation(result) should equal(Some(AreYouRegisteredPage.url))
    }

    "redirect to next page after the agree button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = change_of_address.LoginConfirmation.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(AuthenticationPopulate.url))
    }
  }
}