package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.{AddressDto, VehicleDetailsDto, VehicleDetailsResponse, VehicleDetailsRequest}
import services.fakes.FakeResponse
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupWebService}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global

class VehicleLookupUnitSpec extends UnitSpec {

  "VehicleLookup - Controller" should {
    val vehicleLookupSuccess = {
      val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
      when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
        val vehicleDetailsResponse =
          VehicleDetailsResponse(true,
            message = "Fake Web Lookup Service - Good response",
            vehicleDetailsDto = VehicleDetailsDto(registrationNumber = "PJ056YY",
              vehicleMake = "Alfa Romeo",
              vehicleModel = "Alfasud ti",
              keeperName = "Keeper Name",
              keeperAddress = AddressDto(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))))
        val responseAsJson = Json.toJson(vehicleDetailsResponse)

        new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
      })

      val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)

      new disposal_of_vehicle.VehicleLookup(vehicleLookupServiceImpl)
    }

    "present" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession()

      val result = vehicleLookupSuccess.present(request)

      status(result) should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> referenceNumberValid,
        registrationNumberId -> registrationNumberValid,
        consentId -> consentValid)

      val result = vehicleLookupSuccess.submit(request)

      redirectLocation(result) should equal (Some(DisposePage.address))
     }

    "submit removes spaces from registrationNumber" in new WithApplication { // DE7 Spaces should be stripped
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> referenceNumberValid,
        registrationNumberId -> "9999 AAA",
        consentId -> consentValid)

      val result = vehicleLookupSuccess.submit(request)

      whenReady(result) {
        r => controllers.disposal_of_vehicle.Helpers.fetchVehicleLookupDetailsFromCache match {
          case Some(f) => f.registrationNumber should equal("9999AAA")
          case _ => fail("Should have found model in the cache")
        }
      }
    }


    "redirect to VehicleLookupFailure after a submit and false message returned from the fake microservice" in new WithApplication {
      val vehicleLookupFailure = {
        val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
        when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
          val vehicleDetailsResponse =
            VehicleDetailsResponse(false,
              message = "Fake Web Dispose Service - Bad response",
              vehicleDetailsDto = VehicleDetailsDto(registrationNumber = "PJ056YY",
                vehicleMake = "Alfa Romeo",
                vehicleModel = "Alfasud ti",
                keeperName = "Keeper Name",
                keeperAddress = AddressDto(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))))
          val responseAsJson = Json.toJson(vehicleDetailsResponse)

          new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
        })

        val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)

        new disposal_of_vehicle.VehicleLookup(vehicleLookupServiceImpl)
      }

      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> referenceNumberValid,
        registrationNumberId -> registrationNumberValid,
        consentId -> consentValid)

      val result = vehicleLookupFailure.submit(request)

      redirectLocation(result) should equal (Some(VehicleLookupFailurePage.address))
    }

    "redirect to setupTradeDetails page when user has not set up a trader for disposal" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = vehicleLookupSuccess.present(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request if no details are entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if empty strings are entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> "",
        registrationNumberId -> "")

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only ReferenceNumber is entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> referenceNumberValid)

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request if only RegistrationNumber is entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        registrationNumberId -> registrationNumberValid)

      val result = vehicleLookupSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to EnterAddressManually when back button is pressed and there is no uprn" in new WithApplication {
      CacheSetup.businessChooseYourAddress()

      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(EnterAddressManuallyPage.address))
    }

    "redirect to BusinessChooseYourAddress when back button is pressed and there is a uprn" in new WithApplication {
      CacheSetup.businessChooseYourAddress(addressWithUprn)

      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.address))
    }

    "redirect to SetUpTradeDetails when back button and the user has completed the vehicle lookup form " in new WithApplication {
      CacheSetup.businessChooseYourAddress(addressWithUprn)

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        referenceNumberId -> referenceNumberValid,
        registrationNumberId -> registrationNumberValid,
        consentId -> consentValid)

      val result = vehicleLookupSuccess.back(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.address))
    }
  }
}