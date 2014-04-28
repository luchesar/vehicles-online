package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.{VehicleDetailsResponse, VehicleDetailsRequest}
import services.fakes.FakeResponse
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupWebService}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.{JsValue, Json}
import ExecutionContext.Implicits.global
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeAddressLookupService._
import play.api.http.Status.OK
import services.session.PlaySessionState

class VehicleLookupUnitSpec extends UnitSpec {

  "VehicleLookup - Controller" should {

    "present" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = FakeRequest().withSession()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).present(request)

      result.futureValue.header.status should equal(OK)
    }

    "redirect to Dispose after a valid submit and true message returned from the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(DisposePage.address))
    }

    "submit removes spaces from registrationNumber" in new WithApplication {
      // DE7 Spaces should be stripped
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(registrationNumber = registrationNumberWithSpaceValid)
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)

      whenReady(result) {
        r => sessionState.fetchVehicleLookupDetailsFromCache match {
          case Some(f) => f.registrationNumber should equal(registrationNumberValid)
          case _ => fail("Should have found registration number in the cache")
        }
      }
    }

    "redirect to VehicleLookupFailure after a submit and no response code and no vehicledetailsdto returned from the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseNotFoundResponseCode).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
    }

    "redirect to VehicleLookupFailure after a submit and vrm not found by the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseVRMNotFound).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(VehicleLookupFailurePage.address))
    }

    "redirect to VehicleLookupFailure after a submit and document reference number mismatch returned by the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseDocRefNumberNotLatest).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(VehicleLookupFailurePage.address))
    }

    "redirect to VehicleLookupFailure after a submit and vss error returned by the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsServerDown).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(VehicleLookupFailurePage.address))
    }

    "redirect to setupTradeDetails page when user has not set up a trader for disposal" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(newSessionState, vehicleDetailsResponseSuccess).present(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request if no details are entered" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(referenceNumber = "", registrationNumber = "", consent = "")
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)

      result.futureValue.header.status should equal(BAD_REQUEST)
    }

    "replace max length error message for document reference number with standard error message (US43)" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(referenceNumber = "1" * (referenceNumberLength + 1))
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)
      // check the validation summary text
      countSubstring(contentAsString(result), "Document reference number - Document reference number must be an 11-digit number") should equal(1)
      // check the form item validation
      countSubstring(contentAsString(result), "\"error\">Document reference number must be an 11-digit number") should equal(1)
    }

    "replace required and min length error messages for document reference number with standard error message (US43)" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(referenceNumber = "")
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)
      // check the validation summary text
      countSubstring(contentAsString(result), "Document reference number - Document reference number must be an 11-digit number") should equal(1)
      // check the form item validation
      countSubstring(contentAsString(result), "\"error\">Document reference number must be an 11-digit number") should equal(1)
    }

    "replace max length error message for vehicle registration mark with standard error message (US43)" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(registrationNumber = "PJ05YYYX")
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)
      val count = countSubstring(contentAsString(result), "Must be valid format")

      count should equal(2)
    }

    "replace required and min length error messages for vehicle registration mark with standard error message (US43)" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest(registrationNumber = "")
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).submit(request)
      val count = countSubstring(contentAsString(result), "Must be valid format")

      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of the page and once above the field.
    }

    "redirect to EnterAddressManually when back button is pressed and there is no uprn" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).back(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(EnterAddressManuallyPage.address))
    }

    "redirect to BusinessChooseYourAddress when back button is pressed and there is a uprn" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress(addressWithUprn)
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).back(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(BusinessChooseYourAddressPage.address))
    }

    "redirect to SetUpTradeDetails when back button and the user has completed the vehicle lookup form" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress(addressWithUprn)
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(sessionState, vehicleDetailsResponseSuccess).back(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(BusinessChooseYourAddressPage.address))
    }

    "redirect to SetUpTradeDetails when back button clicked and there are no trader details stored in cache" in new WithApplication {
      // No cache setup with dealer details
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupResponseGenerator(newSessionState, vehicleDetailsResponseSuccess).back(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some("/disposal-of-vehicle/setup-trade-details"))
    }

    "redirect to MicroserviceError when microservice throws" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress()
      val request = buildCorrectlyPopulatedRequest()
      val result = vehicleLookupError(sessionState).submit(request)

      result.futureValue.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
    }
  }

  private def vehicleLookupResponseGenerator(sessionState: DisposalOfVehicleSessionState, fullResponse:(Int, Option[VehicleDetailsResponse])) = {
  val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
    when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
      val responseAsJson : Option[JsValue] = fullResponse._2 match {
        case Some(e) => Some(Json.toJson(e))
        case _ => None
      }
      new FakeResponse(status = fullResponse._1, fakeJson = responseAsJson)// Any call to a webservice will always return this successful response.
    })
    val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)
    new disposal_of_vehicle.VehicleLookup(sessionState, vehicleLookupServiceImpl)
  }

  private def vehicleLookupError(sessionState: DisposalOfVehicleSessionState) = {
    val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
    when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
      throw new IllegalArgumentException
    })
    val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)
    new disposal_of_vehicle.VehicleLookup(sessionState, vehicleLookupServiceImpl)
  }

  private def buildCorrectlyPopulatedRequest(referenceNumber: String = referenceNumberValid,
                                             registrationNumber: String = registrationNumberValid,
                                             consent: String = consentValid) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      referenceNumberId -> referenceNumber,
      registrationNumberId -> registrationNumber,
      consentId -> consent)
  }

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
