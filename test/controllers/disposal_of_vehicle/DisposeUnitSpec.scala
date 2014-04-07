package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.{FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.DateServiceImpl

class DisposeUnitSpec extends UnitSpec {

  "Dispose - Controller" should {
    def dateService(day: Int = dateOfDisposalDayValid.toInt, month: Int = dateOfDisposalMonthValid.toInt, year: Int = dateOfDisposalYearValid.toInt) = {
      val dateService = mock[DateServiceImpl]
      when(dateService.today).thenReturn(new models.DayMonthYear(day = day,
        month = month,
        year = year))
      dateService
    }

    val disposeSuccess = {
      val ws = mock[DisposeWebService]
      when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
        val disposeResponse =
          DisposeResponse(success = true,
            message = "Fake Web Dispose Service - Good response",
            transactionId = "1234",
            registrationNumber = registrationNumberValid,
            auditId = "7575")
        val responseAsJson = Json.toJson(disposeResponse)

        new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
      })

      val disposeServiceImpl = new DisposeServiceImpl(ws)

      new disposal_of_vehicle.Dispose(disposeServiceImpl, dateService())
    }

    "present" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()

      val request = FakeRequest().withSession()

      val result = disposeSuccess.present(request)

      status(result) should equal(OK)
    }

    "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = disposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
    }

    "redirect to dispose error when a fail message is returned by the fake microservice" in new WithApplication {
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val disposeResponse =
            DisposeResponse(success = false,
              message = "Fake Web Dispose Service - Bad response",
              transactionId = "",
              registrationNumber = "",
              auditId = "")
          val responseAsJson = Json.toJson(disposeResponse)

          new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
        })

        val disposeServiceImpl = new DisposeServiceImpl(ws)

        new disposal_of_vehicle.Dispose(disposeServiceImpl, dateService())
      }

      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = disposeFailure.submit(request)

      redirectLocation(result) should equal(Some(DisposeFailurePage.address))
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      CacheSetup.setupTradeDetails()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = disposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request when no details are entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = disposeSuccess.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = disposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request when calling webservice throws exception" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"$dateOfDisposalId.day" -> dateOfDisposalDayValid,
        s"$dateOfDisposalId.month" -> dateOfDisposalMonthValid,
        s"$dateOfDisposalId.year" -> dateOfDisposalYearValid)

      val disposeResponseThrows = mock[DisposeResponse]
      when(disposeResponseThrows.success).thenThrow(new RuntimeException("expected by DisposeUnitSpec"))
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future {
        disposeResponseThrows
      })

      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows, dateService())

      val result = dispose.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}