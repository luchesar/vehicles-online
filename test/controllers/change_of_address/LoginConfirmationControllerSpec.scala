package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{Mappings, change_of_address}
import org.scalatest.{Matchers, WordSpec}
import play.api.cache.Cache
import models.domain.change_of_address.{Address, LoginConfirmationModel}
import org.specs2.mock.Mockito


class LoginConfirmationControllerSpec extends WordSpec with Matchers with Mockito {

  "LoginConfirmation - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      //TODO: refactor so we can re use this block of code from the helper in other tests
      val address = mock[Address]
      address.line1 returns "mock line1"
      address.postCode returns "mock postcode"
      val loginConfirmationModel = mock[LoginConfirmationModel]
      loginConfirmationModel.firstName returns "mock firstName"
      loginConfirmationModel.surname returns "mock surname"
      loginConfirmationModel.address returns address
      val key = Mappings.LoginConfirmationModel.key
      play.api.cache.Cache.set(key, loginConfirmationModel)


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
    val result = change_of_address.AreYouRegistered.present(request)

    // Assert
    status(result) should equal(OK)
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