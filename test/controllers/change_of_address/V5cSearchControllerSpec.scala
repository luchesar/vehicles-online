package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress._

class V5cSearchControllerSpec extends WordSpec with Matchers{

  "V5cSearch - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.V5cSearch.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val V5cReferenceNumberValid = "12345678910"
      val vehicleVRNValid = "a1"
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(V5cReferenceNumberNID -> V5cReferenceNumberValid,vehicleVRNID-> vehicleVRNValid)

      // Act
      val result = change_of_address.V5cSearch.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/confirm-vehicle-details"))
    }

  }
}