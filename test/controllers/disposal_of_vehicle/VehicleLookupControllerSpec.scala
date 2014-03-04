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
import services.fakes.FakeVehicleLookupService

class VehicleLookupControllerSpec extends WordSpec with Matchers with MockitoSugar {

  "VehicleLookup - Controller" should {
    val mockVehicleLookupFormModelSuccess = mock[VehicleLookupFormModel]
    val mockWebServiceSuccess = mock[services.VehicleLookupService]
    when(mockWebServiceSuccess.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelSuccess))
    val vehicleLookupSuccess = new disposal_of_vehicle.VehicleLookup(mockWebServiceSuccess)

    "present" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = vehicleLookupSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(DisposePage.url))
     }

    "redirect to VehicleLookupFailure after a valid submit and false message returned from the fake microservice" in new WithApplication {
      val mockVehicleLookupFormModelFailure = mock[VehicleLookupFormModel]
      when (mockVehicleLookupFormModelFailure.v5cKeeperName).thenReturn("fail")
      val mockWebServiceFailure = mock[services.VehicleLookupService]
      when(mockWebServiceFailure.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelFailure))
      val vehicleLookupFailure = new disposal_of_vehicle.VehicleLookup(mockWebServiceFailure)

      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupFailure.submit(request)

      // Assert
      redirectLocation(result) should equal (Some(VehicleLookupFailurePage.url))
    }

    "redirect to setupTradeDetails page when user has not set up a trader ofr disposal" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = vehicleLookupSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request if no details are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if empty strings are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> "", v5cRegistrationNumberId -> "", v5cKeeperNameId -> "", v5cPostcodeId -> "")


      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber is entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only RegistrationNumber is entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only KeeperName is entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cKeeperNameId -> v5cKeeperNameValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only Postcode is entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber and RegistrationNumber are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber and KeeperName are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cKeeperNameId -> v5cKeeperNameValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber and Postcode are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only RegistrationNumber and KeeperName are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only RegistrationNumber and Postcode are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only KeeperName and Postcode are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber, RegistrationNumber and Postcode are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cReferenceNumberId -> v5cVehicleRegistrationNumberValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber, RegistrationNumber and KeeperName are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cReferenceNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only PostCode, RegistrationNumber and Postcode are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cPostcodeId -> v5cPostcodeValid, v5cReferenceNumberId -> v5cVehicleRegistrationNumberValid, v5cPostcodeId -> v5cPostcodeValid)

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}