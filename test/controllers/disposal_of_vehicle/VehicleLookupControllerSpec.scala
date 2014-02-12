package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage}

class VehicleLookupControllerSpec extends WordSpec with Matchers {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
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
      val v5cPostcodeValid = "SA99 1BD" //TODO make use for helper values

      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cReferenceNumberValid, v5cRegistrationNumberId -> v5cRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = disposal_of_vehicle.VehicleLookup.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/disposal-of-vehicle/dispose"))
     }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookup.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/disposal-of-vehicle/setup-trade-details"))
    }
  }
}