package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.{FakeVehicleLookupWebService, FakeDisposeWebServiceImpl, FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.DateServiceImpl
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import FakeVehicleLookupWebService.registrationNumberValid
import services.session.{SessionState, PlaySessionState}

class DisposeUnitSpec extends UnitSpec {

  "Dispose - Controller" should {

    "present" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress().
        vehicleDetailsModel()
      val request = FakeRequest().withSession()
      val result = disposeSuccess(newSessionState).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress().
        vehicleDetailsModel().
        vehicleLookupFormModel()
      val result = disposeSuccess(newSessionState).submit(buildCorrectlyPopulatedRequest)
      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(DisposeSuccessPage.address))
          sessionState.fetchDisposeTransactionTimestampInCache match {
            case Some(transactionTimestamp) => transactionTimestamp should include(s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-$dateOfDisposalDayValid")
            case _ => fail("Should have found transaction timestamp in cache")
          }
      }
    }

    "redirect to micro-service error page when unsuccessful and response code is returned" in new WithApplication {
      val sessionState = newSessionState
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseFailure)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      cacheSetup(sessionState.inner)

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)

      // Verify that the transaction id is now stored in the cache
      whenReady(result) {
        r => {
          r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
          sessionState.fetchDisposeTransactionIdFromCache match {
            case Some(txId) =>
              txId should equal(FakeDisposeWebServiceImpl.transactionIdValid)
            case _ => fail("Should have found transaction id in the cache")
          }
        }
      }
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).setupTradeDetails()
      val request = buildCorrectlyPopulatedRequest
      val result = disposeSuccess(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeSuccess(newSessionState).present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "return a bad request when no details are entered" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress().
        vehicleDetailsModel()
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = disposeSuccess(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = disposeSuccess(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to micro-service error page when calling webservice throws exception" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).businessChooseYourAddress().
        vehicleDetailsModel().
        vehicleLookupFormModel()

      val disposeResponseThrows = mock[DisposeResponse]
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose(sessionState, mockWebServiceThrows, dateServiceStubbed())
      val request = buildCorrectlyPopulatedRequest
      val result = dispose.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "redirect to soap endpoint error page when unsuccessful and response code says soap endpoint is down" in new WithApplication {
      val sessionState = newSessionState
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseSoapEndpointFailure)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      cacheSetup(sessionState.inner)

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address))
      }
    }

    "redirect to micro-service error page when unsuccessful and any response code that is not soap endpoint down" in new WithApplication {
      val sessionState = newSessionState
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseWithResponseCode)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      cacheSetup(sessionState.inner)

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "redirect to Soap endpoint error page when endpoint timeout" in new WithApplication {
      val sessionState = newSessionState
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseSoapEndpointTimeout)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      cacheSetup(sessionState.inner)

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)

      // Verify that the transaction id is now stored in the cache
      whenReady(result) {
        r => {
          r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address))
        }
      }
    }

    "redirect to dispose success when applicationBeingProcessed" in new WithApplication {
      val sessionState = newSessionState
      new CacheSetup(sessionState.inner).
        businessChooseYourAddress().
        vehicleDetailsModel().
        vehicleLookupFormModel()

      val disposeSuccess = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseApplicationBeingProcessed)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      val result = disposeSuccess.submit(buildCorrectlyPopulatedRequest)
      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(DisposeSuccessPage.address))
          sessionState.fetchDisposeTransactionTimestampInCache match {
            case Some(transactionTimestamp) => transactionTimestamp should include(s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-$dateOfDisposalDayValid")
            case _ => fail("Should have found transaction timestamp in cache")
          }
          sessionState.fetchDisposeRegistrationNumberFromCache match {
            case Some(disposeRegistrationNumber) => disposeRegistrationNumber should equal(registrationNumberValid)
            case _ => fail("Should have found DisposeRegistrationNumber in cache")
          }
      }
    }

    "redirect to Soap endpoint error page when unableToProcessApplication" in new WithApplication {
      val sessionState = newSessionState
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseUnableToProcessApplication)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
      }

      cacheSetup(sessionState.inner)

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)

      // Verify that the transaction id is now stored in the cache
      whenReady(result) {
        r => {
          r.header.headers.get(LOCATION) should equal(Some(DisposeFailurePage.address))
        }
      }
    }
  }

  private def dateServiceStubbed(day: Int = dateOfDisposalDayValid.toInt, month: Int = dateOfDisposalMonthValid.toInt, year: Int = dateOfDisposalYearValid.toInt) = {
    val dateService = mock[DateServiceImpl]
    when(dateService.today).thenReturn(new models.DayMonthYear(day = day,
      month = month,
      year = year))
    dateService
  }

  private def buildCorrectlyPopulatedRequest = {
    import mappings.common.DayMonthYear._
    FakeRequest().withSession().withFormUrlEncodedBody(
      mileageId -> mileageValid,
      s"$dateOfDisposalId.$dayId" -> dateOfDisposalDayValid,
      s"$dateOfDisposalId.$monthId" -> dateOfDisposalMonthValid,
      s"$dateOfDisposalId.$yearId" -> dateOfDisposalYearValid,
      consentId -> consentValid,
      lossOfRegistrationConsentId -> consentValid)
  }

  private def disposeSuccess(sessionState: DisposalOfVehicleSessionState) = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val responseAsJson = Json.toJson(disposeResponseSuccess)
      new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(ws)
    new disposal_of_vehicle.Dispose(sessionState, disposeServiceImpl, dateServiceStubbed())
  }

  private def cacheSetup(sessionState: SessionState) = {
    new CacheSetup(sessionState).businessChooseYourAddress().
      vehicleLookupFormModel().
      vehicleDetailsModel().
      disposeModel()
  }

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
