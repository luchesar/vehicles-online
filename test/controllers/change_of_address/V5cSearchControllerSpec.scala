package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress.V5cSearch._
import org.specs2.mock.Mockito
import helpers.change_of_address.LoginCachePopulate
import LoginCachePopulate._

class V5cSearchControllerSpec extends WordSpec with Matchers with Mockito{

  "V5cSearch - Controller" should {

    "present when user has logged in" in new WithApplication {
      // Arrange
      loginCachePopulate()

      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.VehicleSearch.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "present login page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.VehicleSearch.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/are-you-registered"))
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val v5cReferenceNumberValid = "12345678910"
      val v5cRegistrationNumberValid = "a1"
      val v5cPostcodeValid = "sa44dw"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberID -> v5cReferenceNumberValid,v5cRegistrationNumberID-> v5cRegistrationNumberValid, v5cPostcodeID -> v5cPostcodeValid)

      // Act
      val result = change_of_address.VehicleSearch.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/confirm-vehicle-details"))
    }

  }
}