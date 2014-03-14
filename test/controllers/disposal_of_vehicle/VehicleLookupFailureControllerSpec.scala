package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._
import scala.Some
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup

class VehicleLookupFailureControllerSpec extends WordSpec with Matchers {

  "VehicleLookupFailurePage - Controller" should {

    "present" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to vehiclelookup on submit" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(VehicleLookupPage.address))
    }

    "redirect to setuptraderdetails when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on if only BusinessChooseYourAddress cache is populated" in new WithApplication {
      //Arrange
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }

    "redirect to setuptraderdetails on if only VehicleLookupFormModelCache is populated" in new WithApplication {
      //Arrange
      CacheSetup.vehicleLookupFormModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.VehicleLookupFailure.present(request)

      // Assert
      redirectLocation(result) should equal (Some(SetupTradeDetailsPage.address))
    }
  }
}
