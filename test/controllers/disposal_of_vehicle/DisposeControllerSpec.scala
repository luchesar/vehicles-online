package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.{DisposePage,DisposeSuccessPage, DisposeFailurePage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import helpers.disposal_of_vehicle.Helper._
import org.scalatest.mock.MockitoSugar
import models.domain.disposal_of_vehicle.{DisposeResponse, DisposeModel}
import org.mockito.Mockito._
import org.mockito.Matchers._
import services.fakes.FakeDisposeService
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class DisposeControllerSpec extends WordSpec with Matchers with MockitoSugar {
  "Dispose - Controller" should {

       val mockDisposeModel = mock[DisposeModel]
       val mockWebServiceSuccess = mock[services.DisposeService]
       when(mockWebServiceSuccess.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModel))
       val dispose = new disposal_of_vehicle.Dispose(mockWebServiceSuccess)

       "present" in new WithApplication {
         // Arrange
         BusinessChooseYourAddressPage.setupCache
         VehicleLookupPage.setupVehicleDetailsModelCache()
         val request = FakeRequest().withSession()

         // Act
         val result = dispose.present(request)

         // Assert
         status(result) should equal(OK)
       }

       "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
         //Arrange
         BusinessChooseYourAddressPage.setupCache
         VehicleLookupPage.setupVehicleDetailsModelCache()
         VehicleLookupPage.setupVehicleLookupFormModelCache()
         val request = FakeRequest().withSession()
           .withFormUrlEncodedBody(
             mileageId -> mileageValid,
             s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
             s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
             s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
           )

         // Act
         val result = dispose.submit(request)

         // Assert
         status(result) should equal(SEE_OTHER)
         redirectLocation(result) should equal(Some(DisposeSuccessPage.url))
       }

    "redirect to dispose error when a fail message is returned by the fake microservice" in new WithApplication {
      val mockDisposeModelFails = mock[DisposeModel]
      when (mockDisposeModelFails.referenceNumber).thenReturn(FakeDisposeService.failureReferenceNumber)
      val mockWebServiceFailure = mock[services.DisposeService]
      when(mockWebServiceFailure.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModelFails))
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceFailure)

      BusinessChooseYourAddressPage.setupCache
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
      // Act
      val result = dispose.submit(request)

      //Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(DisposeFailurePage.url))
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid)

      // Act
      val result = dispose.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = dispose.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request when no details are entered" in new WithApplication {
      // Arrange
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupVehicleDetailsModelCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = dispose.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = dispose.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request when calling webservice throws exception" in new WithApplication {
      //Arrange
      BusinessChooseYourAddressPage.setupCache
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
      when(disposeResponseThrows.success).thenThrow(new RuntimeException("expected by DisposeControllerSpec"))
      val mockWebServiceThrows = mock[services.DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeModel])).thenReturn(Future {
        disposeResponseThrows
      })
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows)

      // Act
      val result = dispose.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}