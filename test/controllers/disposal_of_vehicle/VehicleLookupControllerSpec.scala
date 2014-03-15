package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage, DisposePage, VehicleLookupFailurePage, EnterAddressManuallyPage}
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import services.fakes.{FakeDisposeService, FakeVehicleLookupService}
import helpers.UnitSpec

class VehicleLookupUnitSpec extends UnitSpec {

  "VehicleLookup - Controller" should {
    val mockVehicleLookupFormModelSuccess = mock[VehicleLookupFormModel]
    val mockWebServiceSuccess = mock[services.VehicleLookupService]
    when(mockWebServiceSuccess.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelSuccess))
    val vehicleLookupSuccess = new disposal_of_vehicle.VehicleLookup(mockWebServiceSuccess)

    "present" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      val result = vehicleLookupSuccess.present(request)

      status(result) should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> referenceNumberValid, registrationNumberId -> registrationNumberValid, consentId -> consentValid)

      val result = vehicleLookupSuccess.submit(request)

      redirectLocation(result) should equal (Some(DisposePage.url))
     }

    "redirect to VehicleLookupFailure after a submit and false message returned from the fake microservice" in new WithApplication {
      val mockVehicleLookupFormModelFailure = mock[VehicleLookupFormModel]
      when (mockVehicleLookupFormModelFailure.referenceNumber).thenReturn(FakeDisposeService.failureReferenceNumber)
      val mockWebServiceFailure = mock[services.VehicleLookupService]
      when(mockWebServiceFailure.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModelFailure))
      val vehicleLookupFailure = new disposal_of_vehicle.VehicleLookup(mockWebServiceFailure)

      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> referenceNumberValid, registrationNumberId -> registrationNumberValid, consentId -> consentValid)

      val result = vehicleLookupFailure.submit(request)

      redirectLocation(result) should equal (Some(VehicleLookupFailurePage.url))
    }

    "redirect to setupTradeDetails page when user has not set up a trader for disposal" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = vehicleLookupSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request if no details are entered" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if empty strings are entered" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> "", registrationNumberId -> "")

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber is entered" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> referenceNumberValid)

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only RegistrationNumber is entered" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(registrationNumberId -> registrationNumberValid)

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to EnterAddressManually when back button is pressed and there is no uprn" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(EnterAddressManuallyPage.url))
    }

    "redirect to BusinessChooseYourAddress when back button is pressed and there is a uprn" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache(addressWithUprn)
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.url))
    }

    "redirect to SetUpTradeDetails when back button and the user has completed the vehicle lookup form " in new WithApplication {
      BusinessChooseYourAddressPage.setupCache(addressWithUprn)
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(referenceNumberId -> referenceNumberValid, registrationNumberId -> registrationNumberValid, consentId -> consentValid)

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.url))
    }
  }
}