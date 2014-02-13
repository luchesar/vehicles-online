package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage, DisposePopulate}
import helpers.disposal_of_vehicle.Helper._

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
     SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = disposal_of_vehicle.VehicleLookup.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(DisposePopulate.url))
     }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookup.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}