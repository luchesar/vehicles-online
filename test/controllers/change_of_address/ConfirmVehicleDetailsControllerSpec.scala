package controllers.change_of_address

import app.ChangeOfAddress._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import scala.Some

class ConfirmVehicleDetailsControllerSpec extends WordSpec with Matchers {

  "ConfirmVehicleDetails - Controller" should {

   "present" in new WithApplication {
     /*
      // Arrange
      val V5cReferenceNumberValid = "12345678910"
      val vehicleVRNValid = "a1"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(V5cReferenceNumberNID -> V5cReferenceNumberValid,vehicleVRNID -> vehicleVRNValid)

      change_of_address.V5cSearch.submit(request)
      val request2 = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request2)

      // Assert
      status(result) should equal(OK)*/
     org.scalatest.Pending
    }


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