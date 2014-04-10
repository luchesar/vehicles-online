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
import services.fakes.{FakeDisposeWebServiceImpl, FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.DateServiceImpl
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._

class DisposeUnitSpec extends UnitSpec {
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

  private val disposeSuccess = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val responseAsJson = Json.toJson(disposeResponseSuccess)
      new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(ws)
    new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())
  }

  "Dispose - Controller" should {
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
      val result = disposeSuccess.submit(buildCorrectlyPopulatedRequest)
      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
    }

    "redirect to dispose error when a fail message is returned by the fake microservice" in new WithApplication {
      val disposeFailure = {
        val ws = mock[DisposeWebService]
        when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
          val responseAsJson = Json.toJson(disposeResponseFailure)
          new FakeResponse(status = 200, fakeJson = Some(responseAsJson))
        })
        val disposeServiceImpl = new DisposeServiceImpl(ws)
        new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())
      }

      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeModel()

      val result = disposeFailure.submit(buildCorrectlyPopulatedRequest)
      redirectLocation(result) should equal(Some(DisposeFailurePage.address))

      // Verify that the transaction id is now stored in the cache
      whenReady(result) {
        r => controllers.disposal_of_vehicle.Helpers.fetchDisposeTransactionIdFromCache match {
          case Some(txId) =>
            txId should equal(FakeDisposeWebServiceImpl.transactionIdValid)
          case _ => fail("Should have found transaction id in the cache")
        }
      }
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = buildCorrectlyPopulatedRequest
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

      val disposeResponseThrows = mock[DisposeResponse]
      when(disposeResponseThrows.success).thenThrow(new RuntimeException("expected by DisposeUnitSpec"))
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows, dateServiceStubbed())
      val request = buildCorrectlyPopulatedRequest
      val result = dispose.submit(request)
      status(result) should equal(BAD_REQUEST)
    }

  }
}