package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.{AddressViewModel, VehicleLookupFormModel}
import services.fakes.FakeVehicleLookupService
import scala.Some

class VehicleLookupControllerSpec extends WordSpec with Matchers with MockitoSugar {

  "VehicleLookup - Controller" should {
    val mockVehicleLookupFormModelSuccess = mock[VehicleLookupFormModel]
    val mockWebServiceSuccess = mock[services.VehicleLookupService]
    when(mockWebServiceSuccess.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelSuccess))
    val vehicleLookupSuccess = new disposal_of_vehicle.VehicleLookup(mockWebServiceSuccess)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      // Act
      val result = vehicleLookupSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      // Arrange
     SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache()
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
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid, v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid, v5cKeeperNameId -> v5cKeeperNameValid, v5cPostcodeId -> v5cPostcodeValid)

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
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = vehicleLookupSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "redirect to manual address screen when back button is pressed and there is no uprn" in new WithApplication {
      //Arrange
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      //Act
      val result = vehicleLookup.back(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(EnterAddressManuallyPage.url))
    }

    "redirect to select address screen when back button is pressed and there is a uprn" in new WithApplication {
      //Arrange
      val addressWithUprn = AddressViewModel(uprn=Some(12345L),address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
      BusinessChooseYourAddressPage.setupCache(addressWithUprn)
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      //Act
      val result = vehicleLookup.back(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.url))
    }
  }
}