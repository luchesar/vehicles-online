package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{Mappings, change_of_address}
import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress._
import models.domain.change_of_address.{LoginConfirmationModel, Address}
import org.specs2.mock.Mockito
import controllers.Formulate.PopulateLoginCache

class V5cSearchControllerSpec extends WordSpec with Matchers with Mockito{

  "V5cSearch - Controller" should {

    "present when user has logged in" in new WithApplication {
      // Arrange
      PopulateLoginCache()

      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.V5cSearch.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "present login page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.V5cSearch.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/are-you-registered"))
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val v5cReferenceNumberValid = "12345678910"
      val v5cRegistrationNumberValid = "a1"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberID -> v5cReferenceNumberValid,v5cRegistrationNumberID-> v5cRegistrationNumberValid)

      // Act
      val result = change_of_address.V5cSearch.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/confirm-vehicle-details"))
    }

  }
}