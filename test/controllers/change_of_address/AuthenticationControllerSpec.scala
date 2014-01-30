package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{Mappings, change_of_address}
import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress._
import models.domain.change_of_address.{LoginConfirmationModel, Address}
import org.specs2.mock.Mockito

class AuthenticationControllerSpec extends WordSpec with Matchers with Mockito{

  "Authentication - Controller" should {

    "present" in new WithApplication {
      // Arrange
      //TODO:add this to the helper as reusable code!
      val address = mock[Address]
      address.line1 returns "mock line1"
      address.postCode returns "mock postcode"
      val loginConfirmationModel = mock[LoginConfirmationModel]
      loginConfirmationModel.firstName returns "mock firstName"
      loginConfirmationModel.surname returns "mock surname"
      loginConfirmationModel.address returns address
      val key = Mappings.LoginConfirmationModel.key
      play.api.cache.Cache.set(key, loginConfirmationModel)

      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.Authentication.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "present login page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.Authentication.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/are-you-registered"))
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(pinFormID -> "123456")

      // Act
      val result = change_of_address.Authentication.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/v5c-search"))
    }
  }
}
