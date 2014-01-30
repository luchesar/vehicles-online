package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{Mappings, change_of_address}
import org.scalatest.{Matchers, WordSpec}
import models.domain.change_of_address.V5cSearchConfirmationModel

class ConfirmVehicleDetailsControllerSpec extends WordSpec with Matchers {
  "ConfirmVehicleDetails - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val v5cReferenceNumberValid = "12345678910"
      val vehicleVRNValid = "a1"
      val request = FakeRequest().withSession()

      play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cReferenceNumberValid)
      play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, vehicleVRNValid)
      val key = v5cReferenceNumberValid + "." + vehicleVRNValid

      play.api.cache.Cache.set(key, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))

      // Act
      val result = change_of_address.ConfirmVehicleDetails.present(request)

      // Assert
      status(result) should equal(OK)
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