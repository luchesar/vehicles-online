package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import helpers.disposal_of_vehicle._
import controllers.disposal_of_vehicle
import play.api.test.Helpers._
import scala.Some

class VehicleLookupFailureControllerSpec extends WordSpec with Matchers {

  "VehicleLookupFailurePage - Controller" should {

    "present" in new WithApplication {
      // Arrange
      VehicleLookupFailurePage.cacheSetup()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to vehiclelookup on submit" in new WithApplication {
      // Arrange
      VehicleLookupFailurePage.cacheSetup()
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

    "redirect to setuptraderdetails on if only BusinessChooseYourAddress cache is populated" in new WithApplication {
      //Arrange
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setuptraderdetails on if only VehicleLookupFormModelCache is populated" in new WithApplication {
      //Arrange
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetUpTradeDetailsPage.url))
    }
  }
}
