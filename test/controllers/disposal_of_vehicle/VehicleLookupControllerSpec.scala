package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage, DisposePage, VehicleLookupFailurePage}
import helpers.disposal_of_vehicle.Helper._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import services.fakes.{FakeDisposeService, FakeVehicleLookupService}

class VehicleLookupControllerSpec extends WordSpec with Matchers with MockitoSugar {

  "VehicleLookup - Controller" should {
    val mockVehicleLookupFormModelSuccess = mock[VehicleLookupFormModel]
    val mockWebServiceSuccess = mock[services.VehicleLookupService]
    when(mockWebServiceSuccess.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelSuccess))
    val vehicleLookupSuccess = new disposal_of_vehicle.VehicleLookup(mockWebServiceSuccess)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = vehicleLookupSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      // Arrange
     SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> documentReferenceNumberValid, registrationNumberId -> vehicleRegistrationNumberValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(DisposePage.url))
     }

    "redirect to VehicleLookupFailure after a submit and false message returned from the fake microservice" in new WithApplication {
      val mockVehicleLookupFormModelFailure = mock[VehicleLookupFormModel]
      when (mockVehicleLookupFormModelFailure.referenceNumber).thenReturn(FakeDisposeService.failureReferenceNumber)
      val mockWebServiceFailure = mock[services.VehicleLookupService]
      when(mockWebServiceFailure.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelFailure))
      val vehicleLookupFailure = new disposal_of_vehicle.VehicleLookup(mockWebServiceFailure)

      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> documentReferenceNumberValid, registrationNumberId -> vehicleRegistrationNumberValid)

      // Act
      val result = vehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(VehicleLookupFailurePage.url))
    }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = vehicleLookupSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request if no details are entered" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}