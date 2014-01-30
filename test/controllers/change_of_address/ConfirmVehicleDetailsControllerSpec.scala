package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{Mappings, change_of_address}
import org.scalatest.{Matchers, WordSpec}
import models.domain.change_of_address.{LoginConfirmationModel, Address, V5cSearchConfirmationModel}
import org.specs2.mock.Mockito

class ConfirmVehicleDetailsControllerSpec extends WordSpec with Matchers with Mockito {
  "ConfirmVehicleDetails - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val v5cReferenceNumberValid = "12345678910"
      val vehicleVRNValid = "a1"
      val request = FakeRequest().withSession()
      val address = mock[Address]

      //TODO: refactor and add to helper
      address.line1 returns "mock line1"
      address.postCode returns "mock postcode"
      val loginConfirmationModel = mock[LoginConfirmationModel]
      loginConfirmationModel.firstName returns "mock firstName"
      loginConfirmationModel.surname returns "mock surname"
      loginConfirmationModel.address returns address
      val key = Mappings.LoginConfirmationModel.key
      play.api.cache.Cache.set(key, loginConfirmationModel)

      play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cReferenceNumberValid)
      play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, vehicleVRNValid)
      val v5ckey = v5cReferenceNumberValid + "." + vehicleVRNValid

      play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to v5c search page when user is logged in but not entered vehicle details" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
      val address = mock[Address]

      //TODO: refactor and add to helper
      address.line1 returns "mock line1"
      address.postCode returns "mock postcode"
      val loginConfirmationModel = mock[LoginConfirmationModel]
      loginConfirmationModel.firstName returns "mock firstName"
      loginConfirmationModel.surname returns "mock surname"
      loginConfirmationModel.address returns address
      val key = Mappings.LoginConfirmationModel.key
      play.api.cache.Cache.set(key, loginConfirmationModel)

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      redirectLocation(result) should equal(Some("/v5c-search"))
    }

  "redirect to start page if the details are not in the cache" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
    redirectLocation(result) should equal(Some("/are-you-registered"))

  }

    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.ConfirmVehicleDetails.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some("/confirm-vehicle-details"))
    }
  }
}