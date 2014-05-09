package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.{CookieFactoryForUnitSpecs, CookieFactoryForUISpecs}
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.{FakeVehicleLookupWebService, FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.DateServiceImpl
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import FakeVehicleLookupWebService.registrationNumberValid
import services.session.{SessionState, PlaySessionState}
import play.api.mvc.Cookies

class DisposeUnitSpec extends UnitSpec {

  "Dispose - Controller" should {

    "present" in new WithApplication {
      
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.dealerDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
      
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())

      val result = disposeController().submit(request)

      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(DisposeSuccessPage.address))
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundTimestamp =  cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.disposeFormTimestamp()))
          foundTimestamp should equal(true)
      }
    }

    "redirect to micro-service error page when an unexpected error occurs" in new WithApplication {
      
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          new FakeResponse(status = INTERNAL_SERVER_ERROR)
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose( disposeServiceImpl, dateServiceStubbed())
      }

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "return a bad request when no details are entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody().
        withCookies(CookieFactoryForUnitSpecs.dealerDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to micro-service error page when calling webservice throws exception" in new WithApplication {

      val disposeResponseThrows = mock[(Int, Option[DisposeResponse])]
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose( mockWebServiceThrows, dateServiceStubbed())
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())

      val result = dispose.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    ">>> redirect to micro-service error page when calling webservice throws exception" in new WithApplication {

      val disposeResponseThrows = mock[(Int, Option[DisposeResponse])]
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future.failed(new RuntimeException))
      val dispose = new disposal_of_vehicle.Dispose( mockWebServiceThrows, dateServiceStubbed())
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = dispose.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

  "redirect to soap endpoint error page when service is unavailable" in new WithApplication {
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          new FakeResponse(status = SERVICE_UNAVAILABLE)
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose( disposeServiceImpl, dateServiceStubbed())
      }

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address))
      }
    }

    "redirect to dispose success when applicationBeingProcessed" in new WithApplication {

      val disposeSuccess = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseApplicationBeingProcessed)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose( disposeServiceImpl, dateServiceStubbed())
      }

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.submit(request)
      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(DisposeSuccessPage.address))

          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundTimestamp =  cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.disposeFormTimestamp()))
          foundTimestamp should equal(true)

          val foundRegistrationNumber =  cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.disposeFormRegistrationNumber()))
          foundRegistrationNumber should equal(true)
      }
    }

    "redirect to dispose failure page when unableToProcessApplication" in new WithApplication {
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseUnableToProcessApplication)
          new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())
      }

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())

      val result = disposeFailure.submit(request)

      // Verify that the transaction id is now stored in the cache
      whenReady(result) {
        r => {
          r.header.headers.get(LOCATION) should equal(Some(DisposeFailurePage.address))
        }
      }
    }
  }

  "redirect to error page when undefined error is returned" in new WithApplication {
    val disposeFailure = {
      val ws = mock[DisposeWebService]
      when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
        val responseAsJson = Json.toJson(disposeResponseUndefinedError)
        new FakeResponse(status = OK, fakeJson = Some(responseAsJson))
      })
      val disposeServiceImpl = new DisposeServiceImpl(ws)
      new disposal_of_vehicle.Dispose( disposeServiceImpl, dateServiceStubbed())
    }


    val request = buildCorrectlyPopulatedRequest.
      withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
      withCookies(CookieFactoryForUnitSpecs.disposeModel())

    val result = disposeFailure.submit(request)

    whenReady(result) {
      r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
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

  private def disposeController() = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val responseAsJson = Json.toJson(disposeResponseSuccess)
      new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(ws)
    new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())
  }
}
