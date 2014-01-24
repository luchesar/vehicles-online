package controllers.change_of_address

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address


class V5cSearchControllerSpec extends Specification with Tags {

  "V5cSearch - Controller" should {
    val V5cReferenceNumberNID = "V5cReferenceNumber"
    val V5cReferenceNumberValid = "12345678910"
    val vehicleVRNID = "vehicleVRN"
    val vehicleVRNValid = "a1"

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.V5cSearch.present(request)

      // Assert
      status(result) mustEqual OK
    }


    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(V5cReferenceNumberNID -> V5cReferenceNumberValid,vehicleVRNID-> vehicleVRNValid)

      // Act
      val result = change_of_address.V5cSearch.submit(request)

      // Assert
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual (Some("/confirm-vehicle-details")) //TODO update with next page url
    }

  }

}