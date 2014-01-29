package controllers.change_of_address

import app.ChangeOfAddress._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}


class LoginConfirmationControllerSpec extends WordSpec with Matchers {

  "LoginConfirmation - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val usernameValid = "Roger"
      val passwordValid = "examplepassword"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(usernameId -> usernameValid, passwordId -> passwordValid)

      change_of_address.LoginPage.submit(request)
      val request2 = FakeRequest().withSession()

      // Act
      val result = change_of_address.LoginConfirmation.present(request2)

      // Assert
      status(result) should equal(OK)
    }
  }

    "redirect to next page after the agree button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.LoginConfirmation.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/authentication"))
    }
}