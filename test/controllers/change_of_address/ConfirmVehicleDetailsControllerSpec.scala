package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.change_of_address.{V5cCachePopulate, LoginCachePopulate, V5cSearchPagePopulate, AreYouRegisteredPage, ConfirmVehicleDetailsPage}
import LoginCachePopulate._
import V5cCachePopulate._

class ConfirmVehicleDetailsControllerSpec extends WordSpec with Matchers with Mockito {
  "ConfirmVehicleDetails - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
      setupCache
      happyPath()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to v5c search page when user is logged in but not entered vehicle details" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
      setupCache

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      redirectLocation(result) should equal(Some(V5cSearchPagePopulate.url))
    }

  "redirect to start page if the details are not in the cache" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      redirectLocation(result) should equal(Some(AreYouRegisteredPage.url))
  }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(ConfirmVehicleDetailsPage.url))
    }
  }
}