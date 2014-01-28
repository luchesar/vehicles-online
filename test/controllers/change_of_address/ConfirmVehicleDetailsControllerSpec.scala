package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}

class ConfirmVehicleDetailsControllerSpec extends WordSpec with Matchers {

  "ConfirmVehicleDetails - Controller" should {
/*

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      status(result) should equal(OK)
    }
*/

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/confirm-vehicle-details"))
    }
  }
}