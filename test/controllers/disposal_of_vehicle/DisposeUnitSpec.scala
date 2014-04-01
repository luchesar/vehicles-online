package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{disposal_of_vehicle}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse, DisposeModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.DisposeService
import services.fakes.FakeDisposeService

class DisposeUnitSpec extends UnitSpec {

  "Dispose - Controller" should {

       val mockDisposeRequest = mock[DisposeRequest]
       val mockWebServiceSuccess = mock[DisposeService]
       when(mockWebServiceSuccess.invoke(any[DisposeRequest])).thenReturn(new FakeDisposeService().invoke(mockDisposeRequest))
       val dispose = new disposal_of_vehicle.Dispose(mockWebServiceSuccess)

       "present" in new WithApplication {
         CacheSetup.businessChooseYourAddress()
         CacheSetup.vehicleDetailsModel()

         val request = FakeRequest().withSession()

         val result = dispose.present(request)

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

         val result = dispose.submit(request)

         redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
       }

    "redirect to dispose error when a fail message is returned by the fake microservice" in new WithApplication {
      val mockDisposeRequestFails = mock[DisposeRequest]
      when (mockDisposeRequestFails.referenceNumber).thenReturn(FakeDisposeService.failureReferenceNumber)
      val mockWebServiceFailure = mock[DisposeService]
      when(mockWebServiceFailure.invoke(any[DisposeRequest])).thenReturn(new FakeDisposeService().invoke(mockDisposeRequestFails))
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceFailure)

      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleLookupFormModel()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = dispose.submit(request)

      //Assert
      redirectLocation(result) should equal(Some(DisposeFailurePage.address))
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      CacheSetup.setupTradeDetails()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = dispose.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = dispose.present(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request when no details are entered" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = dispose.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = dispose.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "return a bad request when calling webservice throws exception" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.vehicleLookupFormModel()

      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        mileageId -> mileageValid,
        s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
        s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
        s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val disposeResponseThrows = mock[DisposeResponse]
      when(disposeResponseThrows.success).thenThrow(new RuntimeException("expected by DisposeUnitSpec"))
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows)

      val result = dispose.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}