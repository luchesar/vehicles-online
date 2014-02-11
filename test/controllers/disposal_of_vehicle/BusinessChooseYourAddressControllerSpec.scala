package controllers.disposal_of_vehicle

import app.DisposalOfVehicle.BusinessAddressSelect._
import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._


class BusinessChooseYourAddressControllerSpec extends WordSpec with Matchers {

  "BusinessChooseYourAddress - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after a valid submit" in new WithApplication {
      // Arrange
      val businessNameValid = "DVLA"
      val addressSelectValid = "1"

      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(businessNameId -> businessNameValid,
          addressSelectId -> addressSelectValid)

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/disposal-of-vehicle/vehicle-lookup")) //TODO: This needs to look at the correct next page
    }

    "return a bad request after an invalid submission" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(businessNameId -> "",
          addressSelectId -> "")

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}