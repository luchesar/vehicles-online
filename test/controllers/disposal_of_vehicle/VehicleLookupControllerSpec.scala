package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._

class VehicleLookupControllerSpec extends WordSpec with Matchers {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookup.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after a valid submit" in new WithApplication {
      // Arrange
      val v5cReferenceNumberValid = "12345678910"
      val v5cRegistrationNumberValid = "ABC123"
      val v5cKeeperNameValid = "John"
      val v5cPostcodeValid = "SA99 1BD"

      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberID -> v5cReferenceNumberValid, v5cRegistrationNumberID -> v5cRegistrationNumberValid, v5cKeeperNameID -> v5cKeeperNameValid, v5cPostcodeID -> v5cPostcodeValid)

      // Act
      val result = disposal_of_vehicle.VehicleLookup.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/disposal-of-vehicle/dispose")) //TODO: This needs to not look at itself but at the next page
     }
  }
}