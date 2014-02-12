package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.BusinessAddressSelect._
import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.SetUpTradeDetailsPage
import mappings.disposal_of_vehicle.BusinessAddressSelect._

class BusinessChooseYourAddressControllerSpec extends WordSpec with Matchers {

  "BusinessChooseYourAddress - Controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after a valid submit" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache

      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          addressSelectId -> address1.toViewFormat())

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/disposal-of-vehicle/vehicle-lookup"))
    }

    "return a bad request after an invalid submission" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          addressSelectId -> "")

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.BusinessChooseYourAddress.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/disposal-of-vehicle/setup-trade-details"))
    }
  }
}
