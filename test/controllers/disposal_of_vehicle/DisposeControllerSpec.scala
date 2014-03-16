package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.{disposal_of_vehicle}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.{DisposePage,DisposeSuccessPage, DisposeFailurePage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.{DisposeResponse, DisposeModel}
import services.fakes.FakeDisposeService
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec

class DisposeUnitSpec extends UnitSpec {

  "Dispose - Controller" should {

       val mockDisposeModel = mock[DisposeModel]
       val mockWebServiceSuccess = mock[services.DisposeService]
       when(mockWebServiceSuccess.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModel))
       val dispose = new disposal_of_vehicle.Dispose(mockWebServiceSuccess)

       "present" in new WithApplication {
         BusinessChooseYourAddressPage.setupCache()
         VehicleLookupPage.setupVehicleDetailsModelCache()
         val request = FakeRequest().withSession()

         val result = dispose.present(request)

         status(result) should equal(OK)
       }

       "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
         BusinessChooseYourAddressPage.setupCache()
         VehicleLookupPage.setupVehicleDetailsModelCache()
         VehicleLookupPage.setupVehicleLookupFormModelCache()
         val request = FakeRequest().withSession()
           .withFormUrlEncodedBody(
             mileageId -> mileageValid,
             s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
             s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
             s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
           )

         val result = dispose.submit(request)

         status(result) should equal(SEE_OTHER)
         redirectLocation(result) should equal(Some(DisposeSuccessPage.url))
       }

    "redirect to dispose error when a fail message is returned by the fake microservice" in new WithApplication {
      val mockDisposeModelFails = mock[DisposeModel]
      when (mockDisposeModelFails.referenceNumber).thenReturn(FakeDisposeService.failureReferenceNumber)
      val mockWebServiceFailure = mock[services.DisposeService]
      when(mockWebServiceFailure.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModelFails))
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceFailure)

      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      DisposePage.setupDisposeModelCache()

      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
        )

      val result = dispose.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(DisposeFailurePage.url))
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {

      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      val result = dispose.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = dispose.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request when no details are entered" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = dispose.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = dispose.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request when calling webservice throws exception" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
        )

      val disposeResponseThrows = mock[DisposeResponse]
      when(disposeResponseThrows.success).thenThrow(new RuntimeException("expected by DisposeUnitSpec"))
      val mockWebServiceThrows = mock[services.DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeModel])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows)

      val result = dispose.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}