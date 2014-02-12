package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import app.DisposalOfVehicle.DisposeConfirmation._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.SetUpTradeDetailsPopulate

class DisposeConfirmationControllerSpec extends WordSpec with Matchers with Mockito {

  "Disposal confirmation controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPopulate.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeConfirmation.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      SetUpTradeDetailsPopulate.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          emailAddressId -> ""
        )

      // Act
      val result = disposal_of_vehicle.DisposeConfirmation.submit(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeConfirmation.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/disposal-of-vehicle/setup-trade-details"))
    }

  }
}