package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import helpers.disposal_of_vehicle.{DisposeSuccessPage, VehicleLookupPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._

class VehicleLookupFailureControllerSpec extends WordSpec with Matchers {
  "VehicleLookupFailure - Controller" should {
    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to vehiclelookup on submit" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(VehicleLookupPage.url))
    }

    "redirect to setuptraderdetails when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setuptraderdetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(SetUpTradeDetailsPage.url))
    }
  }
}
