package controllers.change_of_address

import app.ChangeOfAddress._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import scala.Some

class LoginPageControllerSpec extends WordSpec with Matchers {

  "LoginPage - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.LoginPage.present(request)

      // Assert
      status(result)should equal(OK)
    }
  }

    "redirect to next page after the next button is clicked" in new WithApplication {
      // Arrange
      val usernameValid = "Roger"
      val passwordValid = "examplepassword"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(usernameId -> usernameValid, passwordId -> passwordValid)

      // Act
      val result = change_of_address.LoginPage.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/login-confirmation"))
    }
}