package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.change_of_address.LoginCachePopulate
import LoginCachePopulate._

class LoginConfirmationControllerSpec extends WordSpec with Matchers with Mockito {

  "LoginConfirmation - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
      loginCachePopulate

      // Act
      val result = change_of_address.LoginConfirmation.present(request)

      // Assert
      status(result) should equal(OK)
    }
  }

  "present login page when user is not logged in" in new WithApplication {
    // Arrange
    val request = FakeRequest().withSession()

    // Act
    val result = change_of_address.LoginConfirmation.present(request)

    // Assert
    redirectLocation(result) should equal(Some("/are-you-registered"))
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